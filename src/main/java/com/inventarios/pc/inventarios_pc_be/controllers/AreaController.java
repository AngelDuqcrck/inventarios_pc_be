package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;
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
import com.inventarios.pc.inventarios_pc_be.services.implementations.AreaServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.AreaDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SedeDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
/**
 * Controlador para manejar operaciones CRUD relacionadas con Áreas.
 */
@RestController
@RequestMapping("/area")
public class AreaController {
    
    @Autowired
    AreaServiceImplementation areaServiceImplementation;

    /**
     * Crea una nueva área.
     *
     * @param areaDTO Datos del área a crear.
     * @return Respuesta HTTP con el estado de la operación.
     * @throws LocationNotFoundException Si la sede asociada al área no es encontrada.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearArea(@RequestBody AreaDTO areaDTO) throws LocationNotFoundException {
        areaServiceImplementation.crearArea(areaDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Área creada exitosamente"),
                HttpStatus.OK);
    }

    /**
     * Lista todas las áreas.
     *
     * @return Lista de áreas en formato DTO.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AreaDTO>> listarAreas() {
        return ResponseEntity.ok(
                areaServiceImplementation.listarAreas().stream().map(area -> {
                    AreaDTO areaDTO = new AreaDTO();
                    BeanUtils.copyProperties(area, areaDTO);
                    areaDTO.setSede(area.getSede());
                    return areaDTO;
                }).collect(Collectors.toList()));
    }

    /**
     * Actualiza un área existente.
     *
     * @param areaId  ID del área a actualizar.
     * @param areaDTO Nuevos datos del área.
     * @return Respuesta HTTP con el estado de la operación.
     * @throws LocationNotFoundException Si el área o la sede asociada no son encontradas.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualizar/{areaId}")
    public ResponseEntity<HttpResponse> actualizarArea(@PathVariable Integer areaId, @RequestBody AreaDTO areaDTO)
            throws LocationNotFoundException {
        areaServiceImplementation.actualizarArea(areaId, areaDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Área actualizada exitosamente"),
                HttpStatus.OK);

    }

    /**
     * Elimina un área existente.
     *
     * @param areaId ID del área a eliminar.
     * @return Respuesta HTTP con el estado de la operación.
     * @throws LocationNotFoundException   Si el área no es encontrada.
     * @throws DeleteNotAllowedException   Si la eliminación no está permitida.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/eliminar/{areaId}")
    public ResponseEntity<HttpResponse> eliminarSede(@PathVariable Integer areaId) throws LocationNotFoundException, DeleteNotAllowedException {
        areaServiceImplementation.eliminarArea(areaId);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Área eliminada exitosamente"),
                HttpStatus.OK);
    }
}
