package com.inventarios.pc.inventarios_pc_be.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IHistorialComputadorService;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComputadoresResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.DispositivosXPcResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HistorialResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HistorialUbicacionesXPcResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HojaVidaPcResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HttpResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SoftwareXPcResponse;

@RestController
@RequestMapping("/historial")
public class HistorialComputadorController {

    @Autowired
    private IHistorialComputadorService historialComputadorService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @PostMapping("/vincular-dispositivo")
    public ResponseEntity<HttpResponse> vincularDispositivo(@RequestParam Integer computadorId,
            @RequestParam Integer dispositivoId)
            throws ComputerNotFoundException, SelectNotAllowedException, DeviceNotFoundException {
        historialComputadorService.vincularDispositivo(computadorId, dispositivoId);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Dispositivo vinculado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @PostMapping("/vincular-software")
    public ResponseEntity<HttpResponse> vincularSoftware(@RequestParam Integer computadorId,
            @RequestParam Integer softwareId)
            throws ComputerNotFoundException, SelectNotAllowedException, SoftwareNotFoundException {
        historialComputadorService.vincularSoftware(computadorId, softwareId);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Software vinculado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @PostMapping("/desvincular-dispositivo")
    public ResponseEntity<HttpResponse> desvincularDispositivo(@RequestParam Integer computadorId,
            @RequestParam Integer dispositivoId, @RequestParam String justificacion)
            throws ComputerNotFoundException, SelectNotAllowedException, DeviceNotFoundException {
        historialComputadorService.desvincularDispositivo(computadorId, dispositivoId, justificacion);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Dispositivo desvinculado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @PostMapping("/desvincular-software")
    public ResponseEntity<HttpResponse> desvincularSoftware(@RequestParam Integer computadorId,
            @RequestParam Integer softwareId, @RequestParam String justificacion)
            throws ComputerNotFoundException, SelectNotAllowedException, SoftwareNotFoundException {
        historialComputadorService.desvincularSoftware(computadorId, softwareId, justificacion);

        return new ResponseEntity<>(
                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                        "Software desvinculado exitosamente"),
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/dispositivos-pc/{computadorId}")
    public ResponseEntity<DispositivosXPcResponse> listarDispositivosXPc(@PathVariable Integer computadorId)
            throws ComputerNotFoundException {
        DispositivosXPcResponse dispositivosXPcResponse = historialComputadorService
                .listarDispositivosXPc(computadorId);

        return new ResponseEntity<>(dispositivosXPcResponse, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/software-pc/{computadorId}")
    public ResponseEntity<SoftwareXPcResponse> listarSoftwaresXPc(@PathVariable Integer computadorId)
            throws ComputerNotFoundException {
        SoftwareXPcResponse softwareXPcResponse = historialComputadorService.listarSoftwaresXPc(computadorId);

        return new ResponseEntity<>(softwareXPcResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @GetMapping("/computador-vinculado/{dispositivoId}")
    public ResponseEntity<ComputadoresResponse> listarComputadorVinculadoByDispositivo(@PathVariable Integer dispositivoId) throws DeviceNotFoundException, SelectNotAllowedException {
        ComputadoresResponse computadoresResponse = historialComputadorService.listarComputadorVinculadoByDispositivo(dispositivoId);

        return new ResponseEntity<>(computadoresResponse, HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @GetMapping("/cv-pc/{computadorId}")
    public ResponseEntity<HojaVidaPcResponse> hojaDeVidaPc(@PathVariable Integer computadorId)
            throws ComputerNotFoundException {
        
        HojaVidaPcResponse hojaVidaPcResponse = historialComputadorService.hojaDeVidaPc(computadorId);
        return new ResponseEntity<>(hojaVidaPcResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @GetMapping("/pc/{computadorId}")
    public ResponseEntity<HistorialResponse> listarHistorialDispositivoXPc(@PathVariable Integer computadorId)
            throws ComputerNotFoundException {
        
        HistorialResponse historialResponse = historialComputadorService.listarHistorialDispositivosXPc(computadorId);
        return new ResponseEntity<>(historialResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'SISTEMAS')")
    @GetMapping("/ubicacion-computador/{computadorId}")
    public ResponseEntity<HistorialUbicacionesXPcResponse> listarUbicacionesXPc(@PathVariable Integer computadorId)throws ComputerNotFoundException{
        HistorialUbicacionesXPcResponse historialUbicacionesXPcResponse = historialComputadorService.listarHistorialUbicacionesXPc(computadorId);

        return new ResponseEntity<>(historialUbicacionesXPcResponse, HttpStatus.OK);
    }
}
