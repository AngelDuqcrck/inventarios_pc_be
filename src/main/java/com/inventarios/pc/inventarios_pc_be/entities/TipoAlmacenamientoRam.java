package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//Esta entidad crea en la base de datos la tabla tipo almacenamiento y ram la cual almacena los tipos de dispositivos de almacenamiento y los tipos de memoria
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipo_almacenamiento_ram")
public class TipoAlmacenamientoRam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String nombre;

    @NotNull
    @Column(name = "delete_flag", nullable = false)
    private Boolean deleteFlag;
}
