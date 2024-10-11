package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.TipoComponente;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;
import com.inventarios.pc.inventarios_pc_be.entities.TipoSoftware;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoComponenteRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDocumentoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoSoftwareRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITiposService;

@Service
public class TiposServiceImplementation implements ITiposService {
    
    @Autowired
    private TipoComponenteRepository tipoComponenteRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private TipoSoftwareRepository tipoSoftwareRepository;

     @Autowired
    EstadoDispositivoRepository estadoDispositivoRepository;

    /**
     * Obtiene una lista de todos los tipos de documentos (DNI) registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoDocumento} que representan todos los tipos de documentos.
     */
    @Override
    public List<TipoComponente> listarTipoComponente(){
        return (List<TipoComponente>) tipoComponenteRepository.findAll();
    }

    /**
     * Obtiene una lista de todos los tipos de documentos (DNI) registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoDocumento} que representan todos los tipos de documentos.
     */
    @Override
    public List<TipoDocumento> listarTipoDocumentos(){
        return (List<TipoDocumento>) tipoDocumentoRepository.findAll();
    }

    /**
     * Obtiene una lista de todos los tipos de documentos (DNI) registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoDocumento} que representan todos los tipos de documentos.
     */
    @Override
    public List<TipoSoftware> listarTipoSoftware(){
        return (List<TipoSoftware>) tipoSoftwareRepository.findAll();
    }

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
