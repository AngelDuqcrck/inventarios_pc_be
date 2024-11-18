package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.Marca;
import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.MarcaDTO;

public interface IMarcaService {
    
    public MarcaDTO crearMarca(MarcaDTO marcaDTO) throws DuplicateEntityException;

    public List<Marca> listarMarcas();

    public MarcaDTO actualizarMarca(Integer id, MarcaDTO marcaDTO)throws DuplicateEntityException, MarcaNotFoundException, UpdateNotAllowedException;
    
    public void eliminarMarca(Integer id)throws MarcaNotFoundException, DeleteNotAllowedException;
    
    public MarcaDTO listarMarcaById(Integer id)throws MarcaNotFoundException;

    public void activarMarca(Integer id)throws MarcaNotFoundException, ActivateNotAllowedException;
}
