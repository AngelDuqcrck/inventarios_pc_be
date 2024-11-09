package com.inventarios.pc.inventarios_pc_be.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class MayorDeEdadValidator implements ConstraintValidator<MayorDeEdad, Date> {

    @Override
    public void initialize(MayorDeEdad constraintAnnotation) {
    }

    @Override
    public boolean isValid(Date fechaNacimiento, ConstraintValidatorContext context) {
        if (fechaNacimiento == null) {
            return false;
        }
        
        LocalDate nacimiento = new java.sql.Date(fechaNacimiento.getTime()).toLocalDate();
        LocalDate ahora = LocalDate.now();
        return Period.between(nacimiento, ahora).getYears() >= 15;
    }
}
