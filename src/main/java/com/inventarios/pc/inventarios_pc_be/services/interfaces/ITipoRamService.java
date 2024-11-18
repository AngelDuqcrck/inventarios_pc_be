package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import com.inventarios.pc.inventarios_pc_be.entities.TipoRam;
import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoRamDTO;

import java.util.List;

public interface ITipoRamService {
    public void crearTipoRam(TipoRamDTO tipoRamDTO) throws DuplicateEntityException;

    public List<TipoRam> listarTiposRam();

    public void actualizarTipoRam(Integer id, TipoRamDTO tipoRamDTO)throws DuplicateEntityException,TypeRamNotFoundException, UpdateNotAllowedException;

    public void eliminarTipoRam (Integer id) throws DeleteNotAllowedException, TypeRamNotFoundException;

    public void activarTipoRam (Integer id) throws ActivateNotAllowedException, TypeRamNotFoundException;

    public TipoRamDTO listarTipoRamById (Integer id)throws TypeRamNotFoundException;

}
