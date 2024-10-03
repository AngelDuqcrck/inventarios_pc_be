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


import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoSoftwareService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoSoftwDTO;

@RestController
@RequestMapping("/ts")
public class TipoSoftController {

    @Autowired
    private ITipoSoftwareService tipoSoftwareServiceImplementation;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TipoSoftwDTO>> getTipoSoftware(){
        return ResponseEntity.ok(
            tipoSoftwareServiceImplementation.listarTipoSoftware().stream().map(TS ->{
                    TipoSoftwDTO tsDTO = new TipoSoftwDTO();
                    BeanUtils.copyProperties(TS, tsDTO);
                    return tsDTO;
                }).collect(Collectors.toList()));
    }
}
