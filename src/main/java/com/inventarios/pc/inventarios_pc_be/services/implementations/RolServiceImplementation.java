package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.services.interfaces.IRolService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.repositories.RolRepository;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.RolDTO;

@Service
public class RolServiceImplementation implements IRolService {
    
    @Autowired
    RolRepository rolRepository;

    @Override
    public RolDTO crearRol (RolDTO rolDTO){
        Rol rol = new Rol();
        BeanUtils.copyProperties(rolDTO, rol);
        Rol rolCreado = rolRepository.save(rol);
        RolDTO rolCreadoDTO  = new RolDTO();
        BeanUtils.copyProperties(rolCreado, rolCreadoDTO);
        return rolCreadoDTO;
    }

    @Override
    public List<Rol> listarRoles(){
        return (List<Rol>) rolRepository.findAll();
    }

    @Override
    public RolDTO actualizarRol(RolDTO rolDTO){
        Rol rol = rolRepository.findById(rolDTO.getId()).orElse(null);
        if(rol == null  ){
            throw new IllegalArgumentException("Rol no encontrado");
        }
        BeanUtils.copyProperties(rolDTO, rol);
        Rol rolActualizado = rolRepository.save(rol);
        RolDTO rolActualizadoDTO = new RolDTO();
        BeanUtils.copyProperties(rolActualizado, rolActualizadoDTO);
        return  rolActualizadoDTO;
    }

    @Override
    public void deshabilitarRol (Integer rolId){

    }

}
