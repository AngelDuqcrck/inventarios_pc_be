package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoSolicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoTickets;
import com.inventarios.pc.inventarios_pc_be.entities.TipoAlmacenamientoRam;
import com.inventarios.pc.inventarios_pc_be.entities.TipoComponente;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;
import com.inventarios.pc.inventarios_pc_be.entities.TipoSoftware;
import com.inventarios.pc.inventarios_pc_be.entities.TipoSolicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.TipoSolicitudes;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoSolicitudesRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoTicketsRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoAlmacenamientoRamRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoComponenteRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDocumentoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoPcRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoSoftwareRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoSolicitudRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoSolicitudRepository;
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
    private EstadoDispositivoRepository estadoDispositivoRepository;

    @Autowired
    private TipoAlmacenamientoRamRepository tipoAlmacenamientoRamRepository;

    @Autowired
    private TipoSolicitudRepository tipoSolicitudRepository;

    @Autowired
    private EstadoSolicitudesRepository estadoSolicitudesRepository;

    @Autowired
    private EstadoTicketsRepository estadoTicketsRepository;

    /**
     * Obtiene una lista de todos los tipos de componentes registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoComponente} que representan todos los tipos de componentes.
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
     * Obtiene una lista de todos los tipos de software registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoSoftware} que representan todos los tipos de software.
     */
    @Override
    public List<TipoSoftware> listarTipoSoftware(){
        return (List<TipoSoftware>) tipoSoftwareRepository.findAll();
    }

     /**
     * Obtiene una lista de todos los tipos de estados para dispositivos registrados en el sistema.
     * 
     * @return Una lista de objetos {@link EstadoDispositivo} que representan todos los tipos de estados para dispositivos.
     */
    @Override
    public List<EstadoDispositivo> listarEstadosDisp(){
        return (List<EstadoDispositivo>) estadoDispositivoRepository.findAll();
    }

     /**
     * Obtiene una lista de todos los tipos de almacenamiento y RAM registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoAlmacenamientoRam} que representan todos los tipos de almacenamiento y RAM.
     */
    @Override
    public List<TipoAlmacenamientoRam> listarTipoAlmRam(){
        return (List<TipoAlmacenamientoRam>) tipoAlmacenamientoRamRepository.findAll();
    }

     /**
     * Obtiene una lista de todos los tipos de solicitudes registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoSolicitudes} que representan todos los tipos de solicitudes.
     */
    @Override
    public List<TipoSolicitudes> listarTipoSolicitudes(){
        return (List<TipoSolicitudes>) tipoSolicitudRepository.findAll();
    }

    /**
     * Obtiene una lista de todos los estados de las solicitudes registrados en el sistema.
     * 
     * @return Una lista de objetos {@link EstadoSolicitudes} que representan todos los estados de las solicitudes.
     */
    @Override
    public List<EstadoSolicitudes> listarEstadosSolicitud(){
        return (List<EstadoSolicitudes>) estadoSolicitudesRepository.findAll();
    }

    /**
     * Obtiene una lista de todos los estados de los tickets registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoSolicitudes} que representan todos los estados de los tickets.
     */
    @Override
    public List<EstadoTickets> listarEstadosTickets(){
        return (List<EstadoTickets>) estadoTicketsRepository.findAll();
    }
}
