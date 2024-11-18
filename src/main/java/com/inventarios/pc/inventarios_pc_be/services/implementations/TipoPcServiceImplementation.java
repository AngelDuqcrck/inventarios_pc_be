package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.*;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.TipoPC;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DuplicateEntityException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypePcNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoPcRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoPcService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoComputadorDTO;

@Service
public class TipoPcServiceImplementation implements ITipoPcService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";

    @Autowired
    TipoPcRepository tipoPcRepository;

    @Override
    public TipoComputadorDTO crearTipoPC(TipoComputadorDTO tipoComputadorDTO) throws DuplicateEntityException {
        TipoPC tipoPC = new TipoPC();
        if(tipoPcRepository.existsByNombreIgnoreCase(tipoComputadorDTO.getNombre())){
            throw new DuplicateEntityException("Ya existe un tipo de pc con el nombre " + tipoComputadorDTO.getNombre());
        }
        BeanUtils.copyProperties(tipoComputadorDTO, tipoPC);
        tipoPC.setDeleteFlag(false);
        TipoPC tipoPcCreado = tipoPcRepository.save(tipoPC);
        TipoComputadorDTO tipoPcCreadoDTO = new TipoComputadorDTO();
        BeanUtils.copyProperties(tipoPcCreado, tipoPcCreadoDTO);
        return tipoPcCreadoDTO;

    }

    @Override
    public List<TipoPC> listarTiposPc() {
        return (List<TipoPC>) tipoPcRepository.findAll();
    }

    @Override
    public TipoComputadorDTO actualizarTipoPC(Integer id, TipoComputadorDTO tipoComputadorDTO)
            throws TypePcNotFoundException, UpdateNotAllowedException, DuplicateEntityException {
        TipoPC tipoPC = tipoPcRepository.findById(id).orElse(null);
        if (tipoPC == null) {
            throw new TypePcNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE PC").toUpperCase());
        }

        if (tipoPC.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTE TIPO DE PC").toUpperCase());
        }
        if(tipoPcRepository.existsByNombreIgnoreCaseAndIdNot(tipoComputadorDTO.getNombre(), id)){
            throw new DuplicateEntityException("Ya existe un tipo de pc con el nombre " + tipoComputadorDTO.getNombre());
        }
        BeanUtils.copyProperties(tipoComputadorDTO, tipoPC);
        tipoPC.setDeleteFlag(false);
        TipoPC tipoPcActualizado = tipoPcRepository.save(tipoPC);
        TipoComputadorDTO tipoPcActualizadoDTO = new TipoComputadorDTO();
        BeanUtils.copyProperties(tipoPcActualizado, tipoPcActualizadoDTO);
        return tipoPcActualizadoDTO;
    }

    @Override
    public void eliminarTipoPc(Integer id) throws TypePcNotFoundException, DeleteNotAllowedException {
        TipoPC tipoPC = tipoPcRepository.findById(id).orElse(null);
        if (tipoPC == null) {
            throw new TypePcNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE PC").toUpperCase());
        }
        if (tipoPC.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR ESTE TIPO DE PC").toUpperCase());
        }

        tipoPC.setDeleteFlag(true);
        tipoPcRepository.save(tipoPC);
    }

    @Override
    public void activarTipoPc(Integer id) throws TypePcNotFoundException, ActivateNotAllowedException {
        TipoPC tipoPC = tipoPcRepository.findById(id).orElse(null);
        if (tipoPC == null) {
            throw new TypePcNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE PC").toUpperCase());
        }
        if (tipoPC.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVAR ESTE TIPO DE PC").toUpperCase());
        }

        tipoPC.setDeleteFlag(false);
        tipoPcRepository.save(tipoPC);
    }
}
