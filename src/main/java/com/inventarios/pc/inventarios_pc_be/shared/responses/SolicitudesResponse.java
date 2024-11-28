package com.inventarios.pc.inventarios_pc_be.shared.responses;

import java.util.Date;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitudesResponse {
    
    public Integer id;

    public String titulo;

    public String responsable;

    public String role;

    public String tipoSolicitud;

    public String estadoSolicitud;

    public Date fechaCreacion;

    private Date fechaCierre;
}
