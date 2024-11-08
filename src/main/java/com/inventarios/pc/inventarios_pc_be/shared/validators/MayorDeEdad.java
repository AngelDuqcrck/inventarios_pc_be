package com.inventarios.pc.inventarios_pc_be.shared.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MayorDeEdadValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MayorDeEdad {
    String message() default "El usuario debe ser mayor de 15 años";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}