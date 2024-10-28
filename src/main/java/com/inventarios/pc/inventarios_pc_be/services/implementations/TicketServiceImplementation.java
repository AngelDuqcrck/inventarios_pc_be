package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.CambioEstadoTickets;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoSolicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoTickets;
import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.entities.Solicitudes;
import com.inventarios.pc.inventarios_pc_be.entities.Tickets;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.RequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TicketNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.CambioEstTicketRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoSolicitudesRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoTicketsRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.RolRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SolicitudRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TicketRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UsuarioRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITicketService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TicketDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.ObservacionRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketsResponse;

@Service
public class TicketServiceImplementation implements ITicketService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";
    public static final String IS_NOT_VINCULATED = "%s no esta vinculado";

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

    @Override
    public TicketDTO crearTicket(TicketDTO ticketDTO) throws RequestNotFoundException, StateNotFoundException,
            SelectNotAllowedException, RolNotFoundException, UserNotFoundException {

        Solicitudes solicitud = solicitudRepository.findById(ticketDTO.getSolicitudes()).orElse(null);

        if (solicitud == null) {
            throw new RequestNotFoundException(String.format(IS_NOT_FOUND, "SOLICITUD").toUpperCase());
        }

        if (!solicitud.getEstadoSolicitudes().getNombre().equals("En Revision")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA SOLICITUD").toUpperCase());
        }

        Tickets ticket = new Tickets();
        BeanUtils.copyProperties(ticketDTO, ticket);

        ticket.setSolicitudes(solicitud);

        asignarTecnicoATicket(ticketDTO.getUsuario(), ticket);
        ticket.setFecha_asig(new Date());
        EstadoTickets estadoTickets = estadoTicketsRepository.findByNombre("En Proceso").orElse(null);

        if (estadoTickets == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DEL TICKET").toUpperCase());
        }

        ticket.setEstadoTickets(estadoTickets);

        Tickets ticketCreado = ticketRepository.save(ticket);
        crearCambioEstado(ticketCreado, estadoTickets);

        EstadoSolicitudes estadoSolicitudes = estadoSolicitudesRepository.findById(2).orElse(null);
        if (estadoSolicitudes == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DE LA SOLICITUD").toUpperCase());
        }

        solicitud.setEstadoSolicitudes(estadoSolicitudes);
        solicitudRepository.save(solicitud);

        TicketDTO ticketCreadoDTO = new TicketDTO();
        ticketCreadoDTO.setSolicitudes(ticket.getSolicitudes().getId());
        ticketCreadoDTO.setUsuario(ticket.getUsuario().getId());
        BeanUtils.copyProperties(ticketCreado, ticketCreadoDTO);
        return ticketCreadoDTO;
    }

    @Override
    public List<TicketsResponse> listarTickets() {

        List<Tickets> tickets = ticketRepository.findAll();

        List<TicketsResponse> ticketsResponses = new ArrayList<>();

        for (Tickets ticket : tickets) {

            TicketsResponse ticketResponse = new TicketsResponse().builder()
                    .id(ticket.getId())
                    .nombre(ticket.getNombre())
                    .fechaCierre(ticket.getFechaCierre())
                    .fecha_asig(ticket.getFecha_asig())
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
            throw new TicketNotFoundException(String.format(IS_NOT_FOUND, "TICKET").toUpperCase());

        }

        TicketIdResponse ticketIdResponse = new TicketIdResponse();

        BeanUtils.copyProperties(ticket, ticketIdResponse);

        ticketIdResponse.setEstadoTickets(ticket.getEstadoTickets().getNombre());
        ticketIdResponse.setSolicitudId(ticket.getSolicitudes().getId());
        ticketIdResponse.setSolicitudes(ticket.getSolicitudes().getTitulo());
        ticketIdResponse
                .setUsuario(ticket.getUsuario().getPrimerNombre() + " " + ticket.getUsuario().getPrimerApellido());

        return ticketIdResponse;
    }

    @Override
    public List<TicketsResponse> listarTicketsByUsuario(String correo)
            throws RolNotFoundException, UserNotFoundException {
        Rol rol = rolRepository.findByNombre("TECNICO_SISTEMAS").orElse(null);

        if (rol == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "ROL").toUpperCase());
        }

        Usuario usuario = usuarioRepository.findByCorreoAndRolId(correo, rol).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }

        List<Tickets> tickets = ticketRepository.findByUsuario(usuario);

        List<TicketsResponse> ticketsResponses = new ArrayList<>();

        for (Tickets ticket : tickets) {

            TicketsResponse ticketResponse = new TicketsResponse().builder()
                    .id(ticket.getId())
                    .nombre(ticket.getNombre())
                    .fechaCierre(ticket.getFechaCierre())
                    .fecha_asig(ticket.getFecha_asig())
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
            throw new TicketNotFoundException(String.format(IS_NOT_FOUND, "TICKET").toUpperCase());

        }

        if (!ticket.getEstadoTickets().getNombre().equals("En Proceso")) {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTE TICKET").toUpperCase());
        }

        BeanUtils.copyProperties(ticketDTO, ticket);
        ticket.setId(ticketId);

        if (ticketDTO.getUsuario() != null) {

            asignarTecnicoATicket(ticketDTO.getUsuario(), ticket);

        } else {
            ticket.setUsuario(ticket.getUsuario());
        }


        Tickets ticketEditado = ticketRepository.save(ticket);
        TicketDTO ticketEditadoDTO = new TicketDTO();
        BeanUtils.copyProperties(ticketEditado, ticketEditadoDTO);
        ticketDTO.setSolicitudes(ticketEditado.getSolicitudes().getId());
        ticketDTO.setUsuario(ticketEditado.getUsuario().getId());

        return ticketEditadoDTO;

    }

    @Override
    public void registrarObservacion(ObservacionRequest observacionRequest)throws TicketNotFoundException, SelectNotAllowedException{

        Tickets ticket = ticketRepository.findById(observacionRequest.getTicketId()).orElse(null);

        if (ticket == null) {
            throw new TicketNotFoundException(String.format(IS_NOT_FOUND, "TICKET").toUpperCase());

        }

        if (!ticket.getEstadoTickets().getNombre().equals("En Proceso")) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE TICKET").toUpperCase());
        }

        ticket.setObservacion(observacionRequest.getObservacion());

        ticketRepository.save(ticket);
    }


    // |--------------------------------------------------------------------------------------------------------------------------------------------------------------|

    private void asignarTecnicoATicket(Integer tecnicoId, Tickets ticket)
            throws RolNotFoundException, UserNotFoundException {

        Rol rol = rolRepository.findByNombre("TECNICO_SISTEMAS").orElse(null);

        if (rol == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "ROL").toUpperCase());
        }

        Usuario usuario = usuarioRepository.findByIdAndRolId(tecnicoId, rol).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }

        ticket.setUsuario(usuario);

    }

    private void crearCambioEstado(Tickets tickets, EstadoTickets estadoTickets) {
        CambioEstadoTickets cambioEstadoTickets = new CambioEstadoTickets();
        cambioEstadoTickets.setTickets(tickets);
        cambioEstadoTickets.setEstadoTickets(estadoTickets);
        cambioEstadoTickets.setFecha_cambio(new Date());

        cambioEstadoTicketsRepository.save(cambioEstadoTickets);
    }
}
