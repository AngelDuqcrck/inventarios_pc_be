package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.controllers.NotificationController;
import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.Marca;
import com.inventarios.pc.inventarios_pc_be.entities.Propietario;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;
import com.inventarios.pc.inventarios_pc_be.exceptions.ChangeNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DuplicateEntityException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MarcaNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.OwnerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeDeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.MarcaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.PropietarioRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IDispositivoService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.DispositivoDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.CambiarEstadoDispositivoRequest;
import com.inventarios.pc.inventarios_pc_be.shared.requests.DispositivoRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.DispositivoResponse;

@Service
public class DispositivoServiceImplementation implements IDispositivoService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "no esta permitido %s ";
    public static final String IS_NOT_VALID = "no es valido %s";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";

    @Autowired
    private HistorialComputadorService historialComputadorService;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Autowired
    private TipoDispositivoRepository tipoDispositivoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private EstadoDispositivoRepository estadoDispositivoRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private NotificationController notificationController;

    @Override
    public DispositivoRequest crearDispositivo(DispositivoRequest dispositivoRequest) throws SelectNotAllowedException,
            TypeDeviceNotFoundException, MarcaNotFoundException, StateNotFoundException, OwnerNotFoundException {
        DispositivoPC dispositivoPC = new DispositivoPC();
        if(dispositivoRepository.existsByPlacaIgnoreCase(dispositivoRequest.getPlaca())){
            throw new DuplicateEntityException("Ya existe un dispositivo registrado con la placa "+dispositivoRequest.getPlaca());
        }
        BeanUtils.copyProperties(dispositivoRequest, dispositivoPC);

        TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(dispositivoRequest.getTipoDispositivo())
                .orElse(null);
        if (tipoDispositivo == null) {
            throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, " EL TIPO DE DISPOSITIVO").toUpperCase());
        }
        if (tipoDispositivo.getDeleteFlag() == true) {

            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR EL TIPO DE DISPOSITIVO "+tipoDispositivo.getNombre()+" PORQUE SE ENCUENTRA DESACTIVADO").toUpperCase());

        }
        dispositivoPC.setTipoDispositivo(tipoDispositivo);

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("Disponible").orElse(null);
        if (estadoDispositivo == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO DEL DISPOSITIVO").toUpperCase());
        }

        if (estadoDispositivo.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR EL ESTADO DE DISPOSITIVO "+dispositivoPC.getNombre()+" PORQUE SE ENCUENTRA DESACTIVADO").toUpperCase());
        }
        dispositivoPC.setEstadoDispositivo(estadoDispositivo);

        Marca marca = marcaRepository.findById(dispositivoRequest.getMarca()).orElse(null);
        if (marca == null) {
            throw new MarcaNotFoundException(String.format(IS_NOT_FOUND_F, "LA MARCA").toUpperCase());
        }
        if (marca.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR LA MARCA "+marca.getNombre()+" PORQUE ESTA DESACTIVADA").toUpperCase());
        }
        dispositivoPC.setMarca(marca);

        Propietario propietario = propietarioRepository.findById(dispositivoRequest.getPropietario()).orElse(null);
        if (propietario == null) {
            throw new OwnerNotFoundException(String.format(IS_NOT_FOUND, "EL PROPIETARIO").toUpperCase());
        }
        if (propietario.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR EL PROPIETARIO "+propietario.getNombre()+" PORQUE ESTA DESACTIVADA").toUpperCase());
        }
        dispositivoPC.setPropietario(propietario);
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
            StateNotFoundException, DeviceNotFoundException, SelectNotAllowedException, OwnerNotFoundException {
        DispositivoPC dispositivoPC = dispositivoRepository.findById(id).orElse(null);

        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "EL DISPOSITIVO").toUpperCase());
        }

        if (dispositivoPC.getEstadoDispositivo().getNombre().equals("Baja")) {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTE DISPOSITIVO PORQUE TIENE ESTADO DADO DE BAJA").toUpperCase());
        }

        if(dispositivoPC.getTipoDispositivo().getId() == 8){
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR UN DISPOSITIVO DE TIPO TORRE").toUpperCase());
        }

        if(dispositivoRepository.existsByPlacaIgnoreCaseAndIdNot(dispositivoRequest.getPlaca(), id)){
            throw new DuplicateEntityException("Ya existe un dispositivo registrado con la placa "+dispositivoRequest.getPlaca());
        }
        
        BeanUtils.copyProperties(dispositivoRequest, dispositivoPC);

        if (dispositivoRequest.getEstadoDispositivo() != null) {
            EstadoDispositivo estadoDispositivo = estadoDispositivoRepository
                    .findById(dispositivoRequest.getEstadoDispositivo()).orElse(null);
            if (estadoDispositivo == null) {
                throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO DEL DISPOSITIVO").toUpperCase());
            }
            if (estadoDispositivo.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR EL ESTADO DEL DISPOSITIVO "+estadoDispositivo.getNombre()+" PORQUE SE ENCUENTRA DESACTIVADO").toUpperCase());
            }
            dispositivoPC.setEstadoDispositivo(estadoDispositivo);

        } else {
            dispositivoPC.setEstadoDispositivo(dispositivoPC.getEstadoDispositivo());
        }

        if (dispositivoRequest.getMarca() != null) {
            Marca marca = marcaRepository.findById(dispositivoRequest.getMarca()).orElse(null);
            if (marca == null) {
                throw new MarcaNotFoundException(String.format(IS_NOT_FOUND_F, "LA MARCA").toUpperCase());
            }
            if (marca.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR LA MARCA "+marca.getNombre()+" PORQUE SE ENCUENTRA DESACTIVADA" ).toUpperCase());
            }
            dispositivoPC.setMarca(marca);
        } else {
            dispositivoPC.setMarca(dispositivoPC.getMarca());
        }

        if (dispositivoRequest.getTipoDispositivo() != null) {
            TipoDispositivo tipoDispositivo = tipoDispositivoRepository
                    .findById(dispositivoRequest.getTipoDispositivo()).orElse(null);

            if (tipoDispositivo == null) {
                throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE DISPOSITIVO").toUpperCase());
            }
            if (tipoDispositivo.getDeleteFlag() == true) {

                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR EL TIPO DE DISPOSITIVO "+tipoDispositivo.getNombre()+" PORQUE SE ENCUENTRA DESACTIVADO").toUpperCase());

            }
            dispositivoPC.setTipoDispositivo(tipoDispositivo);
        } else {
            dispositivoPC.setTipoDispositivo(dispositivoPC.getTipoDispositivo());
        }

        if(dispositivoRequest.getPropietario() != null){
            Propietario propietario = propietarioRepository.findById(dispositivoRequest.getPropietario()).orElse(null);
            if (propietario == null) {
                throw new OwnerNotFoundException(String.format(IS_NOT_FOUND, "EL PROPIETARIO").toUpperCase());
            }
            if (propietario.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR EL PROPIETARIO "+propietario.getNombre()+" PORQUE ESTA DESACTIVADA").toUpperCase());
            }
            dispositivoPC.setPropietario(propietario);
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
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "EL DISPOSITIVO").toUpperCase());
        }

        DispositivoResponse dispositivoResponse = new DispositivoResponse();
        BeanUtils.copyProperties(dispositivoPC, dispositivoResponse);
        dispositivoResponse.setEstadoDispositivo(dispositivoPC.getEstadoDispositivo().getNombre());
        dispositivoResponse.setMarca(dispositivoPC.getMarca().getNombre());
        dispositivoResponse.setTipoDispositivo(dispositivoPC.getTipoDispositivo().getNombre());
        dispositivoResponse.setPropietario(dispositivoPC.getPropietario().getNombre());
        return dispositivoResponse;
    }

    @Override
    public void eliminarDispositivo(Integer id) throws DeviceNotFoundException, DeleteNotAllowedException {
        DispositivoPC dispositivoPC = dispositivoRepository.findById(id).orElse(null);

        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "EL DISPOSITIVO").toUpperCase());
        }

        if(dispositivoPC.getTipoDispositivo().getId() == 8){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR ESTE TIPO DE DISPOSITIVO PORQUE ES UNA TORRE").toUpperCase());
        }

        if (dispositivoPC.getEstadoDispositivo().getNombre().equals("Baja")) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR ESTE DISPOSITIVO PORQUE YA FUE DADO DE BAJA").toUpperCase());
        }

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("Baja").get();
        dispositivoPC.setEstadoDispositivo(estadoDispositivo);
        dispositivoRepository.save(dispositivoPC);
    }

    @Override
    public void cambiarEstadoDispositivo(CambiarEstadoDispositivoRequest cambiarEstadoDispositivoRequest)
            throws DeviceNotFoundException, StateNotFoundException, ChangeNotAllowedException, ComputerNotFoundException, SelectNotAllowedException {
        DispositivoPC dispositivoPC = dispositivoRepository.findById(cambiarEstadoDispositivoRequest.getDispositivoId()).orElse(null);

        if (dispositivoPC == null) {
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "EL DISPOSITIVO").toUpperCase());
        }

        EstadoDispositivo nuevoEstadoDispositivo = estadoDispositivoRepository.findById(cambiarEstadoDispositivoRequest.getNuevoEstadoDispositivoId())
                .orElse(null);
        if (nuevoEstadoDispositivo == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO DEL DISPOSITIVO").toUpperCase());
        }

        String estadoActual = dispositivoPC.getEstadoDispositivo().getNombre();

        // Lógica de validación basada en las reglas de cambio de estado
        switch (cambiarEstadoDispositivoRequest.getNuevoEstadoDispositivoId()) {
            case 1: // En uso
                if (!estadoActual.equals("Disponible") && !estadoActual.equals("En reparacion")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "EL ESTADO EN USO PORQUE SU ESTADO ACTUAL ES DIFERENTE A DISPONIBLE O EN REPARACIÓN")
                                    .toUpperCase());

                }

                historialComputadorService.vincularDispositivo(cambiarEstadoDispositivoRequest.getComputadorId(), cambiarEstadoDispositivoRequest.getDispositivoId());
                break;

            case 3: // Averiado
                if (!estadoActual.equals("En uso")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "EL ESTADO AVERIADO PORQUE SU ESTADO ACTUAL ES DIFERENTE A EN USO").toUpperCase());
                }

                historialComputadorService.desvincularDispositivo(cambiarEstadoDispositivoRequest.getComputadorId(), cambiarEstadoDispositivoRequest.getDispositivoId(), cambiarEstadoDispositivoRequest.getJustificacion());
                break;

            case 2: // En reparacion
                if (!estadoActual.equals("Averiado")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "EL ESTADO EN REPARACIÓN PORQUE SU ESTADO ACTUAL ES DIFERENTE A AVERIADO")
                                    .toUpperCase());
                }
                break;

            case 4: // Disponible
                if (!estadoActual.equals("Disponible") && !estadoActual.equals("En reparacion") && !estadoActual.equals("En uso")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "EL ESTADO DISPONIBLE PORQUE SU ESTADO ACTUAL ES DIFERENTE A EN REPARACION O EN USO")
                                    .toUpperCase());
                }

                if(estadoActual.equals("En uso")){
                    historialComputadorService.desvincularDispositivo(cambiarEstadoDispositivoRequest.getComputadorId(), cambiarEstadoDispositivoRequest.getDispositivoId(), cambiarEstadoDispositivoRequest.getJustificacion());
                }
                
                break;

            case 5: // Baja
                if (!estadoActual.equals("Averiado") && !estadoActual.equals("Disponible")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "EL ESTADO DADO DE BAJA PORQUE SU ESTADO ACTUAL ES DIFERENTE A AVERIADO O DISPONIBLE")
                                    .toUpperCase());
                
                }
                break;

            default:
                throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO DEL DISPOSITIVO").toUpperCase());

        }

        dispositivoPC.setEstadoDispositivo(nuevoEstadoDispositivo);
        dispositivoRepository.save(dispositivoPC);
        notificationController.notifyStatusUpdate("DISPOSITIVO", dispositivoPC.getId(), dispositivoPC.getEstadoDispositivo().getNombre(), null);
    }


}
