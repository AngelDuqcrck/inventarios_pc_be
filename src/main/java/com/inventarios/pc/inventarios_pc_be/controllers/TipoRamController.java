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
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoRamService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoAlmacenamientoDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoRamDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

@RestController
@RequestMapping("/tr")
public class TipoRamController {

    @Autowired
    private ITipoRamService tipoRamService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearTipoAlmacenamiento(@RequestBody TipoRamDTO tipoRamDTO) throws DuplicateEntityException {
        tipoRamService.crearTipoRam(tipoRamDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Tipo de RAM creada exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TipoRamDTO>> listarTiposRam() {
        return ResponseEntity.ok(
                tipoRamService.listarTiposRam().stream().map(tipoRam -> {
                    TipoRamDTO tipoRamDTO = new TipoRamDTO();
                    BeanUtils.copyProperties(tipoRam, tipoRamDTO);
                    tipoRamDTO.setTipoComponente("Tipo de RAM");
                    return tipoRamDTO;
                }).collect(Collectors.toList()));
    }

        @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TipoRamDTO> listarTipoRamById(@PathVariable Integer id)
            throws TypeRamNotFoundException {
        TipoRamDTO tipoRamDTO = tipoRamService.listarTipoRamById(id);

        return new ResponseEntity<>(tipoRamDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<HttpResponse> actualizarTipoRam(@PathVariable Integer id,
            @RequestBody TipoRamDTO tipoRamDTO) throws TypeRamNotFoundException, UpdateNotAllowedException, DuplicateEntityException {
        tipoRamService.actualizarTipoRam(id, tipoRamDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Tipo de RAM actualizada exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/activar/{id}")
    public ResponseEntity<HttpResponse> activarTipoRam(@PathVariable Integer id)
            throws ActivateNotAllowedException, TypeRamNotFoundException {
        tipoRamService.activarTipoRam(id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Tipo de RAM activada exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<HttpResponse> eliminarTipoRam(@PathVariable Integer id)
            throws DeleteNotAllowedException, TypeRamNotFoundException {
        tipoRamService.eliminarTipoRam(id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Tipo de RAM eliminada exitosamente"),
                HttpStatus.OK);
    }
}
