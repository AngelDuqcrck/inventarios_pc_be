package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
// Esta entidad crea en la base de datos la tabla solicitudes y en ella se guardan las solicitudes que realizan a sistemas para resolver problemas

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "solicitudes")
public class Solicitudes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String descripcion;
    
    @NotEmpty
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "computador_id")
    private Computador computador;

    @ManyToOne
    @JoinColumn(name = "tipo_solicitudes_id")
    private TipoSolicitudes tipoSolicitudes;
}
