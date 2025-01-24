package com.nexmind3.octoaqua.dto;

import com.nexmind3.octoaqua.enumeration.State;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private boolean cameraAccess;

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private String legalNumber;

    private State state;

    private boolean dummyUser ;

    private Integer userType ;


}
