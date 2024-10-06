package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IEstadosDispositivoService;

@Service
public class EstadosDispositivoServiceImplementation implements IEstadosDispositivoService{

    @Autowired
    EstadoDispositivoRepository estadoDispositivoRepository;

    /**
     * Obtiene una lista de todos los tipos de documentos (DNI) registrados en el sistema.
     * 
     * @return Una lista de objetos {@link EstadoDispositivo} que representan todos los tipos de documentos.
     */
    @Override
    public List<EstadoDispositivo> listarEstadosDisp(){
        return (List<EstadoDispositivo>) estadoDispositivoRepository.findAll();
    }
}
