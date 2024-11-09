package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import com.inventarios.pc.inventarios_pc_be.entities.TipoRam;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeRamNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoRamDTO;

import java.util.List;

public interface ITipoRamService {
    public void crearTipoRam(TipoRamDTO tipoRamDTO);

    public List<TipoRam> listarTiposRam();

    public void actualizarTipoRam(Integer id, TipoRamDTO tipoRamDTO)throws TypeRamNotFoundException, UpdateNotAllowedException;

    public void eliminarTipoRam (Integer id) throws DeleteNotAllowedException, TypeRamNotFoundException;

    public void activarTipoRam (Integer id) throws ActivateNotAllowedException, TypeRamNotFoundException;

    public TipoRamDTO listarTipoRamById (Integer id)throws TypeRamNotFoundException;

}
