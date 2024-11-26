package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.services.interfaces.IMarcaService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.MarcaDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

@RestController
@RequestMapping("/marca")
public class MarcaController {

        @Autowired
        IMarcaService marcaService;

        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @PostMapping("/crear")
        public ResponseEntity<HttpResponse> crearMarca(@RequestBody MarcaDTO marcaDTO) throws DuplicateEntityException {
                marcaService.crearMarca(marcaDTO);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Marca creada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @GetMapping
        public ResponseEntity<List<MarcaDTO>> listarMarcas() {
                return ResponseEntity.ok(
                                marcaService.listarMarcas().stream().map(marca -> {
                                        MarcaDTO marcaDTO = new MarcaDTO();
                                        marcaDTO.setTipoMisc("Marca");
                                        BeanUtils.copyProperties(marca, marcaDTO);
                                        return marcaDTO;
                                }).collect(Collectors.toList()));

        }

        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @GetMapping("/{id}")
        public ResponseEntity<MarcaDTO> listarMarcaById(@PathVariable Integer id) throws MarcaNotFoundException {
                MarcaDTO marcaDTO = marcaService.listarMarcaById(id);
                marcaDTO.setTipoMisc("Marca");
                return new ResponseEntity<>(marcaDTO, HttpStatus.OK);
        }

        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @PutMapping("/actualizar/{marcaId}")
        public ResponseEntity<HttpResponse> actualizarMarca(@PathVariable Integer marcaId,
                        @RequestBody MarcaDTO marcaDTO) throws MarcaNotFoundException, UpdateNotAllowedException, DuplicateFormatFlagsException {
                marcaService.actualizarMarca(marcaId, marcaDTO);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Marca actualizada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @DeleteMapping("/eliminar/{marcaId}")
        public ResponseEntity<HttpResponse> eliminarMarca(@PathVariable Integer marcaId)
                        throws DeleteNotAllowedException, MarcaNotFoundException {
                marcaService.eliminarMarca(marcaId);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Marca eliminada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @PostMapping("/activar/{marcaId}")
        public ResponseEntity<HttpResponse> activarMarca(@PathVariable Integer marcaId)
                        throws ActivateNotAllowedException, MarcaNotFoundException {
                marcaService.activarMarca(marcaId);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Marca activada exitosamente"),
                                HttpStatus.OK);
        }
}
