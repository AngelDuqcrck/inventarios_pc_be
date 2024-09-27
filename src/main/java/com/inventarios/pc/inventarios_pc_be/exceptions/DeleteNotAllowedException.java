package com.inventarios.pc.inventarios_pc_be.exceptions;

public class DeleteNotAllowedException extends Exception{
    
    public DeleteNotAllowedException(String message){
        super(message);
    }
}
