package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;

public interface IHistorialComputadorService {
    
     public void vincularDispositivo(Integer computadorId, Integer dispositivoId)
            throws ComputerNotFoundException, SelectNotAllowedException, DeviceNotFoundException;
}
