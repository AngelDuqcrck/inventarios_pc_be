package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import com.inventarios.pc.inventarios_pc_be.entities.TipoAlmacenamiento;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeStorageNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoAlmacenamientoDTO;

import java.util.List;

public interface ITipoAlmacenamientoService {
    public void crearTipoAlmacenamiento(TipoAlmacenamientoDTO tipoAlmacenamientoDTO);

    public List<TipoAlmacenamiento> listarTiposAlmacenamiento();

    public void actualizarTipoAlmacenamiento(Integer id, TipoAlmacenamientoDTO tipoAlmacenamientoDTO) throws TypeStorageNotFoundException, UpdateNotAllowedException;

    public void eliminarTipoAlmacenamiento (Integer id) throws DeleteNotAllowedException, TypeStorageNotFoundException;

    public void activarTipoAlmacenamiento(Integer id) throws TypeStorageNotFoundException, ActivateNotAllowedException;

    public TipoAlmacenamientoDTO listarTipoAlmacenamientoById(Integer id) throws TypeStorageNotFoundException;
}