package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.*;

import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;
import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoDispositivoDTO;

public interface ITipoDispositivoService {
    
    public TipoDispositivoDTO creaDispositivoDTO(TipoDispositivoDTO tipoDispositivoDTO) throws DuplicateEntityException;

    public List<TipoDispositivo> listarTipos();
    
    public TipoDispositivoDTO actualizarTipoDispositivo(Integer id, TipoDispositivoDTO tipoDispositivoDTO)
            throws TypeDeviceNotFoundException, UpdateNotAllowedException, DuplicateEntityException;

    public void eliminarTipoDispositivo(Integer id) throws TypeDeviceNotFoundException, DeleteNotAllowedException;

    public void activarTipoDispositivo(Integer id) throws TypeDeviceNotFoundException, ActivateNotAllowedException;
}
