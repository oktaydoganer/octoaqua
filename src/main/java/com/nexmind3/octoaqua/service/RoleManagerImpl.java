package com.nexmind3.octoaqua.service;

import com.nexmind3.octoaqua.dto.RoleDto;
import com.nexmind3.octoaqua.entity.Role;
import com.nexmind3.octoaqua.repository.RoleRepository;
import com.nexmind3.octoaqua.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleManagerImpl implements RoleManager {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public Role save(RoleDto roleDto) {
        Role role = mapperUtil.convertToEntity(roleDto);
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }


}
