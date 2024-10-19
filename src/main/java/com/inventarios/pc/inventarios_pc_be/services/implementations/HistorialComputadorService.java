package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwareCSA;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.HistorialDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SoftwareCsaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SoftwarePcRepository;
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

    @Autowired
    private SoftwareCsaRepository softwareCsaRepository;

    @Autowired
    private SoftwarePcRepository softwarePcRepository;

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
                            "EN ESTE COMPUTADOR UN " + dispositivoPC.getTipoDispositivo().getNombre())
                            .toUpperCase());
        }

        if (!dispositivoPC.getEstadoDispositivo().getNombre().equals("Disponible")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE DISPOSITIVO").toUpperCase());
        }

        historialDispositivo.setDispositivoPC(dispositivoPC);

        historialDispositivo.setFechaCambio(new Date());

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("En uso").get();

        if (estadoDispositivo == null) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_FOUND, "ESTADO DEL DISPOSITIVO 'EN USO'").toUpperCase());
        }

        computador.setEstadoDispositivo(estadoDispositivo);

        dispositivoPC.setEstadoDispositivo(estadoDispositivo);

        computadorRepository.save(computador);

        dispositivoRepository.save(dispositivoPC);

        historialDispositivoRepository.save(historialDispositivo);
    }

    @Override
    public void desvincularDispositivo(Integer computadorId, Integer dispositivoId)
            throws ComputerNotFoundException, DeviceNotFoundException, SelectNotAllowedException {

        Computador computador = computadorRepository.findById(computadorId).orElse(null);
        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "COMPUTADOR").toUpperCase());
        }

        DispositivoPC dispositivoPC = dispositivoRepository.findById(dispositivoId).orElse(null);
        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());
        }

        HistorialDispositivo historialDispositivo = historialDispositivoRepository
                .findByComputadorAndDispositivoPCAndFechaDesvinculacionIsNull(computador, dispositivoPC);

        if (historialDispositivo == null) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "DESVINCULAR ESTE DISPOSITIVO").toUpperCase());
        }

        historialDispositivo.setFechaDesvinculacion(new Date());

        EstadoDispositivo estadoDisponible = estadoDispositivoRepository.findByNombre("Disponible").orElse(null);
        if (estadoDisponible == null) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_FOUND, "ESTADO DEL DISPOSITIVO 'DISPONIBLE'").toUpperCase());
        }

        dispositivoPC.setEstadoDispositivo(estadoDisponible);

        historialDispositivoRepository.save(historialDispositivo);
        dispositivoRepository.save(dispositivoPC);

    }

    @Override
    public void vincularSoftware(Integer computadorId, Integer softwareId)
            throws ComputerNotFoundException, SoftwareNotFoundException, SelectNotAllowedException {

        Computador computador = computadorRepository.findById(computadorId).orElse(null);
        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "COMPUTADOR").toUpperCase());
        }

        if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE COMPUTADOR").toUpperCase());
        }

        SoftwarePC softwarePC = softwarePcRepository.findById(softwareId).orElse(null);

        if (softwarePC == null) {
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "SOFTWARE").toUpperCase());
        }

        if (softwarePC.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE SOFTWARE").toUpperCase());
        }

        boolean softwareYaVinculado = softwareCsaRepository
                .existsByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(computador, softwarePC);
        if (softwareYaVinculado) {
            throw new SelectNotAllowedException(
                    String.format(IS_ALREADY_USE, "ESTE SOFTWARE EN ESTE COMPUTADOR").toUpperCase());
        }

        SoftwareCSA softwareCSA = new SoftwareCSA();

        softwareCSA.setComputador(computador);
        softwareCSA.setSoftwarePC(softwarePC);
        softwareCSA.setFechaVinculacion(new Date());
        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("En uso").orElse(null);

        if (estadoDispositivo == null) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_FOUND, "ESTADO DEL DISPOSITIVO 'EN USO'").toUpperCase());
        }
        computador.setEstadoDispositivo(estadoDispositivo);

        computadorRepository.save(computador);

        softwareCsaRepository.save(softwareCSA);

    }

    @Override
    public void desvincularSoftware(Integer computadorId, Integer softwareId)
            throws ComputerNotFoundException, SoftwareNotFoundException, SelectNotAllowedException {

        Computador computador = computadorRepository.findById(computadorId).orElse(null);
        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "COMPUTADOR").toUpperCase());
        }

        SoftwarePC softwarePC = softwarePcRepository.findById(softwareId).orElse(null);
        if (softwarePC == null) {
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "SOFTWARE").toUpperCase());
        }

        SoftwareCSA softwareCSA = softwareCsaRepository
                .findByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(computador, softwarePC);
        if (softwareCSA == null) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "DESVINCULAR ESTE SOFTWARE").toUpperCase());
        }

        softwareCSA.setFechaDesvinculacion(new Date());

        softwareCsaRepository.save(softwareCSA);
    }

}
