package com.inventarios.pc.inventarios_pc_be.entities;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import com.inventarios.pc.inventarios_pc_be.shared.validators.MayorDeEdad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Esta es la entidad que crea la tabla usuarios en la base de datos, los usuarios tienen sus datos personales, a la vez cuentan con un rol y la ubicación donde se encuentran laborando
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rolId;

    @NotEmpty
    @Column(nullable = false)
    private String primerNombre;

    private String segundoNombre;

    @NotEmpty
    @Column(nullable = false)
    private String primerApellido;

    private String segundoApellido;

    @ManyToOne
    @JoinColumn(name = "tipo_documento_id")
    private TipoDocumento tipoDocumento;

    @NotEmpty(message = "La cédula no puede estar vacía")
    @Size(min = 6, max = 20, message = "El numero de documento debe tener entre 6 y 20 dígitos")
    @Column(nullable = false, unique = true)
    private String cedula;

    @NotEmpty(message = "El correo no puede estar vacío")
    @Email(message = "Debe proporcionar un correo electrónico válido")
    @Column(nullable = false, unique = true)
    private String correo;

    /*
     * Estas anotaciones son utilizadas para validar los siguientes requisitos de la
     * password
     * Mínimo 8 carácteres.
     * Tener una mezcla entre:
     * Letras mayúsculas (A-Z)
     * Letras minúsculas (a-z)
     * Números (0-9)
     * Caracteres especiales (!, @, #, $, %, etc.)
     */

    @NotEmpty
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$", message = "La contraseña debe contener al menos una letra mayuscula, una letra minuscula, un numero y un caracter especial.")
    @Column(nullable = false)
    private String password;

    @Size(min = 10, message = "El número de teléfono debe tener 10 digitos")
    @Pattern(regexp = "^[3-6]\\d{9}$", message = "El número de teléfono debe tener 10 digitos y comenzar con 3 o 6")
    private String telefono;

    @MayorDeEdad(message = "El usuario debe ser mayor de 18 años y menor a 125 años")
    private Date fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacionId;

    @NotNull
    @Column(name = "delete_flag", nullable = false)
    private Boolean deleteFlag;

}
