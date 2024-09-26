package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.services.implementations.RolServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.RolDTO;

@RestController
@RequestMapping("/admin/rol")
public class RolController {
    
    @Autowired
    private RolServiceImplementation rolServiceImplementation;

    @GetMapping
    public ResponseEntity<List<RolDTO>> getRoles(){
        return ResponseEntity.ok(
            rolServiceImplementation.listarRoles().stream().map(rol ->{
                    RolDTO rolDTO = new RolDTO();
                    BeanUtils.copyProperties(rol, rolDTO);
                    return rolDTO;
                }).collect(Collectors.toList()));
    }
    
}


