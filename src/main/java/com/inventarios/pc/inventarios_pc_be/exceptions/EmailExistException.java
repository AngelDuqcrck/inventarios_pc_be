package com.inventarios.pc.inventarios_pc_be.exceptions;

public class EmailExistException extends  Exception{
    
    public EmailExistException(String message) {
        super(message);
    }
}