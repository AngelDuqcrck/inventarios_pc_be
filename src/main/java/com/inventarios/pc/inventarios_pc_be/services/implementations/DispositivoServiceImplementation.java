package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.Marca;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MarcaNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeDeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.MarcaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IDispositivoService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.DispositivoDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.DispositivoRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.DispositivoResponse;

@Service
public class DispositivoServiceImplementation implements IDispositivoService{
    
    public static final String IS_ALREADY_USE = "The %s is already use";
    public static final String IS_NOT_FOUND = "The %s is not found";
    public static final String IS_NOT_ALLOWED = "The %s is not allowed";
    public static final String IS_NOT_VALID = "The %s is not valid";
    public static final String ARE_NOT_EQUALS = "The %s are not equals";
    public static final String IS_NOT_CORRECT = "The %s is not correct";

    @Autowired
    private TipoDispositivoRepository tipoDispositivoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private EstadoDispositivoRepository estadoDispositivoRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Override
    public DispositivoRequest crearDispositivo(DispositivoRequest dispositivoRequest)throws TypeDeviceNotFoundException, MarcaNotFoundException, StateNotFoundException {
        DispositivoPC dispositivoPC = new DispositivoPC();
        BeanUtils.copyProperties(dispositivoRequest, dispositivoPC);
        TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(dispositivoRequest.getTipoDispositivo()).orElse(null);
        if(tipoDispositivo == null){
            throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "TYPE DEVICE").toUpperCase());
        }
        dispositivoPC.setTipoDispositivo(tipoDispositivo);

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("Disponible").orElse(null);
        if(estadoDispositivo == null){
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "STATE DEVICE").toUpperCase());
        }
        dispositivoPC.setEstadoDispositivo(estadoDispositivo);

        Marca marca = marcaRepository.findById(dispositivoRequest.getMarca()).orElse(null);
        if(marca == null){
            throw new MarcaNotFoundException(String.format(IS_NOT_FOUND, "MARCA").toUpperCase());
        }
        dispositivoPC.setMarca(marca);
        
        DispositivoPC dispositivoCreado = dispositivoRepository.save(dispositivoPC);
        DispositivoRequest dispositivoCreadoRequest = new DispositivoRequest();
        BeanUtils.copyProperties(dispositivoCreado, dispositivoCreadoRequest);
        return dispositivoCreadoRequest;
    }

    @Override
    public List<DispositivoPC> listarDispositivos(){
        return (List<DispositivoPC>) dispositivoRepository.findAll();
    }

    @Override
    public DispositivoRequest actualizarDispositivo (Integer id, DispositivoRequest dispositivoRequest)throws TypeDeviceNotFoundException, MarcaNotFoundException, StateNotFoundException, DeviceNotFoundException {
        DispositivoPC dispositivoPC = dispositivoRepository.findById(id).orElse(null);

        if(dispositivoPC == null){
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "DEVICE").toUpperCase());
        }

        BeanUtils.copyProperties(dispositivoRequest, dispositivoPC);

        if(dispositivoRequest.getEstadoDispositivo()!= null){
            EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findById(dispositivoRequest.getEstadoDispositivo()).orElse(null);
                if(estadoDispositivo == null){
                    throw new StateNotFoundException(String.format(IS_NOT_FOUND, "DEVICE STATE").toUpperCase());
                }
                dispositivoPC.setEstadoDispositivo(estadoDispositivo);
            
        }else{
            dispositivoPC.setEstadoDispositivo(dispositivoPC.getEstadoDispositivo());
        }

        if(dispositivoRequest.getMarca()!= null){
            Marca marca = marcaRepository.findById(dispositivoRequest.getMarca()).orElse(null);
            if(marca == null){
                throw new MarcaNotFoundException(String.format(IS_NOT_FOUND, "MARCA").toUpperCase());
            }
            dispositivoPC.setMarca(marca);
        }else{
            dispositivoPC.setMarca(dispositivoPC.getMarca());
        }

        if(dispositivoRequest.getTipoDispositivo()!= null){
            TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(dispositivoRequest.getTipoDispositivo()).orElse(null);
            
            if(tipoDispositivo == null){
                throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "TYPE DEVICE").toUpperCase());
            }
            dispositivoPC.setTipoDispositivo(tipoDispositivo);
        }else{
            dispositivoPC.setTipoDispositivo(dispositivoPC.getTipoDispositivo());
        }

        DispositivoPC dispositivoActualizado = dispositivoRepository.save(dispositivoPC);
        DispositivoRequest dispositivoActualizadoReq = new DispositivoRequest();
        BeanUtils.copyProperties(dispositivoActualizado, dispositivoActualizadoReq);

        return dispositivoActualizadoReq;
    }

    @Override
    public DispositivoResponse listarDispositivoById(Integer id)throws DeviceNotFoundException{
        DispositivoPC dispositivoPC = dispositivoRepository.findById(id).orElse(null);

        if(dispositivoPC == null){
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "DEVICE").toUpperCase());
        }

        
        DispositivoResponse dispositivoResponse = new DispositivoResponse();
        BeanUtils.copyProperties(dispositivoPC, dispositivoResponse);
        dispositivoResponse.setEstadoDispositivo(dispositivoPC.getEstadoDispositivo().getNombre());
        dispositivoResponse.setMarca(dispositivoPC.getMarca().getNombre());
        dispositivoResponse.setTipoDispositivo(dispositivoPC.getTipoDispositivo().getNombre());
        return dispositivoResponse;
    }

    @Override
    public void eliminarDispositivo(Integer id)throws DeviceNotFoundException, DeleteNotAllowedException{
        DispositivoPC dispositivoPC = dispositivoRepository.findById(id).orElse(null);

        if(dispositivoPC == null){
            throw new DeviceNotFoundException(String.format(IS_NOT_FOUND, "DEVICE").toUpperCase());
        }

        if(dispositivoPC.getEstadoDispositivo().getNombre().equals("Baja")){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DELETE DEVICE").toUpperCase());
        }

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("Baja").get();
        dispositivoPC.setEstadoDispositivo(estadoDispositivo);
        dispositivoRepository.save(dispositivoPC);
    }
}
