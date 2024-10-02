package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDocumentoRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoDocumentoService;

@Service
public class TipoDocServiceImplementation implements ITipoDocumentoService{

    @Autowired
    TipoDocumentoRepository tipoDocumentoRepository;

    /**
     * Obtiene una lista de todos los tipos de documentos (DNI) registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoDocumento} que representan todos los tipos de documentos.
     */
    @Override
    public List<TipoDocumento> listarTipoDocumentos(){
        return (List<TipoDocumento>) tipoDocumentoRepository.findAll();
    }
}
