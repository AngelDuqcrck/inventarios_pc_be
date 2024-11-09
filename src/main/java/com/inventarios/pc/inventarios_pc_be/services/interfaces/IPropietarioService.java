package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import com.inventarios.pc.inventarios_pc_be.entities.Propietario;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.OwnerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.PropietarioDTO;

import java.util.List;

public interface IPropietarioService {
     public void crearPropietario(PropietarioDTO propietarioDTO);

     public List<Propietario> listarPropietarios();

     public PropietarioDTO listarPropietarioById(Integer id) throws OwnerNotFoundException;

     public void actualizarPropietario(Integer id, PropietarioDTO propietarioDTO)throws OwnerNotFoundException, UpdateNotAllowedException;

     public void eliminarPropietario(Integer id)throws DeleteNotAllowedException, OwnerNotFoundException;

     public void activarPropietario(Integer id) throws ActivateNotAllowedException, OwnerNotFoundException;
}
