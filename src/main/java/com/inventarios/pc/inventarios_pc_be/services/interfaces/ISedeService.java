package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;
import com.inventarios.pc.inventarios_pc_be.entities.SedePC;
import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SedeDTO;

public interface ISedeService {

    public SedeDTO crearSede(SedeDTO sedeDTO) throws DuplicateEntityException;
    public List<SedePC> listarSedes();
    public SedeDTO listarSedePorId(Integer id)throws LocationNotFoundException;
    public SedeDTO actualizarSede(Integer id, SedeDTO sedeDTO) throws DuplicateEntityException,LocationNotFoundException, UpdateNotAllowedException;
    public void eliminarSede(Integer id) throws LocationNotFoundException, DeleteNotAllowedException, StateNotFoundException;
    public void activarSede(Integer id) throws LocationNotFoundException, ActivateNotAllowedException;

} 