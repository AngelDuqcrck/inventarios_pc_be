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
import com.inventarios.pc.inventarios_pc_be.exceptions.RequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeRequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
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
import com.inventarios.pc.inventarios_pc_be.shared.requests.ActualizarSolicitudRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudIdResponse;
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
    public SolicitudDTO crearSolicitudAsistencial(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
            throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException, LocationNotFoundException,
            TypeRequestNotFoundException {
        Solicitudes solicitudCreadaAsistencial = crearSolicitud(solicitudDTO, tipoSolicitudId);

        solicitudRepository.save(solicitudCreadaAsistencial);

        SolicitudDTO solicitudAsistencialCreadaDTO = new SolicitudDTO();
        BeanUtils.copyProperties(solicitudCreadaAsistencial, solicitudAsistencialCreadaDTO);
        return solicitudAsistencialCreadaDTO;

    }

    @Override
    public SolicitudDTO crearSolicitudAdministrativo(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
            throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException, LocationNotFoundException,
            TypeRequestNotFoundException {

        Solicitudes solicitudCreadaAdministrativo = crearSolicitud(solicitudDTO, tipoSolicitudId);

        Ubicacion ubicacion = solicitudCreadaAdministrativo.getComputador().getUbicacion();

        solicitudCreadaAdministrativo.setUbicacionOrigen(ubicacion);

        solicitudRepository.save(solicitudCreadaAdministrativo);

        SolicitudDTO solicitudCreadaAdministrativoDTO = new SolicitudDTO();
        BeanUtils.copyProperties(solicitudCreadaAdministrativo, solicitudCreadaAdministrativoDTO);

        return solicitudCreadaAdministrativoDTO;

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
    public List<SolicitudesResponse> listarSolicitudesByUsuario(String correo) throws UserNotFoundException {

        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

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

    @Override
    public SolicitudIdResponse listarSolicitudById(Integer solicitudId, String correo) throws RequestNotFoundException, UserNotFoundException, StateNotFoundException {

        Solicitudes solicitud = solicitudRepository.findById(solicitudId).orElse(null);

        if (solicitud == null) {
            throw new RequestNotFoundException(String.format(IS_NOT_FOUND, "SOLICITUD").toUpperCase());
        }

        SolicitudIdResponse solicitudIdResponse = new SolicitudIdResponse();

        BeanUtils.copyProperties(solicitud, solicitudIdResponse);

        solicitudIdResponse.setUsuario(
                solicitud.getUsuario().getPrimerNombre() + " " + solicitud.getUsuario().getPrimerApellido());
        solicitudIdResponse.setComputador(solicitud.getComputador().getNombre());
        solicitudIdResponse.setTipoSolicitudes(solicitud.getTipoSolicitudes().getNombre());
        solicitudIdResponse.setEstadoSolicitudes(solicitud.getEstadoSolicitudes().getNombre());
        solicitudIdResponse.setUbicacionOrigen(solicitud.getUbicacionOrigen().getNombre());
        solicitudIdResponse.setUbicacionOrigenId(solicitud.getUbicacionOrigen().getId());
        solicitudIdResponse.setAreaOrigen(solicitud.getUbicacionOrigen().getArea().getNombre());
        solicitudIdResponse.setSedeOrigen(solicitud.getUbicacionOrigen().getArea().getSede().getNombre());

        if (solicitud.getDispositivoPC() != null) {
            solicitudIdResponse.setDispositivoPC(solicitud.getDispositivoPC().getNombre());
        }
        if (solicitud.getUbicacionDestino() != null) {
            solicitudIdResponse.setUbicacionDestino(solicitud.getUbicacionDestino().getNombre());
            solicitudIdResponse.setAreaDestino(solicitud.getUbicacionDestino().getArea().getNombre());
            solicitudIdResponse.setSedeDestino(solicitud.getUbicacionDestino().getArea().getSede().getNombre());
        }

        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

        if(usuario == null){
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }

        String rol = usuario.getRolId().getNombre();

        if(rol == "ADMIN"){
            EstadoSolicitudes estadoSolicitudes = estadoSolicitudesRepository.findByNombre("En Revision").orElse(null);

            if(estadoSolicitudes == null){
                throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DE LA SOLICITUD").toUpperCase());
            }

            solicitud.setEstadoSolicitudes(estadoSolicitudes);
            solicitudRepository.save(solicitud);
        }
        
        return solicitudIdResponse;

    }

    @Override
    public void rechazarSolicitud(Integer solicitudId)
            throws RequestNotFoundException, SelectNotAllowedException, StateNotFoundException {
        Solicitudes solicitud = solicitudRepository.findById(solicitudId).orElse(null);

        if (solicitud == null) {
            throw new RequestNotFoundException(String.format(IS_NOT_FOUND, "SOLICITUD").toUpperCase());
        }

        if (!solicitud.getEstadoSolicitudes().getNombre().equals("En Revision")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_FOUND, "SELECCIONAR ESTA SOLICITUD").toUpperCase());
        }

        EstadoSolicitudes estadoSolicitudes = estadoSolicitudesRepository.findByNombre("Rechazada").orElse(null);

        if (estadoSolicitudes == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DE LA SOLICITUD").toUpperCase());
        }

        solicitud.setEstadoSolicitudes(estadoSolicitudes);

        solicitudRepository.save(solicitud);
    }

    @Override
    public void cancelarSolicitud(Integer solicitudId)
            throws RequestNotFoundException, SelectNotAllowedException, StateNotFoundException {

        Solicitudes solicitud = solicitudRepository.findById(solicitudId).orElse(null);

        if (solicitud == null) {
            throw new RequestNotFoundException(String.format(IS_NOT_FOUND, "SOLICITUD").toUpperCase());
        }

        if (!solicitud.getEstadoSolicitudes().getNombre().equals("Pendiente")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_FOUND, "SELECCIONAR ESTA SOLICITUD").toUpperCase());
        }

        EstadoSolicitudes estadoSolicitudes = estadoSolicitudesRepository.findByNombre("Cancelada").orElse(null);

        if (estadoSolicitudes == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DE LA SOLICITUD").toUpperCase());
        }

        solicitud.setEstadoSolicitudes(estadoSolicitudes);

        solicitudRepository.save(solicitud);

    }

    @Override
    public SolicitudDTO editarSolicitud(Integer solicitudId, ActualizarSolicitudRequest solicitudRequest)
            throws RequestNotFoundException, SelectNotAllowedException, UpdateNotAllowedException,
            ComputerNotFoundException, StateNotFoundException, LocationNotFoundException, DeviceNotFoundException {
        Solicitudes solicitud = solicitudRepository.findById(solicitudId).orElse(null);

        if (solicitud == null) {
            throw new RequestNotFoundException(String.format(IS_NOT_FOUND, "SOLICITUD").toUpperCase());
        }

        if (!solicitud.getEstadoSolicitudes().getNombre().equals("Pendiente")) {
            throw new UpdateNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTA SOLICITUD").toUpperCase());
        }

        BeanUtils.copyProperties(solicitudRequest, solicitud);

        String rol = solicitud.getUsuario().getRolId().getNombre();
        TipoSolicitudes tipoSolicitud = solicitud.getTipoSolicitudes();
        SolicitudDTO solicitudActualizada = new SolicitudDTO();

        if (tipoSolicitud.getNombre().equalsIgnoreCase("Cambio de ubicacion")) {
            if (solicitudRequest.getUbicacionDestino() != null) {
                Ubicacion ubicacionDestino = ubicacionRepository.findById(solicitudRequest.getUbicacionDestino())
                        .orElse(null);
                if (ubicacionDestino == null) {
                    throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACIÓN DESTINO").toUpperCase());
                }

                if (ubicacionDestino.getDeleteFlag() == true) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA UBICACION").toUpperCase());
                }
                solicitud.setUbicacionDestino(ubicacionDestino);
                solicitud.setDispositivoPC(null);
            } else {
                solicitud.setUbicacionDestino(solicitud.getUbicacionDestino());
                solicitud.setDispositivoPC(null);
            }

            if (solicitudRequest.getComputador() != null) {
                Computador computador = computadorRepository.findById(solicitudRequest.getComputador())
                        .orElse(null);
                if (computador == null) {
                    throw new SelectNotAllowedException(String.format(IS_NOT_FOUND, "EQUIPO").toUpperCase());
                }

                if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                        && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE COMPUTADOR").toUpperCase());
                }
                EstadoDispositivo estadoComputador = estadoDispositivoRepository.findByNombre("En uso").orElse(null);
                if (estadoComputador == null) {
                    throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO").toUpperCase());
                }
                computador.setEstadoDispositivo(estadoComputador);
                computadorRepository.save(computador);
                solicitud.setComputador(computador);

            } else {
                solicitud.setComputador(solicitud.getComputador());
            }

        }

        if (tipoSolicitud.getNombre().equalsIgnoreCase("Reparacion")) {
            switch (rol) {
                case "EMPLEADO_ASISTENCIAL":
                    if (solicitudRequest.getDispositivoPC() != null) {
                        Computador computador = new Computador();
                        if (solicitudRequest.getComputador() != null) {
                            computador = computadorRepository.findById(solicitudRequest.getComputador())
                                    .orElse(null);
                            if (computador == null) {
                                throw new SelectNotAllowedException(
                                        String.format(IS_NOT_FOUND, "EQUIPO").toUpperCase());
                            }

                            if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                                    && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
                                throw new SelectNotAllowedException(
                                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE COMPUTADOR").toUpperCase());
                            }
                        } else {
                            computador = solicitud.getComputador();
                        }

                        DispositivoPC dispositivo = dispositivoRepository.findById(solicitudRequest.getDispositivoPC())
                                .orElse(null);

                        Boolean existeDispositivoVinculado = historialDispositivoRepository
                                .existsByComputadorAndDispositivoPCAndFechaDesvinculacionIsNull(computador,
                                        dispositivo);

                        if (dispositivo == null) {

                            throw new SelectNotAllowedException(
                                    String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());

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

                        EstadoDispositivo nuevoEstadoDispositivoAntiguo = estadoDispositivoRepository
                                .findByNombre("Disponible")
                                .orElse(null);
                        if (nuevoEstadoDispositivoAntiguo == null) {
                            throw new StateNotFoundException(
                                    String.format(IS_NOT_ALLOWED, "ESTADO DEL DISPOSITIVO").toUpperCase());
                        }
                        EstadoDispositivo estadoComputador = estadoDispositivoRepository.findByNombre("En uso")
                                .orElse(null);
                        if (estadoComputador == null) {
                            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO").toUpperCase());
                        }
                        computador.setEstadoDispositivo(estadoComputador);
                        computadorRepository.save(computador);
                        DispositivoPC dispositivoAntiguo = solicitud.getDispositivoPC();
                        dispositivoAntiguo.setEstadoDispositivo(nuevoEstadoDispositivoAntiguo);
                        dispositivoRepository.save(dispositivoAntiguo);
                        solicitud.setDispositivoPC(dispositivo);
                        solicitud.setComputador(computador);
                        dispositivo.setEstadoDispositivo(nuevoEstadoDispositivo);
                        dispositivoRepository.save(dispositivo);

                    }

                    if (solicitudRequest.getUbicacionOrigen() != null) {

                        Ubicacion ubicacionOrigen = ubicacionRepository.findById(solicitudRequest.getUbicacionOrigen())
                                .orElse(null);
                        if (ubicacionOrigen == null) {
                            throw new LocationNotFoundException(
                                    String.format(IS_NOT_FOUND, "UBICACIÓN DE ORIGEN").toUpperCase());
                        }

                        if (ubicacionOrigen.getDeleteFlag() == true) {
                            throw new SelectNotAllowedException(
                                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA UBICACION").toUpperCase());
                        }

                        solicitud.setUbicacionOrigen(ubicacionOrigen);

                    } else {
                        solicitud.setUbicacionOrigen(solicitud.getUbicacionOrigen());
                    }
                    break;

                case "EMPLEADO_ADMINISTRATIVO":
                    if (solicitudRequest.getDispositivoPC() != null) {
                        Computador computador = new Computador();
                        if (solicitudRequest.getComputador() != null) {
                            computador = computadorRepository.findById(solicitudRequest.getComputador())
                                    .orElse(null);
                            if (computador == null) {
                                throw new SelectNotAllowedException(
                                        String.format(IS_NOT_FOUND, "EQUIPO").toUpperCase());
                            }

                            if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                                    && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
                                throw new SelectNotAllowedException(
                                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE COMPUTADOR").toUpperCase());
                            }
                        } else {
                            computador = solicitud.getComputador();
                        }

                        DispositivoPC dispositivo = dispositivoRepository.findById(solicitudRequest.getDispositivoPC())
                                .orElse(null);

                        Boolean existeDispositivoVinculado = historialDispositivoRepository
                                .existsByComputadorAndDispositivoPCAndFechaDesvinculacionIsNull(computador,
                                        dispositivo);

                        if (dispositivo == null) {

                            throw new SelectNotAllowedException(
                                    String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());

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

                        EstadoDispositivo nuevoEstadoDispositivoAntiguo = estadoDispositivoRepository
                                .findByNombre("Disponible")
                                .orElse(null);
                        if (nuevoEstadoDispositivoAntiguo == null) {
                            throw new StateNotFoundException(
                                    String.format(IS_NOT_ALLOWED, "ESTADO DEL DISPOSITIVO").toUpperCase());
                        }

                        EstadoDispositivo estadoComputador = estadoDispositivoRepository.findByNombre("En uso")
                                .orElse(null);
                        if (estadoComputador == null) {
                            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO").toUpperCase());
                        }
                        computador.setEstadoDispositivo(estadoComputador);
                        computadorRepository.save(computador);
                        DispositivoPC dispositivoAntiguo = solicitud.getDispositivoPC();
                        dispositivoAntiguo.setEstadoDispositivo(nuevoEstadoDispositivoAntiguo);
                        dispositivoRepository.save(dispositivoAntiguo);
                        solicitud.setDispositivoPC(dispositivo);
                        solicitud.setComputador(computador);
                        dispositivo.setEstadoDispositivo(nuevoEstadoDispositivo);
                        dispositivoRepository.save(dispositivo);

                        Ubicacion ubicacionOrigen = solicitud.getComputador().getUbicacion();

                        solicitud.setUbicacionOrigen(ubicacionOrigen);

                    }
                    break;
                default:
                    throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ROL").toUpperCase());
            }
        }

        solicitudRepository.save(solicitud);

        BeanUtils.copyProperties(tipoSolicitud, solicitudActualizada);

        return solicitudActualizada;

    }

    private Solicitudes crearSolicitud(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
            throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException, LocationNotFoundException,
            TypeRequestNotFoundException {

        Solicitudes solicitudes = new Solicitudes();

        Usuario usuario = usuarioRepository.findByCorreo(solicitudDTO.getUsuario()).orElse(null);

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

        if (solicitudDTO.getUbicacionOrigen() != null) {
            Ubicacion ubicacionOrigen = ubicacionRepository.findById(solicitudDTO.getUbicacionOrigen()).orElse(null);
            if (ubicacionOrigen == null) {
                throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACIÓN DE ORIGEN").toUpperCase());
            }

            if (ubicacionOrigen.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA UBICACION").toUpperCase());
            }

            solicitudes.setUbicacionOrigen(ubicacionOrigen);
        }

        if (tipoSolicitud.getNombre().equalsIgnoreCase("Cambio de ubicacion")) {
            Ubicacion ubicacionDestino = ubicacionRepository.findById(solicitudDTO.getUbicacionDestino()).orElse(null);
            if (ubicacionDestino == null) {
                throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACIÓN DESTINO").toUpperCase());
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

        return solicitudes;
    }
}
