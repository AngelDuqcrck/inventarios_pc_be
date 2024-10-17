package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.*;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeSoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SoftwarePcDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SoftwareResponse;

/**
 * Servicio que define las operaciones relacionadas con la gestión de software
 * en un PC.
 */
public interface ISoftwarePcService {

  /**
   * Crea un nuevo registro de software en el sistema.
   * 
   * @param softwarePcDTO Objeto DTO que contiene los datos del software a crear.
   * @return Objeto DTO del software creado.
   * @throws TypeSoftwareNotFoundException Si el tipo de software no se encuentra
   *                                       en la base de datos.
   */
  public SoftwarePcDTO crearSoftware(SoftwarePcDTO softwarePcDTO)
      throws TypeSoftwareNotFoundException, SelectNotAllowedException;

  /**
   * Lista todos los registros de software almacenados en el sistema.
   * 
   * @return Lista de entidades SoftwarePC.
   */
  public List<SoftwarePC> listarSoftwares();

  /**
   * Obtiene un software específico por su ID.
   * 
   * @param id Identificador del software a buscar.
   * @return Objeto SoftwareResponse con los detalles del software encontrado.
   * @throws SoftwareNotFoundException Si el software con el ID especificado no se
   *                                   encuentra.
   */
  public SoftwareResponse listarSoftwareById(Integer id) throws SoftwareNotFoundException;

  /**
   * Actualiza los datos de un software existente en el sistema.
   * 
   * @param id            Identificador del software a actualizar.
   * @param softwarePcDTO Objeto DTO con los nuevos datos del software.
   * @return Objeto DTO del software actualizado.
   * @throws TypeSoftwareNotFoundException Si el tipo de software no se encuentra
   *                                       en la base de datos.
   * @throws SoftwareNotFoundException     Si el software con el ID especificado
   *                                       no se encuentra.
   */
  public SoftwarePcDTO actualizarSoftware(Integer id, SoftwarePcDTO softwarePcDTO) throws SelectNotAllowedException,
      UpdateNotAllowedException, TypeSoftwareNotFoundException, SoftwareNotFoundException;

  /**
   * Elimina (marca como eliminado) un software del sistema.
   * 
   * @param id Identificador del software a eliminar.
   * @throws SoftwareNotFoundException Si el software con el ID especificado no se
   *                                   encuentra.
   * @throws DeleteNotAllowedException Si la eliminación del software no está
   *                                   permitida.
   */
  public void eliminarSoftware(Integer id) throws SoftwareNotFoundException, DeleteNotAllowedException;

  public void activarSoftware(Integer id) throws SoftwareNotFoundException, ActivateNotAllowedException;
}
