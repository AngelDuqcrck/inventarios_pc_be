package com.inventarios.pc.inventarios_pc_be.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IHistorialComputadorService;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;

@RestController
@RequestMapping("/historial")
public class HistorialComputadorController {
    
    @Autowired
    private IHistorialComputadorService historialComputadorService;

    @PostMapping("/vincular-dispositivo")
    public ResponseEntity<HttpResponse> vincularDispositivo(@RequestParam Integer computadorId, @RequestParam Integer dispositivoId) throws ComputerNotFoundException, SelectNotAllowedException, DeviceNotFoundException {
        historialComputadorService.vincularDispositivo(computadorId, dispositivoId);

        return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Dispositivo vinculado exitosamente"),
                                HttpStatus.OK);
    }
}
