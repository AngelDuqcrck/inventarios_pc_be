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

import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoComponenteService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoComponenteDTO;

@RestController
@RequestMapping("/tc")
public class TipoCompController {

    @Autowired
    private ITipoComponenteService tipoComponenteServiceImplementation;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TipoComponenteDTO>> getTipoComponente(){
        return ResponseEntity.ok(
            tipoComponenteServiceImplementation.listarTipoComponente().stream().map(TC ->{
                    TipoComponenteDTO tcDTO = new TipoComponenteDTO();
                    BeanUtils.copyProperties(TC, tcDTO);
                    return tcDTO;
                }).collect(Collectors.toList()));
    }
}
