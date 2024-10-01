package com.inventarios.pc.inventarios_pc_be.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.exceptions.EmailNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.PasswordNotEqualsException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TokenNotValidException;
import com.inventarios.pc.inventarios_pc_be.services.implementations.UsuarioServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarPasswordRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UsuarioServiceImplementation usuarioServiceImplementation;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cambiar-password")
    public ResponseEntity<HttpResponse> cambiarPassword( @RequestBody CambiarPasswordRequest cambiarPasswordRequest) throws EmailNotFoundException, TokenNotValidException, PasswordNotEqualsException{
        usuarioServiceImplementation.cambiarContraseña(cambiarPasswordRequest);
        return new ResponseEntity<>(
            new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                    " Contraseña cambiada exitosamente"),
            HttpStatus.OK);
    }
}
