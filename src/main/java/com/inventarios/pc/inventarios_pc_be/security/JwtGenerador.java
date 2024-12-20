package com.inventarios.pc.inventarios_pc_be.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import com.inventarios.pc.inventarios_pc_be.security.CustomUserDetailsService.CustomUserDetails;

import org.springframework.security.core.Authentication;

import java.util.Date;

@Component
public class JwtGenerador {

     @Autowired
    private SessionManager sessionManager;

    // Metodo para crear un token por medio de la autenticación
    public String generarToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String correo = userDetails.getUsername();
        String role = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TIME_TOKEN);

        // Aqui generamos el token con la información adicional
        String token = Jwts.builder()
                .setSubject(correo)
                .claim("role", role)
                .claim("primerNombre", userDetails.getPrimerNombre())
                .claim("primerApellido", userDetails.getPrimerApellido())
                .setIssuedAt(new Date())
                .setExpiration(expiracionToken)
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA)
                .compact();
            
                sessionManager.registerUserSession(correo, token);

        return token;
    }

    // Método para generar un Refresh Token
    public String generarRefreshToken(Authentication authentication) {
        String correo = authentication.getName();
        Date tiempoActual = new Date();
        Date expiracionRefreshToken = new Date(
                tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TIME_REFRESH_TOKEN);

        return Jwts.builder()
                .setSubject(correo)
                .setIssuedAt(tiempoActual)
                .setExpiration(expiracionRefreshToken)
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA)
                .compact();
    }

    // Metodo para extaer un correo a partir de un token
    public String obtenerCorreoDeJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ConstantesSeguridad.JWT_FIRMA)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Metodo para validar el token
    public Boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT ha expirado o es incorrecto");
        }
    }

    // Validar si el Refresh Token ha expirado
    public Boolean validarRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token).getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false; // Refresh Token inválido o expirado
        }
    }

    public Boolean validarTokenRecuperacion(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(ConstantesSeguridad.JWT_FIRMA)
                    .parseClaimsJws(token)
                    .getBody();
    
            // Verificamos si el token ha expirado
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false; // Token inválido o expirado
        }
    }
    

    // Método para generar un token único de recuperación de password
    public String generarTokenRecuperacion(String email) {
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(
                tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TIME_PASSWORD_RESET);

        // Generamos el token basado en el correo del usuario
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expiracionToken)
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA)
                .compact();
    }

}
