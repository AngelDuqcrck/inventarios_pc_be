package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Propietario;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.OwnerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.PropietarioRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IPropietarioService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.PropietarioDTO;

@Service
public class PropietarioServiceImplementation implements IPropietarioService  {
    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "no esta permitido %s ";

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Override
    public void crearPropietario(PropietarioDTO propietarioDTO){
        Propietario propietario = new Propietario();
        BeanUtils.copyProperties(propietarioDTO, propietario);
        propietario.setDeleteFlag(false);
        propietarioRepository.save(propietario);

    }

    @Override
    public List<Propietario> listarPropietarios(){
        return (List<Propietario>) propietarioRepository.findAll();
    }

    @Override
    public PropietarioDTO listarPropietarioById(Integer id) throws OwnerNotFoundException{
        Propietario propietario = propietarioRepository.findById(id).orElse(null);

        if(propietario == null){
            throw new OwnerNotFoundException(String.format(IS_NOT_FOUND, "EL PROPIETARIO NO FUE ENCONTRADO").toUpperCase());
        }

        PropietarioDTO propietarioDTO = new PropietarioDTO();
        BeanUtils.copyProperties(propietario, propietarioDTO);
        return propietarioDTO;
    }

    @Override
    public void actualizarPropietario(Integer id, PropietarioDTO propietarioDTO)throws OwnerNotFoundException, UpdateNotAllowedException{
        Propietario propietario = propietarioRepository.findById(id).orElse(null);

        if(propietario == null){
            throw new OwnerNotFoundException(String.format(IS_NOT_FOUND, "EL PROPIETARIO NO FUE ENCONTRADO").toUpperCase());
        }

        if(propietario.getDeleteFlag() == true){
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR EL PROPIETARIO "+propietario.getNombre()+" PORQUE SE ENCUENTRA INACTIVO").toUpperCase());
        }

        BeanUtils.copyProperties(propietarioDTO, propietario);
        propietario.setDeleteFlag(false);
        propietarioRepository.save(propietario);
    }

    @Override
    public void eliminarPropietario(Integer id)throws DeleteNotAllowedException, OwnerNotFoundException{
        Propietario propietario = propietarioRepository.findById(id).orElse(null);

        if(propietario == null){
            throw new OwnerNotFoundException(String.format(IS_NOT_FOUND, "EL PROPIETARIO NO FUE ENCONTRADO").toUpperCase());
        }

        if(propietario.getDeleteFlag() == true){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DESACTIVAR EL PROPIETARIO "+propietario.getNombre()+" PORQUE SE ENCUENTRA INACTIVO").toUpperCase());
        }

        propietario.setDeleteFlag(true);
        propietarioRepository.save(propietario);
    }

    @Override
    public void activarPropietario(Integer id) throws ActivateNotAllowedException, OwnerNotFoundException{
        Propietario propietario = propietarioRepository.findById(id).orElse(null);

        if(propietario == null){
            throw new OwnerNotFoundException(String.format(IS_NOT_FOUND, "EL PROPIETARIO NO FUE ENCONTRADO").toUpperCase());
        }

        if(propietario.getDeleteFlag() == false){
            throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVAR EL PROPIETARIO "+propietario.getNombre()+" PORQUE YA SE ENCUENTRA ACTIVO").toUpperCase());
        }

        propietario.setDeleteFlag(false);
        propietarioRepository.save(propietario);
    }
}
