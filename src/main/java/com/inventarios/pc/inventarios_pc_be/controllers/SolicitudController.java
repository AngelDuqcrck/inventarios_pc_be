package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.services.interfaces.ISolicitudService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SolicitudDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.ActualizarSolicitudRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudesResponse;

@RestController
@RequestMapping("/solicitud")
public class SolicitudController {

        @Autowired
        private ISolicitudService solicitudService;

        @PreAuthorize("hasAnyAuthority('EMPLEADO_ASISTENCIAL','ADMIN')")
        @PostMapping("/crear/asistencial")
        public ResponseEntity<HttpResponse> crearSolicitudAsistencial(@RequestBody SolicitudDTO solicitudDTO,
                        @RequestParam Integer tipoSolicitudId)
                        throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException,
                        LocationNotFoundException,
                        TypeRequestNotFoundException, SoftwareNotFoundException {

                solicitudService.crearSolicitudAsistencial(solicitudDTO, tipoSolicitudId);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud creada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('EMPLEADO_ADMINISTRATIVO')")
        @PostMapping("/crear/administrativo")
        public ResponseEntity<HttpResponse> crearSolicitudAdministrativo(@RequestBody SolicitudDTO solicitudDTO,
                        @RequestParam Integer tipoSolicitudId)
                        throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException,
                        LocationNotFoundException,
                        TypeRequestNotFoundException, SoftwareNotFoundException {

                solicitudService.crearSolicitudAdministrativo(solicitudDTO, tipoSolicitudId);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud creada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAnyAuthority('EMPLEADO_ASISTENCIAL', 'EMPLEADO_ADMINISTRATIVO', 'ADMIN')")
        @PutMapping("/editar/{solicitudId}")
        public ResponseEntity<HttpResponse> editarSolicitud(@PathVariable Integer solicitudId,
                        @RequestBody ActualizarSolicitudRequest solicitudRequest)  throws RequestNotFoundException, SelectNotAllowedException, UpdateNotAllowedException,
            ComputerNotFoundException, StateNotFoundException, LocationNotFoundException, DeviceNotFoundException{
                solicitudService.editarSolicitud(solicitudId, solicitudRequest);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud actualizada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping
        public ResponseEntity<List<SolicitudesResponse>> listarSolicitudes() {

                List<SolicitudesResponse> solicitudesResponses = solicitudService.listarSolicitudes();

                return new ResponseEntity<>(solicitudesResponses, HttpStatus.OK);
        }

        @PreAuthorize("hasAnyAuthority('EMPLEADO_ASISTENCIAL', 'EMPLEADO_ADMINISTRATIVO')")
        @GetMapping("/usuario/{usuario}")
        public ResponseEntity<List<SolicitudesResponse>> listarSolicitudesByUsuario(@PathVariable String usuario)
                        throws UserNotFoundException {

                List<SolicitudesResponse> solicitudesResponses = solicitudService.listarSolicitudesByUsuario(usuario);

                return new ResponseEntity<>(solicitudesResponses, HttpStatus.OK);
        }

        @PreAuthorize("hasAnyAuthority('EMPLEADO_ASISTENCIAL', 'EMPLEADO_ADMINISTRATIVO')")
        @PostMapping("/cancelar-solicitud/{solicitudId}")
        public ResponseEntity<HttpResponse> cancelarSolicitud(@PathVariable Integer solicitudId)
                        throws StateNotFoundException, RequestNotFoundException, SelectNotAllowedException {

                solicitudService.cancelarSolicitud(solicitudId);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud cancelada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/pendiente/{solicitudId}")
        public ResponseEntity<HttpResponse> retornarSolicitudPendiente(@PathVariable Integer solicitudId)
                        throws StateNotFoundException, RequestNotFoundException, SelectNotAllowedException {

                solicitudService.retornarSolicitudPendiente(solicitudId);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud retornada a pendiente exitosamente"), 
                                HttpStatus.OK);
        }
        @PreAuthorize("hasAnyAuthority('ADMIN')")
        @PostMapping("/rechazar-solicitud/{solicitudId}")
        public ResponseEntity<HttpResponse> rechazarSolicitud(@PathVariable Integer solicitudId)
                        throws StateNotFoundException, RequestNotFoundException, SelectNotAllowedException {

                solicitudService.rechazarSolicitud(solicitudId);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Solicitud rechazada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/{solicitudId}/{correo}")
        public ResponseEntity<SolicitudIdResponse> listarSolicitudesById(@PathVariable Integer solicitudId, @PathVariable String correo)
                        throws RequestNotFoundException, StateNotFoundException, UserNotFoundException, SelectNotAllowedException {

                SolicitudIdResponse solicitudIdResponse = solicitudService.listarSolicitudById(solicitudId, correo);

                return new ResponseEntity<>(solicitudIdResponse, HttpStatus.OK);
        }

}
