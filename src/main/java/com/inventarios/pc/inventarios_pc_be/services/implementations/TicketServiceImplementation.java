package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.Date;

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
    public TicketDTO crearTicket(TicketDTO ticketDTO )throws RequestNotFoundException, StateNotFoundException, SelectNotAllowedException, RolNotFoundException, UserNotFoundException{

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
    
        EstadoTickets estadoTickets = estadoTicketsRepository.findByNombre("En Proceso").orElse(null);

        if(estadoTickets == null){
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DEL TICKET").toUpperCase());
        }

        ticket.setEstadoTickets(estadoTickets);

        Tickets ticketCreado = ticketRepository.save(ticket);
        crearCambioEstado(ticket, estadoTickets);
        
        EstadoSolicitudes estadoSolicitudes = estadoSolicitudesRepository.findByNombre("En Proceso").orElse(null);
        if(estadoSolicitudes == null){
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




    private void asignarTecnicoATicket(Integer tecnicoId, Tickets ticket)throws RolNotFoundException, UserNotFoundException{

        Rol rol = rolRepository.findByNombre("TECNICO_SISTEMAS").orElse(null);

        if(rol == null){
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "SOLICITUD").toUpperCase());
        }

        Usuario usuario = usuarioRepository.findByIdAndRolId(tecnicoId, rol).orElse(null);
        
        if(usuario == null)
        {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }

        ticket.setUsuario(usuario);

        ticket.setFecha_asig(new Date());
    }

    private void  crearCambioEstado (Tickets tickets, EstadoTickets estadoTickets){
        CambioEstadoTickets cambioEstadoTickets= new CambioEstadoTickets();
        cambioEstadoTickets.setTickets(tickets);
        cambioEstadoTickets.setEstadoTickets(estadoTickets);
        cambioEstadoTickets.setFecha_cambio(new Date());

        cambioEstadoTicketsRepository.save(cambioEstadoTickets);
    }
}
