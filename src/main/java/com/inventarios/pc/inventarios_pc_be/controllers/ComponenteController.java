package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.ChangeNotAllowedException;
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

import com.inventarios.pc.inventarios_pc_be.exceptions.ComponentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IComponenteService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.ComponenteDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComponenteResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

@RestController
@RequestMapping("/componente")
public class ComponenteController {

        @Autowired
        private IComponenteService componenteService;

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/crear")
        public ResponseEntity<HttpResponse> crearComponente(@RequestBody ComponenteDTO componenteDTO)
                        throws ComponentNotFoundException, SelectNotAllowedException {
                componenteService.crearComponente(componenteDTO);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Componente creado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping
        public ResponseEntity<List<ComponenteDTO>> listarCompoenetes() {
                return ResponseEntity.ok(
                                componenteService.listarComponentes().stream().map(componente -> {
                                        ComponenteDTO componenteDTO = new ComponenteDTO();
                                        BeanUtils.copyProperties(componente, componenteDTO);
                                        componenteDTO.setTipoComponente(componente.getTipoComponente());
                                        return componenteDTO;
                                }).collect(Collectors.toList()));
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @PutMapping("/actualizar/{componenteId}")
        public ResponseEntity<HttpResponse> actualizarComponente(@PathVariable Integer componenteId,
                        @RequestBody ComponenteDTO componenteDTO)
                        throws ComponentNotFoundException, SelectNotAllowedException, UpdateNotAllowedException {
                componenteService.actualizarComponente(componenteId, componenteDTO);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Componente actualizado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @DeleteMapping("/eliminar/{componenteId}")
        public ResponseEntity<HttpResponse> eliminarComponente(@PathVariable Integer componenteId)
                        throws DeleteNotAllowedException, ComponentNotFoundException {
                componenteService.eliminarComponente(componenteId);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Componente eliminado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/activar/{componenteId}")
        public ResponseEntity<HttpResponse> activarComponente(@PathVariable Integer componenteId)
                        throws ActivateNotAllowedException, ComponentNotFoundException {
                componenteService.activarComponente(componenteId);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Componente activado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping("/{id}")
        public ResponseEntity<ComponenteResponse> listarComponenteById(@PathVariable Integer id)
                        throws ComponentNotFoundException {
                ComponenteResponse componenteResponse = componenteService.listarComponenteById(id);
                return new ResponseEntity<>(componenteResponse, HttpStatus.OK);
        }

}
