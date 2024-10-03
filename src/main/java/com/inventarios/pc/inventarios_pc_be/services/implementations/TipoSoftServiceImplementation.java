package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.TipoSoftware;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoSoftwareRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoSoftwareService;

@Service
public class TipoSoftServiceImplementation implements ITipoSoftwareService{


    @Autowired
    TipoSoftwareRepository tipoSoftwareRepository;

    /**
     * Obtiene una lista de todos los tipos de documentos (DNI) registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoDocumento} que representan todos los tipos de documentos.
     */
    @Override
    public List<TipoSoftware> listarTipoSoftware(){
        return (List<TipoSoftware>) tipoSoftwareRepository.findAll();
    }
}
