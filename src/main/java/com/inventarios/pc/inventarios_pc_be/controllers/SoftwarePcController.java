package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SoftwareResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.inventarios.pc.inventarios_pc_be.exceptions.TypeSoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ISoftwarePcService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SoftwarePcDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

/**
 * Controlador que maneja las operaciones relacionadas con la gestión de
 * software en el sistema.
 */
@RestController
@RequestMapping("/software")
public class SoftwarePcController {

        @Autowired
        private ISoftwarePcService softwarePcService;

        /**
         * Crea un nuevo software en el sistema.
         *
         * @param softareDTO Objeto DTO que contiene los detalles del software a crear.
         * @return Respuesta HTTP indicando el éxito de la operación.
         * @throws TypeSoftwareNotFoundException Si el tipo de software especificado no
         *                                       existe.
         */
        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @PostMapping("/crear")
        public ResponseEntity<HttpResponse> crearSoftware(@RequestBody SoftwarePcDTO softareDTO)
                        throws TypeSoftwareNotFoundException, SelectNotAllowedException {
                softwarePcService.crearSoftware(softareDTO);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Software creado exitosamente"),
                                HttpStatus.OK);
        }

        /**
         * Lista todos los software registrados en el sistema.
         *
         * @return Lista de DTOs de software.
         */
        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @GetMapping
        public ResponseEntity<List<SoftwareResponse>> listarSoftware() {
                return ResponseEntity.ok(
                                softwarePcService.listarSoftwares().stream().map(software -> {
                                        SoftwareResponse softwarePcR = new SoftwareResponse();
                                        BeanUtils.copyProperties(software, softwarePcR);
                                        softwarePcR.setTipoSoftware(software.getTipoSoftware().getNombre());
                                        return softwarePcR;
                                }).collect(Collectors.toList()));

        }

        /**
         * Actualiza un software existente en el sistema.
         *
         * @param softwareId    ID del software a actualizar.
         * @param softwarePcDTO DTO con los nuevos datos del software.
         * @return Respuesta HTTP indicando el éxito de la operación.
         * @throws SoftwareNotFoundException     Si el software con el ID especificado
         *                                       no existe.
         * @throws TypeSoftwareNotFoundException Si el tipo de software especificado no
         *                                       existe.
         */
        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @PutMapping("/actualizar/{softwareId}")
        public ResponseEntity<HttpResponse> actualizarSoftware(@PathVariable Integer softwareId,
                        @RequestBody SoftwarePcDTO softwarePcDTO)
                        throws SoftwareNotFoundException, TypeSoftwareNotFoundException, SelectNotAllowedException,
                        UpdateNotAllowedException {
                softwarePcService.actualizarSoftware(softwareId, softwarePcDTO);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Software actualizado exitosamente"),
                                HttpStatus.OK);

        }

        /**
         * Elimina un software del sistema.
         *
         * @param softwareId ID del software a eliminar.
         * @return Respuesta HTTP indicando el éxito de la operación.
         * @throws DeleteNotAllowedException Si la eliminación del software no está
         *                                   permitida.
         * @throws SoftwareNotFoundException Si el software con el ID especificado no
         *                                   existe.
         */
        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @DeleteMapping("/eliminar/{softwareId}")
        public ResponseEntity<HttpResponse> eliminarSoftware(@PathVariable Integer softwareId)
                        throws DeleteNotAllowedException, SoftwareNotFoundException {
                softwarePcService.eliminarSoftware(softwareId);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Software eliminado exitosamente"),
                                HttpStatus.OK);
        }

        /**
         * Obtiene los detalles de un software por su ID.
         *
         * @param id ID del software a buscar.
         * @return Objeto SoftwareResponse con los detalles del software.
         * @throws SoftwareNotFoundException Si el software con el ID especificado no
         *                                   existe.
         */
        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @GetMapping("/{id}")
        public ResponseEntity<SoftwareResponse> listarSoftwareById(@PathVariable Integer id)
                        throws SoftwareNotFoundException {
                SoftwareResponse softwareResponse = softwarePcService.listarSoftwareById(id);
                return new ResponseEntity<>(softwareResponse, HttpStatus.OK);
        }

        @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
        @PostMapping("/activar/{softwareId}")
        public ResponseEntity<HttpResponse> activarSoftware(@PathVariable Integer softwareId)
                        throws ActivateNotAllowedException, SoftwareNotFoundException {
                softwarePcService.activarSoftware(softwareId);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Software activado exitosamente"),
                                HttpStatus.OK);
        }
}
