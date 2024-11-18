package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.*;

import com.inventarios.pc.inventarios_pc_be.entities.TipoPC;
import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoComputadorDTO;

public interface ITipoPcService {
    
    public TipoComputadorDTO crearTipoPC(TipoComputadorDTO tipoComputadorDTO) throws DuplicateEntityException;

    public List<TipoPC> listarTiposPc();

     public TipoComputadorDTO actualizarTipoPC(Integer id, TipoComputadorDTO tipoComputadorDTO)
            throws TypePcNotFoundException, UpdateNotAllowedException, DuplicateEntityException;

    public void eliminarTipoPc (Integer id) throws TypePcNotFoundException, DeleteNotAllowedException;

    public void activarTipoPc (Integer id) throws TypePcNotFoundException, ActivateNotAllowedException;
}
