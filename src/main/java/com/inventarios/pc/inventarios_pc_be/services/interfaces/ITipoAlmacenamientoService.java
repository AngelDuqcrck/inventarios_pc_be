package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import com.inventarios.pc.inventarios_pc_be.entities.TipoAlmacenamiento;
import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoAlmacenamientoDTO;

import java.util.List;

public interface ITipoAlmacenamientoService {
    public void crearTipoAlmacenamiento(TipoAlmacenamientoDTO tipoAlmacenamientoDTO) throws DuplicateEntityException;

    public List<TipoAlmacenamiento> listarTiposAlmacenamiento();

    public void actualizarTipoAlmacenamiento(Integer id, TipoAlmacenamientoDTO tipoAlmacenamientoDTO) throws DuplicateEntityException,TypeStorageNotFoundException, UpdateNotAllowedException;

    public void eliminarTipoAlmacenamiento (Integer id) throws DeleteNotAllowedException, TypeStorageNotFoundException;

    public void activarTipoAlmacenamiento(Integer id) throws TypeStorageNotFoundException, ActivateNotAllowedException;

    public TipoAlmacenamientoDTO listarTipoAlmacenamientoById(Integer id) throws TypeStorageNotFoundException;
}
