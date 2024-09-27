package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.inventarios.pc.inventarios_pc_be.shared.responses.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.inventarios.pc.inventarios_pc_be.services.implementations.RolServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.RolDTO;

@RestController
@RequestMapping("/rol")
public class RolController {
    
    @Autowired
    private RolServiceImplementation rolServiceImplementation;

    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public Response crearRol (@RequestBody RolDTO rolDTO){
        RolDTO rol = rolServiceImplementation.crearRol(rolDTO);

        if(rol == null)
            return new Response("Error al crear el rol");

        else return  new  Response("Rol Creado exitosamente");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RolDTO>> getRoles(){
        return ResponseEntity.ok(
            rolServiceImplementation.listarRoles().stream().map(rol ->{
                    RolDTO rolDTO = new RolDTO();
                    BeanUtils.copyProperties(rol, rolDTO);
                    return rolDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public Response actualizarRol (@RequestBody RolDTO rolDTO){
        RolDTO rol = rolServiceImplementation.crearRol(rolDTO);

        if(rol == null)
            return new Response("Error al actualizar el rol");

        else return  new  Response("Rol actualizado exitosamente");
    }
    
}


