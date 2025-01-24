package com.nexmind3.octoaqua.util;

import com.nexmind3.octoaqua.dto.UserDto;
import com.nexmind3.octoaqua.exception.CustomException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserValidator {



    public void validateUser(UserDto userDto) throws Exception {
        if(StringUtils.isBlank(userDto.getEmail())){
            throw new CustomException("Email field cannot be empty.");
        }

        Boolean isEmailValid = Pattern.compile(ValidationRegexConstants.EMAIL_VALIDATOR).matcher(userDto.getEmail()).matches();
        if(!isEmailValid){
            throw new CustomException("Email not matched with requirements.");
        }

        if(StringUtils.isBlank(userDto.getPassword())){
            throw new CustomException("Password field cannot be empty.");
        }

        Boolean isPasswordValid = Pattern.compile(ValidationRegexConstants.PASSWORD_VALIDATOR).matcher(userDto.getPassword()).matches();
        if(!isPasswordValid){
            throw new CustomException("Password not matched with requirements.");
        }


    }


}
