package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.RolDTO;

/**
 * Interfaz que define los métodos para gestionar los roles en el sistema.
 */
public interface IRolService {
  /**
     * Crea un nuevo rol en el sistema a partir de un objeto {@link RolDTO}.
     * 
     * @param rolDTO El objeto {@link RolDTO} con los datos del rol a crear.
     * @return Un objeto {@link RolDTO} con los datos del rol recién creado.
     */
    public RolDTO crearRol(RolDTO rolDTO);

    /**
     * Obtiene una lista de todos los roles registrados en el sistema.
     * 
     * @return Una lista de objetos {@link Rol} que representan todos los roles.
     */
    public List<Rol> listarRoles();

    /**
     * Actualiza los datos de un rol existente en el sistema.
     * 
     * @param rolDTO El objeto {@link RolDTO} con los datos actualizados del rol.
     * @return Un objeto {@link RolDTO} con los datos del rol actualizado.
     * @throws IllegalArgumentException Si el rol no se encuentra en la base de datos.
     */
    public RolDTO actualizarRol(RolDTO rolDTO);

    /**
     * Deshabilita un rol en el sistema, impidiendo su uso en operaciones futuras.
     * 
     * @param rolId El ID del rol a deshabilitar.
     */
    public void deshabilitarRol (Integer rolId)throws RolNotFoundException, DeleteNotAllowedException;

    public RolDTO listarRolById(Integer rolId)throws RolNotFoundException;
}
