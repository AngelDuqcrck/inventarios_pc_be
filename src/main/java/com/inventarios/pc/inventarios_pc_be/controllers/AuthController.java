package com.inventarios.pc.inventarios_pc_be.controllers;

import com.inventarios.pc.inventarios_pc_be.shared.responses.TokenRefreshRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TokenRefreshResponse;

import jakarta.validation.Valid;

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

import com.inventarios.pc.inventarios_pc_be.exceptions.DocumentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.EmailExistException;
import com.inventarios.pc.inventarios_pc_be.exceptions.EmailNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.PasswordNotEqualsException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TokenNotValidException;
import com.inventarios.pc.inventarios_pc_be.security.JwtGenerador;
import com.inventarios.pc.inventarios_pc_be.services.implementations.UsuarioServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.LoginDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UsuarioDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarPasswordRequest;
import com.inventarios.pc.inventarios_pc_be.shared.requests.NuevaPasswordRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.AuthResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.Response;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//Controlador donde se encuentran los endpoints de autenticacion tales como el login, el cambiar contraseña cuando fue olvidada y el envio de correo para recuperar contraseña
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtGenerador jwtGenerador;
    @Autowired
    private UsuarioServiceImplementation usuarioService;

    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param usuarioDTO Objeto que contiene los datos del usuario a registrar.
     * @return Respuesta indicando si el usuario fue creado exitosamente o si
     *         ocurrió un error.
     */
    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO)
            throws LocationNotFoundException, RolNotFoundException, DocumentNotFoundException, EmailExistException {
        usuarioService.registrarUsuario(usuarioDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Usuario Registrado con exito"),
                HttpStatus.OK);
    }

    /**
     * Autentica a un usuario y genera un token de acceso y un refresh token.
     * 
     * @param loginDTO Objeto que contiene el correo electrónico y la contraseña del
     *                 usuario.
     * @return Respuesta HTTP con el token de acceso y el refresh token si la
     *         autenticación es exitosa.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getCorreo(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtGenerador.generarToken(authentication);
        String refreshToken = jwtGenerador.generarRefreshToken(authentication);

        return new ResponseEntity<>(new AuthResponse(accessToken, refreshToken), HttpStatus.OK);
    }

    /**
     * Genera un nuevo access token y refresh token utilizando el refresh token
     * proporcionado.
     * 
     * @param request Objeto que contiene el refresh token.
     * @return Respuesta HTTP con los nuevos tokens si el refresh token es válido,
     *         de lo contrario una respuesta de error.
     */
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

    /**
     * Envía un correo de recuperación de contraseña al usuario con un enlace para
     * restablecerla.
     * 
     * @param correo El correo electrónico del usuario que solicita la recuperación.
     * @return Respuesta HTTP con un mensaje indicando que el correo fue enviado.
     * @throws EmailNotFoundException Si no se encuentra un usuario asociado al
     *                                correo proporcionado.
     */
    @PostMapping("/recuperar-password")
    public ResponseEntity<HttpResponse> recuperarPassword(@RequestParam String correo) throws EmailNotFoundException {

        usuarioService.enviarTokenRecuperacion(correo);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Se le ha enviado un correo con instrucciones para restablecer su contraseña"),
                HttpStatus.OK);
    }

    /**
     * Restablece la contraseña de un usuario utilizando un token de recuperación.
     * 
     * @param token            El token de recuperación generado para el usuario.
     * @param nuevaContraseña  La nueva contraseña que el usuario desea establecer.
     * @param nuevaContraseña2 La confirmación de la nueva contraseña.
     * @return Respuesta HTTP con un mensaje indicando que la contraseña fue
     *         actualizada exitosamente.
     * @throws EmailNotFoundException     Si no se encuentra un usuario asociado al
     *                                    token.
     * @throws TokenNotValidException     Si el token proporcionado no es válido.
     * @throws PasswordNotEqualsException Si las nuevas contraseñas no coinciden.
     */
    @PostMapping("/cambiar-password")
    public ResponseEntity<HttpResponse> resetPassword(@Valid @RequestBody NuevaPasswordRequest nuevaPasswordRequest)
            throws EmailNotFoundException, TokenNotValidException, PasswordNotEqualsException {
        usuarioService.restablecerpassword(nuevaPasswordRequest.getToken(), nuevaPasswordRequest.getNuevaPassword(), nuevaPasswordRequest.getNuevaPassword2());

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        " Contraseña actualizada exitosamente"),
                HttpStatus.OK);
    }

}
