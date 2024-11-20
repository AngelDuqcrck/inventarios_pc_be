package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.*;

import com.inventarios.pc.inventarios_pc_be.shared.responses.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.CambioUbicacionPc;
import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwareCSA;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.CambioUbicacionPcRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.HistorialDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SoftwareCsaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SoftwarePcRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IHistorialComputadorService;

@Service
public class HistorialComputadorService implements IHistorialComputadorService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "no esta permitido %s ";
    public static final String IS_NOT_VALID = "no es valido %s";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";
    public static final String IS_NOT_VINCULATED = "%s no esta vinculado";

    @Autowired
    private CambioUbicacionPcRepository cambioUbicacionPcRepository;

    @Autowired
    private TipoDispositivoRepository tipoDispositivoRepository;

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
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR EL COMPUTADOR " + computador.getNombre()
                            + " PORQUE TIENE UN ESTADO DIFERENTE A DISPONIBLE Y EN USO").toUpperCase());
        }

        HistorialDispositivo historialDispositivo = new HistorialDispositivo();

        historialDispositivo.setComputador(computador);

        DispositivoPC dispositivoPC = dispositivoRepository.findById(dispositivoId).orElse(null);

        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "EL DISPOSITIVO").toUpperCase());
        }

        boolean existeDispositivoMismoTipo = historialDispositivoRepository
                .existsByComputadorAndDispositivoPC_TipoDispositivoAndFechaDesvinculacionIsNull(
                        computador, dispositivoPC.getTipoDispositivo());

        if (existeDispositivoMismoTipo) {
            throw new SelectNotAllowedException(
                    String.format(IS_ALREADY_USE,
                            dispositivoPC.getTipoDispositivo().getNombre() + " YA ESTA EN USO EN ESTE COMPUTADOR")
                            .toUpperCase());
        }

        if (!dispositivoPC.getEstadoDispositivo().getNombre().equals("Disponible")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR EL DISPOSITIVO " + dispositivoPC.getNombre()
                            + " PORQUE NO TIENE ESTADO DISPONIBLE").toUpperCase());
        }

        historialDispositivo.setDispositivoPC(dispositivoPC);

        historialDispositivo.setFechaCambio(new Date());

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("En uso").get();

        if (estadoDispositivo == null) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_FOUND, "EL ESTADO EN USO ").toUpperCase());
        }

        computador.setEstadoDispositivo(estadoDispositivo);

        dispositivoPC.setEstadoDispositivo(estadoDispositivo);

        computadorRepository.save(computador);

        dispositivoRepository.save(dispositivoPC);

        historialDispositivoRepository.save(historialDispositivo);
    }

    @Override
    public ComputadoresResponse listarComputadorVinculadoByDispositivo(Integer dispositivoId) throws DeviceNotFoundException, SelectNotAllowedException {

        DispositivoPC dispositivoPC = dispositivoRepository.findById(dispositivoId).orElse(null);
        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "EL DISPOSITIVO").toUpperCase());
        }

        Boolean dispositivoEstaVinculado = historialDispositivoRepository.existsByDispositivoPCAndFechaDesvinculacionIsNull(dispositivoPC);

        if (dispositivoEstaVinculado == false) {
            throw new SelectNotAllowedException(String.format(IS_NOT_VINCULATED, "EL DISPOSITIVO").toUpperCase());
        }

        HistorialDispositivo historial = historialDispositivoRepository.findFirstByDispositivoPCAndFechaDesvinculacionIsNull(dispositivoPC);

        Computador computador = historial.getComputador();

        ComputadoresResponse computadoresResponse = new ComputadoresResponse();
        BeanUtils.copyProperties(computador, computadoresResponse);
        computadoresResponse.setTipoPC(computador.getTipoPC().getNombre());
        computadoresResponse.setUbicacion(computador.getUbicacion().getNombre());
        computadoresResponse.setEstadoDispositivo(computador.getEstadoDispositivo().getNombre());
        computadoresResponse.setPrimerNombreUser(computador.getResponsable().getPrimerNombre());
        computadoresResponse.setArea(computador.getUbicacion().getArea().getNombre());
        computadoresResponse.setSede(computador.getUbicacion().getArea().getSede().getNombre());
        if(computador.getResponsable() != null){
            computadoresResponse.setResponsable(computador.getResponsable().getPrimerNombre() + " " + computador.getResponsable().getSegundoNombre() + " " + computador.getResponsable().getPrimerApellido() + " " + computador.getResponsable().getSegundoApellido());
        }
        return computadoresResponse;

    }

    @Override
    public void desvincularDispositivo(Integer computadorId, Integer dispositivoId, String justificacion)
            throws ComputerNotFoundException, DeviceNotFoundException, SelectNotAllowedException {

        Computador computador = computadorRepository.findById(computadorId).orElse(null);
        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        DispositivoPC dispositivoPC = dispositivoRepository.findById(dispositivoId).orElse(null);
        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "EL DISPOSITIVO").toUpperCase());
        }

        HistorialDispositivo historialDispositivo = historialDispositivoRepository
                .findByComputadorAndDispositivoPCAndFechaDesvinculacionIsNull(computador, dispositivoPC);

        if (historialDispositivo == null) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_FOUND, "EL DISPOSITIVO " + dispositivoPC.getNombre()
                            + " VINCULADO AL COMPUTADOR " + computador.getNombre()).toUpperCase());
        }

        historialDispositivo.setFechaDesvinculacion(new Date());
        historialDispositivo.setJustificacion(justificacion);
        EstadoDispositivo estadoDisponible = estadoDispositivoRepository.findByNombre("Disponible").orElse(null);
        if (estadoDisponible == null) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_FOUND, "EL ESTADO DISPONIBLE").toUpperCase());
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
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        if (!computador.getEstadoDispositivo().getNombre().equals("En uso")
                && !computador.getEstadoDispositivo().getNombre().equals("Disponible")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR EL COMPUTADOR " + computador.getNombre()
                            + " PORQUE TIENE UN ESTADO DIFERENTA A EN USO Y DISPONIBLE").toUpperCase());
        }

        SoftwarePC softwarePC = softwarePcRepository.findById(softwareId).orElse(null);

        if (softwarePC == null) {
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "EL SOFTWARE").toUpperCase());
        }

        if (softwarePC.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED,
                            "SELECCIONAR EL SOFTWARE " + softwarePC.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADO")
                            .toUpperCase());
        }

        boolean softwareYaVinculado = softwareCsaRepository
                .existsByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(computador, softwarePC);
        if (softwareYaVinculado) {
            throw new SelectNotAllowedException(
                    String.format(IS_ALREADY_USE,
                            "EN EL COMPUTADOR " + computador.getNombre() + ", EL SOFTWARE " + softwarePC.getNombre())
                            .toUpperCase());
        }

        SoftwareCSA softwareCSA = new SoftwareCSA();

        softwareCSA.setComputador(computador);
        softwareCSA.setSoftwarePC(softwarePC);
        softwareCSA.setFechaVinculacion(new Date());
        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("En uso").orElse(null);

        if (estadoDispositivo == null) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_FOUND, "EL ESTADO EN USO").toUpperCase());
        }
        computador.setEstadoDispositivo(estadoDispositivo);

        computadorRepository.save(computador);

        softwareCsaRepository.save(softwareCSA);

    }

    @Override
    public void desvincularSoftware(Integer computadorId, Integer softwareId, String justificacion)
            throws ComputerNotFoundException, SoftwareNotFoundException, SelectNotAllowedException {

        Computador computador = computadorRepository.findById(computadorId).orElse(null);
        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        SoftwarePC softwarePC = softwarePcRepository.findById(softwareId).orElse(null);
        if (softwarePC == null) {
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "EL SOFTWARE").toUpperCase());
        }

        SoftwareCSA softwareCSA = softwareCsaRepository
                .findByComputadorAndSoftwarePCAndFechaDesvinculacionIsNull(computador, softwarePC);
        if (softwareCSA == null) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_FOUND,
                            "EN EL COMPUTADOR " + computador.getNombre()
                                    + " NO SE ENCUENTR ACTUALMENTE VINCULADO EL SOFTWARE " + softwarePC.getNombre())
                            .toUpperCase());
        }

        softwareCSA.setFechaDesvinculacion(new Date());
        softwareCSA.setJustificacion(justificacion);

        softwareCsaRepository.save(softwareCSA);
    }

    @Override
    public DispositivosXPcResponse listarDispositivosXPc(Integer computadorId) throws ComputerNotFoundException {

        Computador computador = computadorRepository.findById(computadorId).orElse(null);
        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        List<TipoDispositivo> tiposDispositivos = tipoDispositivoRepository.findAllByDeleteFlagFalse();

        List<DispositivosVinculadosResponse> dispositivosVinculadosList = new ArrayList<>();

        for (TipoDispositivo tipo : tiposDispositivos) {

            HistorialDispositivo historial = historialDispositivoRepository
                    .findFirstByComputadorAndDispositivoPC_TipoDispositivoAndFechaDesvinculacionIsNull(
                            computador, tipo);

                            if (historial != null) {
                                DispositivosVinculadosResponse dispositivoVinculado = new DispositivosVinculadosResponse();
                                dispositivoVinculado.setId(historial.getDispositivoPC().getId());
                                dispositivoVinculado.setPlaca(historial.getDispositivoPC().getPlaca());
                                dispositivoVinculado.setMarca(historial.getDispositivoPC().getMarca().getNombre());
                                dispositivoVinculado.setModelo(historial.getDispositivoPC().getModelo());
                                dispositivoVinculado.setSerial(historial.getDispositivoPC().getSerial());
                                
                                if(tipo.getId()!= 8){
                                    dispositivoVinculado.setTipoDispositivo(historial.getDispositivoPC().getTipoDispositivo().getNombre());
                                }else{
                                    dispositivoVinculado.setTipoDispositivo(historial.getTipoTorre());
                                }
                                
                                dispositivosVinculadosList.add(dispositivoVinculado);
            } else {
                DispositivosVinculadosResponse dispositivoNoVinculado = DispositivosVinculadosResponse.builder()
                        .id(null).placa(null).serial(null).marca(null).modelo(null).tipoDispositivo(tipo.getNombre())
                        .build();
                dispositivosVinculadosList.add(dispositivoNoVinculado);
            }
        }

        DispositivosXPcResponse response = DispositivosXPcResponse.builder()
                .id(computador.getId())
                .nombre(computador.getNombre())
                .dispositivosVinculados(dispositivosVinculadosList)
                .build();

        return response;
    }

    @Override
    public SoftwareXPcResponse listarSoftwaresXPc(Integer computadorId) throws ComputerNotFoundException {
        Computador computador = computadorRepository.findById(computadorId).orElse(null);
        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        List<SoftwareCSA> softwaresVinculados = softwareCsaRepository
                .findByComputadorAndFechaDesvinculacionIsNull(computador);

        List<SoftwareVinculadosResponse> softwareVinculadosResponses = new ArrayList<>();

        for (SoftwareCSA software : softwaresVinculados) {
            SoftwareVinculadosResponse softwareResponse = SoftwareVinculadosResponse.builder()
                    .id(software.getSoftwarePC().getId())
                    .nombre(software.getSoftwarePC().getNombre())
                    .version(software.getSoftwarePC().getVersion())
                    .empresa(software.getSoftwarePC().getEmpresa())
                    .tipoSoftware(software.getSoftwarePC().getTipoSoftware().getNombre())
                    .build();

            softwareVinculadosResponses.add(softwareResponse);
        }

        SoftwareXPcResponse softwareXPcResponse = SoftwareXPcResponse.builder()
                .id(computador.getId())
                .nombre(computador.getNombre())
                .softwareVinculados(softwareVinculadosResponses)
                .build();

        return softwareXPcResponse;
    }

    @Override
    public HojaVidaPcResponse hojaDeVidaPc(Integer computadorId) throws ComputerNotFoundException {

        Computador computador = computadorRepository.findById(computadorId).orElse(null);
        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        HojaVidaPcResponse hojadeVidaPc = new HojaVidaPcResponse();

        BeanUtils.copyProperties(computador, hojadeVidaPc);
        hojadeVidaPc.setTipoPC(computador.getTipoPC().getNombre());
        if (computador.getResponsable() != null) {
            hojadeVidaPc.setResponsable(
                    computador.getResponsable().getTipoDocumento().getAbreviatura()+": "+computador.getResponsable().getCedula()+" - "+
                    computador.getResponsable().getPrimerNombre() + " " + computador.getResponsable().getSegundoNombre()
                            + " " + computador.getResponsable().getPrimerApellido() + " "
                            + computador.getResponsable().getSegundoApellido());
        } else {
            hojadeVidaPc.setResponsable(null);
        }
        hojadeVidaPc.setUbicacion(computador.getUbicacion().getNombre());
        hojadeVidaPc.setSede(computador.getUbicacion().getArea().getSede().getNombre());
        hojadeVidaPc.setArea(computador.getUbicacion().getArea().getNombre());
        hojadeVidaPc.setMarca(computador.getMarca().getNombre());
        hojadeVidaPc.setProcesador(computador.getProcesador().getNombre());
        hojadeVidaPc.setPropietario(computador.getPropietario().getNombre());
        hojadeVidaPc.setRam(computador.getRam().getNombre()+" "+computador.getRam().getUnidad());
        hojadeVidaPc.setAlmacenamiento(computador.getAlmacenamiento().getNombre()+" "+computador.getAlmacenamiento().getUnidad());
        hojadeVidaPc.setEstadoDispositivo(computador.getEstadoDispositivo().getNombre());
        hojadeVidaPc.setTipoAlmacenamiento(computador.getTipoAlmacenamiento().getNombre());
        hojadeVidaPc.setTipoRam(computador.getTipoRam().getNombre());
        // hojadeVidaPc.setResPrimerNombre(computador.getResponsable().getPrimerNombre());

        List<TipoDispositivo> tiposDispositivos = tipoDispositivoRepository.findAllByDeleteFlagFalse();

        List<DispositivosVinculadosResponse> dispositivosVinculadosList = new ArrayList<>();
        List<SoftwareVinculadosResponse> softwareVinculadosList = new ArrayList<>();

        for (TipoDispositivo tipo : tiposDispositivos) {

            HistorialDispositivo historial = historialDispositivoRepository
                    .findFirstByComputadorAndDispositivoPC_TipoDispositivoAndFechaDesvinculacionIsNull(
                            computador, tipo);

            
            if (historial != null) {
                DispositivosVinculadosResponse dispositivoVinculado = new DispositivosVinculadosResponse();
                dispositivoVinculado.setId(historial.getDispositivoPC().getId());
                dispositivoVinculado.setPlaca(historial.getDispositivoPC().getPlaca());
                dispositivoVinculado.setMarca(historial.getDispositivoPC().getMarca().getNombre());
                dispositivoVinculado.setModelo(historial.getDispositivoPC().getModelo());
                dispositivoVinculado.setSerial(historial.getDispositivoPC().getSerial());
                
                if(tipo.getId()!= 8){
                    dispositivoVinculado.setTipoDispositivo(historial.getDispositivoPC().getTipoDispositivo().getNombre());
                }else{
                    dispositivoVinculado.setTipoDispositivo(historial.getTipoTorre());
                }
                
                dispositivosVinculadosList.add(dispositivoVinculado);
            } else {
                DispositivosVinculadosResponse dispositivoNoVinculado = DispositivosVinculadosResponse.builder()
                        .id(null).marca(null).placa(null).modelo(null).serial(null).tipoDispositivo(tipo.getNombre())
                        .build();
                dispositivosVinculadosList.add(dispositivoNoVinculado);
            }
        }

        List<SoftwareCSA> softwaresCSA = softwareCsaRepository
                .findAllByComputadorAndFechaDesvinculacionIsNull(computador);
        for (SoftwareCSA softwareCSA : softwaresCSA) {
            SoftwareVinculadosResponse softwareVinculado = SoftwareVinculadosResponse.builder()
                    .id(softwareCSA.getSoftwarePC().getId())
                    .nombre(softwareCSA.getSoftwarePC().getNombre())
                    .version(softwareCSA.getSoftwarePC().getVersion())
                    .tipoSoftware(softwareCSA.getSoftwarePC().getTipoSoftware().getNombre())
                    .empresa(softwareCSA.getSoftwarePC().getEmpresa())
                    .build();
            softwareVinculadosList.add(softwareVinculado);
        }

        hojadeVidaPc.setDispositivosVinculados(dispositivosVinculadosList);
        hojadeVidaPc.setSoftwareVinculados(softwareVinculadosList);

        return hojadeVidaPc;
    }

    @Override
    public HistorialUbicacionesXPcResponse listarHistorialUbicacionesXPc(Integer computadorId)
            throws ComputerNotFoundException {
        Computador computador = computadorRepository.findById(computadorId).orElse(null);
        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        List<CambioUbicacionPc> cambioUbicacionPcs = cambioUbicacionPcRepository.findByComputador(computador);

        List<HistorialUbicaciones> historialUbicaciones = new ArrayList<>();

        for (CambioUbicacionPc cambioUbicacionPc : cambioUbicacionPcs) {
            HistorialUbicaciones historialUbicacion = HistorialUbicaciones.builder()
                    .id(cambioUbicacionPc.getId())
                    .nombre(cambioUbicacionPc.getUbicacion().getNombre())
                    .sede(cambioUbicacionPc.getUbicacion().getArea().getSede().getNombre())
                    .area(cambioUbicacionPc.getUbicacion().getArea().getNombre())
                    .fechaIngreso(cambioUbicacionPc.getFechaIngreso())
                    .fechaCambio(cambioUbicacionPc.getFechaCambio())
                    .justificacion(cambioUbicacionPc.getJustificacion())
                    .build();

            historialUbicaciones.add(historialUbicacion);
        }

        HistorialUbicacionesXPcResponse historialUbicacionesXPcResponse = HistorialUbicacionesXPcResponse.builder()
                .id(computador.getId())
                .nombre(computador.getNombre())
                .historialUbicaciones(historialUbicaciones)
                .build();

        return historialUbicacionesXPcResponse;
    }

    @Override
    public HistorialResponse listarHistorialDispositivosXPc(Integer computadorId) throws ComputerNotFoundException {
        Computador computador = computadorRepository.findById(computadorId).orElse(null);
        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        List<HistorialDispositivo> historialDispositivos = historialDispositivoRepository.findByComputador(computador);

        List<HistorialDispositivosResponse> historialDispositivosResponses = new ArrayList<>();

        for (HistorialDispositivo historial : historialDispositivos) {
            HistorialDispositivosResponse historialResponse = HistorialDispositivosResponse.builder()
                    .id(historial.getDispositivoPC().getId())
                    .nombre(historial.getDispositivoPC().getNombre())
                    .tipoDispositivo(historial.getDispositivoPC().getTipoDispositivo().getNombre())
                    .fechaCambio(historial.getFechaCambio())
                    .fechaDesvinculacion(historial.getFechaDesvinculacion())
                    .justificacion(historial.getJustificacion())
                    .build();

            historialDispositivosResponses.add(historialResponse);
        }

        HistorialResponse historialResponse = HistorialResponse.builder()
                .id(computador.getId())
                .nombre(computador.getNombre())
                .historialDispositivosResponses(historialDispositivosResponses)
                .build();

        return historialResponse;
    }
}
