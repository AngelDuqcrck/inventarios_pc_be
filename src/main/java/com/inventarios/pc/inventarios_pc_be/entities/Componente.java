package com.inventarios.pc.inventarios_pc_be.entities;


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

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "componentes")
//Esta entidad crea en la base de datos la tabla componente, la cual almacena el procesador, la ram, la cantidad o tipo de almacenamiento que puede tener un equipo
public class Componente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String nombre;

    private Boolean deleteFlag;

    private String cantidad;

    @ManyToOne
    @JoinColumn(name = "tipo_componente_id")
    private TipoComponente tipoComponente;
}
