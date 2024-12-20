package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.RequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TicketNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeRequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TicketDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarEstadoTicketRequest;
import com.inventarios.pc.inventarios_pc_be.shared.requests.ObservacionRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketsResponse;

public interface ITicketService {

        public TicketDTO crearTicket(TicketDTO ticketDTO) throws RequestNotFoundException, StateNotFoundException,
             RolNotFoundException, UserNotFoundException, SelectNotAllowedException;

     public List<TicketsResponse> listarTickets ();

     public TicketIdResponse listarTicketById(Integer ticketId)throws TicketNotFoundException;

     public List<TicketsResponse> listarTicketsByUsuario(String correo) throws  RolNotFoundException, UserNotFoundException;

    public TicketDTO editarTicket(Integer ticketId, TicketDTO ticketDTO)
            throws RequestNotFoundException, StateNotFoundException,
            SelectNotAllowedException, RolNotFoundException, UserNotFoundException, TicketNotFoundException,
            UpdateNotAllowedException;

    public void registrarObservacion(ObservacionRequest observacionRequest)throws TicketNotFoundException, SelectNotAllowedException;

    public void cambiarEstadoTickets(CambiarEstadoTicketRequest cambiarEstadoTicketRequest)
    throws SelectNotAllowedException, TicketNotFoundException, StateNotFoundException, TypeRequestNotFoundException;

    public void reasignarTicket(Integer ticketId, Integer tecnicoId)throws TicketNotFoundException, RolNotFoundException, UserNotFoundException, SelectNotAllowedException;
}