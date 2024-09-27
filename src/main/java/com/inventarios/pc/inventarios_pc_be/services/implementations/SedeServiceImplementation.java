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
public class SedeServiceImplementation implements ISedeService{
    
    public static final String IS_ALREADY_USE = "The %s is already use";
    public static final String IS_NOT_FOUND = "The %s is not found";
    public static final String IS_NOT_ALLOWED = "The %s is not allowed";

    @Autowired
    private SedeRepository sedeRepository;

    @Override
    public SedeDTO crearSede(SedeDTO sedeDTO){
        SedePC sedePC = new SedePC();
        BeanUtils.copyProperties(sedeDTO, sedePC);
        sedePC.setDeleteFlag(false);
        SedePC sedeCreada = sedeRepository.save(sedePC);
        SedeDTO sedeCreadaDto = new SedeDTO();
        BeanUtils.copyProperties(sedeCreada, sedeCreadaDto);
        return sedeCreadaDto;
        
    }

    @Override
    public List<SedePC> listarSedes(){
        return (List<SedePC>) sedeRepository.findAll();
    }

    @Override
    public SedeDTO actualizarSede(Integer id, SedeDTO sedeDTO) throws LocationNotFoundException{
        SedePC sedePC = sedeRepository.findById(id).orElse(null);

        if(sedePC == null){
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "SEDE").toUpperCase());
        }
        BeanUtils.copyProperties(sedeDTO, sedePC);
        SedePC sedeActualizada = sedeRepository.save(sedePC);
        SedeDTO sedeActualizadaDTO = new SedeDTO();
        BeanUtils.copyProperties(sedeActualizada, sedeActualizadaDTO);
        return null;
    }


    @Override
    public void eliminarSede(Integer id) throws LocationNotFoundException, DeleteNotAllowedException{
        SedePC sedePC = sedeRepository.findById(id).orElse(null);

        if(sedePC == null){
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "SEDE").toUpperCase());
        }

        if(sedePC.getDeleteFlag()== true){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DELETE SEDE").toUpperCase());
        }
        sedePC.setDeleteFlag(true);
        sedeRepository.save(sedePC);
    }
}
