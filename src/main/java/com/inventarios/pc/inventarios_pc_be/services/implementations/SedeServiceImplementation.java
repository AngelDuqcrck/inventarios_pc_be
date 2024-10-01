package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.inventarios.pc.inventarios_pc_be.entities.SedePC;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.SedeRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ISedeService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SedeDTO;
import org.springframework.stereotype.Service;

@Service
public class SedeServiceImplementation implements ISedeService {

    public static final String IS_ALREADY_USE = "The %s is already use";
    public static final String IS_NOT_FOUND = "The %s is not found";
    public static final String IS_NOT_ALLOWED = "The %s is not allowed";

    @Autowired
    private SedeRepository sedeRepository;

    /**
     * Crea una nueva sede en el sistema.
     * 
     * @param sedeDTO Objeto SedeDTO que contiene la información de la nueva sede.
     * @return SedeDTO con la información de la sede creada.
     */

    @Override
    public SedeDTO crearSede(SedeDTO sedeDTO) {
        SedePC sedePC = new SedePC();
        BeanUtils.copyProperties(sedeDTO, sedePC);
        sedePC.setDeleteFlag(false);
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
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "SEDE").toUpperCase());
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
    public SedeDTO actualizarSede(Integer id, SedeDTO sedeDTO) throws LocationNotFoundException {
        SedePC sedePC = sedeRepository.findById(id).orElse(null);

        if (sedePC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "SEDE").toUpperCase());
        }
        BeanUtils.copyProperties(sedeDTO, sedePC);
        SedePC sedeActualizada = sedeRepository.save(sedePC);
        SedeDTO sedeActualizadaDTO = new SedeDTO();
        BeanUtils.copyProperties(sedeActualizada, sedeActualizadaDTO);
        return null;
    }

    /**
     * Elimina (marca como eliminada) una sede en el sistema.
     * 
     * @param id El ID de la sede que se va a eliminar.
     * @throws LocationNotFoundException Si la sede no se encuentra en la base de
     *                                   datos.
     * @throws DeleteNotAllowedException Si la sede ya ha sido eliminada
     *                                   previamente.
     */
    @Override
    public void eliminarSede(Integer id) throws LocationNotFoundException, DeleteNotAllowedException {
        SedePC sedePC = sedeRepository.findById(id).orElse(null);

        if (sedePC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "SEDE").toUpperCase());
        }

        if (sedePC.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DELETE SEDE").toUpperCase());
        }
        sedePC.setDeleteFlag(true);
        sedeRepository.save(sedePC);
    }
}
