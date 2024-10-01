package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosResponse {

    private String nombreCompleto;

    private String correo;

    private String rol;

    private String ubicacion;
}
