package com.inventarios.pc.inventarios_pc_be.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
//Esta entidad es la que crea la tabla estado de los tickets y almacena los estados que puede tener un ticket
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "estado_tickets")
public class EstadoTickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    private String nombre;
}