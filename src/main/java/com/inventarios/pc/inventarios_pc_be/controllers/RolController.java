package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.implementations.RolServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IRolService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.RolDTO;

@RestController
@RequestMapping("/rol")
public class RolController {
    
    @Autowired
    private IRolService rolServiceImplementation;

    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<HttpResponse> crearRol (@RequestBody RolDTO rolDTO){
        rolServiceImplementation.crearRol(rolDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Rol creado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RolDTO>> getRoles(){
        return ResponseEntity.ok(
            rolServiceImplementation.listarRoles().stream().map(rol ->{
                    RolDTO rolDTO = new RolDTO();
                    BeanUtils.copyProperties(rol, rolDTO);
                    return rolDTO;
                }).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<RolDTO> listarRolById(@PathVariable Integer id)throws RolNotFoundException{
        RolDTO rolDTO = new RolDTO();
        rolDTO = rolServiceImplementation.listarRolById(id);
        return new ResponseEntity<>(rolDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity<HttpResponse> actualizarRol (@RequestBody RolDTO rolDTO){
        rolServiceImplementation.crearRol(rolDTO);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Rol actualizado exitosamente"),
                HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<HttpResponse> eliminarRol(@PathVariable Integer id)throws RolNotFoundException, DeleteNotAllowedException{
        rolServiceImplementation.deshabilitarRol(id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Rol eliminado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/activar/{id}")
    public ResponseEntity<HttpResponse> activarRol(@PathVariable Integer id)throws RolNotFoundException, ActivateNotAllowedException {
        rolServiceImplementation.activarRol(id);
        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Rol activado exitosamente"),
                HttpStatus.OK);
    }
    
}


