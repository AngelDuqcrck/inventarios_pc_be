package com.inventarios.pc.inventarios_pc_be.entities;

import java.util.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//Esta entidad se encarga de guardar en la base de datos los software que se encuentran instalados en un equipo
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "software_csa")
public class SoftwareCSA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "software_pc_id")
    private SoftwarePC softwarePC;

    @ManyToOne
    @JoinColumn(name = "computador_id")
    private Computador computador;

    private Date fechaDesvinculacion;

    private Date fechaVinculacion;

    private String justificacion;

}
