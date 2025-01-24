package com.nexmind3.octoaqua.util;


import com.nexmind3.octoaqua.dto.RoleDto;
import com.nexmind3.octoaqua.dto.UserDto;
import com.nexmind3.octoaqua.dto.UserSecurityKeyDto;
import com.nexmind3.octoaqua.entity.Authority;
import com.nexmind3.octoaqua.entity.Role;
import com.nexmind3.octoaqua.entity.User;
import com.nexmind3.octoaqua.entity.UserSecurityKey;
import com.nexmind3.octoaqua.enumeration.SecurityKeyType;
import com.nexmind3.octoaqua.enumeration.State;
import com.nexmind3.octoaqua.repository.AuthorityRepository;
import com.nexmind3.octoaqua.repository.CompanyRepository;
import com.nexmind3.octoaqua.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class MapperUtil {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy", Locale.ROOT);


    public User convertToEntity(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        if(!userDto.isDummyUser()){ // dummy user isee temp user tipinde ilişki ekle
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else{
            user.setPassword("");
        }
        user.setAccountConfirmed(false);
        user.setAccountLock(false);
        user.setPhone(userDto.getPhone());
        user.setState(userDto.getState());
        return user;
    }

    public UserSecurityKey convertToEntity(UserSecurityKeyDto securityKeyDto){
        UserSecurityKey userSecurityKey = new UserSecurityKey();
        userSecurityKey.setExpirationTime(securityKeyDto.getExpirationTime());
        userSecurityKey.setSecurityKeyType(Arrays.stream(SecurityKeyType.values())
                .filter(type -> type.ordinal() == securityKeyDto.getSecurityKeyType())
                .findFirst().orElse(null));
        userSecurityKey.setState(Arrays.stream(State.values())
                .filter(type -> type.ordinal() == securityKeyDto.getState())
                .findFirst().orElse(State.ACTIVE));
        userSecurityKey.setKey(securityKeyDto.getKey());
        return userSecurityKey;
    }

    public Role convertToEntity(RoleDto roleDto){
        Role role = new Role();
        role.setName(role.getName());
        role.setDescription(roleDto.getDescription());
        role.setState(roleDto.getState());
        if(!CollectionUtils.isEmpty(roleDto.getAuthorities())){
            List<Authority> authorityList = authorityRepository.findAllById(roleDto.getAuthorities());
            role.setAuthorities(authorityList);
        }
        return role;
    }

    public String generateRandomKey(Integer charCount){
        if(charCount == null){
            charCount = 20;
        }
        String randomKey = RandomStringUtils.random( charCount, 0, ValidationRegexConstants.POSSIBLE_CHARS.length-1, false, false, ValidationRegexConstants.POSSIBLE_CHARS, new SecureRandom());
        return randomKey;
    }

    public UserDto convertToDto(User user){
        UserDto userDto = new UserDto();
        if(user == null)
            return userDto;
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setState(user.getState());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setLegalNumber(user.getLegalIdentityNumber());
        return userDto;
    }

}
