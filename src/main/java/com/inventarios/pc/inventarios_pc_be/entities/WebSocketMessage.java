package com.inventarios.pc.inventarios_pc_be.entities;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage<T> {
    private String type;
    private Integer id;
    private String status;
    private Date date;
    private T data;
    private LocalDateTime timestamp;

    public WebSocketMessage(String type, Integer id, T data) {
        this.type = type;
        this.id = id;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.date = new Date();
    }
}