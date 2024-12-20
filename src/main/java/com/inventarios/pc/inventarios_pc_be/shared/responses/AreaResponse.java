package com.inventarios.pc.inventarios_pc_be.shared.responses;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaResponse {
    
    private Integer id;

    private String nombre;

    private String desc;

    private String sede;

    private Boolean deleteFlag;

    private String rol;
}
