package com.inventarios.pc.inventarios_pc_be.entities;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

//Esta es la entidad que crea en la base de datos la tabla computador, aqui se almacena toda la informacion de un equipo de computo

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "computadores")
public class Computador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_pc_id")
    private TipoPC tipoPC;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Usuario responsable;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String nombre;


    private String modelo;

    private String serial;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "procesador_id")
    private Componente procesador;

    @ManyToOne
    @JoinColumn(name = "ram_id")
    private Componente ram;

    @ManyToOne
    @JoinColumn(name = "almacenamiento_id")
    private Componente almacenamiento;

    @ManyToOne
    @JoinColumn(name = "tipo_almacenamiento_id")
    private TipoAlmacenamiento tipoAlmacenamiento;

    @ManyToOne
    @JoinColumn(name = "tipo_ram_id")
    private TipoRam tipoRam;

    @ManyToOne
    @JoinColumn(name = "propietario_id")
    private Propietario propietario;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Size(max = 15, message = "La placa debe tener como máximo 15 caracteres")
    private String placa;

    @Column(unique = true)
    private String ipAsignada;

    @ManyToOne
    @JoinColumn(name = "estado_dispositivo_id")
    private EstadoDispositivo estadoDispositivo;

}
