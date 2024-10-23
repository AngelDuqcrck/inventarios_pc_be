package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeRequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ISolicitudService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SolicitudDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudesResponse;

@RestController
@RequestMapping("/solicitud")
public class SolicitudController {

    @Autowired
    private ISolicitudService solicitudService;

    @PreAuthorize("hasAuthority('EMPLEADO_ASISTENCIAL')")
    @PostMapping("/crear/asistencial")
    public ResponseEntity<HttpResponse> crearSolicitudAsistencial(@RequestBody SolicitudDTO solicitudDTO,
            @RequestParam Integer tipoSolicitudId)
            throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException, LocationNotFoundException,
            TypeRequestNotFoundException {

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
            throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException, LocationNotFoundException,
            TypeRequestNotFoundException {

        solicitudService.crearSolicitudAdministrativo(solicitudDTO, tipoSolicitudId);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Solicitud creada exitosamente"),
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{solicitudId}")
    public ResponseEntity<SolicitudIdResponse> listarSolicitudesById(@PathVariable Integer solicitudId)
            throws RequestNotFoundException {

        SolicitudIdResponse solicitudIdResponse = solicitudService.listarSolicitudById(solicitudId);

        return new ResponseEntity<>(solicitudIdResponse, HttpStatus.OK);
    }

}
