package com.inventarios.pc.inventarios_pc_be.security;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;

@Component
public class SessionManager {
    private final ConcurrentHashMap<String, String> activeUserSessions = new ConcurrentHashMap<>();
    
    public void registerUserSession(String username, String token) {
        // Si existe una sesión previa, la invalidamos
        invalidateUserSession(username);
        // Registramos la nueva sesión
        activeUserSessions.put(username, token);
    }
    
    public boolean isTokenValid(String username, String token) {
        String activeToken = activeUserSessions.get(username);
        return activeToken != null && activeToken.equals(token);
    }
    
    public void invalidateUserSession(String username) {
        activeUserSessions.remove(username);
    }
    
    public Optional<String> getActiveToken(String username) {
        return Optional.ofNullable(activeUserSessions.get(username));
    }
    
    public boolean hasActiveSession(String username) {
        return activeUserSessions.containsKey(username);
    }
}