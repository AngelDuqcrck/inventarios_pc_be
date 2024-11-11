package com.inventarios.pc.inventarios_pc_be.entities;

import java.util.Date;

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

    
    private String descripcion;
    

    private Date fechaCreacion;

    private Date fechaCierre;
    
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

    @ManyToOne
    @JoinColumn(name = "estado_solicitudes_id")
    private EstadoSolicitudes estadoSolicitudes;

    @ManyToOne
    @JoinColumn(name = "ubicacion_origen_id")
    private Ubicacion ubicacionOrigen;

    @ManyToOne
    @JoinColumn(name = "ubicacion_destino_id")
    private Ubicacion ubicacionDestino;

    @ManyToOne
    @JoinColumn( name = "dispositivo_id")
    private DispositivoPC dispositivoPC;


    @ManyToOne
    @JoinColumn(name = "software_id")
    private  SoftwarePC softwarePC;

    private Boolean esHardaware;
}
