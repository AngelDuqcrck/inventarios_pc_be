package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

//Esta es la entidad que crea en la base de datos la tabla tipo de almacenamiento donde se guarda los tipos de unidades de almacenamiento que puede tener un equipo
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tipo_almacenamiento")
public class TipoAlmacenamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String nombre;

}
