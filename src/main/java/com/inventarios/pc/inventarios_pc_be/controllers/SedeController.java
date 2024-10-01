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
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ISedeService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SedeDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
/**
 * Controlador para manejar operaciones CRUD relacionadas con Sedes.
 */
@RestController
@RequestMapping("/sede")
public class SedeController {


    @Autowired
    private ISedeService sedeServiceImplementation;

     /**
     * Crea una nueva sede.
     *
     * @param sedeDTO Datos de la sede a crear.
     * @return Respuesta HTTP con el estado de la operación.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearSede(@RequestBody SedeDTO sedeDTO) {
        sedeServiceImplementation.crearSede(sedeDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Sede creada exitosamente"),
                HttpStatus.OK);
    }

    /**
     * Lista todas las sedes.
     *
     * @return Lista de sedes en formato DTO.
     */
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

    /**
     * Actualiza una sede existente.
     *
     * @param sedeId  ID de la sede a actualizar.
     * @param sedeDTO Nuevos datos de la sede.
     * @return Respuesta HTTP con el estado de la operación.
     * @throws LocationNotFoundException Si la sede no es encontrada.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualizar/{sedeId}")
    public ResponseEntity<HttpResponse> actualizarSede(@PathVariable Integer sedeId, @RequestBody SedeDTO sedeDTO)
            throws LocationNotFoundException {
        sedeServiceImplementation.actualizarSede(sedeId, sedeDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Sede actualizada exitosamente"),
                HttpStatus.OK);

    }

    /**
     * Elimina una sede existente.
     *
     * @param sedeId ID de la sede a eliminar.
     * @return Respuesta HTTP con el estado de la operación.
     * @throws LocationNotFoundException   Si la sede no es encontrada.
     * @throws DeleteNotAllowedException   Si la eliminación no está permitida.
     */
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