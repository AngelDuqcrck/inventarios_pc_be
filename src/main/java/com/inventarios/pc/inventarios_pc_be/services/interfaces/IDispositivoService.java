package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MarcaNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeDeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.requests.DispositivoRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.DispositivoResponse;

public interface IDispositivoService {
    
    public DispositivoRequest crearDispositivo(DispositivoRequest dispositivoRequest)throws TypeDeviceNotFoundException, MarcaNotFoundException, StateNotFoundException;

    public List<DispositivoPC> listarDispositivos();

    public DispositivoRequest actualizarDispositivo (Integer id, DispositivoRequest dispositivoRequest)throws TypeDeviceNotFoundException, MarcaNotFoundException, StateNotFoundException, DeviceNotFoundException;

    public DispositivoResponse listarDispositivoById(Integer id)throws DeviceNotFoundException;

    public void eliminarDispositivo(Integer id)throws DeviceNotFoundException, DeleteNotAllowedException;
}
