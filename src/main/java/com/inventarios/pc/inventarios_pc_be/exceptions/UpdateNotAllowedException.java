package com.inventarios.pc.inventarios_pc_be.exceptions;

public class UpdateNotAllowedException extends Exception {
    
    public UpdateNotAllowedException (String message){
        super(message);
    }
}
