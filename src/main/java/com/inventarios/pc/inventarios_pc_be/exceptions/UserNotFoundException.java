package com.inventarios.pc.inventarios_pc_be.exceptions;


public class UserNotFoundException extends  Exception{
    public UserNotFoundException(String message) {
        super(message);
    }
}
