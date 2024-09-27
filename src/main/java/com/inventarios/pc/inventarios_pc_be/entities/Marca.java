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

//Esta es la entidad que genera la tabla en la base de datos donde se va a guardar las marcas de los dispositivos y de los equipos
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table (name = "marcas")
public class Marca {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    @NotEmpty
    private String nombre;

    private Boolean deleteFlag;
    
}

