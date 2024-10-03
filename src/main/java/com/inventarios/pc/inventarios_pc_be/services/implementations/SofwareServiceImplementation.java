package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.shared.responses.SoftwareResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;
import com.inventarios.pc.inventarios_pc_be.entities.TipoSoftware;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeSoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.SoftwarePcRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoSoftwareRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ISoftwarePcService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SoftwarePcDTO;
import org.springframework.stereotype.Service;

@Service
public class SofwareServiceImplementation implements ISoftwarePcService{

    public static final String IS_ALREADY_USE = "The %s is already use";
    public static final String IS_NOT_FOUND = "The %s is not found";
    public static final String IS_NOT_ALLOWED = "The %s is not allowed";
    public static final String IS_NOT_VALID = "The %s is not valid";
    public static final String ARE_NOT_EQUALS = "The %s are not equals";
    public static final String IS_NOT_CORRECT = "The %s is not correct";

    @Autowired
    private SoftwarePcRepository softwarePcRepository;

    @Autowired
    private TipoSoftwareRepository tipoSoftwareRepository;


    @Override
    public SoftwarePcDTO crearSoftware(SoftwarePcDTO  softwarePcDTO)throws TypeSoftwareNotFoundException{
        SoftwarePC softwarePC = new SoftwarePC();

        TipoSoftware tipoSoftware = tipoSoftwareRepository.findById(softwarePcDTO.getTipoSoftware().getId()).orElse(null);

        if(tipoSoftware == null){
            throw new TypeSoftwareNotFoundException(String.format(IS_NOT_FOUND, "TYPE SOFTWARE").toUpperCase());        
        }

        BeanUtils.copyProperties(softwarePcDTO, softwarePC);
        softwarePC.setTipoSoftware(tipoSoftware);
        softwarePC.setDeleteFlag(false);
        SoftwarePC softwareCreado = softwarePcRepository.save(softwarePC);
        SoftwarePcDTO softwareCreadoDto = new SoftwarePcDTO();
        BeanUtils.copyProperties(softwareCreado, softwareCreadoDto);

        return softwareCreadoDto;
    }

    @Override
    public List<SoftwarePC> listarSoftwares() {
        return (List<SoftwarePC>) softwarePcRepository.findAll();
    }

    @Override
    public SoftwareResponse listarSoftwareById(Integer id) throws SoftwareNotFoundException{
        SoftwarePC softwarePC = softwarePcRepository.findById(id).orElse(null);

        if(softwarePC == null){
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "SOFTWARE").toUpperCase());        
        }
        SoftwareResponse softwareResponse = new SoftwareResponse();
        BeanUtils.copyProperties(softwarePC, softwareResponse);
        softwareResponse.setTipoSoftware(softwarePC.getTipoSoftware().getNombre());
        return  softwareResponse;
    }

    @Override
    public SoftwarePcDTO actualizarSoftware(Integer id, SoftwarePcDTO softwarePcDTO) throws TypeSoftwareNotFoundException, SoftwareNotFoundException{
        SoftwarePC softwarePC = softwarePcRepository.findById(id).orElse(null);

        if(softwarePC == null){
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "SOFTWARE").toUpperCase());        
        }

        BeanUtils.copyProperties(softwarePcDTO, softwarePC);
        softwarePC.setDeleteFlag(false);
        if(softwarePcDTO.getTipoSoftware()!= null){
            TipoSoftware tipoSoftware = tipoSoftwareRepository.findById(softwarePcDTO.getTipoSoftware().getId()).orElse(null);

            if(tipoSoftware == null){
                throw new TypeSoftwareNotFoundException(String.format(IS_NOT_FOUND, "TYPE SOFTWARE").toUpperCase());        
            }
            softwarePC.setTipoSoftware(tipoSoftware);
        }else{
            softwarePC.setTipoSoftware(softwarePC.getTipoSoftware());
        }
        
        SoftwarePC softwareActualizado = softwarePcRepository.save(softwarePC);
        SoftwarePcDTO softwareActualizadoDTO = new SoftwarePcDTO();
        BeanUtils.copyProperties(softwareActualizado, softwareActualizadoDTO);
        return softwareActualizadoDTO;
    }

    @Override
    public void eliminarSoftware(Integer id) throws SoftwareNotFoundException, DeleteNotAllowedException{
        SoftwarePC softwarePC = softwarePcRepository.findById(id).orElse(null);

        if(softwarePC == null){
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "SOFTWARE").toUpperCase());        
        }
        
        if(softwarePC.getDeleteFlag() == true){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DELETE SOFTWARE").toUpperCase());
        }
        softwarePC.setDeleteFlag(true);
        softwarePcRepository.save(softwarePC);
    }

}
