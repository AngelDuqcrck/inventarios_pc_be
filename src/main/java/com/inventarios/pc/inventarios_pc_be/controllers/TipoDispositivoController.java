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
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeDeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoDispositivoService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoDispositivoDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

@RestController
@RequestMapping("/td")
public class TipoDispositivoController {
    
    @Autowired
    private ITipoDispositivoService tipoDispositivoService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearTipoDispositivo(@RequestBody TipoDispositivoDTO tipoDispositivoDTO){
        tipoDispositivoService.creaDispositivoDTO(tipoDispositivoDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Tipo de dispositivo creado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TipoDispositivoDTO>> listarTiposDispositivos(){
        return ResponseEntity.ok(
            tipoDispositivoService.listarTipos().stream().map(tipoDispositivo ->{
                TipoDispositivoDTO tipoDispositivoDTO = new TipoDispositivoDTO();
                BeanUtils.copyProperties(tipoDispositivo, tipoDispositivoDTO);
                return tipoDispositivoDTO;
            }).collect(Collectors.toList()));
        
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<HttpResponse> actualizarTipoDispositivo(@PathVariable Integer id, @RequestBody TipoDispositivoDTO tipoDispositivoDTO) throws TypeDeviceNotFoundException{
        tipoDispositivoService.actualizarTipoDispositivo(id, tipoDispositivoDTO);

        return new ResponseEntity<>(
            new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                    "Tipo dispositivo actualizado exitosamente"),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<HttpResponse> eliminarTipoDispositivo (@PathVariable Integer id) throws TypeDeviceNotFoundException, DeleteNotAllowedException{
        tipoDispositivoService.eliminarTipoDispositivo(id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Tipo dispositivo eliminada exitosamente"),
                HttpStatus.OK);
    }
}
