package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IRolService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.RolRepository;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.RolDTO;

/**
 * Servicio que implementa las operaciones CRUD para la gestión de roles en el
 * sistema.
 */
@Service
public class RolServiceImplementation implements IRolService {
    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "no esta permitido %s ";

    @Autowired
    RolRepository rolRepository;

    /**
     * Crea un nuevo rol en el sistema a partir de los datos proporcionados.
     * 
     * @param rolDTO Un objeto {@link RolDTO} que contiene los datos del nuevo rol.
     * @return Un objeto {@link RolDTO} con los datos del rol recién creado.
     */
    @Override
    public RolDTO crearRol(RolDTO rolDTO) {
        Rol rol = new Rol();
        BeanUtils.copyProperties(rolDTO, rol);
        rol.setDeleteFlag(false);
        Rol rolCreado = rolRepository.save(rol);
        RolDTO rolCreadoDTO = new RolDTO();
        BeanUtils.copyProperties(rolCreado, rolCreadoDTO);
        return rolCreadoDTO;
    }

    /**
     * Obtiene una lista de todos los roles registrados en el sistema.
     * 
     * @return Una lista de objetos {@link Rol} que representan todos los roles.
     */
    @Override
    public List<Rol> listarRoles() {
        return (List<Rol>) rolRepository.findAll();
    }

    /**
     * Actualiza los datos de un rol existente.
     * 
     * @param rolDTO Un objeto {@link RolDTO} que contiene los nuevos datos del rol.
     * @return Un objeto {@link RolDTO} con los datos del rol actualizado.
     * @throws IllegalArgumentException Si el rol no se encuentra en la base de
     *                                  datos.
     */
    @Override
    public RolDTO actualizarRol(RolDTO rolDTO) throws RolNotFoundException, UpdateNotAllowedException {
        Rol rol = rolRepository.findById(rolDTO.getId()).orElse(null);
        if (rol == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "EL ROL").toUpperCase());
        }

        if (rol.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR EL ROL "+rol.getNombre()+" PORQUE SE ENCUENTRA DESACTIVADO").toUpperCase());
        }
        BeanUtils.copyProperties(rolDTO, rol);
        Rol rolActualizado = rolRepository.save(rol);
        RolDTO rolActualizadoDTO = new RolDTO();
        BeanUtils.copyProperties(rolActualizado, rolActualizadoDTO);
        return rolActualizadoDTO;
    }

    /**
     * Deshabilita un rol en el sistema, impidiendo su uso en operaciones futuras.
     * 
     * @param rolId El ID del rol que se desea deshabilitar.
     */
    @Override
    public void deshabilitarRol(Integer rolId) throws RolNotFoundException, DeleteNotAllowedException {
        Rol rol = rolRepository.findById(rolId).orElse(null);
        if (rol == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "EL ROL").toUpperCase());

        }

        if (rol.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DESACTIVAR EL ROL "+rol.getNombre()+" PORQUE SE ENCUENTRA DESACTIVADO").toUpperCase());

        }

        rol.setDeleteFlag(true);
        rolRepository.save(rol);
    }

    @Override
    public void activarRol(Integer rolId) throws RolNotFoundException, ActivateNotAllowedException {
        Rol rol = rolRepository.findById(rolId).orElse(null);
        if (rol == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "EL ROL").toUpperCase());

        }

        if (rol.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVAR EL ROL "+rol.getNombre()+" PORQUE SE ENCUENTRA ACTIVADO").toUpperCase());

        }

        rol.setDeleteFlag(false);
        rolRepository.save(rol);
    }

    @Override
    public RolDTO listarRolById(Integer rolId) throws RolNotFoundException {
        Rol rol = rolRepository.findById(rolId).orElse(null);
        if (rol == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "EL ROL").toUpperCase());
        }
        RolDTO rolDTO = new RolDTO();
        BeanUtils.copyProperties(rol, rolDTO);

        return rolDTO;
    }

}
