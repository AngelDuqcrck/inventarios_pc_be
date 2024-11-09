package com.inventarios.pc.inventarios_pc_be.entities;

import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cambio_estado_solicitudes")
public class CambioEstadoSolicitudes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "solicitudes_id")
    private Solicitudes solicitudes;

    @ManyToOne
    @JoinColumn(name = "estado_solicitudes_id")
    private EstadoSolicitudes estadoSolicitudes;

    private Date fechaCambio;

    
}
