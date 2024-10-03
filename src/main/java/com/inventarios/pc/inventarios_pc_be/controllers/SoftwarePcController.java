package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SoftwareResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.inventarios.pc.inventarios_pc_be.exceptions.TypeSoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ISoftwarePcService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SoftwarePcDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

@RestController
@RequestMapping("/software")
public class SoftwarePcController {
    
    @Autowired
    private ISoftwarePcService softwarePcService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearSoftware (@RequestBody SoftwarePcDTO softareDTO)throws TypeSoftwareNotFoundException{
        softwarePcService.crearSoftware(softareDTO);
        
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Software creado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SoftwarePcDTO>> listarSoftware(){
        return ResponseEntity.ok(
            softwarePcService.listarSoftwares().stream().map(software ->{
                SoftwarePcDTO softwarePcDTO = new SoftwarePcDTO();
                BeanUtils.copyProperties(software, softwarePcDTO);
                softwarePcDTO.setTipoSoftware(software.getTipoSoftware());
                return softwarePcDTO;
            }).collect(Collectors.toList()));
        
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualizar/{softwareId}")
    public ResponseEntity<HttpResponse> actualizarSoftware(@PathVariable Integer softwareId, @RequestBody SoftwarePcDTO softwarePcDTO)throws SoftwareNotFoundException, TypeSoftwareNotFoundException {
        softwarePcService.actualizarSoftware(softwareId, softwarePcDTO);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Software actualizado exitosamente"),
                HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/eliminar/{softwareId}")
    public ResponseEntity<HttpResponse> eliminarSoftware(@PathVariable Integer softwareId)throws DeleteNotAllowedException, SoftwareNotFoundException {
        softwarePcService.eliminarSoftware(softwareId);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Software eliminado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SoftwareResponse> listarSoftwareById(@PathVariable Integer id)throws SoftwareNotFoundException{
        SoftwareResponse softwareResponse = softwarePcService.listarSoftwareById(id);
        return  new ResponseEntity<>(softwareResponse, HttpStatus.OK);
    }


}
