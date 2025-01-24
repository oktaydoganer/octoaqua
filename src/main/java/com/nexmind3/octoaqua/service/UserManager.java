package com.nexmind3.octoaqua.service;

import com.nexmind3.octoaqua.dto.ResetPasswordDto;
import com.nexmind3.octoaqua.entity.User;
import com.nexmind3.octoaqua.enumeration.CompanyUserType;
import com.nexmind3.octoaqua.entity.Company;
import com.nexmind3.octoaqua.entity.CompanyUser;
import com.nexmind3.octoaqua.exception.CustomException;
import com.nexmind3.octoaqua.dto.UserDto;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserManager {
    User register(UserDto userDto) throws CustomException, MessagingException, IOException;

    UserDto saveCustomerUser(UserDto userDto) throws CustomException;

    Boolean checkCustomerUser(Long userId) throws CustomException;

    boolean checkUserExistsByEmail(String email);

    User verifyUserAccount(String email, String key) throws Exception;

    User resetPasswordRequest(String email) throws Exception;

    boolean resetPassword(ResetPasswordDto resetPasswordDto) throws Exception;

    List<User> getAllUsers();

    Long getUserIdByMail(String email);

    Long getCompanyIdByMail(String email) throws CustomException;

    List<UserDto> getUsersByRole(String roleName) throws Exception;

    CompanyUser saveCompanyUser(User user, Company company, CompanyUserType companyUserType) throws CustomException ;
}
