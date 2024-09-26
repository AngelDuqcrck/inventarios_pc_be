package com.inventarios.pc.inventarios_pc_be.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.security.JwtGenerador;
import com.inventarios.pc.inventarios_pc_be.services.implementations.UsuarioServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.LoginDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.AuthResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.Response;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtGenerador jwtGenerador; 
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
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
        (loginDTO.getCorreo(), loginDTO.getContraseña()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerador.generarToken(authentication);

        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

}
