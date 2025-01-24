package com.nexmind3.octoaqua.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSecurityKeyDto {

    private Long id;

    private Long userId;

    private Date expirationTime;

    private int securityKeyType;

    private int state;

    private String key;


}
