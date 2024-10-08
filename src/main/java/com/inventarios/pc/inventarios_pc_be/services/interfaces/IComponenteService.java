package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.Componente;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComponentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.ComponenteDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComponenteResponse;

public interface IComponenteService {
    public ComponenteDTO crearComponente(ComponenteDTO componenteDTO)throws ComponentNotFoundException;

    public List<Componente> listarComponentes();

    public ComponenteResponse listarComponenteById(Integer id)throws ComponentNotFoundException;

    public ComponenteDTO actualizarComponente(Integer id, ComponenteDTO componenteDTO)throws ComponentNotFoundException;

    public void eliminarComponente (Integer id) throws ComponentNotFoundException, DeleteNotAllowedException;

}
