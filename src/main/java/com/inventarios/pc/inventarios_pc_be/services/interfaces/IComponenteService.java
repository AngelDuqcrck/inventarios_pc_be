package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.Componente;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.ChangeNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComponentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.ComponenteDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComponenteResponse;

public interface IComponenteService {
    public ComponenteDTO crearComponente(ComponenteDTO componenteDTO)throws SelectNotAllowedException , ComponentNotFoundException;

    public List<Componente> listarComponentes();

    public ComponenteResponse listarComponenteById(Integer id)throws ComponentNotFoundException;

    public ComponenteDTO actualizarComponente(Integer id, ComponenteDTO componenteDTO)throws SelectNotAllowedException ,UpdateNotAllowedException , ComponentNotFoundException;

    public void eliminarComponente (Integer id) throws ComponentNotFoundException, DeleteNotAllowedException;

    public void activarComponente (Integer id) throws ComponentNotFoundException, ActivateNotAllowedException;
}
