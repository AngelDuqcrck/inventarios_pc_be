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


import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITiposService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.EstadosDispositivoDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoComponenteDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoDocDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoSoftwDTO;

@RestController
@RequestMapping("/tipos")
public class TiposController {

    @Autowired
    private ITiposService tiposService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/tc")
    public ResponseEntity<List<TipoComponenteDTO>> getTipoComponente(){
        return ResponseEntity.ok(
            tiposService.listarTipoComponente().stream().map(TC ->{
                    TipoComponenteDTO tcDTO = new TipoComponenteDTO();
                    BeanUtils.copyProperties(TC, tcDTO);
                    return tcDTO;
                }).collect(Collectors.toList()));
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/td")
    public ResponseEntity<List<TipoDocDTO>> getTipoDocumentos(){
        return ResponseEntity.ok(
                tiposService.listarTipoDocumentos().stream().map(TD ->{
                    TipoDocDTO tdDTO = new TipoDocDTO();
                    BeanUtils.copyProperties(TD, tdDTO);
                    return tdDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/ts")
    public ResponseEntity<List<TipoSoftwDTO>> getTipoSoftware(){
        return ResponseEntity.ok(
            tiposService.listarTipoSoftware().stream().map(TS ->{
                    TipoSoftwDTO tsDTO = new TipoSoftwDTO();
                    BeanUtils.copyProperties(TS, tsDTO);
                    return tsDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/ed")
    public ResponseEntity<List<EstadosDispositivoDTO>> getEstadosDisp(){
        return ResponseEntity.ok(
            tiposService.listarEstadosDisp().stream().map(ED ->{
                    EstadosDispositivoDTO edDTO = new EstadosDispositivoDTO();
                    BeanUtils.copyProperties(ED, edDTO);
                    return edDTO;
                }).collect(Collectors.toList()));
    }

    
}
