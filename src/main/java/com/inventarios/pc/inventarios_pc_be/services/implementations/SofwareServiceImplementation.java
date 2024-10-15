package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SoftwareResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;
import com.inventarios.pc.inventarios_pc_be.entities.TipoSoftware;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeSoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
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


     /**
     * Crea un nuevo software en el sistema.
     * 
     * @param softwarePcDTO Objeto DTO que contiene los detalles del software a crear.
     * @return El DTO del software creado.
     * @throws TypeSoftwareNotFoundException Si el tipo de software especificado no existe.
     */
    @Override
    public SoftwarePcDTO crearSoftware(SoftwarePcDTO  softwarePcDTO)throws TypeSoftwareNotFoundException, SelectNotAllowedException{
        SoftwarePC softwarePC = new SoftwarePC();

        TipoSoftware tipoSoftware = tipoSoftwareRepository.findById(softwarePcDTO.getTipoSoftware()).orElse(null);

        if(tipoSoftware == null){
            throw new TypeSoftwareNotFoundException(String.format(IS_NOT_FOUND, "TYPE SOFTWARE").toUpperCase());        
        }
        if(tipoSoftware.getDeleteFlag() == true){
             throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECT TYPE SOFTWARE").toUpperCase());
        }

        BeanUtils.copyProperties(softwarePcDTO, softwarePC);
        softwarePC.setTipoSoftware(tipoSoftware);
        softwarePC.setDeleteFlag(false);
        SoftwarePC softwareCreado = softwarePcRepository.save(softwarePC);
        SoftwarePcDTO softwareCreadoDto = new SoftwarePcDTO();
        BeanUtils.copyProperties(softwareCreado, softwareCreadoDto);

        return softwareCreadoDto;
    }

    /**
     * Retorna una lista de todos los software registrados en el sistema.
     * 
     * @return Lista de entidades SoftwarePC.
     */
    @Override
    public List<SoftwarePC> listarSoftwares() {
        return (List<SoftwarePC>) softwarePcRepository.findAll();
    }

    /**
     * Obtiene los detalles de un software por su ID.
     * 
     * @param id ID del software a buscar.
     * @return Objeto SoftwareResponse con los detalles del software.
     * @throws SoftwareNotFoundException Si el software con el ID especificado no existe.
     */
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

     /**
     * Actualiza los detalles de un software existente.
     * 
     * @param id ID del software a actualizar.
     * @param softwarePcDTO DTO con los nuevos detalles del software.
     * @return DTO del software actualizado.
     * @throws TypeSoftwareNotFoundException Si el tipo de software especificado no existe.
     * @throws SoftwareNotFoundException Si el software con el ID especificado no existe.
     */
    @Override
    public SoftwarePcDTO actualizarSoftware(Integer id, SoftwarePcDTO softwarePcDTO) throws  UpdateNotAllowedException , SelectNotAllowedException ,TypeSoftwareNotFoundException, SoftwareNotFoundException{
        SoftwarePC softwarePC = softwarePcRepository.findById(id).orElse(null);

        if(softwarePC == null){
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "SOFTWARE").toUpperCase());        
        }

        if(softwarePC.getDeleteFlag()== true){
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "UPDATE SOFTWARE").toUpperCase());
        }

        BeanUtils.copyProperties(softwarePcDTO, softwarePC);
        softwarePC.setDeleteFlag(false);
        if(softwarePcDTO.getTipoSoftware()!= null){
            TipoSoftware tipoSoftware = tipoSoftwareRepository.findById(softwarePcDTO.getTipoSoftware()).orElse(null);

            if(tipoSoftware == null){
                throw new TypeSoftwareNotFoundException(String.format(IS_NOT_FOUND, "TYPE SOFTWARE").toUpperCase());        
            }

            if(tipoSoftware.getDeleteFlag() == true){
                throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECT TYPE SOFTWARE").toUpperCase());
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

     /**
     * Elimina (o marca como eliminado) un software por su ID.
     * 
     * @param id ID del software a eliminar.
     * @throws SoftwareNotFoundException Si el software con el ID especificado no existe.
     * @throws DeleteNotAllowedException Si el software no se puede eliminar.
     */
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

    @Override
    public void activarSoftware(Integer id) throws SoftwareNotFoundException, ActivateNotAllowedException {
        SoftwarePC softwarePC = softwarePcRepository.findById(id).orElse(null);

        if(softwarePC == null){
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "SOFTWARE").toUpperCase());
        }

        if(softwarePC.getDeleteFlag() == false){
            throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVATE SOFTWARE").toUpperCase());
        }
        softwarePC.setDeleteFlag(false);
        softwarePcRepository.save(softwarePC);
    }
}
