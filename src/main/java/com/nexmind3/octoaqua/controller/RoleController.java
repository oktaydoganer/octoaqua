package com.nexmind3.octoaqua.controller;

import com.nexmind3.octoaqua.dto.RoleDto;
import com.nexmind3.octoaqua.entity.Role;
import com.nexmind3.octoaqua.service.RoleManager;
import com.nexmind3.octoaqua.util.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/role")
@RestController
@RequiredArgsConstructor
public class RoleController {

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private RoleValidator roleValidator;

    @PostMapping("/save")
    public Role saveRole(@RequestBody RoleDto roleDto) throws Exception {
        try{
            roleValidator.validateRole(roleDto);
            Role role =  roleManager.save(roleDto);
            return role;
        }catch (Exception e){
            throw new Exception(e);
        }
    }
    // Yeni bir role "Camera Manager" eklemek için
    @PostMapping("/addCameraRole")
    public Role addCameraRole() throws Exception {
        RoleDto cameraRoleDto = RoleDto.builder()
                .name("CAMERA_MANAGER")
                .description("Role to manage camera operations")
                .build();
        return roleManager.save(cameraRoleDto);
    }

    @GetMapping("/all")
    public List<Role> getAllRoles() throws Exception {
        try{
            return roleManager.getAll();
        }catch (Exception e){
            throw new Exception(e);
        }
    }

}
