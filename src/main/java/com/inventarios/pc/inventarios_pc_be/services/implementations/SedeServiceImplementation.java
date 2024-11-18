package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.*;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.entities.CambioUbicacionPc;
import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.SedePC;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DuplicateEntityException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.AreaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.CambioUbicacionPcRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.HistorialDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SedeRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UbicacionRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ISedeService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SedeDTO;
import org.springframework.stereotype.Service;

@Service
public class SedeServiceImplementation implements ISedeService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "no esta permitido %s ";


    @Autowired
    private ComputadorServiceImplementation computadorServiceImplementation;
    @Autowired
    private CambioUbicacionPcRepository cambioUbicacionPcRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private ComputadorRepository computadorRepository;

    @Autowired
    private EstadoDispositivoRepository estadoDispositivoRepository;

    @Autowired
    private HistorialDispositivoRepository historialDispositivoRepository;

    /**
     * Crea una nueva sede en el sistema.
     * 
     * @param sedeDTO Objeto SedeDTO que contiene la información de la nueva sede.
     * @return SedeDTO con la información de la sede creada.
     */

    @Override
    public SedeDTO crearSede(SedeDTO sedeDTO)  throws DuplicateEntityException{
        SedePC sedePC = new SedePC();
        BeanUtils.copyProperties(sedeDTO, sedePC);
        sedePC.setDeleteFlag(false);
        if(sedeRepository.existsByNombreIgnoreCase(sedePC.getNombre())){
            throw new DuplicateEntityException("Ya existe una sede con el nombre " + sedePC.getNombre());
        }
        SedePC sedeCreada = sedeRepository.save(sedePC);
        SedeDTO sedeCreadaDto = new SedeDTO();
        BeanUtils.copyProperties(sedeCreada, sedeCreadaDto);
        return sedeCreadaDto;

    }

    /**
     * Obtiene la información de una sede por su ID.
     * 
     * @param id El ID de la sede a buscar.
     * @return SedeDTO con la información de la sede encontrada.
     * @throws LocationNotFoundException Si la sede no se encuentra en la base de
     *                                   datos.
     */
    @Override
    public SedeDTO listarSedePorId(Integer id) throws LocationNotFoundException {
        SedePC sedePC = sedeRepository.findById(id).orElse(null);
        if (sedePC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND_F, "LA SEDE").toUpperCase());
        }
        SedeDTO sedeDTO = new SedeDTO();
        BeanUtils.copyProperties(sedePC, sedeDTO);
        return sedeDTO;
    }

    /**
     * Lista todas las sedes en el sistema.
     * 
     * @return Lista de objetos SedePC con la información de todas las sedes.
     */
    @Override
    public List<SedePC> listarSedes() {
        return (List<SedePC>) sedeRepository.findAll();
    }

    /**
     * Actualiza la información de una sede existente.
     * 
     * @param id      El ID de la sede a actualizar.
     * @param sedeDTO Objeto SedeDTO que contiene la nueva información de la sede.
     * @return SedeDTO con la información de la sede actualizada.
     * @throws LocationNotFoundException Si la sede no se encuentra en la base de
     *                                   datos.
     */
    @Override
    public SedeDTO actualizarSede(Integer id, SedeDTO sedeDTO)
            throws LocationNotFoundException, UpdateNotAllowedException , DuplicateEntityException{
        SedePC sedePC = sedeRepository.findById(id).orElse(null);

        if (sedePC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND_F, "LA SEDE").toUpperCase());
        }
        if (sedePC.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTA SEDE PORQUE ESTA INACTIVA").toUpperCase());
        }
        if(sedeRepository.existsByNombreIgnoreCaseAndIdNot(sedeDTO.getNombre(), id)){
            throw new DuplicateEntityException("Ya existe una sede con el nombre " + sedeDTO.getNombre());
        }
        BeanUtils.copyProperties(sedeDTO, sedePC);
        SedePC sedeActualizada = sedeRepository.save(sedePC);
        SedeDTO sedeActualizadaDTO = new SedeDTO();
        BeanUtils.copyProperties(sedeActualizada, sedeActualizadaDTO);
        return null;
    }

    @Override
    public void eliminarSede(Integer id)
            throws LocationNotFoundException, DeleteNotAllowedException, StateNotFoundException {
        SedePC sedePC = sedeRepository.findById(id).orElse(null);

        if (sedePC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND_F, "LA SEDE").toUpperCase());
        }

        if (sedePC.getId() == 1) {
            throw new DeleteNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "DESACTIVAR LA SEDE PRINCIPAL").toUpperCase());
        }
        if (sedePC.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "DESACTIVAR ESTA SEDE PORQUE YA ESTA DESACTIVADA").toUpperCase());
        }

        List<AreaPC> areas = areaRepository.findBySede(sedePC);

        if (areas.isEmpty()) {

            sedeRepository.delete(sedePC);
        } else {

            sedePC.setDeleteFlag(true);
            sedeRepository.save(sedePC);

            for (AreaPC area : areas) {
                area.setDeleteFlag(true);
                areaRepository.save(area);

                List<Ubicacion> ubicaciones = ubicacionRepository.findByArea(area);
                for (Ubicacion ubicacion : ubicaciones) {
                    ubicacion.setDeleteFlag(true);
                    ubicacionRepository.save(ubicacion);

                    List<Computador> computadores = computadorRepository.findByUbicacion(ubicacion);

                    for (Computador computador : computadores) {

                        Ubicacion antiguaUbicacion = computador.getUbicacion();

                        EstadoDispositivo estadoComputador = estadoDispositivoRepository.findById(4).orElse(null); // Estado
                                                                                                                   // Disponible
                        if (estadoComputador == null) {
                            throw new StateNotFoundException(String
                                    .format(IS_NOT_FOUND, "EL ESTADO DISPONIBLE NO FUE ENCONTRADO").toUpperCase());
                        }

                        computador.setEstadoDispositivo(estadoComputador);

                        Ubicacion bodegaSistemas = ubicacionRepository.findById(4).orElse(null);

                        if (bodegaSistemas == null) {
                            throw new StateNotFoundException(String
                                    .format(IS_NOT_FOUND, "LA BODEGA DE SISTEMAS DE LA SEDE PRINCIPAL").toUpperCase());
                        }

                        computadorServiceImplementation.crearCambioUbicacionPc(computador, antiguaUbicacion, bodegaSistemas, "La sede fue desactivada y el computador "+computador.getNombre()+" fue movido a la bodega de sistemas de la sede principal"); 
                        
                        computador.setUbicacion(bodegaSistemas);
                        computador.setResponsable(null);

                        List<HistorialDispositivo> historialDispositivos = historialDispositivoRepository
                                .findByComputadorAndFechaDesvinculacionIsNull(computador);

                        for (HistorialDispositivo historialDispositivo : historialDispositivos) {

                            DispositivoPC dispositivoPC = historialDispositivo.getDispositivoPC();
                            if (dispositivoPC.getTipoDispositivo().getId() != 8) {
                                historialDispositivo.setFechaDesvinculacion(new Date());
                                historialDispositivo.setJustificacion("El dispositivo fue desvinculado, porque la sede "+sedePC.getNombre().toLowerCase()+ " fue desactivada y el computador fue movido a la bodega de sistemas"); 
                                historialDispositivoRepository.save(historialDispositivo);

                            }

                            dispositivoPC.setEstadoDispositivo(estadoComputador);
                            dispositivoRepository.save(dispositivoPC);
                        }

                        computadorRepository.save(computador);
                    }
                }
            }
        }
    }

    @Override
    public void activarSede(Integer id) throws LocationNotFoundException, ActivateNotAllowedException {
        SedePC sedePC = sedeRepository.findById(id).orElse(null);

        if (sedePC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "LA SEDE").toUpperCase());
        }

        if (sedePC.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ACTIVAR ESTA SEDE PORQUE YA ESTA ACTIVADA").toUpperCase());
        }
        sedePC.setDeleteFlag(false);
        sedeRepository.save(sedePC);

        List<AreaPC> areas = areaRepository.findBySede(sedePC);

        for (AreaPC area : areas) {
            area.setDeleteFlag(false);
            areaRepository.save(area);

            // Desactivar las ubicaciones vinculadas a esta área
            List<Ubicacion> ubicaciones = ubicacionRepository.findByArea(area);
            for (Ubicacion ubicacion : ubicaciones) {
                ubicacion.setDeleteFlag(false);
                ubicacionRepository.save(ubicacion);
            }
        }
    }
}
