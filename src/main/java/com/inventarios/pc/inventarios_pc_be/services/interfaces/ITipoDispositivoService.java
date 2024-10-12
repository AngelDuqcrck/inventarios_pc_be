package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.*;

import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeDeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoDispositivoDTO;

public interface ITipoDispositivoService {
    
    public TipoDispositivoDTO creaDispositivoDTO(TipoDispositivoDTO tipoDispositivoDTO);

    public List<TipoDispositivo> listarTipos();
    
    public TipoDispositivoDTO actualizarTipoDispositivo(Integer id, TipoDispositivoDTO tipoDispositivoDTO)throws TypeDeviceNotFoundException;

    public void eliminarTipoDispositivo(Integer id) throws TypeDeviceNotFoundException, DeleteNotAllowedException;

    public void activarTipoDispositivo(Integer id) throws TypeDeviceNotFoundException, ActivateNotAllowedException;
}
