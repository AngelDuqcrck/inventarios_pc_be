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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MarcaNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeDeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IDispositivoService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.DispositivoDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.DispositivoRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.DispositivoResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

@RestController
@RequestMapping("/dispositivo")
public class DispositivoController {

    @Autowired
    private IDispositivoService dispositivoService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<HttpResponse> crearDispositivo(@RequestBody DispositivoRequest dispositivoRequest)
            throws TypeDeviceNotFoundException, MarcaNotFoundException, StateNotFoundException {
        dispositivoService.crearDispositivo(dispositivoRequest);
         return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Dispositivo creado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<DispositivoDTO>> listarDispositivo(){
        return ResponseEntity.ok(
            dispositivoService.listarDispositivos().stream().map(dispositivo ->{
                DispositivoDTO dispositivoDTO = new DispositivoDTO();
                BeanUtils.copyProperties(dispositivo, dispositivoDTO);
                return dispositivoDTO;
            }).collect(Collectors.toList())
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/actualizar/{dispositivoId}")
    public ResponseEntity<HttpResponse> actualizarDispositivo (@PathVariable Integer dispositivoId, @RequestBody DispositivoRequest dispositivoRequest) throws TypeDeviceNotFoundException, MarcaNotFoundException, StateNotFoundException, DeviceNotFoundException{
        dispositivoService.actualizarDispositivo(dispositivoId, dispositivoRequest);

        return new ResponseEntity<>(
            new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                    "Dispositivo actualizado exitosamente"),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{dispositivoId}")
    public ResponseEntity<DispositivoResponse> listarDispositivoById(@PathVariable Integer dispositivoId)throws DeviceNotFoundException {
        DispositivoResponse dispositivoResponse = dispositivoService.listarDispositivoById(dispositivoId);
        return new ResponseEntity<>(dispositivoResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/eliminar/{dispositivoId}")
    public ResponseEntity<HttpResponse> eliminarDispositivo(@PathVariable Integer dispositivoId)throws DeviceNotFoundException, DeleteNotAllowedException{
        dispositivoService.eliminarDispositivo(dispositivoId);
        return new ResponseEntity<>(
            new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                    "Dispositivo eliminado exitosamente"),
            HttpStatus.OK);
    }

}
