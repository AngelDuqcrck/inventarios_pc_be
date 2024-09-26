package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String correo;

    private String contraseña;
}
