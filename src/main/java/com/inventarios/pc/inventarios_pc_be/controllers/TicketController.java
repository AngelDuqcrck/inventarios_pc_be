package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.exceptions.RequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TicketNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeRequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITicketService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TicketDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarEstadoTicketRequest;
import com.inventarios.pc.inventarios_pc_be.shared.requests.ObservacionRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.TicketsResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/ticket")
public class TicketController {

        @Autowired
        private ITicketService ticketService;

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/crear")
        public ResponseEntity<HttpResponse> crearTicket(@RequestBody TicketDTO ticketDTO)
                        throws RequestNotFoundException,
                        StateNotFoundException, SelectNotAllowedException, RolNotFoundException, UserNotFoundException {
                ticketService.crearTicket(ticketDTO);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Ticket creado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @PutMapping("/editar/{ticketId}")
        public ResponseEntity<HttpResponse> editarTicket(@PathVariable Integer ticketId,
                        @RequestBody TicketDTO ticketDTO)
                        throws RequestNotFoundException, StateNotFoundException,
                        SelectNotAllowedException, RolNotFoundException, UserNotFoundException, TicketNotFoundException,
                        UpdateNotAllowedException {

                ticketService.editarTicket(ticketId, ticketDTO);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Ticket editado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping
        public ResponseEntity<List<TicketsResponse>> listarTickets() {
                List<TicketsResponse> ticketsResponses = ticketService.listarTickets();

                return new ResponseEntity<>(ticketsResponses, HttpStatus.OK);
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/{ticketId}")
        public ResponseEntity<TicketIdResponse> listarTicketById(@PathVariable Integer ticketId)
                        throws TicketNotFoundException {
                TicketIdResponse ticketIdResponse = ticketService.listarTicketById(ticketId);

                return new ResponseEntity<>(ticketIdResponse, HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('TECNICO_SISTEMAS')")
        @GetMapping("/usuario/{correo}")
        public ResponseEntity<List<TicketsResponse>> listarTicketByUsuario(@PathVariable String correo)
                        throws RolNotFoundException, UserNotFoundException {
                List<TicketsResponse> ticketsResponses = ticketService.listarTicketsByUsuario(correo);

                return new ResponseEntity<>(ticketsResponses, HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('TECNICO_SISTEMAS')")
        @PostMapping("/observacion")
        public ResponseEntity<HttpResponse> registrarObservacion(@RequestBody ObservacionRequest observacionRequest)
                        throws TicketNotFoundException, SelectNotAllowedException {
                ticketService.registrarObservacion(observacionRequest);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Observacion registrada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('TECNICO_SISTEMAS')")
        @PostMapping("/finalizar")
        public ResponseEntity<HttpResponse> finalizarTicket(@RequestBody CambiarEstadoTicketRequest cambiarEstadoTicketRequest)
                        throws SelectNotAllowedException, TicketNotFoundException, StateNotFoundException, TypeRequestNotFoundException {
                ticketService.cambiarEstadoTickets(cambiarEstadoTicketRequest); //Estado 2 finalizado

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Ticket finalizado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('TECNICO_SISTEMAS')")
        @PostMapping("/reasignacion")
        public ResponseEntity<HttpResponse> solicitarReasginacionTicket(@RequestBody CambiarEstadoTicketRequest cambiarEstadoTicketRequest)
                        throws SelectNotAllowedException, TicketNotFoundException, StateNotFoundException, TypeRequestNotFoundException {
                ticketService.cambiarEstadoTickets(cambiarEstadoTicketRequest); //Estado 4 reasignado

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud de reasignación de ticket realizada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/reasignar")
        public ResponseEntity<HttpResponse> reasignarTicket(@RequestParam Integer ticketId, @RequestParam Integer tecnicoId)
                        throws SelectNotAllowedException, TicketNotFoundException, StateNotFoundException, RolNotFoundException, UserNotFoundException {
                ticketService.reasignarTicket(ticketId, tecnicoId);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Ticket reasignado exitosamente"),
                                HttpStatus.OK);
        }
        

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/cancelar")
        public ResponseEntity<HttpResponse> cancelarTicket(@RequestBody CambiarEstadoTicketRequest cambiarEstadoTicketRequest)
                        throws SelectNotAllowedException, TicketNotFoundException, StateNotFoundException, TypeRequestNotFoundException {
                ticketService.cambiarEstadoTickets(cambiarEstadoTicketRequest); //Estado 3 cancelado

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Ticket cancelado exitosamente"),
                                HttpStatus.OK);
        }
}
