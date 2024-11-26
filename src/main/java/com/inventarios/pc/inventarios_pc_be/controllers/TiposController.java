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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @GetMapping("/tc")
    public ResponseEntity<List<TipoComponenteDTO>> getTipoComponente(){
        return ResponseEntity.ok(
            tiposService.listarTipoComponente().stream().map(TC ->{
                    TipoComponenteDTO tcDTO = new TipoComponenteDTO();
                    BeanUtils.copyProperties(TC, tcDTO);
                    return tcDTO;
                }).collect(Collectors.toList()));
    }
    
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @GetMapping("/td")
    public ResponseEntity<List<TipoDocDTO>> getTipoDocumentos(){
        return ResponseEntity.ok(
                tiposService.listarTipoDocumentos().stream().map(TD ->{
                    TipoDocDTO tdDTO = new TipoDocDTO();
                    BeanUtils.copyProperties(TD, tdDTO);
                    return tdDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @GetMapping("/ts")
    public ResponseEntity<List<TipoSoftwDTO>> getTipoSoftware(){
        return ResponseEntity.ok(
            tiposService.listarTipoSoftware().stream().map(TS ->{
                    TipoSoftwDTO tsDTO = new TipoSoftwDTO();
                    BeanUtils.copyProperties(TS, tsDTO);
                    return tsDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @GetMapping("/ed")
    public ResponseEntity<List<EstadosDispositivoDTO>> getEstadosDisp(){
        return ResponseEntity.ok(
            tiposService.listarEstadosDisp().stream().map(ED ->{
                    EstadosDispositivoDTO edDTO = new EstadosDispositivoDTO();
                    BeanUtils.copyProperties(ED, edDTO);
                    return edDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @GetMapping("/almram")
    public ResponseEntity<List<TipoComponenteDTO>> getTipoAlmacenamientoRam(){
        return ResponseEntity.ok(
            tiposService.listarTipoAlmRam().stream().map(TAR ->{
                TipoComponenteDTO tarDTO = new TipoComponenteDTO();
                    BeanUtils.copyProperties(TAR, tarDTO);
                    return tarDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tps")
    public ResponseEntity<List<TipoComponenteDTO>> getTipoSolicitudes(){
        return ResponseEntity.ok(
            tiposService.listarTipoSolicitudes().stream().map(TS ->{
                TipoComponenteDTO tsDTO = new TipoComponenteDTO();
                    BeanUtils.copyProperties(TS, tsDTO);
                    return tsDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/es")
    public ResponseEntity<List<TipoComponenteDTO>> getEstadoSolicitudes(){
        return ResponseEntity.ok(
            tiposService.listarEstadosSolicitud().stream().map(ES ->{
                TipoComponenteDTO esDTO = new TipoComponenteDTO();
                    BeanUtils.copyProperties(ES, esDTO);
                    return esDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("isAuthenticated()") //Revisar para confirmar el role de ADMIN y Tecnico Sistemas
    @GetMapping("/et")
    public ResponseEntity<List<TipoComponenteDTO>> getEstadoTickets(){
        return ResponseEntity.ok(
            tiposService.listarEstadosTickets().stream().map(ET ->{
                TipoComponenteDTO etDTO = new TipoComponenteDTO();
                    BeanUtils.copyProperties(ET, etDTO);
                    return etDTO;
                }).collect(Collectors.toList()));
    }

    
}
