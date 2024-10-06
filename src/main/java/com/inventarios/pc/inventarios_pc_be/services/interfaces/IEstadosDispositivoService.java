package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;

public interface IEstadosDispositivoService {
/**
     * Obtiene una lista de todos los estados de los dispositivos registrados en el sistema.
     * 
     * @return Una lista de objetos {@link EstadoDispositivo} que representan todos los estados de los dispositivos.
     */
    public List<EstadoDispositivo> listarEstadosDisp();
}
