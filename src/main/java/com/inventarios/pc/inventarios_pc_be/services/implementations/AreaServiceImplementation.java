package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.entities.SedePC;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.AreaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SedeRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IAreaService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.AreaDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.AreaResponse;

/**
 * Implementación del servicio para gestionar las áreas del sistema.
 */
@Service
public class AreaServiceImplementation implements IAreaService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    SedeRepository sedeRepository;

    /**
     * Crea una nueva área en el sistema.
     *
     * @param areaDTO El objeto {@link AreaDTO} con los datos del área a crear.
     * @return Un objeto {@link AreaDTO} con los datos del área recién creada.
     * @throws LocationNotFoundException Si no se encuentra la sede especificada.
     */
    @Override
    public AreaDTO crearArea(AreaDTO areaDTO) throws LocationNotFoundException, SelectNotAllowedException {
        AreaPC areaPC = new AreaPC();
        BeanUtils.copyProperties(areaDTO, areaPC);
        // Con el id de la sede llamamos al repositorio para consultar y traernos la
        // info, si no lo consigue manda nulo y manda la excepcion
        SedePC sedePC = sedeRepository.findById(areaDTO.getSede()).orElse(null);

        if (sedePC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "SEDE").toUpperCase());
        }

        if (sedePC.getDeleteFlag() == true) {
            throw new

            SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR SEDE").toUpperCase());
        }
        areaPC.setSede(sedePC);
        areaPC.setDeleteFlag(false);

        AreaPC areaCreada = areaRepository.save(areaPC);
        AreaDTO areaCreadaDTO = new AreaDTO();
        BeanUtils.copyProperties(areaCreada, areaCreadaDTO);

        return areaCreadaDTO;
    }

    /**
     * Lista todas las áreas registradas en el sistema.
     *
     * @return Una lista de objetos {@link AreaPC} que representan todas las áreas.
     */
    @Override
    public List<AreaPC> listarAreas() {
        return (List<AreaPC>) areaRepository.findAll();
    }

        /**
     * Lista todas las áreas registradas en el sistema.
     *
     * @return Una lista de objetos {@link AreaPC} que representan todas las áreas.
     */
    @Override
    public List<AreaPC> listarAreasPorSede(Integer sedeId) throws LocationNotFoundException, SelectNotAllowedException {
        SedePC sedePC = sedeRepository.findById(sedeId).orElse(null);

        if (sedePC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "SEDE").toUpperCase());
        }

        if (sedePC.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR SEDE").toUpperCase());
        }

        List<AreaPC> areas = areaRepository.findBySede(sedePC);

        if(areas.isEmpty()){
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());
        }
        return areas;
    }


    // Metodo que lista toda la informacion especifica de acuerdo a su id
    @Override
    public AreaResponse listarAreaById(Integer id) throws LocationNotFoundException {
        AreaPC areaPC = areaRepository.findById(id).orElse(null);

        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());
        }
        AreaResponse areaResponse = new AreaResponse();
        BeanUtils.copyProperties(areaPC, areaResponse);
        areaResponse.setSede(areaPC.getSede().getNombre());
        return areaResponse;
    }

    /**
     * Actualiza los datos de un área existente.
     *
     * @param id      El ID del área a actualizar.
     * @param areaDTO El objeto {@link AreaDTO} con los nuevos datos del área.
     * @return Un objeto {@link AreaDTO} con los datos del área actualizada.
     * @throws LocationNotFoundException Si no se encuentra el área o la sede
     *                                   especificada.
     */
    @Override
    public AreaDTO actualizarArea(Integer id, AreaDTO areaDTO)
            throws SelectNotAllowedException, LocationNotFoundException, UpdateNotAllowedException {
        AreaPC areaPC = areaRepository.findById(id).orElse(null);
        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());
        }

        if (areaPC.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR AREA").toUpperCase());
        }
        BeanUtils.copyProperties(areaDTO, areaPC);

        if (areaDTO.getSede() != null) {
            SedePC sedePC = sedeRepository.findById(areaDTO.getSede()).orElse(null);
            if (sedePC == null) {
                throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "SEDE").toUpperCase());
            }
            if (sedePC.getDeleteFlag() == true) {
                throw new

                SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR SEDE").toUpperCase());
            }
            areaPC.setSede(sedePC);
        }

        AreaPC areaActualizada = areaRepository.save(areaPC);
        AreaDTO areaActualizadaDTO = new AreaDTO();
        BeanUtils.copyProperties(areaActualizada, areaActualizadaDTO);
        areaActualizadaDTO.setSede(areaActualizada.getSede().getId());
        return areaActualizadaDTO;
    }

    /**
     * Elimina (deshabilita) un área del sistema, marcándola como eliminada.
     *
     * @param id El ID del área a eliminar.
     * @throws LocationNotFoundException Si no se encuentra el área especificada.
     * @throws DeleteNotAllowedException Si la eliminación del área no está
     *                                   permitida.
     */
    @Override
    public void eliminarArea(Integer id) throws LocationNotFoundException, DeleteNotAllowedException {
        AreaPC areaPC = areaRepository.findById(id).orElse(null);
        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());
        }

        if (areaPC.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR AREA").toUpperCase());
        }
        areaPC.setDeleteFlag(true);
        areaRepository.save(areaPC);
    }

    @Override
    public void activarArea(Integer id) throws LocationNotFoundException, ActivateNotAllowedException {
        AreaPC areaPC = areaRepository.findById(id).orElse(null);
        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());
        }

        if (areaPC.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVAR AREA").toUpperCase());
        }
        areaPC.setDeleteFlag(false);
        areaRepository.save(areaPC);
    }
}
