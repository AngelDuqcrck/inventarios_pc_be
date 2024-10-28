package com.inventarios.pc.inventarios_pc_be.shared.DTOs;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarcaDTO {

    private int id;

    private String nombre;

    private String tipoMisc;

    private Boolean deleteFlag;
}
