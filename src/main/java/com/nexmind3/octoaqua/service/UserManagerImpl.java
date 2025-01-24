package com.nexmind3.octoaqua.service;

import com.nexmind3.octoaqua.dto.*;
import com.nexmind3.octoaqua.entity.User;
import com.nexmind3.octoaqua.enumeration.CompanyUserType;
import com.nexmind3.octoaqua.enumeration.SecurityKeyType;
import com.nexmind3.octoaqua.enumeration.State;
import com.nexmind3.octoaqua.exception.CustomException;
import com.nexmind3.octoaqua.exception.UserNotFoundException;
import com.nexmind3.octoaqua.repository.CompanyUserRepository;
import com.nexmind3.octoaqua.util.MapperUtil;
import com.nexmind3.octoaqua.util.ValidationRegexConstants;
import com.nexmind3.octoaqua.entity.Company;
import com.nexmind3.octoaqua.entity.CompanyUser;
import com.nexmind3.octoaqua.entity.UserSecurityKey;
import com.nexmind3.octoaqua.repository.CompanyRepository;
import com.nexmind3.octoaqua.repository.UserRepository;
import com.nexmind3.octoaqua.repository.UserSecurityKeyRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserManagerImpl implements UserManager {

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private UserSecurityKeyRepository userSecurityKeyRepository ;

    @Autowired
    private CompanyUserRepository companyUserRepository ;


    @Autowired
    private EmailManager emailManager ;

    @Autowired
    private MapperUtil mapperUtil ;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder ;

    @Autowired
    CompanyRepository companyRepository ;



    private String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }


    @Override
    public User register(UserDto userDto) throws CustomException, MessagingException, IOException {
        User user = userRepository.findByEmail(userDto.getEmail());

        if(user != null){
            UserSecurityKey userSecurityKey = userSecurityKeyRepository.getActiveSecurityKey(user.getId(), SecurityKeyType.APPROVE_ACCOUNT, null);
            if (user.isAccountConfirmed()){
                throw new CustomException("The e-mail address you entered is already registered.");
            }
            if(userSecurityKey != null){
                throw new CustomException("Account confirmation link already sent. Please approve your account and login.");
            }
        }else{
            user = mapperUtil.convertToEntity(userDto);
        }

        String randomKey = mapperUtil.generateRandomKey(20);
        UserSecurityKey userSecurityKey = mapperUtil.convertToEntity(new UserSecurityKeyDto(null, null,
                DateUtils.addHours(new Date(), 6),
                SecurityKeyType.APPROVE_ACCOUNT.ordinal(), State.ACTIVE.ordinal(), randomKey));

        userRepository.save(user);
        userSecurityKey.setUser(user);
        userSecurityKeyRepository.save(userSecurityKey );
        userDto.setId(user.getId());

        //send registration validation mail
        MailTemplateNotification mailTemplateNotification = new MailTemplateNotification();
        mailTemplateNotification.setEmail(user.getEmail());
        String approveUserGatewayUrl = getBaseUrl() + ("/user/verifyUserAccount/" + userSecurityKey.getKey() + "/"+ user.getEmail());
        mailTemplateNotification.setSubject("Account Confirmation");
        mailTemplateNotification.setTemplateName("register");
        Map<String, Object> map = new HashMap<>();
        map.put("name", StringUtils.join(Arrays.asList(user.getFirstName(), user.getLastName()), ' '));
        map.put("location", user.getEmail());
        map.put("confirmationURL", approveUserGatewayUrl);
        map.put("confirmationURLText", "Click Me");
        map.put("registrationCode", userSecurityKey.getKey());
        map.put("title", "WELCOME");
        mailTemplateNotification.setVariables(map);
        emailManager.sendEmailWithTemplate(mailTemplateNotification);

        return user;
    }

    @Override
    public CompanyUser saveCompanyUser(User user, Company company, CompanyUserType companyUserType) throws CustomException{
        CompanyUser companyUser = new CompanyUser();
        if(user == null){
            throw new CustomException("user could not be empty on company user relation");
        }
        if(company == null){
            throw new CustomException("company could not be empty on company user relation");
        }

        companyUser.setCompany(company);
        companyUser.setType(companyUserType);
        companyUser.setUser(user);
        companyUser.setState(State.ACTIVE);
        companyUser = companyUserRepository.save(companyUser);
        return companyUser;
    }

    @Override
    public UserDto saveCustomerUser(UserDto userDto) throws CustomException{
        User user = null;
        if(userDto != null){
            if(userDto.getId() != null){
                user = userRepository.findById(userDto.getId()).orElse(null);
            }
            else if(userDto.getEmail() !=null && user == null){
                user = userRepository.findByEmail(userDto.getEmail());
            }
        }
        if(user == null){
            user = mapperUtil.convertToEntity(userDto);
            user = userRepository.save(user);
            UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userEmail = currentUser.getUsername();
            Long companyId = getCompanyIdByMail(userEmail);
            if(companyId != null) {
                Company company = companyRepository.getById(companyId);
                saveCompanyUser(user, company, CompanyUserType.TEMP_CUSTOMER);
            }
        }
        userDto.setId(user.getId());
        return userDto;
    }

    @Override
    public Long getUserIdByMail(String email) {
        return  userRepository.getUserIdByMail(email);
    }

    @Override
    public Long getCompanyIdByMail(String email) throws CustomException{
        List<Long> userCompanyIds =  userRepository.getCompanyIdByUserMail(email);
        if(CollectionUtils.isEmpty(userCompanyIds)){
            throw new CustomException("Company of user is not specified.");
        }
        if(userCompanyIds.size() > 1 ){
            throw new CustomException("User related with more than one company.");
        }
        return userCompanyIds.get(0);
    }

    @Override
    public List<UserDto> getUsersByRole(String roleName) throws Exception {
        return List.of();
    }

    @Override
    public Boolean checkCustomerUser(Long userId) throws CustomException{
        Boolean isCustomerUserExists = false;
        if(userId != null){
            if(userRepository.existsById(userId)){
                isCustomerUserExists = true;
            }
        }
        return isCustomerUserExists;
    }

    @Override
    public boolean checkUserExistsByEmail(String email) {
        if(StringUtils.isBlank(email)){
            return false;
        }
        User user = userRepository.findByEmail(email);
        if(user == null){
            return false;
        }
        return true;
    }

    @Override
    public List<User> getAllUsers()  {
        return userRepository.findAll();
    }

    @Override
    public User verifyUserAccount(String email, String key) throws Exception {
        if(StringUtils.isBlank(email) || StringUtils.isBlank(key)){
            throw new UserNotFoundException("The account could not be verified.");
        }
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException("The account could not be verified.");
        }
        UserSecurityKey userSecurityKey = userSecurityKeyRepository.getActiveSecurityKey(user.getId(), SecurityKeyType.APPROVE_ACCOUNT, key);

        if(userSecurityKey == null){
            throw new Exception("This link has expired due to inactivity. Please try registering again");
        }
        userSecurityKey.setState(State.PASSIVE);
        userSecurityKeyRepository.save(userSecurityKey);
        user.setAccountConfirmed(true);
        return userRepository.save(user);

    }

    @Override
    @Transactional
    public User resetPasswordRequest(String email) throws Exception {
        try {
            User user = userRepository.findByEmail(email);
            if(user == null){
                throw new UserNotFoundException("user not found");
            }

            UserSecurityKey userSecurityKey = userSecurityKeyRepository.getActiveSecurityKey(user.getId(), SecurityKeyType.CHANGE_PASSWORD, null);

            if(userSecurityKey != null){
                return null;
            }

            String randomKey = mapperUtil.generateRandomKey(20);
            userSecurityKey = mapperUtil.convertToEntity(new UserSecurityKeyDto(null, null,
                    DateUtils.addHours(new Date(), 4),
                    SecurityKeyType.CHANGE_PASSWORD.ordinal(), State.ACTIVE.ordinal(), randomKey));
            userSecurityKey.setUser(user);
            userSecurityKeyRepository.save(userSecurityKey);
            String passwordChangeGatewayUrl = ("/ui/resetPassword/" + userSecurityKey.getKey() + "/"+user.getEmail());
            MailNotification mailNotification = new MailNotification(user.getEmail(), "Change password url:" + passwordChangeGatewayUrl);
            emailManager.sendBasicMail(mailNotification);
            return user;
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    @Override
    @Transactional
    public boolean resetPassword(ResetPasswordDto resetPasswordDto) throws Exception {
        try {
            boolean isPasswordReset = false;
            if(resetPasswordDto == null
                    || StringUtils.isBlank(resetPasswordDto.getPassword())
                    || StringUtils.isBlank(resetPasswordDto.getSecondLevelPassword())){
                throw new Exception("Please enter your new password.");
            }
            User user = checkResetPasswordOperation(resetPasswordDto.getChangeKey(), resetPasswordDto.getEmail()) ;
            if(user == null ){
                return isPasswordReset;
            }

            if(!resetPasswordDto.getPassword().equals(resetPasswordDto.getSecondLevelPassword())){
                throw new CustomException("Passwords do not match.");
            }

            Boolean isPasswordValid = Pattern.compile(ValidationRegexConstants.PASSWORD_VALIDATOR).matcher(resetPasswordDto.getPassword()).matches();
            if(!isPasswordValid){
                throw new CustomException("Your password does not meet minimum security specifications. Please edit your password again." +
                        "Please make sure that your password contains upper - lower case letters, numbers and at least 8 characters.");
            }

            user.setPassword(bCryptPasswordEncoder.encode(resetPasswordDto.getPassword()));
            userRepository.save(user);
            MailNotification mailNotification = new MailNotification(user.getEmail(), "Your password Changed successfully.");
            emailManager.sendBasicMail(mailNotification);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    private User checkResetPasswordOperation(String changeKey, String email) throws Exception{
        if(StringUtils.isBlank(changeKey) || StringUtils.isBlank(email)){
            return null;
        }
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException("User not found");
        }

        UserSecurityKey userSecurityKey = userSecurityKeyRepository.getActiveSecurityKey(user.getId(), SecurityKeyType.CHANGE_PASSWORD, changeKey);

        if(userSecurityKey == null){
            return null;
        }
        userSecurityKey.setState(State.PASSIVE);
        userSecurityKeyRepository.save(userSecurityKey);
        return user;
    }

}
