package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.AreaDTO;

/**
 * Interfaz que define los métodos para gestionar los roles en el sistema.
 */
public interface IAreaService {
    
    /**
     * Crea una nueva área en el sistema.
     *
     * @param areaDTO El objeto {@link AreaDTO} con los datos del área a crear.
     * @return Un objeto {@link AreaDTO} con los datos del área recién creada.
     * @throws LocationNotFoundException Si no se encuentra la sede especificada.
     */
    public AreaDTO crearArea(AreaDTO areaDTO) throws LocationNotFoundException;

    /**
     * Lista todas las áreas registradas en el sistema.
     *
     * @return Una lista de objetos {@link AreaPC} que representan todas las áreas.
     * @throws LocationNotFoundException Si no se encuentran áreas en el sistema.
     */
    public List<AreaPC> listarAreas() throws LocationNotFoundException;

    /**
     * Actualiza los datos de una área existente.
     *
     * @param id El ID de la área a actualizar.
     * @param areaDTO El objeto {@link AreaDTO} con los nuevos datos del área.
     * @return Un objeto {@link AreaDTO} con los datos del área actualizada.
     * @throws LocationNotFoundException Si no se encuentra la área o la sede especificada.
     */
    public AreaDTO actualizarArea(Integer id, AreaDTO areaDTO) throws LocationNotFoundException;

    /**
     * Elimina (deshabilita) un área del sistema, marcándola como eliminada.
     *
     * @param id El ID de la área a eliminar.
     * @throws LocationNotFoundException Si no se encuentra la área especificada.
     * @throws DeleteNotAllowedException Si la eliminación del área no está permitida.
     */
    public void eliminarArea(Integer id) throws LocationNotFoundException, DeleteNotAllowedException;
}
