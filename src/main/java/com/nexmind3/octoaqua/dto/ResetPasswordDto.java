package com.nexmind3.octoaqua.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {


    private String changeKey;

    private String email;

    private String password;

    private String secondLevelPassword;

}
