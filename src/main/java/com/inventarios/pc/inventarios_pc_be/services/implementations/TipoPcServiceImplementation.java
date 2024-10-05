package com.inventarios.pc.inventarios_pc_be.services.implementations;
import java.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.TipoPC;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypePcNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoPcRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoPcService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoComputadorDTO;

@Service
public class TipoPcServiceImplementation implements ITipoPcService {
    
    public static final String IS_ALREADY_USE = "The %s is already use";
    public static final String IS_NOT_FOUND = "The %s is not found";
    public static final String IS_NOT_ALLOWED = "The %s is not allowed";
    
    @Autowired
    TipoPcRepository tipoPcRepository;

    @Override
    public TipoComputadorDTO crearTipoPC(TipoComputadorDTO tipoComputadorDTO){
        TipoPC tipoPC = new TipoPC();
        BeanUtils.copyProperties(tipoComputadorDTO, tipoPC);
        tipoPC.setDeleteFlag(false);
        TipoPC tipoPcCreado = tipoPcRepository.save(tipoPC);
        TipoComputadorDTO tipoPcCreadoDTO = new TipoComputadorDTO();
        BeanUtils.copyProperties(tipoPcCreado, tipoPcCreadoDTO);
        return tipoPcCreadoDTO;

    }
    @Override
    public List<TipoPC> listarTiposPc(){
        return (List<TipoPC>) tipoPcRepository.findAll();
    }

    @Override
    public TipoComputadorDTO actualizarTipoPC(Integer id, TipoComputadorDTO tipoComputadorDTO)throws TypePcNotFoundException{
        TipoPC tipoPC = tipoPcRepository.findById(id).orElse(null);
        if(tipoPC == null){
            throw new TypePcNotFoundException(String.format(IS_NOT_FOUND, "TYPE PC").toUpperCase());
        }
        BeanUtils.copyProperties(tipoComputadorDTO, tipoPC);
        tipoPC.setDeleteFlag(false);
        TipoPC tipoPcActualizado = tipoPcRepository.save(tipoPC);
        TipoComputadorDTO tipoPcActualizadoDTO = new TipoComputadorDTO();
        BeanUtils.copyProperties(tipoPcActualizado, tipoPcActualizadoDTO);
        return tipoPcActualizadoDTO;
    }

    @Override
    public void eliminarTipoPc (Integer id) throws TypePcNotFoundException, DeleteNotAllowedException{
        TipoPC tipoPC = tipoPcRepository.findById(id).orElse(null);
        if(tipoPC == null){
            throw new TypePcNotFoundException(String.format(IS_NOT_FOUND, "TYPE PC").toUpperCase());
        }
        if(tipoPC.getDeleteFlag() == true){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DELETE TYPE PC").toUpperCase());        
        }

        tipoPC.setDeleteFlag(true);
        tipoPcRepository.save(tipoPC);
    }
}
