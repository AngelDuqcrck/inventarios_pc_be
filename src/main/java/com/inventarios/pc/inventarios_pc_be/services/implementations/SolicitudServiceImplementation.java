package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoSolicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.Solicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.TipoSolicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeRequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoSolicitudesRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.HistorialDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SolicitudRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoSolicitudRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UbicacionRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UsuarioRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ISolicitudService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SolicitudDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudesResponse;

@Service
public class SolicitudServiceImplementation implements ISolicitudService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";
    public static final String IS_NOT_VINCULATED = "%s no esta vinculado";

    @Autowired
    private HistorialDispositivoRepository historialDispositivoRepository;

    @Autowired
    private EstadoDispositivoRepository estadoDispositivoRepository;

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
    private SolicitudRepository solicitudRepository;

    @Autowired
    private EstadoSolicitudesRepository estadoSolicitudesRepository;

    @Override
    public SolicitudDTO crearSolicitud(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
            throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException, LocationNotFoundException,
            TypeRequestNotFoundException {
        Solicitudes solicitudes = new Solicitudes();

        Usuario usuario = usuarioRepository.findById(solicitudDTO.getUsuario()).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }

        if (usuario.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE USUARIO").toUpperCase());
        }

        TipoSolicitudes tipoSolicitud = tipoSolicitudRepository.findById(tipoSolicitudId).orElse(null);
        if (tipoSolicitud == null) {
            throw new TypeRequestNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE SOLICITUD").toUpperCase());
        }

        Ubicacion ubicacionOrigen = ubicacionRepository.findById(solicitudDTO.getUbicacionOrigen()).orElse(null);
        if (ubicacionOrigen == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACIÓN DE ORIGEN").toUpperCase());
        }

        if (ubicacionOrigen.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA UBICACION").toUpperCase());
        }

        if (tipoSolicitud.getNombre().equalsIgnoreCase("Cambio de ubicacion")) {
            Ubicacion ubicacionDestino = ubicacionRepository.findById(solicitudDTO.getUbicacionDestino()).orElse(null);
            if (ubicacionDestino == null) {
                throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACIÓN DE DESTINO").toUpperCase());
            }

            if (ubicacionDestino.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA UBICACION").toUpperCase());
            }
            solicitudes.setUbicacionDestino(ubicacionDestino);
            solicitudes.setDispositivoPC(null);
        } else {

            if (solicitudDTO.getDispositivoPC() != null && solicitudDTO.getComputador() != null) {
                Computador computador = computadorRepository.findById(solicitudDTO.getComputador()).orElse(null);
                if (computador == null) {
                    throw new SelectNotAllowedException(String.format(IS_NOT_FOUND, "EQUIPO").toUpperCase());
                }

                if (!computador.getEstadoDispositivo().getNombre().equals("En uso")) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE COMPUTADOR").toUpperCase());
                }

                DispositivoPC dispositivo = dispositivoRepository.findById(solicitudDTO.getDispositivoPC())
                        .orElse(null);

                Boolean existeDispositivoVinculado = historialDispositivoRepository
                        .existsByComputadorAndDispositivoPCAndFechaDesvinculacionIsNull(computador, dispositivo);

                if (dispositivo == null) {

                    throw new SelectNotAllowedException(String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());

                }
                if (existeDispositivoVinculado == false) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_VINCULATED, " DISPOSITIVO").toUpperCase());
                }

                EstadoDispositivo nuevoEstadoDispositivo = estadoDispositivoRepository.findByNombre("Averiado")
                        .orElse(null);
                if (nuevoEstadoDispositivo == null) {
                    throw new StateNotFoundException(
                            String.format(IS_NOT_ALLOWED, "ESTADO DEL DISPOSITIVO").toUpperCase());
                }
                solicitudes.setDispositivoPC(dispositivo);
                dispositivo.setEstadoDispositivo(nuevoEstadoDispositivo);
                dispositivoRepository.save(dispositivo);
            }
        }

        EstadoSolicitudes estadoSolicitudes = estadoSolicitudesRepository.findByNombre("Pendiente").orElse(null);

        if (estadoSolicitudes == null || estadoSolicitudes.getDeleteFlag() != false) {
            throw new StateNotFoundException(String.format(IS_NOT_ALLOWED, "ESTADO DE LA SOLICITUD").toUpperCase());
        }

        BeanUtils.copyProperties(solicitudDTO, solicitudes);
        solicitudes.setUsuario(usuario);
        solicitudes.setTipoSolicitudes(tipoSolicitud);
        solicitudes.setEstadoSolicitudes(estadoSolicitudes);
        solicitudes.setUbicacionOrigen(ubicacionOrigen);
        solicitudes.setFechaCreacion(new Date());
        Computador computador = computadorRepository.findById(solicitudDTO.getComputador()).orElse(null);
        if (computador == null) {
            throw new SelectNotAllowedException(String.format(IS_NOT_FOUND, "EQUIPO").toUpperCase());
        }

        if (!computador.getEstadoDispositivo().getNombre().equals("En uso")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE COMPUTADOR").toUpperCase());
        }

        solicitudes.setComputador(computador);

        solicitudes = solicitudRepository.save(solicitudes);

        SolicitudDTO solicitudCreada = new SolicitudDTO();
        BeanUtils.copyProperties(solicitudes, solicitudCreada);

        return solicitudCreada;

    }

    @Override
    public List<SolicitudesResponse> listarSolicitudes() {

        List<Solicitudes> solicitudes = solicitudRepository.findAll();

        List<SolicitudesResponse> solicitudesResponses = new ArrayList<>();

        for (Solicitudes solicitud : solicitudes) {

            SolicitudesResponse solicitudResponse = new SolicitudesResponse();
            BeanUtils.copyProperties(solicitud, solicitudResponse);
            solicitudResponse.setEstadoSolicitud(solicitud.getEstadoSolicitudes().getNombre());
            solicitudResponse.setResponsable(
                    solicitud.getUsuario().getPrimerNombre() + " " + solicitud.getUsuario().getPrimerApellido());
            solicitudResponse.setTipoSolicitud(solicitud.getTipoSolicitudes().getNombre());
            solicitudResponse.setFechaCierre(solicitud.getFechaCierre());
            solicitudesResponses.add(solicitudResponse);
        }

        return solicitudesResponses;
    }


    @Override
    public List<SolicitudesResponse> listarSolicitudesByUsuario(Integer usuarioId) throws UserNotFoundException {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario == null) {

            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());

        }

        List<Solicitudes> solicitudes = solicitudRepository.findByUsuario(usuario);

        List<SolicitudesResponse> solicitudesResponses = new ArrayList<>();

        for (Solicitudes solicitud : solicitudes) {

            SolicitudesResponse solicitudResponse = new SolicitudesResponse();
            BeanUtils.copyProperties(solicitud, solicitudResponse);
            solicitudResponse.setEstadoSolicitud(solicitud.getEstadoSolicitudes().getNombre());
            solicitudResponse.setResponsable(
                    solicitud.getUsuario().getPrimerNombre() + " " + solicitud.getUsuario().getPrimerApellido());
            solicitudResponse.setTipoSolicitud(solicitud.getTipoSolicitudes().getNombre());
            solicitudResponse.setFechaCierre(solicitud.getFechaCierre());
            solicitudesResponses.add(solicitudResponse);
        }

        return solicitudesResponses;
    }
}
