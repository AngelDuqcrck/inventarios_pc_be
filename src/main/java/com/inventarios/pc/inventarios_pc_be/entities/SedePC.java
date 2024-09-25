package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.Column;
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

// Esta es la entidad que crea la tabla sedes en la base de datos, son las
// diversas sedes que tiene la clinica y en las cuales presta servicios.
// Almacena el id, el nombre y la dirreción donde se encuentra la sede
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sedes_PC")
public class SedePC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(nullable = false)
    private String nombre;

    @NotEmpty
    @Column(nullable = false)
    private String dirección;

}
