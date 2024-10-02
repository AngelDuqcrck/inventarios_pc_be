package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.*;
import java.util.*;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.RolDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoDocDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UbicacionDTO;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {

    private int id;

    private Rol rol;

    private String primerNombre;

    private String segundoNombre;

    private String primerApellido;

    private String segundoApellido;

    private TipoDocumento tipoDocumento;

    private String cedula;

    private String correo;

    private String telefono;

    private Date fechaNacimiento;

    private UbicacionResponse ubicacionId;

    private Boolean delete_flag;
}
