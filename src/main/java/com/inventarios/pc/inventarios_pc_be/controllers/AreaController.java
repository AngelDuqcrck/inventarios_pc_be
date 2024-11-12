package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.services.interfaces.IAreaService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.AreaDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.AreaResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

/**
 * Controlador para manejar operaciones CRUD relacionadas con Áreas.
 */
@RestController
@RequestMapping("/area")
public class AreaController {

        @Autowired
        IAreaService areaServiceImplementation;

        /**
         * Crea una nueva área.
         *
         * @param areaDTO Datos del área a crear.
         * @return Respuesta HTTP con el estado de la operación.
         * @throws LocationNotFoundException Si la sede asociada al área no es
         *                                   encontrada.
         */
        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/crear")
        public ResponseEntity<HttpResponse> crearArea(@RequestBody AreaDTO areaDTO)
                        throws SelectNotAllowedException, LocationNotFoundException, RolNotFoundException {
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
        public ResponseEntity<List<AreaResponse>> listarAreas() throws LocationNotFoundException {
                return ResponseEntity.ok(
                                areaServiceImplementation.listarAreas().stream().map(area -> {
                                        AreaResponse areaR = new AreaResponse();
                                        BeanUtils.copyProperties(area, areaR);
                                        areaR.setSede(area.getSede().getNombre());
                                        areaR.setRol(area.getRol().getNombre());
                                        return areaR;
                                }).collect(Collectors.toList()));
        }

        /**
         * Actualiza un área existente.
         *
         * @param areaId  ID del área a actualizar.
         * @param areaDTO Nuevos datos del área.
         * @return Respuesta HTTP con el estado de la operación.
         * @throws LocationNotFoundException Si el área o la sede asociada no son
         *                                   encontradas.
         */
        @PreAuthorize("hasAuthority('ADMIN')")
        @PutMapping("/actualizar/{areaId}")
        public ResponseEntity<HttpResponse> actualizarArea(@PathVariable Integer areaId, @RequestBody AreaDTO areaDTO)
                        throws LocationNotFoundException, UpdateNotAllowedException, SelectNotAllowedException,
                        RolNotFoundException {
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
         * @throws LocationNotFoundException Si el área no es encontrada.
         * @throws DeleteNotAllowedException Si la eliminación no está permitida.
         */
        @PreAuthorize("hasAuthority('ADMIN')")
        @DeleteMapping("/eliminar/{areaId}")
        public ResponseEntity<HttpResponse> eliminarArea(@PathVariable Integer areaId)
                        throws LocationNotFoundException, DeleteNotAllowedException, StateNotFoundException {
                areaServiceImplementation.eliminarArea(areaId);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Área eliminada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/activar/{areaId}")
        public ResponseEntity<HttpResponse> activarArea(@PathVariable Integer areaId)
                        throws LocationNotFoundException, ActivateNotAllowedException {
                areaServiceImplementation.activarArea(areaId);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Área activada exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping("/{id}")
        public ResponseEntity<AreaResponse> listarAreaById(@PathVariable Integer id) throws LocationNotFoundException {
                AreaResponse areaResponse = areaServiceImplementation.listarAreaById(id);
                return new ResponseEntity<>(areaResponse, HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping("/sede-rol")
        public ResponseEntity<List<AreaResponse>> listarAreasPorSedeYRol(@RequestParam Integer rol, @RequestParam Integer sedeId)
                        throws LocationNotFoundException, SelectNotAllowedException, RolNotFoundException {
                List<AreaResponse> areasResponses = areaServiceImplementation.listarAreasPorRolySede(rol, sedeId);
                return new ResponseEntity<>(areasResponses, HttpStatus.OK);
        }

        @PreAuthorize("isAuthenticated()") // ACA VA TAMBIÉN ROL ASISTENCIAL, ADMINISTRATIVO, ETC
        @GetMapping("/sede")
        public ResponseEntity<List<AreaResponse>> listarAreaBySede(@RequestParam Integer sedeId)
                        throws LocationNotFoundException, SelectNotAllowedException {
                return ResponseEntity.ok(
                                areaServiceImplementation.listarAreasPorSede(sedeId).stream().map(area -> {
                                        AreaResponse areaR = new AreaResponse();
                                        BeanUtils.copyProperties(area, areaR);
                                        areaR.setSede(area.getSede().getNombre());
                                        return areaR;
                                }).collect(Collectors.toList()));
        }
}
