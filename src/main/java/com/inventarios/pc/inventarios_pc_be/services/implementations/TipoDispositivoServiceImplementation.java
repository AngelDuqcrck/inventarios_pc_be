package com.inventarios.pc.inventarios_pc_be.services.implementations;
import java.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeDeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoDispositivoService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoDispositivoDTO;

@Service
public class TipoDispositivoServiceImplementation implements ITipoDispositivoService {
    
    public static final String IS_ALREADY_USE = "The %s is already use";
    public static final String IS_NOT_FOUND = "The %s is not found";
    public static final String IS_NOT_ALLOWED = "The %s is not allowed";
    
    @Autowired
    private TipoDispositivoRepository tipoDispositivoRepository;


    @Override
    public TipoDispositivoDTO creaDispositivoDTO(TipoDispositivoDTO tipoDispositivoDTO){
        TipoDispositivo tipoDispositivo = new TipoDispositivo();
        BeanUtils.copyProperties(tipoDispositivoDTO, tipoDispositivo);
        tipoDispositivo.setDeleteFlag(false);
        TipoDispositivo tipoDispCreado = tipoDispositivoRepository.save(tipoDispositivo);
        TipoDispositivoDTO tipoDispCreadoDto = new TipoDispositivoDTO();
        BeanUtils.copyProperties(tipoDispCreado,tipoDispCreadoDto );
        return tipoDispCreadoDto;
    }

    @Override
    public List<TipoDispositivo> listarTipos(){
        return (List<TipoDispositivo>) tipoDispositivoRepository.findAll();
    }

    @Override
    public TipoDispositivoDTO actualizarTipoDispositivo(Integer id, TipoDispositivoDTO tipoDispositivoDTO)throws TypeDeviceNotFoundException{
        TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(id).orElse(null);
        if(tipoDispositivo == null){
            throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "TYPE DEVICE").toUpperCase());
        }
        BeanUtils.copyProperties(tipoDispositivoDTO, tipoDispositivo);
        tipoDispositivo.setDeleteFlag(false);
        TipoDispositivo tipoDispositivoActualizado = tipoDispositivoRepository.save(tipoDispositivo);
        TipoDispositivoDTO tipoDispositivoActualizadoDTO = new TipoDispositivoDTO();
        BeanUtils.copyProperties(tipoDispositivoActualizado, tipoDispositivoActualizadoDTO);
        return tipoDispositivoActualizadoDTO;

    }

    @Override
    public void eliminarTipoDispositivo(Integer id) throws TypeDeviceNotFoundException, DeleteNotAllowedException{
        TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(id).orElse(null);
        if(tipoDispositivo == null){
            throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "TYPE DEVICE").toUpperCase());
        }
        
        if(tipoDispositivo.getDeleteFlag()== true){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DELETE TYPE DEVICE").toUpperCase());        
        }
        tipoDispositivo.setDeleteFlag(true);
        tipoDispositivoRepository.save(tipoDispositivo);
    }
}
