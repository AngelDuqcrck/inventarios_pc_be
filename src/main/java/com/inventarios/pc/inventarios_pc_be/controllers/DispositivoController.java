package com.inventarios.pc.inventarios_pc_be.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarEstadoDispositivoRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
                        throws TypeDeviceNotFoundException, MarcaNotFoundException, StateNotFoundException,
                        SelectNotAllowedException, OwnerNotFoundException {
                dispositivoService.crearDispositivo(dispositivoRequest);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Dispositivo creado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping
        public ResponseEntity<List<DispositivoResponse>> listarDispositivos() {
                return ResponseEntity.ok(
                                dispositivoService.listarDispositivos().stream().map(dispositivo -> {
                                        DispositivoResponse dispositivoR = new DispositivoResponse();
                                        BeanUtils.copyProperties(dispositivo, dispositivoR);
                                        dispositivoR.setTipoDispositivo(dispositivo.getTipoDispositivo().getNombre());
                                        dispositivoR.setMarca(dispositivo.getMarca().getNombre());
                                        dispositivoR.setEstadoDispositivo(
                                                        dispositivo.getEstadoDispositivo().getNombre());
                                        dispositivoR.setPropietario(dispositivo.getPropietario().getNombre());
                                        return dispositivoR;
                                }).collect(Collectors.toList()));
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @GetMapping("/listbytipe-state")
        public ResponseEntity<List<DispositivoResponse>> listarDispTipoDisponible(@RequestParam Integer tipoDispositivo) {
                return ResponseEntity.ok(
                                dispositivoService.listarDispTipoEstado(tipoDispositivo, 4).stream().map(dispositivo -> {
                                        DispositivoResponse dispositivoR = new DispositivoResponse();
                                        BeanUtils.copyProperties(dispositivo, dispositivoR);
                                        dispositivoR.setTipoDispositivo(dispositivo.getTipoDispositivo().getNombre());
                                        dispositivoR.setMarca(dispositivo.getMarca().getNombre());
                                        dispositivoR.setPlaca(dispositivo.getPlaca());
                                        dispositivoR.setSerial(dispositivo.getSerial());
                                        dispositivoR.setModelo(dispositivo.getModelo());
                                        dispositivoR.setNombre(dispositivo.getModelo()+" "+dispositivo.getMarca().getNombre()+" "+dispositivo.getPlaca());
                                        dispositivoR.setEstadoDispositivo(
                                                        dispositivo.getEstadoDispositivo().getNombre());
                                        return dispositivoR;
                                }).collect(Collectors.toList()));
        }


        @PreAuthorize("hasAuthority('ADMIN')")
        @PutMapping("/actualizar/{dispositivoId}")
        public ResponseEntity<HttpResponse> actualizarDispositivo(@PathVariable Integer dispositivoId,
                        @RequestBody DispositivoRequest dispositivoRequest) throws TypeDeviceNotFoundException,
                        MarcaNotFoundException, StateNotFoundException, DeviceNotFoundException,
                        SelectNotAllowedException, UpdateNotAllowedException, OwnerNotFoundException {
                dispositivoService.actualizarDispositivo(dispositivoId, dispositivoRequest);

                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Dispositivo actualizado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/{dispositivoId}")
        public ResponseEntity<DispositivoResponse> listarDispositivoById(@PathVariable Integer dispositivoId)
                        throws DeviceNotFoundException {
                DispositivoResponse dispositivoResponse = dispositivoService.listarDispositivoById(dispositivoId);
                return new ResponseEntity<>(dispositivoResponse, HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @DeleteMapping("/eliminar/{dispositivoId}")
        public ResponseEntity<HttpResponse> eliminarDispositivo(@PathVariable Integer dispositivoId)
                        throws DeviceNotFoundException, DeleteNotAllowedException {
                dispositivoService.eliminarDispositivo(dispositivoId);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Dispositivo eliminado exitosamente"),
                                HttpStatus.OK);
        }

        @PreAuthorize("hasAuthority('ADMIN')")
        @PostMapping("/cambiar-estado")
        public ResponseEntity<HttpResponse> cambiarEstadoDispositivo(@RequestBody CambiarEstadoDispositivoRequest cambiarEstadoDispositivoRequest)
                        throws DeviceNotFoundException, StateNotFoundException, ChangeNotAllowedException, ComputerNotFoundException, SelectNotAllowedException {
                dispositivoService.cambiarEstadoDispositivo(cambiarEstadoDispositivoRequest);
                return new ResponseEntity<>(
                                new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK, HttpStatus.OK.getReasonPhrase(),
                                                "Estado cambiado exitosamente"),
                                HttpStatus.OK);
        }

}
