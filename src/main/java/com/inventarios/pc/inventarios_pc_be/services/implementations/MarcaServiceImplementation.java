package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Marca;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MarcaNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.MarcaRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IMarcaService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.MarcaDTO;

@Service
public class MarcaServiceImplementation implements IMarcaService {
    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "no esta permitido %s ";
    @Autowired
    private MarcaRepository marcaRepository;

    @Override
    public MarcaDTO crearMarca(MarcaDTO marcaDTO) {
        Marca marca = new Marca();
        BeanUtils.copyProperties(marcaDTO, marca);
        marca.setDeleteFlag(false);
        Marca marcaCreada = marcaRepository.save(marca);
        MarcaDTO marcaCreadaDTO = new MarcaDTO();
        BeanUtils.copyProperties(marcaCreada, marcaCreadaDTO);
        return marcaCreadaDTO;
    }

    @Override
    public List<Marca> listarMarcas() {
        return (List<Marca>) marcaRepository.findAll();
    }

    @Override
    public MarcaDTO actualizarMarca(Integer id, MarcaDTO marcaDTO)
            throws MarcaNotFoundException, UpdateNotAllowedException {
        Marca marca = marcaRepository.findById(id).orElse(null);
        if (marca == null) {
            throw new MarcaNotFoundException(String.format(IS_NOT_FOUND_F, "LA MARCA").toUpperCase());
        }
        if (marca.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR LA MARCA "+marca.getNombre()+" PORQUE SE ENCUENTRA INACTIVA").toUpperCase());
        }
        BeanUtils.copyProperties(marcaDTO, marca);
        marca.setDeleteFlag(false);
        Marca marcaActualizada = marcaRepository.save(marca);
        MarcaDTO marcaActualizadaDTO = new MarcaDTO();
        BeanUtils.copyProperties(marcaActualizada, marcaActualizadaDTO);
        return marcaActualizadaDTO;
    }

    @Override
    public void eliminarMarca(Integer id) throws MarcaNotFoundException, DeleteNotAllowedException {
        Marca marca = marcaRepository.findById(id).orElse(null);
        if (marca == null) {
            throw new MarcaNotFoundException(String.format(IS_NOT_FOUND_F, "LA MARCA").toUpperCase());
        }
        if (marca.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR LA MARCA "+marca.getNombre()+" PORQUE SE ENCUENTRA INACTIVA").toUpperCase());
        }
        marca.setDeleteFlag(true);
        marcaRepository.save(marca);
    }

    public void activarMarca(Integer id) throws MarcaNotFoundException, ActivateNotAllowedException {
        Marca marca = marcaRepository.findById(id).orElse(null);
        if (marca == null) {
            throw new MarcaNotFoundException(String.format(IS_NOT_FOUND_F, "LA MARCA").toUpperCase());
        }
        if (marca.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVAR LA MARCA"+marca.getNombre()+" PORQUE SE ENCUENTRA INACTIVA").toUpperCase());
        }
        marca.setDeleteFlag(false);
        marcaRepository.save(marca);
    }

    @Override
    public MarcaDTO listarMarcaById(Integer id) throws MarcaNotFoundException {
        Marca marca = marcaRepository.findById(id).orElse(null);
        if (marca == null) {
            throw new MarcaNotFoundException(String.format(IS_NOT_FOUND_F, "LA MARCA").toUpperCase());
        }
        MarcaDTO marcaDTO = new MarcaDTO();
        BeanUtils.copyProperties(marca, marcaDTO);
        return marcaDTO;
    }
}
