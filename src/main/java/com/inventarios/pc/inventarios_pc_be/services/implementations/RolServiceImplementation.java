package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.repositories.RolRepository;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.RolDTO;

@Service
public class RolServiceImplementation {
    
    @Autowired
    RolRepository rolRepository;

    public RolDTO crearRol (RolDTO rolDTO){
        return null;
    }

    public List<Rol> listarRoles(){
        return (List<Rol>) rolRepository.findAll();
    }
    
    public RolDTO actualizarRol(RolDTO rolDTO){
        return null;
    }

    public void deshabilitarRol (Integer rolId){

    }

}
