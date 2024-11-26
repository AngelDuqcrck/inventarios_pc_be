package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.controllers.NotificationController;
import com.inventarios.pc.inventarios_pc_be.entities.CambioEstadoTickets;
import com.inventarios.pc.inventarios_pc_be.entities.CambioUbicacionPc;
import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoSolicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoTickets;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwareCSA;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;
import com.inventarios.pc.inventarios_pc_be.entities.Solicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.Tickets;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.RequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TicketNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeRequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.CambioEstTicketRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.CambioUbicacionPcRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoSolicitudesRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoTicketsRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.HistorialDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.RolRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SoftwareCsaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SolicitudRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TicketRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UsuarioRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITicketService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TicketDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarEstadoTicketRequest;
import com.inventarios.pc.inventarios_pc_be.shared.requests.ObservacionRequest;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarEstadoTicketRequest.CambiarEstadoTicketRequestBuilder;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComputadoresResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketsResponse;

@Service
public class TicketServiceImplementation implements ITicketService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";
    public static final String IS_NOT_VINCULATED = "%s no esta vinculado";

    @Autowired
    private SoftwareCsaRepository softwareCsaRepository;
    @Autowired
    private HistorialDispositivoRepository historialDispositivoRepository;
    @Autowired
    private ComputadorRepository computadorRepository;

    @Autowired
    private CambioUbicacionPcRepository cambioUbicacionPcRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;
    @Autowired
    private EstadoDispositivoRepository estadoDispositivoRepository;

    @Autowired
    private ComputadorServiceImplementation computadorService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstadoTicketsRepository estadoTicketsRepository;

    @Autowired
    private CambioEstTicketRepository cambioEstadoTicketsRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EstadoSolicitudesRepository estadoSolicitudesRepository;

    @Autowired
    private NotificationController notificationController;

    @Override
    public TicketDTO crearTicket(TicketDTO ticketDTO) throws RequestNotFoundException, StateNotFoundException,
            RolNotFoundException, UserNotFoundException, SelectNotAllowedException {

        Solicitudes solicitud = solicitudRepository.findById(ticketDTO.getSolicitudes()).orElse(null);

        if (solicitud == null) {
            throw new RequestNotFoundException(String.format(IS_NOT_FOUND_F, "LA SOLICITUD").toUpperCase());
        }

        if (!solicitud.getEstadoSolicitudes().getNombre().equals("En Revision")
                && !solicitud.getEstadoSolicitudes().getNombre().equals("En Proceso")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA SOLICITUD PORQUE SU ESTADO ACTUAL NO ES EN REVISION").toUpperCase());
        }

        ticketRepository.findBySolicitudesAndEstadoTicketsNombreNot(solicitud, "Cancelado")
                .ifPresent(existingTicket -> {
                    try {
                        throw new SelectNotAllowedException(
                                "Ya existe un ticket asociado a esta solicitud que no está cancelado");
                    } catch (SelectNotAllowedException e) {
                        throw new RuntimeException(e);
                    }
                });

        Tickets ticket = new Tickets();
        BeanUtils.copyProperties(ticketDTO, ticket);

        ticket.setSolicitudes(solicitud);

        asignarTecnicoATicket(ticketDTO.getUsuario(), ticket);
        ticket.setFecha_asig(new Date());
        EstadoTickets estadoTickets = estadoTicketsRepository.findByNombre("En Proceso").orElse(null);

        if (estadoTickets == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO EN PROCESO").toUpperCase());
        }

        ticket.setEstadoTickets(estadoTickets);

        Tickets ticketCreado = ticketRepository.save(ticket);
        //crearCambioEstado(ticketCreado, estadoTickets);

        EstadoSolicitudes estadoSolicitudes = estadoSolicitudesRepository.findById(2).orElse(null);
        if (estadoSolicitudes == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO EN PROCESO").toUpperCase());
        }

        solicitud.setEstadoSolicitudes(estadoSolicitudes);
        solicitudRepository.save(solicitud);
        notificationController.notifyStatusUpdate("SOLICITUD", solicitud.getId(), solicitud.getEstadoSolicitudes().getNombre(), null);

        TicketDTO ticketCreadoDTO = new TicketDTO();
        ticketCreadoDTO.setSolicitudes(ticket.getSolicitudes().getId());
        ticketCreadoDTO.setUsuario(ticket.getUsuario().getId());
        BeanUtils.copyProperties(ticketCreado, ticketCreadoDTO);
        TicketsResponse ticketCreadoResponse = new TicketsResponse();
        ticketCreadoResponse.setId(ticketCreado.getId());
        ticketCreadoResponse.setFechaCierre(ticketCreado.getFechaCierre());
        ticketCreadoResponse.setFecha_asig(ticketCreado.getFecha_asig());
        ticketCreadoResponse.setNombre(ticketCreado.getSolicitudes().getTipoSolicitudes().getNombre());
        ticketCreadoResponse.setUsuario(ticketCreado.getUsuario().getPrimerNombre() + " " + ticketCreado.getUsuario().getPrimerApellido());
        ticketCreadoResponse.setEstadoTicket(ticketCreado.getEstadoTickets().getNombre());
        notificationController.notifyNewTicket("NEWTICKET", ticketCreado.getId(), ticketCreadoResponse);
        return ticketCreadoDTO;
    }

    @Override
    public List<TicketsResponse> listarTickets() {

        List<Tickets> tickets = ticketRepository.findAll();

        List<TicketsResponse> ticketsResponses = new ArrayList<>();

        for (Tickets ticket : tickets) {

            TicketsResponse ticketResponse = new TicketsResponse().builder()
                    .id(ticket.getId())
                    .fechaCierre(ticket.getFechaCierre())
                    .fecha_asig(ticket.getFecha_asig())
                    .nombre(ticket.getSolicitudes().getTipoSolicitudes().getNombre())
                    .usuario(ticket.getUsuario().getPrimerNombre() + " " + ticket.getUsuario().getPrimerApellido())
                    .estadoTicket(ticket.getEstadoTickets().getNombre())
                    .build();

            ticketsResponses.add(ticketResponse);

        }

        return ticketsResponses;
    }

    @Override
    public TicketIdResponse listarTicketById(Integer ticketId) throws TicketNotFoundException {
        Tickets ticket = ticketRepository.findById(ticketId).orElse(null);

        if (ticket == null) {
            throw new TicketNotFoundException(String.format(IS_NOT_FOUND, "EL TICKET").toUpperCase());

        }

        TicketIdResponse ticketIdResponse = new TicketIdResponse();

        BeanUtils.copyProperties(ticket, ticketIdResponse);

        ticketIdResponse.setEstadoTickets(ticket.getEstadoTickets().getNombre());
        ticketIdResponse.setNombre(ticket.getSolicitudes().getTipoSolicitudes().getNombre());
        ticketIdResponse.setSolicitudId(ticket.getSolicitudes().getId());
        ticketIdResponse.setSolicitudes(ticket.getSolicitudes().getTitulo());
        ticketIdResponse
                .setUsuario(ticket.getUsuario().getPrimerNombre() + " " + ticket.getUsuario().getPrimerApellido());

        return ticketIdResponse;
    }

    @Override
    public List<TicketsResponse> listarTicketsByUsuario(String correo)
            throws RolNotFoundException, UserNotFoundException {
        Rol rol = rolRepository.findByNombre("SISTEMAS").orElse(null);

        if (rol == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "EL ROL SISTEMAS").toUpperCase());
        }

        Usuario usuario = usuarioRepository.findByCorreoAndRolId(correo, rol).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "EL USUARIO").toUpperCase());
        }

        List<Tickets> tickets = ticketRepository.findByUsuario(usuario);

        List<TicketsResponse> ticketsResponses = new ArrayList<>();

        for (Tickets ticket : tickets) {

            TicketsResponse ticketResponse = new TicketsResponse().builder()
                    .id(ticket.getId())
                    .fechaCierre(ticket.getFechaCierre())
                    .fecha_asig(ticket.getFecha_asig())
                    .nombre(ticket.getSolicitudes().getTipoSolicitudes().getNombre())
                    .usuario(ticket.getUsuario().getPrimerNombre() + " " + ticket.getUsuario().getPrimerApellido())
                    .estadoTicket(ticket.getEstadoTickets().getNombre())
                    .build();

            ticketsResponses.add(ticketResponse);

        }

        return ticketsResponses;

    }

    @Override
    public TicketDTO editarTicket(Integer ticketId, TicketDTO ticketDTO)
            throws RequestNotFoundException, StateNotFoundException,
            SelectNotAllowedException, RolNotFoundException, UserNotFoundException, TicketNotFoundException,
            UpdateNotAllowedException {
        Tickets ticket = ticketRepository.findById(ticketId).orElse(null);

        if (ticket == null) {
            throw new TicketNotFoundException(String.format(IS_NOT_FOUND, "EL TICKET").toUpperCase());

        }

        if (!ticket.getEstadoTickets().getNombre().equals("En Proceso")) {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTE TICKET PORQUE SU ESTADO ACTUAL NO ES EN PROCESO").toUpperCase());
        }

        BeanUtils.copyProperties(ticketDTO, ticket);
        ticket.setId(ticketId);

        if (ticketDTO.getUsuario() != null) {

            asignarTecnicoATicket(ticketDTO.getUsuario(), ticket);

        } else {
            ticket.setUsuario(ticket.getUsuario());
        }

        Tickets ticketEditado = ticketRepository.save(ticket);
        TicketsResponse ticketR = new TicketsResponse();
        ticketR.setId(ticketEditado.getId());
        ticketR.setFechaCierre(ticketEditado.getFechaCierre());
        ticketR.setFecha_asig(ticketEditado.getFecha_asig());
        ticketR.setNombre(ticketEditado.getSolicitudes().getTipoSolicitudes().getNombre());
        ticketR.setUsuario(ticketEditado.getUsuario().getPrimerNombre() + " " + ticketEditado.getUsuario().getPrimerApellido());
        ticketR.setEstadoTicket(ticketEditado.getEstadoTickets().getNombre());
        notificationController.sendNotification("NEWTECHTICKET", ticketEditado.getId(), ticketR);
        TicketDTO ticketEditadoDTO = new TicketDTO();
        BeanUtils.copyProperties(ticketEditado, ticketEditadoDTO);
        ticketDTO.setSolicitudes(ticketEditado.getSolicitudes().getId());
        ticketDTO.setUsuario(ticketEditado.getUsuario().getId());

        return ticketEditadoDTO;

    }

    @Override
    public void registrarObservacion(ObservacionRequest observacionRequest)
            throws TicketNotFoundException, SelectNotAllowedException {

        Tickets ticket = ticketRepository.findById(observacionRequest.getTicketId()).orElse(null);

        if (ticket == null) {
            throw new TicketNotFoundException(String.format(IS_NOT_FOUND, "EL TICKET").toUpperCase());

        }

        if (!ticket.getEstadoTickets().getNombre().equals("En Proceso")) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE TICKET PORQUE SU ESTADO ACTUAL NO ES EN PROCESO").toUpperCase());
        }

        ticket.setObservacion(observacionRequest.getObservacion());

        ticketRepository.save(ticket);
    }

    public void cambiarEstadoTickets(CambiarEstadoTicketRequest cambiarEstadoTicketRequest)
            throws SelectNotAllowedException, TicketNotFoundException, StateNotFoundException,
            TypeRequestNotFoundException {

        Tickets ticket = ticketRepository.findById(cambiarEstadoTicketRequest.getTicketId()).orElse(null);

        if (ticket == null) {
            throw new TicketNotFoundException(String.format(IS_NOT_FOUND, "EL TICKET").toUpperCase());

        }

        EstadoTickets estadoTickets = estadoTicketsRepository
                .findById(cambiarEstadoTicketRequest.getNuevoEstadoTicketId()).orElse(null);

        if (estadoTickets == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, " EL ESTADO DEL TICKET").toUpperCase());
        }

        Solicitudes solicitud = ticket.getSolicitudes();

        switch (cambiarEstadoTicketRequest.getNuevoEstadoTicketId()) {
            case 2: // Finalizado
                if (!ticket.getEstadoTickets().getNombre().equals("En Proceso")) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE TICKET PORQUE SU ESTADO ACTUAL NO ES EN PROCESO").toUpperCase());
                }

                ticket.setFechaCierre(new Date());

                EstadoSolicitudes estadoSolicitudFinalizada = estadoSolicitudesRepository.findById(3).orElse(null);
                if (estadoSolicitudFinalizada == null) {
                    throw new StateNotFoundException(
                            String.format(IS_NOT_FOUND, "EL ESTADO COMPLETADO").toUpperCase());
                }

                Integer tipoSolicitud = solicitud.getTipoSolicitudes().getId();

                switch (tipoSolicitud) {
                    case 1: // Reparacion
                        if (cambiarEstadoTicketRequest.getResuelto() == true) {
                            DispositivoPC dispositivoPC = solicitud.getDispositivoPC();
                            Computador computador = solicitud.getComputador();
                            if (solicitud.getEsHardaware() == true) {

                                EstadoDispositivo estadoEnUso = estadoDispositivoRepository.findById(1)
                                        .orElse(null);

                                if (estadoEnUso == null) {
                                    throw new StateNotFoundException(
                                            String.format(IS_NOT_FOUND, "EL ESTADO EN USO").toUpperCase());
                                }

                                dispositivoPC.setEstadoDispositivo(estadoEnUso);

                                HistorialDispositivo historial = historialDispositivoRepository
                                        .findTopByComputadorAndDispositivoPCOrderByFechaDesvinculacionDesc(
                                                computador, dispositivoPC);
                                if (dispositivoPC.getTipoDispositivo().getId() != 8)// Tipo de dispositivo diferente a
                                                                                    // torre

                                {
                                    if (historial != null) {
                                        historial.setFechaDesvinculacion(null);
                                        historial.setJustificacion(null);
                                        historialDispositivoRepository.save(historial);
                                    }
                                }
                                dispositivoRepository.save(dispositivoPC);
                                notificationController.notifyStatusUpdate("DISPOSITIVO", dispositivoPC.getId(), dispositivoPC.getEstadoDispositivo().getNombre(), null);

                            }
                            if (solicitud.getEsHardaware() == false) {
                                SoftwarePC softwarePC = solicitud.getSoftwarePC();
                                SoftwareCSA softwareCSA = softwareCsaRepository
                                        .findTopByComputadorAndSoftwarePCOrderByFechaDesvinculacionDesc(computador,
                                                softwarePC);

                                if (softwareCSA != null) {
                                    softwareCSA.setFechaDesvinculacion(null);
                                    softwareCSA.setJustificacion(null);
                                    softwareCsaRepository.save(softwareCSA);
                                }
                            }
                        }
                        if (cambiarEstadoTicketRequest.getResuelto() == false) {
                            if (solicitud.getEsHardaware() == true) {
                                DispositivoPC dispositivoPC = solicitud.getDispositivoPC();

                                EstadoDispositivo estadoBaja = estadoDispositivoRepository.findById(5).orElse(null);
                                if (estadoBaja == null) {
                                    throw new StateNotFoundException(
                                            String.format(IS_NOT_FOUND, "EL ESTADO DADO DE BAJA").toUpperCase());
                                }
                                dispositivoPC.setEstadoDispositivo(estadoBaja);
                                dispositivoRepository.save(dispositivoPC);
                                notificationController.notifyStatusUpdate("DISPOSITIVO", dispositivoPC.getId(), dispositivoPC.getEstadoDispositivo().getNombre(), null);
                            }
                        }
                        break;

                    case 2:// Cambio de ubicacion
                        if (cambiarEstadoTicketRequest.getResuelto() == true) {
                            Ubicacion ubicacionDestino = solicitud.getUbicacionDestino();

                            Computador computador = solicitud.getComputador();

                            Ubicacion ubicacionOrigen = solicitud.getUbicacionOrigen();

                            computadorService.crearCambioUbicacionPc(computador, ubicacionDestino, ubicacionOrigen,
                                    "Se ha reasignado exitosamente el equipo " + computador.getNombre()
                                            + " a la ubicacion " + ubicacionDestino.getNombre());

                            computador.setUbicacion(ubicacionDestino);
                            //computador.setResponsable(null);
                            Computador computadorActualizado =computadorRepository.save(computador);
                            ComputadoresResponse computadorResponse = new ComputadoresResponse();
        computadorResponse.setId(computadorActualizado.getId());
        computadorResponse.setTipoPC(computador.getTipoPC().getNombre());
        computadorResponse.setSede(computadorActualizado.getUbicacion().getArea().getSede().getNombre());
        computadorResponse.setArea(computadorActualizado.getUbicacion().getArea().getNombre());
        computadorResponse.setUbicacion(computadorActualizado.getUbicacion().getNombre());
        computadorResponse.setNombre(computadorActualizado.getNombre());
        computadorResponse.setPlaca(computadorActualizado.getPlaca());
        computadorResponse.setModelo(computadorActualizado.getModelo());
        computadorResponse.setSerial(computadorActualizado.getSerial());
        computadorResponse.setMarca(computadorActualizado.getMarca().getNombre());
        computadorResponse.setEstadoDispositivo(computadorActualizado.getEstadoDispositivo().getNombre());
        computadorResponse.setIpAsignada(computadorActualizado.getIpAsignada());
        notificationController.sendNotification("COMPUTADOR", computadorActualizado.getId(), computadorResponse);

                        }
                        if (cambiarEstadoTicketRequest.getResuelto() == false) {
                            Computador computador = solicitud.getComputador();

                            Ubicacion ubicacionOrigen = solicitud.getUbicacionOrigen();

                            computador.setUbicacion(ubicacionOrigen);

                            Computador computadorActualizado =computadorRepository.save(computador);
                            ComputadoresResponse computadorResponse = new ComputadoresResponse();
        computadorResponse.setId(computadorActualizado.getId());
        computadorResponse.setTipoPC(computador.getTipoPC().getNombre());
        computadorResponse.setSede(computadorActualizado.getUbicacion().getArea().getSede().getNombre());
        computadorResponse.setArea(computadorActualizado.getUbicacion().getArea().getNombre());
        computadorResponse.setUbicacion(computadorActualizado.getUbicacion().getNombre());
        computadorResponse.setNombre(computadorActualizado.getNombre());
        computadorResponse.setPlaca(computadorActualizado.getPlaca());
        computadorResponse.setModelo(computadorActualizado.getModelo());
        computadorResponse.setSerial(computadorActualizado.getSerial());
        computadorResponse.setMarca(computadorActualizado.getMarca().getNombre());
        computadorResponse.setEstadoDispositivo(computadorActualizado.getEstadoDispositivo().getNombre());
        computadorResponse.setIpAsignada(computadorActualizado.getIpAsignada());
        notificationController.sendNotification("COMPUTADOR", computadorActualizado.getId(), computadorResponse);
                        }
                        
                        break;

                    case 3: // Mantenimiento preventivo
                        if (cambiarEstadoTicketRequest.getResuelto() == true) {
                            DispositivoPC dispositivoPC = solicitud.getDispositivoPC();
                            Computador computador = solicitud.getComputador();
                            if (solicitud.getEsHardaware() == true) {

                                EstadoDispositivo estadoEnUso = estadoDispositivoRepository.findById(1)
                                        .orElse(null);

                                if (estadoEnUso == null) {
                                    throw new StateNotFoundException(
                                            String.format(IS_NOT_FOUND, "EL ESTADO EN USO").toUpperCase());
                                }

                                dispositivoPC.setEstadoDispositivo(estadoEnUso);

                                HistorialDispositivo historial = historialDispositivoRepository
                                        .findTopByComputadorAndDispositivoPCOrderByFechaDesvinculacionDesc(
                                                computador, dispositivoPC);
                                if (dispositivoPC.getTipoDispositivo().getId() != 8)// Tipo de dispositivo diferente a
                                                                                    // torre

                                {
                                    if (historial != null) {
                                        historial.setFechaDesvinculacion(null);
                                        historial.setJustificacion(null);
                                        historialDispositivoRepository.save(historial);
                                    }
                                }
                                dispositivoRepository.save(dispositivoPC);
                                notificationController.notifyStatusUpdate("DISPOSITIVO", dispositivoPC.getId(), dispositivoPC.getEstadoDispositivo().getNombre(), null);

                            }
                            if (solicitud.getEsHardaware() == false) {
                                SoftwarePC softwarePC = solicitud.getSoftwarePC();
                                SoftwareCSA softwareCSA = softwareCsaRepository
                                        .findTopByComputadorAndSoftwarePCOrderByFechaDesvinculacionDesc(computador,
                                                softwarePC);

                                if (softwareCSA != null) {
                                    softwareCSA.setFechaDesvinculacion(null);
                                    softwareCSA.setJustificacion(null);
                                    softwareCsaRepository.save(softwareCSA);
                                }
                            }
                        }

                        if (cambiarEstadoTicketRequest.getResuelto() == false) {
                            EstadoTickets estadoReasignado = estadoTicketsRepository.findById(4).orElse(null);

                            if (estadoReasignado == null) {
                                throw new StateNotFoundException(
                                        String.format(IS_NOT_FOUND, "EL ESTADO REASIGNADO").toUpperCase());
                            }
                            ticket.setEstadoTickets(estadoReasignado);
                        }
                        break;
                    case 4:
                        ticket.setEstadoTickets(estadoTickets);
                        break;
                    default:
                        throw new TypeRequestNotFoundException(
                                String.format(IS_NOT_FOUND, "EL TIPO DE SOLICITUD").toUpperCase());
                }
                solicitud.setEstadoSolicitudes(estadoSolicitudFinalizada);
                solicitud.setFechaCierre(new Date());
                solicitudRepository.save(solicitud);
                notificationController.notifyStatusUpdate("SOLICITUD", solicitud.getId(), solicitud.getEstadoSolicitudes().getNombre(), solicitud.getFechaCierre());
                notificationController.notifyNewDate("TICKET", ticket.getId(), ticket.getFechaCierre());

                break;

            case 3: // Cancelado
                if (!ticket.getEstadoTickets().getNombre().equals("En Proceso")) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "SELECCIONAR ESTE TICKET PORQUE SU ESTADO ACTUAL ES DIFERENTE A EN PROCESO")
                                    .toUpperCase());

                }

                ticket.setFechaCierre(new Date());
                notificationController.notifyNewDate("TICKET", ticket.getId(), ticket.getFechaCierre());

                EstadoSolicitudes estadoEnRevision = estadoSolicitudesRepository.findById(6).orElse(null);
                if (estadoEnRevision == null) {
                    throw new StateNotFoundException(
                            String.format(IS_NOT_FOUND, "EL ESTADO DE LA SOLICITUD").toUpperCase());
                }

                solicitud.setEstadoSolicitudes(estadoEnRevision);
                //notificationController.notifyNewDate("SOLICITUD", solicitud.getId(), solicitud.getFechaCierre());
                solicitudRepository.save(solicitud);
                notificationController.notifyStatusUpdate("SOLICITUD", solicitud.getId(), solicitud.getEstadoSolicitudes().getNombre(), solicitud.getFechaCierre());
                break;

            case 4: // Reasignado
                if (!ticket.getEstadoTickets().getNombre().equals("En Proceso")) {
                    throw new SelectNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "SELECCIONAR ESTE TICKET PORQUE SU ESTADO ACTUAL ES DIFERENTE A EN PROCESO")
                                    .toUpperCase());

                }
                break;

            default:
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "UTILIZAR ESTE ESTADO DE TICKET").toUpperCase());
        }

        ticket.setEstadoTickets(estadoTickets);

        if (ticket.getSolicitudes().getTipoSolicitudes().getId() == 3
                && cambiarEstadoTicketRequest.getResuelto() == false) {
            EstadoTickets estadoReasignado = estadoTicketsRepository.findById(4).orElse(null);

            if (estadoReasignado == null) {
                throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO REASIGNADO").toUpperCase());
            }
            ticket.setEstadoTickets(estadoReasignado);
        }

        ticketRepository.save(ticket);
        notificationController.notifyStatusUpdate("TICKET", ticket.getId(), ticket.getEstadoTickets().getNombre(), ticket.getFechaCierre());
        //crearCambioEstado(ticket, estadoTickets);
    }

    @Override
    public void reasignarTicket(Integer ticketId, Integer tecnicoId)
            throws TicketNotFoundException, RolNotFoundException, UserNotFoundException, SelectNotAllowedException {
        Tickets ticket = ticketRepository.findById(ticketId).orElse(null);

        if (ticket == null) {
            throw new TicketNotFoundException(String.format(IS_NOT_FOUND, "EL TICKET").toUpperCase());

        }
        if (!ticket.getEstadoTickets().getNombre().equals("Reasignado")) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED,
                    "SELECCIONAR ESTE TICKET PORQUE SU ESTADO ACTUAL ES DIFERENTE A EN PROCESO"));

        }
        asignarTecnicoATicket(tecnicoId, ticket);

        EstadoTickets estadoTickets = estadoTicketsRepository.findById(1).orElse(null);
        if (estadoTickets == null) {
            throw new SelectNotAllowedException(String.format(IS_NOT_FOUND, "EL ESTADO EN PROCESO").toUpperCase());
        }
        if (estadoTickets.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String
                    .format(IS_NOT_ALLOWED, "EL ESTADO EN PROCESO PORQUE SE ENCUENTRA DESACTIVADO").toUpperCase());
        }
        ticket.setEstadoTickets(estadoTickets);
        Tickets ticketEditado = ticketRepository.save(ticket);
        TicketsResponse ticketR = new TicketsResponse();
        ticketR.setId(ticketEditado.getId());
        ticketR.setFechaCierre(ticketEditado.getFechaCierre());
        ticketR.setFecha_asig(ticketEditado.getFecha_asig());
        ticketR.setNombre(ticketEditado.getSolicitudes().getTipoSolicitudes().getNombre());
        ticketR.setUsuario(ticketEditado.getUsuario().getPrimerNombre() + " " + ticketEditado.getUsuario().getPrimerApellido());
        ticketR.setEstadoTicket(ticketEditado.getEstadoTickets().getNombre());
        notificationController.sendNotification("NEWTECHTICKET", ticketEditado.getId(), ticketR);
        notificationController.notifyStatusUpdate("TICKET", ticket.getId(), ticket.getEstadoTickets().getNombre(),null);
    }
    // |--------------------------------------------------------------------------------------------------------------------------------------------------------------|

    private void asignarTecnicoATicket(Integer tecnicoId, Tickets ticket)
            throws RolNotFoundException, UserNotFoundException {

        Rol rol = rolRepository.findByNombre("SISTEMAS").orElse(null);

        if (rol == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "ROL").toUpperCase());
        }

        Usuario usuario = usuarioRepository.findByIdAndRolId(tecnicoId, rol).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }

        ticket.setUsuario(usuario);

    }

}
