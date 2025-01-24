package com.nexmind3.octoaqua.util;

import com.nexmind3.octoaqua.dto.RoleDto;
import com.nexmind3.octoaqua.exception.CustomException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {



    public void validateRole(RoleDto roleDto) throws Exception {
        if(StringUtils.isBlank(roleDto.getName())){
            throw new CustomException("Name of role cannot be empty.");
        }

    }


}
