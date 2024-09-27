package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
//Esta entidad es la que crea la tabla estado de solicitudes y almacena los estados que puede tener una solicitud
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "estado_solicitudes")
public class EstadoSolicitudes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String nombre;

    private Boolean deleteFlag;
}