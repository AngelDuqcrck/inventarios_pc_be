package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Esta es la entidad que crea la tabla ubicaciones en la base de datos, las
// ubicaciones cuentan con un id, el nombre y el area donde se encuentra
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ubicaciones")
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private AreaPC areaId;

}
