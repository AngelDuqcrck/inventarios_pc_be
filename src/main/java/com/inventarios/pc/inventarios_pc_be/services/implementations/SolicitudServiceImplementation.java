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
import com.inventarios.pc.inventarios_pc_be.entities.SoftwareCSA;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;
import com.inventarios.pc.inventarios_pc_be.entities.Solicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.TipoSolicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeRequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoSolicitudesRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.HistorialDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SoftwareCsaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SoftwarePcRepository;
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
    public static final String IS_NOT_FOUND_F = "%s no fue encontradA";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";
    public static final String IS_NOT_VINCULATED = "%s no esta vinculado";

    @Autowired
    private SoftwarePcRepository softwarePcRepository;

    @Autowired
    private SoftwareCsaRepository softwareCsaRepository;
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
            TypeRequestNotFoundException, SoftwareNotFoundException {
        Solicitudes solicitudCreadaAsistencial = crearSolicitud(solicitudDTO, tipoSolicitudId);

        solicitudRepository.save(solicitudCreadaAsistencial);

        SolicitudDTO solicitudAsistencialCreadaDTO = new SolicitudDTO();
        BeanUtils.copyProperties(solicitudCreadaAsistencial, solicitudAsistencialCreadaDTO);
        return solicitudAsistencialCreadaDTO;

    }

    @Override
    public SolicitudDTO crearSolicitudAdministrativo(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
            throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException, LocationNotFoundException,
            TypeRequestNotFoundException, SoftwareNotFoundException {

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
    public SolicitudIdResponse listarSolicitudById(Integer solicitudId, String correo)
            throws RequestNotFoundException, UserNotFoundException, StateNotFoundException, SelectNotAllowedException {

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
        solicitudIdResponse.setEsHardaware(solicitud.getEsHardaware());
        solicitudIdResponse.setAreaOrigen(solicitud.getUbicacionOrigen().getArea().getNombre());
        solicitudIdResponse.setSedeOrigen(solicitud.getUbicacionOrigen().getArea().getSede().getNombre());

        if (solicitud.getDispositivoPC() != null) {
            solicitudIdResponse.setDispositivoPC(
                    solicitud.getDispositivoPC().getModelo() + " " + solicitud.getDispositivoPC().getMarca().getNombre()
                            + " " + solicitud.getDispositivoPC().getPlaca());
        }
        if (solicitud.getSoftwarePC() != null) {
            solicitudIdResponse.setSoftwarePC(solicitud.getSoftwarePC().getNombre());
        }
        if (solicitud.getUbicacionDestino() != null) {
            solicitudIdResponse.setUbicacionDestino(solicitud.getUbicacionDestino().getNombre());
            solicitudIdResponse.setAreaDestino(solicitud.getUbicacionDestino().getArea().getNombre());
            solicitudIdResponse.setSedeDestino(solicitud.getUbicacionDestino().getArea().getSede().getNombre());
        }

        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }

        Integer rol = usuario.getRolId().getId();

        if (rol == 1) {

            Integer estadoSolicitudActual = solicitud.getEstadoSolicitudes().getId();

            if (estadoSolicitudActual == 1 || estadoSolicitudActual == 6) {
                EstadoSolicitudes estadoSolicitudes = estadoSolicitudesRepository.findById(6).orElse(null);
                if (estadoSolicitudes == null) {
                    throw new StateNotFoundException(
                            String.format(IS_NOT_FOUND, "ESTADO DE LA SOLICITUD").toUpperCase());
                }

                solicitud.setEstadoSolicitudes(estadoSolicitudes);

            }
            alterarSolicitud(solicitud);
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
        retornarModificacionesSolicitud(solicitud);
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

    public void retornarSolicitudPendiente(Integer solicitudId)
            throws RequestNotFoundException, SelectNotAllowedException, StateNotFoundException {
        Solicitudes solicitud = solicitudRepository.findById(solicitudId).orElse(null);

        if (solicitud == null) {
            throw new RequestNotFoundException(String.format(IS_NOT_FOUND, "LA SOLICITUD").toUpperCase());
        }

        if (solicitud.getEstadoSolicitudes().getId() != 6) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA SOLICITUD PORQUE NO TIENE EL ESTADO EN REVISION")
                            .toUpperCase());
        }

        EstadoSolicitudes estadoSolicitudes = estadoSolicitudesRepository.findById(1).orElse(null); // Estado Pendiente

        if (estadoSolicitudes == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO PENDIENTE").toUpperCase());
        }
        
            retornarModificacionesSolicitud(solicitud);
        
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

        Integer rol = solicitud.getUsuario().getRolId().getId();

        if (rol != 1) {
            if (!solicitud.getEstadoSolicitudes().getNombre().equals("Pendiente")) {

                throw new UpdateNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTA SOLICITUD PORQUE SU ESTADO NO ES PENDIENTE")
                                .toUpperCase());

            }
        } else {
            if (!solicitud.getEstadoSolicitudes().getNombre().equals("En Revision")
                    && !solicitud.getEstadoSolicitudes().getNombre().equals("Pendiente")) {

                throw new UpdateNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTA SOLICITUD PORQUE SU ESTADO NO ES EN REVISIÓN")
                                .toUpperCase());
            }
        }

        BeanUtils.copyProperties(solicitud, solicitudRequest);

        TipoSolicitudes tipoSolicitud = solicitud.getTipoSolicitudes();
        SolicitudDTO solicitudActualizada = new SolicitudDTO();
        Computador computador = new Computador();

        if (tipoSolicitud.getId() == 1 || tipoSolicitud.getId() == 3) { // Solicitud de mantenimiento correctivo o
                                                                        // mantenimiento preventivo

            if (rol == 1) { // Rol ADMIN
                if (solicitudRequest.getComputador() != null) {
                    computador = computadorRepository.findById(solicitudRequest.getComputador()).orElse(null);
                    if (computador == null) {
                        throw new SelectNotAllowedException(String.format(IS_NOT_FOUND, "EQUIPO").toUpperCase());
                    }

                    if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                            && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
                        throw new SelectNotAllowedException(
                                String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE COMPUTADOR").toUpperCase());
                    }
                }

                if (solicitudRequest.getEsHardaware() != null) {
                    if (solicitudRequest.getEsHardaware() == true) {
                        DispositivoPC dispositivoNuevo = dispositivoRepository
                                .findById(solicitudRequest.getDispositivoPC())
                                .orElse(null);

                        if (dispositivoNuevo == null) {
                            throw new SelectNotAllowedException(
                                    String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());
                        }

                        
                        HistorialDispositivo dispositivoVinculado = historialDispositivoRepository
                                .findByComputadorAndDispositivoPCAndFechaDesvinculacionIsNull(computador,
                                        dispositivoNuevo);

                        if (dispositivoVinculado == null) {
                            throw new SelectNotAllowedException(
                                    String.format(IS_NOT_VINCULATED, "EL DISPOSITIVO").toUpperCase());
                        }
 
                        EstadoDispositivo estadoComputador = estadoDispositivoRepository.findByNombre("En uso")
                                .orElse(null);
                        if (estadoComputador == null) {
                            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO").toUpperCase());
                        }
                        computador.setEstadoDispositivo(estadoComputador);
                        computadorRepository.save(computador);
                        
                        solicitud.setDispositivoPC(dispositivoNuevo);

                    }
                    if (solicitudRequest.getEsHardaware() == false) {
                        if (solicitudRequest.getSoftwarePC() != null) {
                            SoftwarePC softwarePcNuevo = softwarePcRepository.findById(solicitudRequest.getSoftwarePC())
                                    .orElse(null);

                            if (softwarePcNuevo == null) {
                                throw new SelectNotAllowedException(
                                        String.format(IS_NOT_FOUND, "EL SOFTWARE").toUpperCase());
                            }

                            SoftwareCSA softwareCSA = softwareCsaRepository
                                    .findByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(computador,
                                            softwarePcNuevo);
                            if (softwareCSA == null) {
                                throw new SelectNotAllowedException(
                                        String.format(IS_NOT_VINCULATED, "EL SOFTWARE").toUpperCase());
                            }
                            EstadoDispositivo estadoComputador = estadoDispositivoRepository.findByNombre("En uso")
                                    .orElse(null);
                            if (estadoComputador == null) {
                                throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO").toUpperCase());
                            }
                            computador.setEstadoDispositivo(estadoComputador);
                            computadorRepository.save(computador);

                            solicitud.setSoftwarePC(softwarePcNuevo);
                            solicitud.setComputador(computador);
                            
                        }
                    }
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

                }

            } else if (rol == 2 || rol == 3) { // Rol EMPLEADO_ASISTENCIAL o EMPLEADO_ADMINISTRATIVO
                computador = new Computador();
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
                if (solicitudRequest.getEsHardaware() != null) {
                    if (solicitudRequest.getEsHardaware() == true) {
                        DispositivoPC dispositivoNuevo = dispositivoRepository
                                .findById(solicitudRequest.getDispositivoPC())
                                .orElse(null);

                        if (dispositivoNuevo == null) {

                            throw new SelectNotAllowedException(
                                    String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());

                        }

                        

                        HistorialDispositivo dispositivoVinculado = historialDispositivoRepository
                                .findByComputadorAndDispositivoPCAndFechaDesvinculacionIsNull(computador,
                                        dispositivoNuevo);

                        if (dispositivoVinculado == null) {
                            throw new SelectNotAllowedException(
                                    String.format(IS_NOT_VINCULATED, "EL DISPOSITIVO").toUpperCase());
                        } 
                        EstadoDispositivo estadoComputador = estadoDispositivoRepository.findByNombre("En uso")
                                .orElse(null);
                        if (estadoComputador == null) {
                            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO").toUpperCase());
                        }
                        computador.setEstadoDispositivo(estadoComputador);
                        computadorRepository.save(computador);
                        
                        solicitud.setDispositivoPC(dispositivoNuevo);
                        solicitud.setComputador(computador);
                        

                    }
                    if (solicitudRequest.getEsHardaware() == false) {
                        if (solicitudRequest.getSoftwarePC() != null) {
                            SoftwarePC softwarePcNuevo = softwarePcRepository
                                    .findById(solicitudRequest.getSoftwarePC())
                                    .orElse(null);

                            if (softwarePcNuevo == null) {
                                throw new SelectNotAllowedException(
                                        String.format(IS_NOT_FOUND, "EL SOFTWARE").toUpperCase());
                            }

                            SoftwareCSA softwareCSA = softwareCsaRepository
                                    .findByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(computador,
                                            softwarePcNuevo);
                            if (softwareCSA == null) {
                                throw new SelectNotAllowedException(
                                        String.format(IS_NOT_VINCULATED, "EL SOFTWARE").toUpperCase());
                            }
                            EstadoDispositivo estadoComputador = estadoDispositivoRepository.findByNombre("En uso")
                                    .orElse(null);
                            if (estadoComputador == null) {
                                throw new StateNotFoundException(
                                        String.format(IS_NOT_FOUND, "ESTADO").toUpperCase());
                            }
                            computador.setEstadoDispositivo(estadoComputador);
                            computadorRepository.save(computador);

                            

                            solicitud.setSoftwarePC(softwarePcNuevo);
                            solicitud.setComputador(computador);
                            
                        }

                        Ubicacion ubicacionOrigen = solicitud.getComputador().getUbicacion();
                        solicitud.setUbicacionOrigen(ubicacionOrigen);
                    }
                }
            }

        } else if (tipoSolicitud.getId() == 2) {
            if (solicitudRequest.getUbicacionDestino() != null) {
                Ubicacion ubicacionDestino = ubicacionRepository
                        .findById(solicitudRequest.getUbicacionDestino())
                        .orElse(null);
                if (ubicacionDestino == null) {
                    throw new LocationNotFoundException(
                            String.format(IS_NOT_FOUND_F, "LA UBICACIÓN DESTINO").toUpperCase());
                }

                if (ubicacionDestino.getDeleteFlag() == true) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "SELECCIONAR ESTA UBICACION PORQUE SE ENCUENTRA DESACTIVADA")
                                    .toUpperCase());
                }
                solicitud.setUbicacionDestino(ubicacionDestino);
                solicitud.setDispositivoPC(null);
            }

            if (solicitudRequest.getComputador() != null) {
                computador = computadorRepository.findById(solicitudRequest.getComputador())
                        .orElse(null);
                if (computador == null) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_FOUND, "EL EQUIPO").toUpperCase());
                }

                if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                        && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "SELECCIONAR ESTE COMPUTADOR PORQUE TIENE UN ESTADO DIFERENTE A EN USO O DISPONIBLE")
                                    .toUpperCase());
                }
                EstadoDispositivo estadoComputador = estadoDispositivoRepository.findByNombre("En uso")
                        .orElse(null);
                if (estadoComputador == null) {
                    throw new StateNotFoundException(
                            String.format(IS_NOT_FOUND, "EL ESTADO EN USO").toUpperCase());
                }
                computador.setEstadoDispositivo(estadoComputador);
                computadorRepository.save(computador);
                solicitud.setComputador(computador);

            }
            
        } else if (tipoSolicitud.getId() == 4) {
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
                            String.format(IS_NOT_ALLOWED,
                                    "SELECCIONAR ESTE COMPUTADOR PORQUE TIENE UN ESTADO DIFERENTE A EN USO O DISPONIBLE")
                                    .toUpperCase());
                }
                solicitud.setComputador(computador);
            } else {
                solicitud.setComputador(solicitud.getComputador());
            }

            if (solicitudRequest.getUbicacionOrigen() != null) {
                Ubicacion ubicacionOrigen = ubicacionRepository
                        .findById(solicitudRequest.getUbicacionOrigen())
                        .orElse(null);
                if (ubicacionOrigen == null) {
                    throw new LocationNotFoundException(
                            String.format(IS_NOT_FOUND, "UBICACIÓN DE ORIGEN").toUpperCase());
                }

                if (ubicacionOrigen.getDeleteFlag() == true) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "SELECCIONAR ESTA UBICACION PORQUE SE ENCUENTRA DESACTIVADA")
                                    .toUpperCase());
                }
                solicitud.setUbicacionOrigen(ubicacionOrigen);
            } else {
                solicitud.setUbicacionOrigen(solicitud.getUbicacionOrigen());
            }

        } else {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ROL").toUpperCase());
        }

        solicitudRepository.save(solicitud);

        BeanUtils.copyProperties(solicitud, solicitudActualizada);

        return solicitudActualizada;

    }

    private Solicitudes crearSolicitud(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
            throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException, LocationNotFoundException,
            TypeRequestNotFoundException, SoftwareNotFoundException {

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
        

        switch (tipoSolicitud.getId()) {
            case 1:// Solicitud de mantenimiento correctivo

                Computador computador = computadorRepository.findById(solicitudDTO.getComputador()).orElse(null);
                if (computador == null) {
                    throw new SelectNotAllowedException(String.format(IS_NOT_FOUND, "EQUIPO").toUpperCase());
                }

                if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                        && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "SELECCIONAR ESTE COMPUTADOR PORQUE TIENE UN ESTADO DIFERENTE A EN USO Y DISPONIBLE")
                                    .toUpperCase());
                }
                if (solicitudDTO.getEsHardaware() == true) {
                    DispositivoPC dispositivo = dispositivoRepository.findById(solicitudDTO.getDispositivoPC())
                            .orElse(null);

                    if (dispositivo == null) {

                        throw new SelectNotAllowedException(
                                String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());

                    }
                    HistorialDispositivo dispositivoVinculado = historialDispositivoRepository
                            .findByComputadorAndDispositivoPCAndFechaDesvinculacionIsNull(computador, dispositivo);

                    if (dispositivoVinculado == null) {
                        throw new SelectNotAllowedException(
                                String.format(IS_NOT_VINCULATED, " DISPOSITIVO").toUpperCase());
                    }



                    solicitudes.setDispositivoPC(dispositivo);

                }

                if (solicitudDTO.getEsHardaware() == false) {
                    SoftwarePC softwarePC = softwarePcRepository.findById(solicitudDTO.getSoftwarePC()).orElse(null);
                    if (softwarePC == null) {
                        throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "SOFTWARE").toUpperCase());
                    }

                    SoftwareCSA softwareVinculado = softwareCsaRepository
                            .findByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(computador, softwarePC);

                    if (softwareVinculado == null) {
                        throw new SelectNotAllowedException(
                                String.format(IS_NOT_VINCULATED, "EL SOFTWARE " + softwarePC.getNombre())
                                        .toUpperCase());
                    }
                    solicitudes.setSoftwarePC(softwarePC);
                }

                break;
            case 2:// Solicitud de cambio de ubicacion
                Ubicacion ubicacionDestino = ubicacionRepository.findById(solicitudDTO.getUbicacionDestino())
                        .orElse(null);
                if (ubicacionDestino == null) {
                    throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACIÓN DESTINO").toUpperCase());
                }

                if (ubicacionDestino.getDeleteFlag() == true) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA UBICACION").toUpperCase());
                }
                solicitudes.setUbicacionDestino(ubicacionDestino);
                solicitudes.setDispositivoPC(null);
                break;
            case 3:// Solicitud de mantenimiento preventivo
                computador = computadorRepository.findById(solicitudDTO.getComputador()).orElse(null);
                if (computador == null) {
                    throw new SelectNotAllowedException(String.format(IS_NOT_FOUND, "EQUIPO").toUpperCase());
                }

                if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                        && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "SELECCIONAR ESTE COMPUTADOR PORQUE TIENE UN ESTADO DIFERENTE A EN USO Y DISPONIBLE")
                                    .toUpperCase());
                }
                if (solicitudDTO.getEsHardaware() == true) {
                    DispositivoPC dispositivo = dispositivoRepository.findById(solicitudDTO.getDispositivoPC())
                            .orElse(null);

                    if (dispositivo == null) {

                        throw new SelectNotAllowedException(
                                String.format(IS_NOT_FOUND, "EL DISPOSITIVO").toUpperCase());

                    }
                    HistorialDispositivo dispositivoVinculado = historialDispositivoRepository
                            .findByComputadorAndDispositivoPCAndFechaDesvinculacionIsNull(computador, dispositivo);

                    if (dispositivoVinculado == null) {
                        throw new SelectNotAllowedException(
                                String.format(IS_NOT_VINCULATED, "EL DISPOSITIVO").toUpperCase());
                    }

                    solicitudes.setDispositivoPC(dispositivo);

                }

                if (solicitudDTO.getEsHardaware() == false) {
                    SoftwarePC softwarePC = softwarePcRepository.findById(solicitudDTO.getSoftwarePC()).orElse(null);
                    if (softwarePC == null) {
                        throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "SOFTWARE").toUpperCase());
                    }

                    SoftwareCSA softwareVinculado = softwareCsaRepository
                            .findByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(computador, softwarePC);

                    if (softwareVinculado == null) {
                        throw new SelectNotAllowedException(
                                String.format(IS_NOT_VINCULATED, "EL SOFTWARE " + softwarePC.getNombre())
                                        .toUpperCase());
                    }


                    solicitudes.setSoftwarePC(softwarePC);
                }

                break;
            case 4:// Solicitud general
                break;
            default:
                throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "TIPO DE SOLICITUD").toUpperCase());

        }

        EstadoSolicitudes estadoSolicitudes = estadoSolicitudesRepository.findByNombre("Pendiente").orElse(null);

        if (estadoSolicitudes == null || estadoSolicitudes.getDeleteFlag() != false) {
            throw new StateNotFoundException(String.format(IS_NOT_ALLOWED, "ESTADO DE LA SOLICITUD").toUpperCase());
        }

        BeanUtils.copyProperties(solicitudDTO, solicitudes);
        solicitudes.setUsuario(usuario);
        solicitudes.setTipoSolicitudes(tipoSolicitud);
        solicitudes.setTitulo(tipoSolicitud.getNombre());
        solicitudes.setEstadoSolicitudes(estadoSolicitudes);
        solicitudes.setFechaCreacion(new Date());
        Computador computador = computadorRepository.findById(solicitudDTO.getComputador()).orElse(null);
        if (computador == null) {
            throw new SelectNotAllowedException(String.format(IS_NOT_FOUND, "EQUIPO").toUpperCase());
        }

        if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE COMPUTADOR").toUpperCase());
        }

        EstadoDispositivo nuevoEstadoComputador = estadoDispositivoRepository.findByNombre("En uso")
                .orElse(null);
        if (nuevoEstadoComputador == null) {
            throw new StateNotFoundException(
                    String.format(IS_NOT_ALLOWED, "ESTADO DEL DISPOSITIVO").toUpperCase());
        }
        computador.setEstadoDispositivo(nuevoEstadoComputador);
        computadorRepository.save(computador);
        solicitudes.setComputador(computador);

        return solicitudes;
    }

    private void alterarSolicitud(Solicitudes solicitud) throws StateNotFoundException, SelectNotAllowedException {
       if(solicitud.getTipoSolicitudes().getId() == 1 || solicitud.getTipoSolicitudes().getId() == 3){

        Computador computador = solicitud.getComputador();
        if(solicitud.getEsHardaware() == true){
            DispositivoPC dispositivo = solicitud.getDispositivoPC();

            HistorialDispositivo dispositivoVinculado = historialDispositivoRepository
                    .findTopByComputadorAndDispositivoPCOrderByFechaDesvinculacionDesc(computador, dispositivo);

                    if (dispositivoVinculado == null) {
                        throw new SelectNotAllowedException(
                                String.format(IS_NOT_VINCULATED, " DISPOSITIVO").toUpperCase());
                    }
                    EstadoDispositivo nuevoEstadoDispositivo = new EstadoDispositivo();

                    if(solicitud.getTipoSolicitudes().getId() == 1){ //Mantenimiento correctivo
                        nuevoEstadoDispositivo = estadoDispositivoRepository.findByNombre("Averiado")
                        .orElse(null);
                        if (nuevoEstadoDispositivo == null) {
                            throw new StateNotFoundException(
                                    String.format(IS_NOT_FOUND, "EL ESTADO AVERIADO").toUpperCase());
                        }
                    }

                    if(solicitud.getTipoSolicitudes().getId() == 3){ //Mantenimiento preventivo
                        nuevoEstadoDispositivo = estadoDispositivoRepository.findByNombre("En reparacion")
                        .orElse(null);
                        if (nuevoEstadoDispositivo == null) {
                            throw new StateNotFoundException(
                                    String.format(IS_NOT_FOUND, "EL ESTADO EN REPARACIÓN").toUpperCase());
                        }
                    }
                   
                    
                    dispositivo.setEstadoDispositivo(nuevoEstadoDispositivo);

                    if (dispositivo.getTipoDispositivo().getId() != 8) // Diferente a dispositivo tipo torre
                    {
                        dispositivoVinculado.setFechaDesvinculacion(new Date());
                        dispositivoVinculado.setJustificacion(
                                "El dispositivo fue desvinculado, porque se le realizara un mantenimiento correctivo");
                        historialDispositivoRepository.save(dispositivoVinculado);
                    }
                    dispositivoRepository.save(dispositivo);
        }
       
        if(solicitud.getEsHardaware() == false){
            SoftwarePC softwarePC = solicitud.getSoftwarePC();
            SoftwareCSA softwareVinculado = softwareCsaRepository
                    .findTopByComputadorAndSoftwarePCOrderByFechaDesvinculacionDesc(computador, softwarePC);

            if (softwareVinculado == null) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_VINCULATED, "EL SOFTWARE " + softwarePC.getNombre())
                                .toUpperCase());
            }

            softwareVinculado.setFechaDesvinculacion(new Date());
            softwareVinculado.setJustificacion(
                    "El software fue desvinculado, porque se le realizara un mantenimiento correctivo");

            softwareCsaRepository.save(softwareVinculado);

        }
       }
       solicitudRepository.save(solicitud);
    }

    private void retornarModificacionesSolicitud(Solicitudes solicitud) throws StateNotFoundException {
        if (solicitud.getTipoSolicitudes().getId() == 1 || solicitud.getTipoSolicitudes().getId() == 3)
        //Solicitud de mantenimiento correctivo o mantenimiento preventivo
        {
            if (solicitud.getEsHardaware() == true) {
                if (solicitud.getDispositivoPC() != null) {
                    EstadoDispositivo estadoEnUso = estadoDispositivoRepository.findByNombre("En uso").orElse(null);
                    if (estadoEnUso == null) {
                        throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO EN USO").toUpperCase());
                    }
                    DispositivoPC dispositivo = solicitud.getDispositivoPC();
                    dispositivo.setEstadoDispositivo(estadoEnUso);

                    Computador computadorAnterior = solicitud.getComputador();
                    HistorialDispositivo historial = historialDispositivoRepository
                            .findTopByComputadorAndDispositivoPCOrderByFechaDesvinculacionDesc(computadorAnterior,
                                    dispositivo);

                    if (dispositivo.getTipoDispositivo().getId() != 8)// Tipo de dispositivo diferente a torre

                    {
                        if (historial != null) {
                            historial.setFechaDesvinculacion(null);
                            historial.setJustificacion(null);
                            historialDispositivoRepository.save(historial);
                        }
                    }

                    dispositivoRepository.save(dispositivo);
                }

            }
            if (solicitud.getEsHardaware() == false) {
                Computador computadorAnterior = solicitud.getComputador();
                SoftwarePC softwarePc = solicitud.getSoftwarePC();

                SoftwareCSA softwareCSA = softwareCsaRepository
                        .findTopByComputadorAndSoftwarePCOrderByFechaDesvinculacionDesc(computadorAnterior, softwarePc);

                if (softwareCSA != null) {
                    softwareCSA.setFechaDesvinculacion(null);
                    softwareCSA.setJustificacion(null);
                    softwareCsaRepository.save(softwareCSA);
                }

            }
        }
    }
}
