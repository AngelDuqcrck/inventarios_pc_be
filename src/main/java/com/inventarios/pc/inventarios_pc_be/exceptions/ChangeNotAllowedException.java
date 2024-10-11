package com.inventarios.pc.inventarios_pc_be.exceptions;

public class ChangeNotAllowedException extends Exception {
    public ChangeNotAllowedException (String message){
        super(message);
    }
}
