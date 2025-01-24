package com.nexmind3.octoaqua.dto;

import com.nexmind3.octoaqua.enumeration.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private Long id;

    private String name;

    private String description;

    List<Long> authorities;

    private State state;


}
