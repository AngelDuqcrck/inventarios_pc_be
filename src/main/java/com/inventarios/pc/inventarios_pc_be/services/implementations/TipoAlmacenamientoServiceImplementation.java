package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.TipoAlmacenamiento;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DuplicateEntityException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeStorageNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoAlmacenamientoRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoAlmacenamientoService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoAlmacenamientoDTO;


@Service
public class TipoAlmacenamientoServiceImplementation implements ITipoAlmacenamientoService {
    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "no esta permitido %s ";

    @Autowired
    private TipoAlmacenamientoRepository tipoAlmacenamientoRepository;

    @Override
    public void crearTipoAlmacenamiento(TipoAlmacenamientoDTO tipoAlmacenamientoDTO)throws DuplicateEntityException{
        TipoAlmacenamiento tipoAlmacenamiento = new TipoAlmacenamiento();
        if(tipoAlmacenamientoRepository.existsByNombreIgnoreCase(tipoAlmacenamientoDTO.getNombre())){
            throw new DuplicateEntityException("Ya existe un tipo de almacenamiento con el nombre " + tipoAlmacenamientoDTO.getNombre());
        }
        BeanUtils.copyProperties(tipoAlmacenamientoDTO, tipoAlmacenamiento);
        tipoAlmacenamiento.setDeleteFlag(false);
        tipoAlmacenamientoRepository.save(tipoAlmacenamiento);
    }

    @Override
    public List<TipoAlmacenamiento> listarTiposAlmacenamiento(){
        return(List<TipoAlmacenamiento>) tipoAlmacenamientoRepository.findAll();
    }

    @Override
    public void actualizarTipoAlmacenamiento(Integer id, TipoAlmacenamientoDTO tipoAlmacenamientoDTO) throws DuplicateEntityException,TypeStorageNotFoundException, UpdateNotAllowedException{
        TipoAlmacenamiento tipoAlmacenamiento = tipoAlmacenamientoRepository.findById(id).orElse(null);

        if(tipoAlmacenamiento == null){
            throw new TypeStorageNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE ALMACENAMIENTO").toUpperCase());
        }

        if(tipoAlmacenamiento.getDeleteFlag() == true){
              throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR EL TIPO DE ALMACENAMIENTO "+tipoAlmacenamiento.getNombre()+" PORQUE SE ENCUENTRA INACTIVO").toUpperCase());
        }
        if(tipoAlmacenamientoRepository.existsByNombreIgnoreCaseAndIdNot(tipoAlmacenamientoDTO.getNombre(), id)){
            throw new DuplicateEntityException("Ya existe un tipo de almacenamiento con el nombre " + tipoAlmacenamientoDTO.getNombre());
        }
        BeanUtils.copyProperties(tipoAlmacenamientoDTO, tipoAlmacenamiento);
        tipoAlmacenamiento.setDeleteFlag(false);

        tipoAlmacenamientoRepository.save(tipoAlmacenamiento);

    }

    @Override
    public void eliminarTipoAlmacenamiento (Integer id) throws DeleteNotAllowedException, TypeStorageNotFoundException{
        TipoAlmacenamiento tipoAlmacenamiento = tipoAlmacenamientoRepository.findById(id).orElse(null);

        if(tipoAlmacenamiento == null){
            throw new TypeStorageNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE ALMACENAMIENTO").toUpperCase());
        }

        if(tipoAlmacenamiento.getDeleteFlag() == true){
              throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DESACTIVAR EL TIPO DE ALMACENAMIENTO "+tipoAlmacenamiento.getNombre()+" PORQUE YA SE ENCUENTRA INACTIVO").toUpperCase());
        }

        tipoAlmacenamiento.setDeleteFlag(true);
        tipoAlmacenamientoRepository.save(tipoAlmacenamiento);
    }

    @Override
    public void activarTipoAlmacenamiento(Integer id) throws TypeStorageNotFoundException, ActivateNotAllowedException{
        TipoAlmacenamiento tipoAlmacenamiento = tipoAlmacenamientoRepository.findById(id).orElse(null);

        if(tipoAlmacenamiento == null){
            throw new TypeStorageNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE ALMACENAMIENTO").toUpperCase());
        }

        if(tipoAlmacenamiento.getDeleteFlag() == false){
              throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVAR EL TIPO DE ALMACENAMIENTO "+tipoAlmacenamiento.getNombre()+" PORQUE YA SE ENCUENTRA ACTIVO").toUpperCase());
        }

        tipoAlmacenamiento.setDeleteFlag(false);
        tipoAlmacenamientoRepository.save(tipoAlmacenamiento);
    }

    public TipoAlmacenamientoDTO listarTipoAlmacenamientoById(Integer id) throws TypeStorageNotFoundException{
        TipoAlmacenamiento tipoAlmacenamiento = tipoAlmacenamientoRepository.findById(id).orElse(null);

        if(tipoAlmacenamiento == null){
            throw new TypeStorageNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE ALMACENAMIENTO").toUpperCase());
        }

        TipoAlmacenamientoDTO tipoAlmacenamientoDTO = new TipoAlmacenamientoDTO();
        BeanUtils.copyProperties(tipoAlmacenamiento, tipoAlmacenamientoDTO);
        return tipoAlmacenamientoDTO;
    }
}
