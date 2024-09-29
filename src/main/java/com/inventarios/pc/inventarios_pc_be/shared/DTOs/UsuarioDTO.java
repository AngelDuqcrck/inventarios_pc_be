package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import org.hibernate.validator.constraints.UniqueElements;
import java.util.*;
import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Integer id;

    private Integer rolId;

    private String primerNombre;

    private String segundoNombre;

    private String primerApellido;

    private String segundoApellido;

    private Integer tipoDocumento;

    private String cedula;

    private String correo;

    private String password;

    private String telefono;

    private Date fechaNacimiento;

    private Integer ubicacionId;
}
