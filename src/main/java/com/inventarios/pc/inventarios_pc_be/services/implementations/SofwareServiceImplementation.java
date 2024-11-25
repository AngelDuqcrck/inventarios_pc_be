package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SoftwareResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.inventarios.pc.inventarios_pc_be.controllers.NotificationController;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;
import com.inventarios.pc.inventarios_pc_be.entities.TipoSoftware;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DuplicateEntityException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeSoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.SoftwarePcRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoSoftwareRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.ISoftwarePcService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SoftwarePcDTO;
import org.springframework.stereotype.Service;

@Service
public class SofwareServiceImplementation implements ISoftwarePcService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";

    @Autowired
    private SoftwarePcRepository softwarePcRepository;

    @Autowired
    private TipoSoftwareRepository tipoSoftwareRepository;

    @Autowired
    private NotificationController notificationController;

    /**
     * Crea un nuevo software en el sistema.
     * 
     * @param softwarePcDTO Objeto DTO que contiene los detalles del software a
     *                      crear.
     * @return El DTO del software creado.
     * @throws TypeSoftwareNotFoundException Si el tipo de software especificado no
     *                                       existe.
     */
    @Override
    public SoftwarePcDTO crearSoftware(SoftwarePcDTO softwarePcDTO)
            throws TypeSoftwareNotFoundException, SelectNotAllowedException {
        SoftwarePC softwarePC = new SoftwarePC();

        TipoSoftware tipoSoftware = tipoSoftwareRepository.findById(softwarePcDTO.getTipoSoftware()).orElse(null);

        if (tipoSoftware == null) {
            throw new TypeSoftwareNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE SOFTWARE").toUpperCase());
        }
        if (tipoSoftware.getDeleteFlag() == true) {
            throw new

            SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE TIPO DE SOFTWARE").toUpperCase());
        }

        if(softwarePcRepository.existsByNombreIgnoreCaseAndVersion(softwarePcDTO.getNombre(), softwarePcDTO.getVersion())){
            throw new SelectNotAllowedException("Ya existe un software registrado con el nombre "+softwarePcDTO.getNombre()+" y la version "+softwarePcDTO.getVersion());
        }
        BeanUtils.copyProperties(softwarePcDTO, softwarePC);
        softwarePC.setTipoSoftware(tipoSoftware);
        softwarePC.setDeleteFlag(false);

        SoftwarePC softwareCreado = softwarePcRepository.save(softwarePC);
        SoftwareResponse softwareResponse = new SoftwareResponse();
        softwareResponse.setId(softwareCreado.getId());
        softwareResponse.setNombre(softwareCreado.getNombre());
        softwareResponse.setVersion(softwareCreado.getVersion());
        softwareResponse.setEmpresa(softwareCreado.getEmpresa());
        softwareResponse.setTipoSoftware(softwareCreado.getTipoSoftware().getNombre());
        notificationController.sendNotification("SOFTWARE", softwareCreado.getId(), softwareResponse);
        SoftwarePcDTO softwareCreadoDto = new SoftwarePcDTO();
        BeanUtils.copyProperties(softwareCreado, softwareCreadoDto);

        return softwareCreadoDto;
    }

    /**
     * Retorna una lista de todos los software registrados en el sistema.
     * 
     * @return Lista de entidades SoftwarePC.
     */
    @Override
    public List<SoftwarePC> listarSoftwares() {
        return (List<SoftwarePC>) softwarePcRepository.findAll();
    }

    /**
     * Obtiene los detalles de un software por su ID.
     * 
     * @param id ID del software a buscar.
     * @return Objeto SoftwareResponse con los detalles del software.
     * @throws SoftwareNotFoundException Si el software con el ID especificado no
     *                                   existe.
     */
    @Override
    public SoftwareResponse listarSoftwareById(Integer id) throws SoftwareNotFoundException {
        SoftwarePC softwarePC = softwarePcRepository.findById(id).orElse(null);

        if (softwarePC == null) {
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "EL SOFTWARE").toUpperCase());
        }
        SoftwareResponse softwareResponse = new SoftwareResponse();
        BeanUtils.copyProperties(softwarePC, softwareResponse);
        softwareResponse.setTipoSoftware(softwarePC.getTipoSoftware().getNombre());
        return softwareResponse;
    }

    /**
     * Actualiza los detalles de un software existente.
     * 
     * @param id            ID del software a actualizar.
     * @param softwarePcDTO DTO con los nuevos detalles del software.
     * @return DTO del software actualizado.
     * @throws TypeSoftwareNotFoundException Si el tipo de software especificado no
     *                                       existe.
     * @throws SoftwareNotFoundException     Si el software con el ID especificado
     *                                       no existe.
     */
    @Override
    public SoftwarePcDTO actualizarSoftware(Integer id, SoftwarePcDTO softwarePcDTO) throws UpdateNotAllowedException,
            SelectNotAllowedException, TypeSoftwareNotFoundException, SoftwareNotFoundException {
        SoftwarePC softwarePC = softwarePcRepository.findById(id).orElse(null);

        if (softwarePC == null) {
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "EL SOFTWARE").toUpperCase());
        }

        if (softwarePC.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ACTUALIZAR EL SOFTWARE "+softwarePC.getNombre()+" PORQUE SE ENCUENTRA INACTIVO").toUpperCase());
        }

        if(softwarePcRepository.existsByNombreIgnoreCaseAndVersionAndIdNot(softwarePcDTO.getNombre(), softwarePcDTO.getVersion(), id)){
            throw new DuplicateEntityException("Ya existe un software registrado con el nombre "+softwarePcDTO.getNombre()+" y la version "+softwarePcDTO.getVersion());
        }
        BeanUtils.copyProperties(softwarePcDTO, softwarePC);
        softwarePC.setDeleteFlag(false);
        if (softwarePcDTO.getTipoSoftware() != null) {
            TipoSoftware tipoSoftware = tipoSoftwareRepository.findById(softwarePcDTO.getTipoSoftware()).orElse(null);

            if (tipoSoftware == null) {
                throw new TypeSoftwareNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE SOFTWARE").toUpperCase());
            }

            if (tipoSoftware.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE TIPO DE SOFTWARE").toUpperCase());
            }

            softwarePC.setTipoSoftware(tipoSoftware);
        } else {
            softwarePC.setTipoSoftware(softwarePC.getTipoSoftware());
        }

        SoftwarePC softwareActualizado = softwarePcRepository.save(softwarePC);
        SoftwarePcDTO softwareActualizadoDTO = new SoftwarePcDTO();
        BeanUtils.copyProperties(softwareActualizado, softwareActualizadoDTO);
        return softwareActualizadoDTO;
    }

    /**
     * Elimina (o marca como eliminado) un software por su ID.
     * 
     * @param id ID del software a eliminar.
     * @throws SoftwareNotFoundException Si el software con el ID especificado no
     *                                   existe.
     * @throws DeleteNotAllowedException Si el software no se puede eliminar.
     */
    @Override
    public void eliminarSoftware(Integer id) throws SoftwareNotFoundException, DeleteNotAllowedException {
        SoftwarePC softwarePC = softwarePcRepository.findById(id).orElse(null);

        if (softwarePC == null) {
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "EL SOFTWARE").toUpperCase());
        }

        if (softwarePC.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR EL SOFTWARE "+softwarePC.getNombre()+" PORQUE YA SE ENCUENTRA INACTIVO").toUpperCase());
        }
        softwarePC.setDeleteFlag(true);
        softwarePcRepository.save(softwarePC);
    }

    @Override
    public void activarSoftware(Integer id) throws SoftwareNotFoundException, ActivateNotAllowedException {
        SoftwarePC softwarePC = softwarePcRepository.findById(id).orElse(null);

        if (softwarePC == null) {
            throw new SoftwareNotFoundException(String.format(IS_NOT_FOUND, "EL SOFTWARE").toUpperCase());
        }

        if (softwarePC.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVAR ESTE SOFTWARE "+softwarePC.getNombre()+" PORQUE YA SE ENCUENTRA ACTIVO").toUpperCase());
        }
        softwarePC.setDeleteFlag(false);
        softwarePcRepository.save(softwarePC);
    }
}
