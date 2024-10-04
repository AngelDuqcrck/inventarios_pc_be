package com.inventarios.pc.inventarios_pc_be.shared.DTOs;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComponenteDTO {
    
     private String nombre;

    private Boolean deleteFlag;

    
    private Integer tipoComponente;
}
