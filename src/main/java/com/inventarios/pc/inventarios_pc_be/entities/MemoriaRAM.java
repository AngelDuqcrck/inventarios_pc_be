package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

//Esta es la entidad que crea en la base de datos la tabla de memorias RAM donde se almacena la cantidad de memoria RAM que pueda tener un equipo
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "memoria_ram")
public class MemoriaRAM {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String cantidad;

}
