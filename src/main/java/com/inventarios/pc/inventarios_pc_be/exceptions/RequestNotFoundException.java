package com.inventarios.pc.inventarios_pc_be.exceptions;

public class RequestNotFoundException extends Exception {
    
    public RequestNotFoundException(String message){
        super(message);
    }
}
