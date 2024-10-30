package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.Marca;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;
import com.inventarios.pc.inventarios_pc_be.exceptions.ChangeNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MarcaNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeDeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.MarcaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IDispositivoService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.DispositivoDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.DispositivoRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.DispositivoResponse;

@Service
public class DispositivoServiceImplementation implements IDispositivoService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";

    @Autowired
    private TipoDispositivoRepository tipoDispositivoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private EstadoDispositivoRepository estadoDispositivoRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Override
    public DispositivoRequest crearDispositivo(DispositivoRequest dispositivoRequest) throws SelectNotAllowedException,
            TypeDeviceNotFoundException, MarcaNotFoundException, StateNotFoundException {
        DispositivoPC dispositivoPC = new DispositivoPC();
        BeanUtils.copyProperties(dispositivoRequest, dispositivoPC);
        TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(dispositivoRequest.getTipoDispositivo())
                .orElse(null);
        if (tipoDispositivo == null) {
            throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE DISPOSITIVO").toUpperCase());
        }
        if (tipoDispositivo.getDeleteFlag() == true) {

            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE TIPO DE DISPOSITIVO").toUpperCase());

        }
        dispositivoPC.setTipoDispositivo(tipoDispositivo);

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("Disponible").orElse(null);
        if (estadoDispositivo == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DEL DISPOSITIVO").toUpperCase());
        }

        if (estadoDispositivo.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE ESTADO DE DISPOSITIVO").toUpperCase());
        }
        dispositivoPC.setEstadoDispositivo(estadoDispositivo);

        Marca marca = marcaRepository.findById(dispositivoRequest.getMarca()).orElse(null);
        if (marca == null) {
            throw new MarcaNotFoundException(String.format(IS_NOT_FOUND, "MARCA").toUpperCase());
        }
        if (marca.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "MARCA").toUpperCase());
        }
        dispositivoPC.setMarca(marca);

        DispositivoPC dispositivoCreado = dispositivoRepository.save(dispositivoPC);
        DispositivoRequest dispositivoCreadoRequest = new DispositivoRequest();
        BeanUtils.copyProperties(dispositivoCreado, dispositivoCreadoRequest);
        return dispositivoCreadoRequest;
    }

    @Override
    public List<DispositivoPC> listarDispositivos() {
        return (List<DispositivoPC>) dispositivoRepository.findAll();
    }

    @Override
    public List<DispositivoPC> listarDispTipoEstado(Integer tipoDispositivo, Integer estadoDispositivo) {
        TipoDispositivo tipoDispositivo2 = tipoDispositivoRepository.findById(tipoDispositivo).orElse(null);
        EstadoDispositivo estadoDispositivo2 = estadoDispositivoRepository.findById(estadoDispositivo).orElse(null);
        return dispositivoRepository.findByTipoDispositivoAndEstadoDispositivo(tipoDispositivo2, estadoDispositivo2);
    }

    @Override
    public DispositivoRequest actualizarDispositivo(Integer id, DispositivoRequest dispositivoRequest)
            throws UpdateNotAllowedException, TypeDeviceNotFoundException, MarcaNotFoundException,
            StateNotFoundException, DeviceNotFoundException, SelectNotAllowedException {
        DispositivoPC dispositivoPC = dispositivoRepository.findById(id).orElse(null);

        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());
        }

        if (dispositivoPC.getEstadoDispositivo().getNombre().equals("Baja")) {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTE DISPOSITIVO").toUpperCase());
        }

        if(dispositivoPC.getTipoDispositivo().getId() == 8){
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTE TIPO DE DISPOSITIVO").toUpperCase());
        }

        BeanUtils.copyProperties(dispositivoRequest, dispositivoPC);

        if (dispositivoRequest.getEstadoDispositivo() != null) {
            EstadoDispositivo estadoDispositivo = estadoDispositivoRepository
                    .findById(dispositivoRequest.getEstadoDispositivo()).orElse(null);
            if (estadoDispositivo == null) {
                throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DEL DISPOSITIVO").toUpperCase());
            }
            if (estadoDispositivo.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE ESTADO DEL DISPOSITIVO").toUpperCase());
            }
            dispositivoPC.setEstadoDispositivo(estadoDispositivo);

        } else {
            dispositivoPC.setEstadoDispositivo(dispositivoPC.getEstadoDispositivo());
        }

        if (dispositivoRequest.getMarca() != null) {
            Marca marca = marcaRepository.findById(dispositivoRequest.getMarca()).orElse(null);
            if (marca == null) {
                throw new MarcaNotFoundException(String.format(IS_NOT_FOUND, "MARCA").toUpperCase());
            }
            if (marca.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "MARCA").toUpperCase());
            }
            dispositivoPC.setMarca(marca);
        } else {
            dispositivoPC.setMarca(dispositivoPC.getMarca());
        }

        if (dispositivoRequest.getTipoDispositivo() != null) {
            TipoDispositivo tipoDispositivo = tipoDispositivoRepository
                    .findById(dispositivoRequest.getTipoDispositivo()).orElse(null);

            if (tipoDispositivo == null) {
                throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE DISPOSITIVO").toUpperCase());
            }
            if (tipoDispositivo.getDeleteFlag() == true) {

                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE TIPO DE DISPOSITIVO").toUpperCase());

            }
            dispositivoPC.setTipoDispositivo(tipoDispositivo);
        } else {
            dispositivoPC.setTipoDispositivo(dispositivoPC.getTipoDispositivo());
        }

        DispositivoPC dispositivoActualizado = dispositivoRepository.save(dispositivoPC);
        DispositivoRequest dispositivoActualizadoReq = new DispositivoRequest();
        BeanUtils.copyProperties(dispositivoActualizado, dispositivoActualizadoReq);

        return dispositivoActualizadoReq;
    }

    @Override
    public DispositivoResponse listarDispositivoById(Integer id) throws DeviceNotFoundException {
        DispositivoPC dispositivoPC = dispositivoRepository.findById(id).orElse(null);

        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());
        }

        DispositivoResponse dispositivoResponse = new DispositivoResponse();
        BeanUtils.copyProperties(dispositivoPC, dispositivoResponse);
        dispositivoResponse.setEstadoDispositivo(dispositivoPC.getEstadoDispositivo().getNombre());
        dispositivoResponse.setMarca(dispositivoPC.getMarca().getNombre());
        dispositivoResponse.setTipoDispositivo(dispositivoPC.getTipoDispositivo().getNombre());
        return dispositivoResponse;
    }

    @Override
    public void eliminarDispositivo(Integer id) throws DeviceNotFoundException, DeleteNotAllowedException {
        DispositivoPC dispositivoPC = dispositivoRepository.findById(id).orElse(null);

        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());
        }

        if(dispositivoPC.getTipoDispositivo().getId() == 8){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR ESTE TIPO DE DISPOSITIVO").toUpperCase());
        }

        if (dispositivoPC.getEstadoDispositivo().getNombre().equals("Baja")) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR ESTE DISPOSITIVO").toUpperCase());
        }

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("Baja").get();
        dispositivoPC.setEstadoDispositivo(estadoDispositivo);
        dispositivoRepository.save(dispositivoPC);
    }

    @Override
    public void cambiarEstadoDispositivo(Integer dispositivoId, Integer nuevoEstadoDispositivoId)
            throws DeviceNotFoundException, StateNotFoundException, ChangeNotAllowedException {
        DispositivoPC dispositivoPC = dispositivoRepository.findById(dispositivoId).orElse(null);

        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "DISPOSITIVO").toUpperCase());
        }

        EstadoDispositivo nuevoEstadoDispositivo = estadoDispositivoRepository.findById(nuevoEstadoDispositivoId)
                .orElse(null);
        if (nuevoEstadoDispositivo == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DEL DISPOSITIVO").toUpperCase());
        }

        String estadoActual = dispositivoPC.getEstadoDispositivo().getNombre();

        // Lógica de validación basada en las reglas de cambio de estado
        switch (nuevoEstadoDispositivoId) {
            case 1: // En uso
                if (!estadoActual.equals("Disponible") && !estadoActual.equals("En reparacion")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "CAMBIO DE ESTADO DEL DISPOSITIVO").toUpperCase());
                }
                break;

            case 3: // Averiado
                if (!estadoActual.equals("En uso")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "CAMBIO DE ESTADO DEL DISPOSITIVO").toUpperCase());
                }
                break;

            case 2: // En reparacion
                if (!estadoActual.equals("Averiado")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "CAMBIO DE ESTADO DEL DISPOSITIVO").toUpperCase());
                }
                break;

            case 4: // Disponible
                if (!estadoActual.equals("Disponible") && !estadoActual.equals("En reparacion") && !estadoActual.equals("En uso")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "CAMBIO DE ESTADO DEL DISPOSITIVOE").toUpperCase());
                }
                break;

            case 5: // Baja
                if (!estadoActual.equals("Averiado") && !estadoActual.equals("Disponible")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "CAMBIO DE ESTADO DEL DISPOSITIVO").toUpperCase());
                }
                break;

            default:
                throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DEL DISPOSITIVO").toUpperCase());

        }

        dispositivoPC.setEstadoDispositivo(nuevoEstadoDispositivo);
        dispositivoRepository.save(dispositivoPC);
    }


}
