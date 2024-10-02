package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoDocumentoService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoDocDTO;

@RestController
@RequestMapping("/td")
public class TipoDocController {

    @Autowired
    private ITipoDocumentoService tipoDocumentoServiceImplementation;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TipoDocDTO>> getTipoDocumentos(){
        return ResponseEntity.ok(
                tipoDocumentoServiceImplementation.listarTipoDocumentos().stream().map(TD ->{
                    TipoDocDTO tdDTO = new TipoDocDTO();
                    BeanUtils.copyProperties(TD, tdDTO);
                    return tdDTO;
                }).collect(Collectors.toList()));
    }
}
