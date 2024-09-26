package com.inventarios.pc.inventarios_pc_be.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.util.Date;

@Component
public class JwtGenerador {

    //Metodo para crear un token por medio de la autenticación
    public String generarToken(Authentication authentication){
        String correo = authentication.getName();
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime()+ ConstantesSeguridad.JWT_EXPIRATION_TIME_TOKEN);

        //Aqui generamos el token
        String token = Jwts.builder()
                .setSubject(correo)
                .setIssuedAt(new Date())
                .setExpiration(expiracionToken)
                .signWith(SignatureAlgorithm.HS512 , ConstantesSeguridad.JWT_FIRMA)
                .compact();

        return  token;
    }

    //Metodo para extaer un correo a partir de un token
    public String obtenerCorreoDeJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(ConstantesSeguridad.JWT_FIRMA)
                .parseClaimsJws(token)
                .getBody();

        return  claims.getSubject();
    }

    //Metodo para validar el token
    public Boolean validarToken(String token){
        try {
            Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token);
            return  true;
        } catch (Exception e){
            throw  new AuthenticationCredentialsNotFoundException("JWT ha expirado o es incorrecto");
        }
    }
}
