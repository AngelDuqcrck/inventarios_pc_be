package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoSolicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoTickets;
import com.inventarios.pc.inventarios_pc_be.entities.TipoAlmacenamientoRam;
import com.inventarios.pc.inventarios_pc_be.entities.TipoComponente;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;
import com.inventarios.pc.inventarios_pc_be.entities.TipoSoftware;
import com.inventarios.pc.inventarios_pc_be.entities.TipoSolicitudes;

public interface ITiposService {
    
    public List<TipoComponente> listarTipoComponente();

    public List<TipoDocumento> listarTipoDocumentos();

    public List<TipoSoftware> listarTipoSoftware();

    public List<EstadoDispositivo> listarEstadosDisp();

    public List<TipoAlmacenamientoRam> listarTipoAlmRam();

    public List<TipoSolicitudes> listarTipoSolicitudes();

    public List<EstadoSolicitudes> listarEstadosSolicitud();

    public List<EstadoTickets> listarEstadosTickets();
}
