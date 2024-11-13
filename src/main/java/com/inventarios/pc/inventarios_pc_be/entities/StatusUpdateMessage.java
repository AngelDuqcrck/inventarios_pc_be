package com.inventarios.pc.inventarios_pc_be.entities;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateMessage {
    private String type;
    private Integer id;
    private String status;
    private LocalDateTime timestamp;
}
