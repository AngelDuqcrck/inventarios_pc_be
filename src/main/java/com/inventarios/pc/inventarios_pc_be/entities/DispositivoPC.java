package com.inventarios.pc.inventarios_pc_be.entities;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

/*
 * Esta es la entidad que nos permite crear la tabla dispositivos_pc en la base de datos
 * Aqui se almacena el id, el tipo de dispositivo, el nombre, el modelo, la placa, 
 * la marca, su estado
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table (name = "dispositivos_pc")
public class DispositivoPC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_dispositivo_id")
    private TipoDispositivo tipoDispositivo;

    
    private String modelo;

    @ManyToOne
    @JoinColumn(name = "estado_dispositivo_id")
    private EstadoDispositivo estadoDispositivo;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Size(max = 15, message = "La placa debe tener como máximo 15 caracteres")
    private String placa;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

    
    private String serial;

    @NotEmpty
    @Column(nullable = false)
    private String nombre;
}
