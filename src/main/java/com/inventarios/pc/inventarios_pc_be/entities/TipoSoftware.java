package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

//Esta es la entidad que crea en la base de datos la tabla tipo de software donde se guarda los tipos de software que pueden tener instalados (libre, propietario, etc)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tipo_software")
public class TipoSoftware {
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
