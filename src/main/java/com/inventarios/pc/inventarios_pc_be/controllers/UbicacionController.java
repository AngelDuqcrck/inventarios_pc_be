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

import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.implementations.UbicacionServiceImplementation;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UbicacionDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

/**
 * Controlador para manejar operaciones CRUD relacionadas con Ubicaciones.
 */
@RestController
@RequestMapping("/ubicacion")
public class UbicacionController {
    
    @Autowired
    private UbicacionServiceImplementation ubicacionServiceImplementation;

     /**
     * Crea una nueva ubicación.
     *
     * @param ubicacionDTO Datos de la ubicación a crear.
     * @return Respuesta HTTP con el estado de la operación.
     * @throws LocationNotFoundException Si el área asociada no es encontrada.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearUbicacion (@RequestBody UbicacionDTO ubicacionDTO) throws LocationNotFoundException{
        ubicacionServiceImplementation.crearUbicacion(ubicacionDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Ubicación creada exitosamente"),
                HttpStatus.OK);
    }

    /**
     * Lista todas las ubicaciones.
     *
     * @return Lista de ubicaciones en formato DTO.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UbicacionDTO>> listarUbicaciones(){
        return ResponseEntity.ok(
            ubicacionServiceImplementation.listarUbicaciones().stream().map(ubicacion ->{
                UbicacionDTO ubicacionDTO = new UbicacionDTO();
                BeanUtils.copyProperties(ubicacion, ubicacionDTO);
                ubicacionDTO.setAreaId(ubicacion.getAreaId().getId());
                return ubicacionDTO;
            }).collect(Collectors.toList())
        );
    }

    /**
     * Actualiza una ubicación existente.
     *
     * @param ubicacionId ID de la ubicación a actualizar.
     * @param ubicacionDTO Nuevos datos de la ubicación.
     * @return Respuesta HTTP con el estado de la operación.
     * @throws LocationNotFoundException Si la ubicación o el área asociada no son encontradas.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualizar/{ubicacionId}")
    public ResponseEntity<HttpResponse> actualizarUbicacion(@PathVariable Integer ubicacionId, @RequestBody UbicacionDTO ubicacionDTO) throws LocationNotFoundException{
        ubicacionServiceImplementation.actualizarUbicacion(ubicacionId, ubicacionDTO);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Ubicación actualizada exitosamente"),
                HttpStatus.OK);
    }

     /**
     * Elimina una ubicación existente.
     *
     * @param ubicacionId ID de la ubicación a eliminar.
     * @return Respuesta HTTP con el estado de la operación.
     * @throws LocationNotFoundException   Si la ubicación no es encontrada.
     * @throws DeleteNotAllowedException   Si la eliminación no está permitida.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping ("/eliminar/{ubicacionId}")
    public ResponseEntity<HttpResponse> eliminarUbicacion(@PathVariable Integer ubicacionId) throws LocationNotFoundException, DeleteNotAllowedException{
        ubicacionServiceImplementation.eliminarUbicacion(ubicacionId);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Ubicación eliminada exitosamente"),
                HttpStatus.OK);
    }
}
