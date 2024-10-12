package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.*;

import com.inventarios.pc.inventarios_pc_be.entities.TipoPC;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypePcNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoComputadorDTO;

public interface ITipoPcService {
    
    public TipoComputadorDTO crearTipoPC(TipoComputadorDTO tipoComputadorDTO);

    public List<TipoPC> listarTiposPc();

    public TipoComputadorDTO actualizarTipoPC(Integer id, TipoComputadorDTO tipoComputadorDTO)throws TypePcNotFoundException;

    public void eliminarTipoPc (Integer id) throws TypePcNotFoundException, DeleteNotAllowedException;

    public void activarTipoPc (Integer id) throws TypePcNotFoundException, ActivateNotAllowedException;
}
