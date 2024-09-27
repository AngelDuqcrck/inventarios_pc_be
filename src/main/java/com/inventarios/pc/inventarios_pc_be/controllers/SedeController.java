package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.*;
import java.util.stream.Collectors;

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

import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.implementations.SedeServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SedeDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

@RestController
@RequestMapping("/sede")
public class SedeController {


    @Autowired
    private SedeServiceImplementation sedeServiceImplementation;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearSede(@RequestBody SedeDTO sedeDTO) {
        sedeServiceImplementation.crearSede(sedeDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Sede creada exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SedeDTO>> listarSedes() {
        return ResponseEntity.ok(
                sedeServiceImplementation.listarSedes().stream().map(sede -> {
                    SedeDTO sedeDTO = new SedeDTO();
                    BeanUtils.copyProperties(sede, sedeDTO);
                    return sedeDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualizar/{sedeId}")
    public ResponseEntity<HttpResponse> actualizarSede(@PathVariable Integer sedeId, @RequestBody SedeDTO sedeDTO)
            throws LocationNotFoundException {
        sedeServiceImplementation.actualizarSede(sedeId, sedeDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Sede actualizado exitosamente"),
                HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/eliminar/{sedeId}")
    public ResponseEntity<HttpResponse> eliminarSede(@PathVariable Integer sedeId) throws LocationNotFoundException, DeleteNotAllowedException {
        sedeServiceImplementation.eliminarSede(sedeId);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Sede eliminada exitosamente"),
                HttpStatus.OK);
    }

}