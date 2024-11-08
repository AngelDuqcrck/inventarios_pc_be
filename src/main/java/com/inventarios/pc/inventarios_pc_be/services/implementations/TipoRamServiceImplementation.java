package com.inventarios.pc.inventarios_pc_be.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.repositories.TipoRamRepository;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoRamDTO;

@Service
public class TipoRamServiceImplementation {
    
    @Autowired
    private TipoRamRepository tipoRamRepository;


    public void crearTipoRam(TipoRamDTO tipoRamDTO){
        
    }
}
