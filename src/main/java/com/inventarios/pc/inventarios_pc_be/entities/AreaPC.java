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
import lombok.*;

// Esta entidad crea las diversas areas que tiene una sede, para ello almacena
// el id, un nombre y la sede donde se encuentra esta area
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "areas_PC")

public class AreaPC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "sede_id")
    private SedePC sedeId;
}
