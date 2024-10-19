package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.responses.DispositivosXPcResponse;

public interface IHistorialComputadorService {

       public void vincularDispositivo(Integer computadorId, Integer dispositivoId)
                     throws ComputerNotFoundException, SelectNotAllowedException, DeviceNotFoundException;

       public void desvincularDispositivo(Integer computadorId, Integer dispositivoId)
                     throws ComputerNotFoundException, DeviceNotFoundException, SelectNotAllowedException;

       public void vincularSoftware(Integer computadorId, Integer softwareId)
                     throws ComputerNotFoundException, SoftwareNotFoundException, SelectNotAllowedException;

       public void desvincularSoftware(Integer computadorId, Integer softwareId)
                     throws ComputerNotFoundException, SoftwareNotFoundException, SelectNotAllowedException;

       public DispositivosXPcResponse listarDispositivosXPc(Integer computadorId) throws ComputerNotFoundException;
}
