package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
//Controlador donde se encuentran todos los endpoints relacionados con los usuarios
import com.inventarios.pc.inventarios_pc_be.shared.responses.UsuariosResponse;
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UsuarioServiceImplementation usuarioServiceImplementation;

    /**
     * Cambia la contraseña de un usuario autenticado.
     * 
     * @param cambiarPasswordRequest Objeto que contiene la contraseña actual y la
     *                               nueva contraseña.
     * @return Respuesta HTTP con un mensaje indicando el éxito o error de la
     *         operación.
     * @throws EmailNotFoundException     Si no se encuentra un usuario asociado al
     *                                    correo.
     * @throws TokenNotValidException     Si el token de autenticación no es válido.
     * @throws PasswordNotEqualsException Si la contraseña actual no coincide o las
     *                                    nuevas contraseñas no son iguales.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cambiar-password")
    public ResponseEntity<HttpResponse> cambiarPassword(@RequestBody CambiarPasswordRequest cambiarPasswordRequest)
            throws EmailNotFoundException, TokenNotValidException, PasswordNotEqualsException {
        usuarioServiceImplementation.cambiarContraseña(cambiarPasswordRequest);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Contraseña cambiada exitosamente"),
                HttpStatus.OK);
    }

   

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuariosResponse>> listarUsuarios(){
        List<UsuariosResponse> usuariosResponses = usuarioServiceImplementation.listarUsuarios();
        return new ResponseEntity<>(usuariosResponses, HttpStatus.OK);
    }
}
