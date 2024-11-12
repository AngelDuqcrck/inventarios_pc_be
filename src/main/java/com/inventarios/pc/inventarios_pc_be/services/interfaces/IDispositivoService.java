package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarEstadoDispositivoRequest;
import com.inventarios.pc.inventarios_pc_be.shared.requests.DispositivoRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.DispositivoResponse;

public interface IDispositivoService {

        public DispositivoRequest crearDispositivo(DispositivoRequest dispositivoRequest)
                        throws SelectNotAllowedException, TypeDeviceNotFoundException, MarcaNotFoundException,
                        StateNotFoundException, OwnerNotFoundException;

        public List<DispositivoPC> listarDispositivos();

        public List<DispositivoPC> listarDispTipoEstado(Integer tipoDispositivo, Integer estadoDispositivo);

        public DispositivoRequest actualizarDispositivo(Integer id, DispositivoRequest dispositivoRequest)
                        throws UpdateNotAllowedException, TypeDeviceNotFoundException, MarcaNotFoundException,
                        StateNotFoundException, DeviceNotFoundException, SelectNotAllowedException, OwnerNotFoundException;

        public DispositivoResponse listarDispositivoById(Integer id) throws DeviceNotFoundException;

        public void eliminarDispositivo(Integer id) throws DeviceNotFoundException, DeleteNotAllowedException;

        public void cambiarEstadoDispositivo(CambiarEstadoDispositivoRequest cambiarEstadoDispositivoRequest)
                throws DeviceNotFoundException, StateNotFoundException, ChangeNotAllowedException, ComputerNotFoundException, SelectNotAllowedException;
}
