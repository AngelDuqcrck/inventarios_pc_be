package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Esta es la entidad que genera la tabla de tipos de dispositivos en la base de datos que es para clasificar los tipos de perifericos que puede tener conectado un equipo
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tipo_dispositivos")
public class TipoDispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String nombre;
}
