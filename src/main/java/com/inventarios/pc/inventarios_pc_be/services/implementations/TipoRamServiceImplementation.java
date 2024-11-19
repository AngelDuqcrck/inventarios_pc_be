package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.TipoRam;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DuplicateEntityException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeRamNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoRamRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoRamService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoRamDTO;

@Service
public class TipoRamServiceImplementation implements ITipoRamService{
    
    @Autowired
    private TipoRamRepository tipoRamRepository;
    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "no esta permitido %s ";

    @Override
    public void crearTipoRam(TipoRamDTO tipoRamDTO)throws DuplicateEntityException{
        TipoRam tipoRam = new TipoRam();
        if(tipoRamRepository.existsByNombreIgnoreCase(tipoRamDTO.getNombre())){
            throw new DuplicateEntityException("Ya existe un tipo de ram con el nombre " + tipoRamDTO.getNombre());
        }
        BeanUtils.copyProperties(tipoRamDTO, tipoRam);
        tipoRam.setDeleteFlag(false);
        tipoRamRepository.save(tipoRam);
    }

    @Override
    public List<TipoRam> listarTiposRam(){
        return (List<TipoRam>) tipoRamRepository.findAll();
    }

    @Override
    public TipoRamDTO listarTipoRamById (Integer id)throws TypeRamNotFoundException{
        TipoRam tipoRam = tipoRamRepository.findById(id).orElse(null);

        if(tipoRam == null){
            throw new TypeRamNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE MEMORIA RAM").toUpperCase());
        }

        TipoRamDTO tipoRamDTO = new TipoRamDTO();
        BeanUtils.copyProperties(tipoRam, tipoRamDTO);
        return tipoRamDTO;

    }

    @Override
    public void actualizarTipoRam(Integer id, TipoRamDTO tipoRamDTO)throws TypeRamNotFoundException, UpdateNotAllowedException, DuplicateEntityException{
        TipoRam tipoRam = tipoRamRepository.findById(id).orElse(null);

        if(tipoRam == null){
            throw new TypeRamNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE MEMORIA RAM").toUpperCase());
        }

        if(tipoRam.getDeleteFlag() == true){
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR EL TIPO DE MEMORIA RAM "+tipoRam.getNombre()+" PORQUE SE ENCUENTRA INACTIVA").toUpperCase());
        }
        if(tipoRamRepository.existsByNombreIgnoreCaseAndIdNot(tipoRamDTO.getNombre(), id)){
            throw new DuplicateEntityException("Ya existe un tipo de ram con el nombre " + tipoRamDTO.getNombre());
        }

        BeanUtils.copyProperties(tipoRamDTO, tipoRam);
        tipoRam.setDeleteFlag(false);
        tipoRamRepository.save(tipoRam);
    }

    @Override
    public void eliminarTipoRam (Integer id) throws DeleteNotAllowedException, TypeRamNotFoundException{
        TipoRam tipoRam = tipoRamRepository.findById(id).orElse(null);

        if(tipoRam == null){
            throw new TypeRamNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE MEMORIA RAM").toUpperCase());
        }

        if(tipoRam.getDeleteFlag() == true){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DESACTIVAR EL TIPO DE MEMORIA RAM "+tipoRam.getNombre()+" PORQUE YA SE ENCUENTRA INACTIVA").toUpperCase());
        }

        tipoRam.setDeleteFlag(true);
        tipoRamRepository.save(tipoRam);
    }

    @Override
    public void activarTipoRam (Integer id) throws ActivateNotAllowedException, TypeRamNotFoundException{
        TipoRam tipoRam = tipoRamRepository.findById(id).orElse(null);

        if(tipoRam == null){
            throw new TypeRamNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE MEMORIA RAM").toUpperCase());
        }

        if(tipoRam.getDeleteFlag() == false){
            throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVAR EL TIPO DE MEMORIA RAM "+tipoRam.getNombre()+" PORQUE YA  SE ENCUENTRA ACTIVA").toUpperCase());
        }

        tipoRam.setDeleteFlag(false);
        tipoRamRepository.save(tipoRam);
    }
}
