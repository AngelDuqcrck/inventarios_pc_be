package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
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
import com.inventarios.pc.inventarios_pc_be.exceptions.TypePcNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoPcService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoComputadorDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoDispositivoDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

@RestController
@RequestMapping("/tpc")
public class TipoPcController {
    
    @Autowired
    private ITipoPcService tipoPcService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearTipoPc(@RequestBody TipoComputadorDTO tipoPC){
        tipoPcService.crearTipoPC(tipoPC);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Tipo de pc creado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TipoComputadorDTO>> listarTiposPC(){
        return ResponseEntity.ok(
            tipoPcService.listarTiposPc().stream().map(tipoPc ->{
                TipoComputadorDTO tipoComputadorDTO = new TipoComputadorDTO();
                BeanUtils.copyProperties(tipoPc, tipoComputadorDTO);
                return tipoComputadorDTO;
            }).collect(Collectors.toList()));
        
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<HttpResponse> actualizarTipoPc(@PathVariable Integer id, @RequestBody TipoComputadorDTO tipoComputadorDTO) throws TypePcNotFoundException{
        tipoPcService.actualizarTipoPC(id, tipoComputadorDTO);

        return new ResponseEntity<>(
            new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                    "Tipo de pc actualizado exitosamente"),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<HttpResponse> eliminarTipoPc (@PathVariable Integer id) throws TypePcNotFoundException, DeleteNotAllowedException{
        tipoPcService.eliminarTipoPc(id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Tipo de pc eliminado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/activar/{id}")
    public ResponseEntity<HttpResponse> activarTipoPc (@PathVariable Integer id) throws TypePcNotFoundException, ActivateNotAllowedException {
        tipoPcService.activarTipoPc(id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Tipo de pc activado exitosamente"),
                HttpStatus.OK);
    }
}
