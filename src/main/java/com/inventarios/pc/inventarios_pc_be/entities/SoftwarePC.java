package com.inventarios.pc.inventarios_pc_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
//Esta entidad crea la tabla software pc en la base de datos donse se almacenan los diferentes software que pueden estar instalados en un equipo

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "software_pc")
public class SoftwarePC {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String nombre;

    private String version;

    private String empresa;

     @NotNull
    @Column(name = "delete_flag", nullable = false)
    private Boolean deleteFlag;
    
    @ManyToOne
    @JoinColumn(name = "tipo_software_id")
    private TipoSoftware tipoSoftware;

}
