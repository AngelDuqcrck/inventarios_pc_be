package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {

    private int id;

    private String rol;

    private String primerNombre;

    private String segundoNombre;

    private String primerApellido;

    private String segundoApellido;

    private String tipoDocumento;

    private String cedula;

    private String correo;

    private String telefono;

    private Date fechaNacimiento;

    private String ubicacion;

    private Boolean delete_flag;
}
