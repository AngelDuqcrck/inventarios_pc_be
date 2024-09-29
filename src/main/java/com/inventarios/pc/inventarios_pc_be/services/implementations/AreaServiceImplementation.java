package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.entities.SedePC;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.AreaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SedeRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IAreaService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.AreaDTO;

/**
 * Implementación del servicio para gestionar las áreas del sistema.
 */
@Service
public class AreaServiceImplementation implements IAreaService {
    
    public static final String IS_ALREADY_USE = "The %s is already use";
    public static final String IS_NOT_FOUND = "The %s is not found";
    public static final String IS_NOT_ALLOWED = "The %s is not allowed";


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
    public AreaDTO crearArea(AreaDTO areaDTO) throws LocationNotFoundException{
        AreaPC areaPC = new AreaPC();
        BeanUtils.copyProperties(areaDTO, areaPC);
        //Con el id de la sede llamamos al repositorio para consultar y traernos la info, si no lo consigue manda nulo y manda la excepcion
        SedePC sedePC = sedeRepository.findById(areaDTO.getSede().getId()).orElse(null);
        
        if(sedePC == null){
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "SEDE").toUpperCase());
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
    public List<AreaPC> listarAreas(){
        return(List<AreaPC>) areaRepository.findAll();
    }

    /**
     * Actualiza los datos de un área existente.
     *
     * @param id El ID del área a actualizar.
     * @param areaDTO El objeto {@link AreaDTO} con los nuevos datos del área.
     * @return Un objeto {@link AreaDTO} con los datos del área actualizada.
     * @throws LocationNotFoundException Si no se encuentra el área o la sede especificada.
     */
    @Override
    public AreaDTO actualizarArea(Integer id, AreaDTO areaDTO) throws LocationNotFoundException{
        AreaPC areaPC = areaRepository.findById(id).orElse(null);
        if(areaPC == null){
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());        
        }
        BeanUtils.copyProperties(areaDTO, areaPC);

        if(areaDTO.getSede()!= null){
            SedePC sedePC = sedeRepository.findById(areaDTO.getSede().getId()).orElse(null);
                if(sedePC == null){
                    throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "SEDE").toUpperCase());
                }
            areaPC.setSede(sedePC);
        }
        AreaPC areaActualizada = areaRepository.save(areaPC);
        AreaDTO areaActualizadaDTO = new AreaDTO();
        BeanUtils.copyProperties(areaActualizada, areaActualizadaDTO);
        areaActualizadaDTO.setSede(areaActualizada.getSede());
        return areaActualizadaDTO;
    }

    /**
     * Elimina (deshabilita) un área del sistema, marcándola como eliminada.
     *
     * @param id El ID del área a eliminar.
     * @throws LocationNotFoundException Si no se encuentra el área especificada.
     * @throws DeleteNotAllowedException Si la eliminación del área no está permitida.
     */
    @Override
    public void eliminarArea(Integer id)throws LocationNotFoundException, DeleteNotAllowedException{
        AreaPC areaPC = areaRepository.findById(id).orElse(null);
        if(areaPC == null){
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());        
        }

        if(areaPC.getDeleteFlag()== true){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DELETE AREA").toUpperCase());
        }
        areaPC.setDeleteFlag(true);
        areaRepository.save(areaPC);
    }
}
