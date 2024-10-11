package com.inventarios.pc.inventarios_pc_be.shared.requests;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarUsuarioRequest {
    
    private Integer rol;

    private String primerNombre;

    private String segundoNombre;

    private String primerApellido;

    private String segundoApellido;

    private Integer tipoDocumento;

    private String cedula;

    private String correo;

    private String telefono;

    private Date fechaNacimiento;

    private Integer ubicacionId;

}
