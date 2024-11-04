package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UbicacionDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.UbicacionResponse;

/**
 * Interfaz que define los métodos para gestionar las ubicaciones en el sistema.
 */

public interface IUbicacionService {

    /**
     * Crea una nueva ubicación en el sistema.
     * 
     * @param ubicacionDTO Un objeto {@link UbicacionDTO} que contiene los datos de
     *                     la ubicación a crear.
     * @return Un objeto {@link UbicacionDTO} con los datos de la ubicación recién
     *         creada.
     * @throws LocationNotFoundException Si no se encuentra el área asociada a la
     *                                   ubicación.
     */
    public UbicacionDTO crearUbicacion(UbicacionDTO ubicacionDTO)
            throws LocationNotFoundException, SelectNotAllowedException;

    /**
     * Obtiene una lista de todas las ubicaciones registradas en el sistema.
     * 
     * @return Una lista de objetos {@link Ubicacion} que representan todas las
     *         ubicaciones.
     */
    public List<Ubicacion> listarUbicaciones();

            /**
     * Lista todas las áreas registradas en el sistema.
     *
     * @return Una lista de objetos {@link Ubicacion} que representan todas las ubicaciones que están en una misma área.
     * @throws LocationNotFoundException Si no se encuentran ubicaciones en el sistema.
     */
    public List<Ubicacion> listarUbicacionesPorArea(Integer areaId) throws LocationNotFoundException, SelectNotAllowedException;

    /**
     * Actualiza los datos de una ubicación existente.
     * 
     * @param id           El ID de la ubicación a actualizar.
     * @param ubicacionDTO Un objeto {@link UbicacionDTO} que contiene los nuevos
     *                     datos de la ubicación.
     * @return Un objeto {@link UbicacionDTO} con los datos de la ubicación
     *         actualizada.
     * @throws LocationNotFoundException Si no se encuentra la ubicación o el área
     *                                   asociada.
     */
    public UbicacionDTO actualizarUbicacion(Integer id, UbicacionDTO ubicacionDTO)
            throws SelectNotAllowedException, UpdateNotAllowedException, LocationNotFoundException;

    /**
     * Elimina (deshabilita) una ubicación existente, marcándola como no activa.
     * 
     * @param id El ID de la ubicación a eliminar.
     * @throws LocationNotFoundException Si no se encuentra la ubicación.
     * @throws DeleteNotAllowedException Si la ubicación ya está marcada como
     *                                   eliminada.
     */
    public void eliminarUbicacion(Integer id) throws LocationNotFoundException, DeleteNotAllowedException, StateNotFoundException;

    // Lista la informacion de una ubicacion por su id
    public UbicacionResponse listarUbicacionById(Integer id) throws LocationNotFoundException;

    public void activarUbicacion(Integer id) throws LocationNotFoundException, ActivateNotAllowedException;
}
