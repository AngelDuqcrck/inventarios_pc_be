package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.*;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DuplicateEntityException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeDeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ITipoDispositivoService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.TipoDispositivoDTO;

@Service
public class TipoDispositivoServiceImplementation implements ITipoDispositivoService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";

    @Autowired
    private TipoDispositivoRepository tipoDispositivoRepository;

    @Override
    public TipoDispositivoDTO creaDispositivoDTO(TipoDispositivoDTO tipoDispositivoDTO) throws DuplicateEntityException {
        TipoDispositivo tipoDispositivo = new TipoDispositivo();
        if(tipoDispositivoRepository.existsByNombreIgnoreCase(tipoDispositivoDTO.getNombre())){
            throw new DuplicateEntityException("Ya existe un tipo de dispositivo con el nombre " + tipoDispositivoDTO.getNombre());
        }
        BeanUtils.copyProperties(tipoDispositivoDTO, tipoDispositivo);
        tipoDispositivo.setDeleteFlag(false);
        TipoDispositivo tipoDispCreado = tipoDispositivoRepository.save(tipoDispositivo);
        TipoDispositivoDTO tipoDispCreadoDto = new TipoDispositivoDTO();
        BeanUtils.copyProperties(tipoDispCreado, tipoDispCreadoDto);
        return tipoDispCreadoDto;
    }

    @Override
    public List<TipoDispositivo> listarTipos() {
        return (List<TipoDispositivo>) tipoDispositivoRepository.findAll();
    }

    @Override
    public TipoDispositivoDTO actualizarTipoDispositivo(Integer id, TipoDispositivoDTO tipoDispositivoDTO)
            throws TypeDeviceNotFoundException, UpdateNotAllowedException {
        TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(id).orElse(null);
        if (tipoDispositivo == null) {
            throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE DISPOSITIVO").toUpperCase());
        }
        if (tipoDispositivo.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTE TIPO DE DISPOSITIVO").toUpperCase());
        }
        if(tipoDispositivoRepository.existsByNombreIgnoreCaseAndIdNot(tipoDispositivoDTO.getNombre(), id)){
            throw new DuplicateEntityException("Ya existe un tipo de dispositivo con el nombre " + tipoDispositivoDTO.getNombre());
        }
        BeanUtils.copyProperties(tipoDispositivoDTO, tipoDispositivo);
        tipoDispositivo.setDeleteFlag(false);
        TipoDispositivo tipoDispositivoActualizado = tipoDispositivoRepository.save(tipoDispositivo);
        TipoDispositivoDTO tipoDispositivoActualizadoDTO = new TipoDispositivoDTO();
        BeanUtils.copyProperties(tipoDispositivoActualizado, tipoDispositivoActualizadoDTO);
        return tipoDispositivoActualizadoDTO;

    }

    @Override
    public void eliminarTipoDispositivo(Integer id) throws TypeDeviceNotFoundException, DeleteNotAllowedException {
        TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(id).orElse(null);
        if (tipoDispositivo == null) {
            throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE DISPOSITIVO").toUpperCase());
        }

        if (tipoDispositivo.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ELIMINAR ESTE TIPO DE DISPOSITIVO").toUpperCase());
        }
        tipoDispositivo.setDeleteFlag(true);
        tipoDispositivoRepository.save(tipoDispositivo);
    }

    @Override
    public void activarTipoDispositivo(Integer id) throws TypeDeviceNotFoundException, ActivateNotAllowedException {
        TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(id).orElse(null);
        if (tipoDispositivo == null) {
            throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE DISPOSITIVO").toUpperCase());
        }

        if (tipoDispositivo.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ACTIVAR ESTE TIPO DE DISPOSITIVO").toUpperCase());
        }
        tipoDispositivo.setDeleteFlag(false);
        tipoDispositivoRepository.save(tipoDispositivo);
    }
}
