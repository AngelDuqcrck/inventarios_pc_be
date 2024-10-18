package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.HistorialDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IHistorialComputadorService;

@Service
public class HistorialComputadorService implements IHistorialComputadorService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";

    @Autowired
    private HistorialDispositivoRepository historialDispositivoRepository;

    @Autowired
    private ComputadorRepository computadorRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private EstadoDispositivoRepository estadoDispositivoRepository;

    @Override
    public void vincularDispositivo(Integer computadorId, Integer dispositivoId)
            throws ComputerNotFoundException, SelectNotAllowedException, DeviceNotFoundException {

        Computador computador = computadorRepository.findById(computadorId).orElse(null);

        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "COMPUTADOR").toUpperCase());
        }

        if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE COMPUTADOR").toUpperCase());
        }

        HistorialDispositivo historialDispositivo = new HistorialDispositivo();

        historialDispositivo.setComputador(computador);

        DispositivoPC dispositivoPC = dispositivoRepository.findById(dispositivoId).orElse(null);

        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());
        }

        boolean existeDispositivoMismoTipo = historialDispositivoRepository
                .existsByComputadorAndDispositivoPC_TipoDispositivoAndFechaDesvinculacionIsNull(
                        computador, dispositivoPC.getTipoDispositivo());

        if (existeDispositivoMismoTipo) {
            throw new SelectNotAllowedException(
                    String.format(IS_ALREADY_USE,
                            "EN ESTE COMPUTADOR UN "+dispositivoPC.getTipoDispositivo().getNombre() )
                            .toUpperCase());
        }


        if (!computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE DISPOSITIVO").toUpperCase());
        }

        historialDispositivo.setDispositivoPC(dispositivoPC);

        historialDispositivo.setFechaCambio(new Date());

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("En uso").get();

        computador.setEstadoDispositivo(estadoDispositivo);

        dispositivoPC.setEstadoDispositivo(estadoDispositivo);

        computadorRepository.save(computador);

        dispositivoRepository.save(dispositivoPC);

        historialDispositivoRepository.save(historialDispositivo);
    }

    public void desvincularDispositivo(Integer computadorId, Integer dispositivoId) {

    }
}
