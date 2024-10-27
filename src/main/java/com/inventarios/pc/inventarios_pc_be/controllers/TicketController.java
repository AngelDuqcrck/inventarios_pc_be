package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.exceptions.RequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TicketNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITicketService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TicketDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketsResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/ticket")
public class TicketController {
    
    @Autowired
    private ITicketService ticketService;


    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearTicket (@RequestBody TicketDTO ticketDTO)throws RequestNotFoundException, StateNotFoundException, SelectNotAllowedException, RolNotFoundException, UserNotFoundException{
        ticketService.crearTicket(ticketDTO);

        return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Ticket creado exitosamente"),
                                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TicketsResponse>> listarTickets() {
        List<TicketsResponse> ticketsResponses = ticketService.listarTickets();

        return new ResponseEntity<>(ticketsResponses, HttpStatus.OK);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketIdResponse> listarTicketById(@PathVariable Integer ticketId) throws TicketNotFoundException {
        TicketIdResponse ticketIdResponse = ticketService.listarTicketById(ticketId);

        return new ResponseEntity<>(ticketIdResponse, HttpStatus.OK);
    }
    
    @GetMapping("/usuario/{correo}")
    public ResponseEntity<List<TicketsResponse>> listarTicketByUsuario(@PathVariable String correo) throws  RolNotFoundException, UserNotFoundException {
        List<TicketsResponse> ticketsResponses = ticketService.listarTicketsByUsuario(correo);

        return new ResponseEntity<>(ticketsResponses, HttpStatus.OK);
    }
    
}
