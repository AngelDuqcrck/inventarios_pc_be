package com.inventarios.pc.inventarios_pc_be.shared.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CambiarPasswordRequest {
    public String token;
    public String actualPassword;
    public String nuevaPassword;
    public String nuevaPassword2;
    
}
