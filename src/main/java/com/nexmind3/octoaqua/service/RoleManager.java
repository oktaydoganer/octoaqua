package com.nexmind3.octoaqua.service;


import com.nexmind3.octoaqua.dto.RoleDto;
import com.nexmind3.octoaqua.entity.Role;

import java.util.List;

public interface RoleManager {

    Role save(RoleDto roleDto);

    List<Role> getAll();

}
