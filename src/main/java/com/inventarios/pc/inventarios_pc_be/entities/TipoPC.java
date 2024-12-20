package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

//Esta es la entidad que crea en la base de datos la tabla tipo de pc donde se guarda los tipos de computador que hay (torre, portatil, all in one, etc)

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tipo_pc")
public class TipoPC {
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

