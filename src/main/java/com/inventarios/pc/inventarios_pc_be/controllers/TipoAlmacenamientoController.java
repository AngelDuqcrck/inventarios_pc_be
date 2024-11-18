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

import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoAlmacenamientoService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoAlmacenamientoDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

@RestController
@RequestMapping("/ta")
public class TipoAlmacenamientoController {

    @Autowired
    private ITipoAlmacenamientoService tipoAlmacenamientoService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearTipoAlmacenamiento(
            @RequestBody TipoAlmacenamientoDTO tipoAlmacenamientoDTO) throws DuplicateEntityException {
        tipoAlmacenamientoService.crearTipoAlmacenamiento(tipoAlmacenamientoDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Tipo de almacenamiento creado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TipoAlmacenamientoDTO>> listarTiposdeAlmacenamiento() {
        return ResponseEntity.ok(
                tipoAlmacenamientoService.listarTiposAlmacenamiento().stream().map(tipoAlmacenamiento -> {
                    TipoAlmacenamientoDTO tipoAlmacenamientoDTO = new TipoAlmacenamientoDTO();
                    BeanUtils.copyProperties(tipoAlmacenamiento, tipoAlmacenamientoDTO);
                    tipoAlmacenamientoDTO.setTipoComponente("Tipo de Disco Duro");
                    return tipoAlmacenamientoDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TipoAlmacenamientoDTO> listarTipoAlmacenamientoById(@PathVariable Integer id)
            throws TypeStorageNotFoundException {
        TipoAlmacenamientoDTO tipoAlmacenamientoDTO = tipoAlmacenamientoService.listarTipoAlmacenamientoById(id);

        return new ResponseEntity<>(tipoAlmacenamientoDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<HttpResponse> actualizarTipoAlmacenamiento(@PathVariable Integer id,
            @RequestBody TipoAlmacenamientoDTO tipoAlmacenamientoDTO) throws DuplicateEntityException,TypeStorageNotFoundException, UpdateNotAllowedException {
        tipoAlmacenamientoService.actualizarTipoAlmacenamiento(id, tipoAlmacenamientoDTO);

        return new ResponseEntity<>(
            new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                            "Tipo de almacenamiento actualizado exitosamente"),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/activar/{id}")
        public ResponseEntity<HttpResponse> activarTipoAlmacenamiento(@PathVariable Integer id)
                        throws ActivateNotAllowedException, TypeStorageNotFoundException {
                tipoAlmacenamientoService.activarTipoAlmacenamiento(id);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Tipo de almacenamiento activado exitosamente"),
                                HttpStatus.OK);
        }

      @PreAuthorize("hasAuthority('ADMIN')")
        @DeleteMapping("/eliminar/{id}")
        public ResponseEntity<HttpResponse> eliminarTipoAlmacenamiento(@PathVariable Integer id)
                        throws DeleteNotAllowedException, TypeStorageNotFoundException {
                tipoAlmacenamientoService.eliminarTipoAlmacenamiento(id);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Tipo de almacenamiento eliminado exitosamente"),
                                HttpStatus.OK);
        }   
}
