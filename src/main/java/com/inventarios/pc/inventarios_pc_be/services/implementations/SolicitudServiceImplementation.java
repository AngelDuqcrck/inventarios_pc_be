package com.inventarios.pc.inventarios_pc_be.services.implementations;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.EstadoSolicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.Solicitudes;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoSolicitudesRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoSolicitudRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UbicacionRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UsuarioRepository;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SolicitudDTO;

@Service
public class SolicitudServiceImplementation {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComputadorRepository computadorRepository;

    @Autowired
    private TipoSolicitudRepository tipoSolicitudRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private EstadoSolicitudesRepository estadoSolicitudesRepository;

    private SolicitudDTO crearSolicitud(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
            throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException{
        Solicitudes solicitudes = new Solicitudes();

        BeanUtils.copyProperties(solicitudDTO, solicitudes);

        EstadoSolicitudes estadoSolicitudes = estadoSolicitudesRepository.findByNombre("Pendiente").orElse(null);

        if (estadoSolicitudes == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DE LA SOLICITUD").toUpperCase());
        }

        if(estadoSolicitudes.getDeleteFlag()  == null){
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE ESTADO DE LA SOLICITUD").toUpperCase());
        }


        return null;
    }
}
