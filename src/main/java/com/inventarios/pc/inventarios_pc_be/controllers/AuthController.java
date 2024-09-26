package com.inventarios.pc.inventarios_pc_be.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.services.implementations.UsuarioServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.Response;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UsuarioServiceImplementation usuarioService;

    @PostMapping("/register")
    public Response registrarUsuario (@RequestBody UsuarioDTO usuarioDTO) {
       UsuarioDTO usuarioCreado = usuarioService.registrarUsuario(usuarioDTO);
       if(usuarioCreado == null){
        return new Response("Error al crear el usuaio");
       }else{
        return new Response("Usuario creado exitosamente");
       }
    }
    

}
