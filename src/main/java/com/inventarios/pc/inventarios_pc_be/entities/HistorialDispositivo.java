package com.inventarios.pc.inventarios_pc_be.entities;
import java.util.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
//En esta entidad se guarda el historial de los dispositivos que se han conectado a un equipo
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "historial_dispositivos")
public class HistorialDispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "dispositivo_pc_id")
    private DispositivoPC dispositivoPC;

    @ManyToOne
    @JoinColumn(name = "computador_id")
    private Computador computador;

    private Date fechaCambio;

    private Date fechaDesvinculacion;
}
