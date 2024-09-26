package com.inventarios.pc.inventarios_pc_be.entities;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
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
@Table (name = "usuarios")
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
    @JoinColumn (name = "tipo_documento_id")
    private TipoDocumento tipoDocumento;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String cedula;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String correo;

    /*
     * Estas anotaciones son utilizadas para validar los siguientes requisitos de la
     * contraseña
     * Mínimo 8 carácteres.
     * Tener una mezcla entre:
     * Letras mayúsculas (A-Z)
     * Letras minúsculas (a-z)
     * Números (0-9)
     * Caracteres especiales (!, @, #, $, %, etc.)
     */
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un número y un carácter especial.")
    @NotEmpty
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String contraseña;

    /*
     * Se valida q el numero de telefono solo reciba numeros, que inicie por 3 y que contenga maximo 10 digitos
     */
    /*
     * @Pattern(
        regexp = "^3\\d{9}$",
        message = "El teléfono debe comenzar con el número 3, contener solo números y tener exactamente 10 dígitos."
    )
     */
    @Size(min = 10, max = 10)
    private String telefono;

    private Date fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacionId;
}
