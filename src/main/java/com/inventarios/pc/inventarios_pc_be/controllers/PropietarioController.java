package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;
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

import com.inventarios.pc.inventarios_pc_be.shared.DTOs.PropietarioDTO;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoAlmacenamientoDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.OwnerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeStorageNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IPropietarioService;

@RestController
@RequestMapping("/propietario")
public class PropietarioController {
    
    @Autowired
    private IPropietarioService propietarioService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearPropietario(@RequestBody PropietarioDTO propietarioDTO) {
        propietarioService.crearPropietario(propietarioDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Propietario creado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PropietarioDTO>> listarPropietario(){
        return ResponseEntity.ok(
            propietarioService.listarPropietarios().stream().map( propietario ->{
                PropietarioDTO propietarioDTO = new PropietarioDTO();
                BeanUtils.copyProperties(propietario, propietarioDTO);
                propietarioDTO.setTipoMisc("PropietarioPC");
                return propietarioDTO;
            }).collect(Collectors.toList())
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PropietarioDTO> listarPropietarioById(@PathVariable Integer id)
            throws OwnerNotFoundException {
        PropietarioDTO propietarioDTO = propietarioService.listarPropietarioById(id);

        return new ResponseEntity<>(propietarioDTO, HttpStatus.OK);
    }

     @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<HttpResponse> actualizarPropietario(@PathVariable Integer id,
            @RequestBody PropietarioDTO propietarioDTO) throws OwnerNotFoundException, UpdateNotAllowedException {
        propietarioService.actualizarPropietario(id, propietarioDTO);

        return new ResponseEntity<>(
            new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                            "Propietario actualizado exitosamente"),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/activar/{id}")
        public ResponseEntity<HttpResponse> activarPropietario(@PathVariable Integer id)
                        throws ActivateNotAllowedException, OwnerNotFoundException {
                propietarioService.activarPropietario(id);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Propietario activado exitosamente"),
                                HttpStatus.OK);
        }

      @PreAuthorize("hasAuthority('ADMIN')")
        @DeleteMapping("/eliminar/{id}")
        public ResponseEntity<HttpResponse> eliminarPropietario(@PathVariable Integer id)
                        throws DeleteNotAllowedException, OwnerNotFoundException {
                propietarioService.eliminarPropietario(id);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Propietario eliminado exitosamente"),
                                HttpStatus.OK);
        }   
}
