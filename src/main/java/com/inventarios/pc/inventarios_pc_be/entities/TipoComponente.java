package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

//Esta es la entidad que genera la tabla de tipos de dispositivos en la base de datos que es para clasificar los tipos de perifericos que puede tener conectado un equipo
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tipo_componentes")
public class TipoComponente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String nombre;

   @NotNull
    @Column(name = "delete_flag", nullable = false)
    private Boolean deleteFlag;
}
