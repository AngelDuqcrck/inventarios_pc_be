package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.services.interfaces.IComputadorService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.ComputadorDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.UbicarPcRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComputadorIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComputadoresResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/computador")
public class ComputadorController {

        @Autowired
        private IComputadorService computadorService;

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/crear")
        public ResponseEntity<HttpResponse> crearComputador(@RequestBody ComputadorDTO computadorDTO)
                        throws TypePcNotFoundException, SelectNotAllowedException, UserNotFoundException,
                        LocationNotFoundException, ComponentNotFoundException, MiscellaneousNotFoundException,
                        StateNotFoundException, MarcaNotFoundException, TypeDeviceNotFoundException, OwnerNotFoundException {

                computadorService.crearComputador(computadorDTO);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Equipo PC creado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/ubicar")
        public ResponseEntity<HttpResponse> ubicarComputador(@RequestBody UbicarPcRequest ubicarPcRequest)
                        throws ComputerNotFoundException, SelectNotAllowedException,
                        UserNotFoundException, LocationNotFoundException, StateNotFoundException {
                computadorService.ubicarPc(ubicarPcRequest);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Equipo PC ubicado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/{computadorId}")
        public ResponseEntity<ComputadorIdResponse> listarComputadorById(@PathVariable Integer computadorId)
                        throws ComputerNotFoundException {
                ComputadorIdResponse computadorIdResponse = computadorService.listarComputadorById(computadorId);

                return new ResponseEntity<>(computadorIdResponse, HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping("/estado")
        public ResponseEntity<List<ComputadoresResponse>> listarComputadoresByEstadoId() {
                List<ComputadoresResponse> computadoresResponses = computadorService.listarComputadoresByEstadoId(1);

                return new ResponseEntity<>(computadoresResponses, HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping
        public ResponseEntity<List<ComputadoresResponse>> listarComputadores() {
                List<ComputadoresResponse> computadoresResponses = computadorService.listarComputadores();

                return new ResponseEntity<>(computadoresResponses, HttpStatus.OK);
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/ubicacionPC")
        public ResponseEntity<List<ComputadoresResponse>> listarComputadoresByUbicacion(
                        @RequestParam Integer ubicacionId) throws LocationNotFoundException {
                List<ComputadoresResponse> computadoresResponses = computadorService
                                .listarComputadoresByUbicacion(ubicacionId);

                return new ResponseEntity<>(computadoresResponses, HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping("/usuario/{usuarioId}")
        public ResponseEntity<List<ComputadoresResponse>> listarComputadoresByUsuario(@PathVariable Integer usuarioId)
                        throws UserNotFoundException {
                List<ComputadoresResponse> computadoresResponses = computadorService
                                .listarComputadoresByUsuario(usuarioId);

                return new ResponseEntity<>(computadoresResponses, HttpStatus.OK);
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/usuarioE/{usuarioId}")
        public ResponseEntity<List<ComputadoresResponse>> listarComputadoresByEmail(@PathVariable String usuarioId)
                        throws UserNotFoundException {
                List<ComputadoresResponse> computadoresResponses = computadorService
                                .listarComputadoresByEmail(usuarioId);

                return new ResponseEntity<>(computadoresResponses, HttpStatus.OK);
        }

        @PutMapping("/actualizar/{computadorId}")
        public ResponseEntity<HttpResponse> actualizarComputador(@PathVariable Integer computadorId,
                        @RequestBody ComputadorDTO computadorDTO)
                        throws TypePcNotFoundException, SelectNotAllowedException, UserNotFoundException,
                        LocationNotFoundException, ComponentNotFoundException, MiscellaneousNotFoundException,
                        StateNotFoundException, MarcaNotFoundException, UpdateNotAllowedException,
                        ComputerNotFoundException,
                        ChangeNotAllowedException, TypeDeviceNotFoundException, OwnerNotFoundException {

                computadorService.actualizarComputador(computadorId, computadorDTO);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Computador actualizado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @DeleteMapping("/baja/{computadorId}")
        public ResponseEntity<HttpResponse> darBajaComputador(@PathVariable Integer computadorId)
                        throws ComputerNotFoundException, DeleteNotAllowedException {
                computadorService.darBajaComputador(computadorId);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Equipo PC dado de baja exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/cambiar-estado")
        public ResponseEntity<HttpResponse> cambiarEstadoDispositivo(@RequestParam Integer computadorId,
                        @RequestParam Integer nuevoEstadoDispositivoId)
                        throws ComputerNotFoundException, StateNotFoundException, ChangeNotAllowedException {
                computadorService.cambiarEstadoPc(computadorId, nuevoEstadoDispositivoId);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Estado cambiado exitosamente"),
                                HttpStatus.OK);
        }

}
