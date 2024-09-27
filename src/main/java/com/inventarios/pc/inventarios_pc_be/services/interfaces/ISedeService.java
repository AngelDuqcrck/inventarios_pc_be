package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;
import com.inventarios.pc.inventarios_pc_be.entities.SedePC;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SedeDTO;

public interface ISedeService {

    public SedeDTO crearSede(SedeDTO sedeDTO);
    public List<SedePC> listarSedes();
    public SedeDTO actualizarSede(Integer id, SedeDTO sedeDTO) throws LocationNotFoundException;
    public void eliminarSede(Integer id) throws LocationNotFoundException, DeleteNotAllowedException;

} 