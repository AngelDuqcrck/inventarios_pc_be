package com.inventarios.pc.inventarios_pc_be.controllers;

import com.inventarios.pc.inventarios_pc_be.shared.responses.TokenRefreshRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TokenRefreshResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.exceptions.EmailExistException;
import com.inventarios.pc.inventarios_pc_be.exceptions.EmailNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.PasswordNotEqualsException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TokenNotValidException;
import com.inventarios.pc.inventarios_pc_be.security.JwtGenerador;
import com.inventarios.pc.inventarios_pc_be.services.implementations.UsuarioServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.LoginDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.AuthResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
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
    public Response registrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioCreado = usuarioService.registrarUsuario(usuarioDTO);
        if (usuarioCreado == null) {
            return new Response("Error al crear el usuaio");
        } else {
            return new Response("Usuario creado exitosamente");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getCorreo(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtGenerador.generarToken(authentication);
        String refreshToken = jwtGenerador.generarRefreshToken(authentication);

        return new ResponseEntity<>(new AuthResponse(accessToken, refreshToken), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        if (jwtGenerador.validarRefreshToken(refreshToken)) {
            String correo = jwtGenerador.obtenerCorreoDeJWT(refreshToken);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Generar nuevo Access Token y Refresh Token
            String newAccessToken = jwtGenerador.generarToken(authentication);
            String newRefreshToken = jwtGenerador.generarRefreshToken(authentication);

            return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, newRefreshToken));
        }
        return ResponseEntity.status(401).body(null);
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<HttpResponse> recuperarPassword(@RequestParam String correo)throws EmailNotFoundException{
        
        usuarioService.enviarTokenRecuperacion(correo);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Se le ha enviado un correo con instrucciones para restablecer su contraseña"),
                HttpStatus.OK);
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<HttpResponse> resetPassword(@RequestParam String token, @RequestParam String nuevaContraseña, @RequestParam String nuevaContraseña2)throws EmailNotFoundException, TokenNotValidException, PasswordNotEqualsException{
        usuarioService.restablecerpassword(token, nuevaContraseña, nuevaContraseña2);

        return new ResponseEntity<>(
            new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                    " Contraseña actualizada exitosamente"),
            HttpStatus.OK);
    }
}
