package com.inventarios.pc.inventarios_pc_be.security;

import com.inventarios.pc.inventarios_pc_be.entities.SesionActiva;
import com.inventarios.pc.inventarios_pc_be.repositories.SesionActivaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Component
public class SessionManager {
    
    @Autowired
    private SesionActivaRepository sesionActivaRepository;
    
    @Transactional
    public void registerUserSession(String username, String token) {
        // Calculamos la fecha de expiración del token
        Claims claims = Jwts.parser()
                .setSigningKey(ConstantesSeguridad.JWT_FIRMA)
                .parseClaimsJws(token)
                .getBody();
        
        LocalDateTime fechaExpiracion = new Date(claims.getExpiration().getTime())
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();

        // Creamos o actualizamos la sesión
        SesionActiva sesion = new SesionActiva(username, token, fechaExpiracion);
        sesionActivaRepository.save(sesion);
    }
    
    @Transactional
    public boolean isTokenValid(String username, String token) {
        Optional<SesionActiva> sesionOpt = sesionActivaRepository.findByCorreoUsuario(username);
        
        if (sesionOpt.isPresent()) {
            SesionActiva sesion = sesionOpt.get();
            boolean isValid = sesion.getToken().equals(token) && 
                            sesion.getFechaExpiracion().isAfter(LocalDateTime.now());
            
            if (isValid) {
                // Actualizamos la última actividad
                sesion.setUltimaActividad(LocalDateTime.now());
                sesionActivaRepository.save(sesion);
            }
            
            return isValid;
        }
        
        return false;
    }
    
    @Transactional
    public void invalidateUserSession(String username) {
        sesionActivaRepository.deleteById(username);
    }
    
    public Optional<String> getActiveToken(String username) {
        return sesionActivaRepository.findByCorreoUsuario(username)
                .map(SesionActiva::getToken);
    }
    
    public boolean hasActiveSession(String username) {
        Optional<SesionActiva> sesion = sesionActivaRepository.findByCorreoUsuario(username);
        return sesion.isPresent() && 
               sesion.get().getFechaExpiracion().isAfter(LocalDateTime.now());
    }
    
    // Tarea programada para limpiar sesiones expiradas
    @Scheduled(fixedRate = 3600000) // Ejecutar cada hora
    @Transactional
    public void limpiarSesionesExpiradas() {
        sesionActivaRepository.eliminarSesionesExpiradas(LocalDateTime.now());
    }
}