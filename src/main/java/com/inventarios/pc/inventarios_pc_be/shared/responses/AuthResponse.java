package com.inventarios.pc.inventarios_pc_be.shared.responses;
//Esta clase nos va a devolver la informacion con el token y el tipo que este sea


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuthResponse {

    
    private String tokenType = "Bearer ";

    private String accessToken;

    public AuthResponse(String accessToken){
        this.accessToken = accessToken;
    }
}
