package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.TipoComponente;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoComponenteRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoComponenteService;

@Service
public class TipoComponenteServiceImplementation implements ITipoComponenteService{

    @Autowired
    TipoComponenteRepository tipoComponenteRepository;

    /**
     * Obtiene una lista de todos los tipos de documentos (DNI) registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoDocumento} que representan todos los tipos de documentos.
     */
    @Override
    public List<TipoComponente> listarTipoComponente(){
        return (List<TipoComponente>) tipoComponenteRepository.findAll();
    }
}
