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

//Esta es la entidad que genera la tabla en la base de datos  que va almacenar los distintos tipos de estados que puede tener un dispositivo/periferico
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "estado_dispositivos")
public class EstadoDispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String nombre;

    private Boolean deleteFlag;
    
}
