package com.inventarios.pc.inventarios_pc_be.shared.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {

    private Integer HttpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;

}