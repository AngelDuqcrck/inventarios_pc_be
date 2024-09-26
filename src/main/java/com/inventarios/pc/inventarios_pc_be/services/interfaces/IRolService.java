package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.RolDTO;

public interface IRolService {
    public RolDTO crearRol (RolDTO rolDTO);

    public List<Rol> listarRoles();
    
    public RolDTO actualizarRol(RolDTO rolDTO);

    public void deshabilitarRol (Integer rolId);
}
