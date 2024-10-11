package com.inventarios.pc.inventarios_pc_be.shared.DTOs;

import org.hibernate.validator.constraints.UniqueElements;
import java.util.*;
import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.shared.validators.MayorDeEdad;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Integer rol;

    private String primerNombre;

    private String segundoNombre;

    private String primerApellido;

    private String segundoApellido;

    private Integer tipoDocumento;

    @NotEmpty(message = "La cédula no puede estar vacía")
    @Size(min = 6, max = 10, message = "La cédula debe tener entre 6 y 10 dígitos")
    private String cedula;

    @NotEmpty(message = "El correo no puede estar vacío")
    @Email(message = "Debe proporcionar un correo electrónico válido")
    private String correo;

    @NotEmpty
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
     @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$",
             message = "La contraseña debe contener al menos una letra mayuscula, una letra minuscula, un numero y un caracter especial.")
    private String password;

     @Pattern(regexp = "3\\d{9}", message = "El número de teléfono debe tener 10 dígitos y comenzar con el número 3")
    @Column(nullable = true) // Si es opcional, puedes dejarlo como nullable = true
    private String telefono;

    @MayorDeEdad(message = "El usuario debe ser mayor de 18 años")
    private Date fechaNacimiento;

    private Integer ubicacionId;

    private Boolean delete_flag;
}
