package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

//Esta es la entidad que crea en la base de datos la tabla tipo de solicitudes (reparación, cambio de ubicación)

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tipo_solicitudes")
public class TipoSolicitudes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String nombre;

}
