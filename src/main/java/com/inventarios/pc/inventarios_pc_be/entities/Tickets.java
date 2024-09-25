package com.inventarios.pc.inventarios_pc_be.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.*;
//Esta entidad crea la tabla tickets donde se almacenan los tickets que sse generan para dar solucion de las solicitudes a traves de los tecnicos de sistemas

import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
public class Tickets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @UniqueElements
    @Column(nullable = false, unique = false)
    private String nombre;

    private Date fechaCierre;

    @ManyToOne
    @JoinColumn(name= "usuario_id")
    private Usuario usuario;

    private Date fecha_asig;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "solicitud_id")
    private Solicitudes solicitudes;

    private String observacion;

    @ManyToOne
    @JoinColumn(name = "estado_ticket_id")
    private EstadoTickets estadoTickets;

}
