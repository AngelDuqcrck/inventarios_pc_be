package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.AreaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SedeRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UbicacionRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IUbicacionService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UbicacionDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.UbicacionResponse;

/**
 * Servicio que implementa las operaciones CRUD para la gestión de ubicaciones
 * en el sistema.
 */
@Service
public class UbicacionServiceImplementation implements IUbicacionService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    SedeRepository sedeRepository;

    @Autowired
    UbicacionRepository ubicacionRepository;

    /**
     * Crea una nueva ubicación en el sistema a partir de los datos proporcionados.
     * 
     * @param ubicacionDTO Un objeto {@link UbicacionDTO} con los datos de la
     *                     ubicación a crear.
     * @return Un objeto {@link UbicacionDTO} con los datos de la ubicación recién
     *         creada.
     * @throws LocationNotFoundException Si no se encuentra el área asociada a la
     *                                   ubicación.
     */
    @Override
    public UbicacionDTO crearUbicacion(UbicacionDTO ubicacionDTO)
            throws LocationNotFoundException, SelectNotAllowedException {
        Ubicacion ubicacion = new Ubicacion();
        BeanUtils.copyProperties(ubicacionDTO, ubicacion);
        // Con el id del area, llamamos al llamamos al repositorio para consultar y
        // traernos la info, si no lo consigue manda nulo y manda la excepcion
        AreaPC areaPC = areaRepository.findById(ubicacionDTO.getArea()).orElse(null);

        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());
        }

        if (areaPC.getDeleteFlag() == true) {
            throw new

            SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA AREA").toUpperCase());
        }
        ubicacion.setArea(areaPC);
        ubicacion.setDeleteFlag(false);

        Ubicacion ubicacionCreada = ubicacionRepository.save(ubicacion);
        UbicacionDTO ubicacionCreadaDTO = new UbicacionDTO();
        BeanUtils.copyProperties(ubicacionCreada, ubicacionCreadaDTO);
        return ubicacionCreadaDTO;
    }

    /**
     * Obtiene una lista de todas las ubicaciones registradas en el sistema.
     * 
     * @return Una lista de objetos {@link Ubicacion} que representan todas las
     *         ubicaciones.
     */
    @Override
    public List<Ubicacion> listarUbicaciones() {
        return (List<Ubicacion>) ubicacionRepository.findAll();
    }

    /**
     * Actualiza los datos de una ubicación existente en el sistema.
     * 
     * @param id           El ID de la ubicación a actualizar.
     * @param ubicacionDTO Un objeto {@link UbicacionDTO} con los nuevos datos de la
     *                     ubicación.
     * @return Un objeto {@link UbicacionDTO} con los datos de la ubicación
     *         actualizada.
     * @throws LocationNotFoundException Si no se encuentra la ubicación o el área
     *                                   asociada.
     */
    @Override
    public UbicacionDTO actualizarUbicacion(Integer id, UbicacionDTO ubicacionDTO)
            throws UpdateNotAllowedException, SelectNotAllowedException, LocationNotFoundException {
        Ubicacion ubicacion = ubicacionRepository.findById(id).orElse(null);
        if (ubicacion == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());

        }

        if (ubicacion.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTA UBICACION").toUpperCase());
        }
        BeanUtils.copyProperties(ubicacionDTO, ubicacion);

        if (ubicacionDTO.getArea() != null) {
            AreaPC areaPC = areaRepository.findById(ubicacionDTO.getArea()).orElse(null);
            if (areaPC == null) {
                throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());
            }
            if (areaPC.getDeleteFlag() == true) {
                throw new

                SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA AREA").toUpperCase());
            }
            ubicacion.setArea(areaPC);
        }

        Ubicacion ubicacionActualizada = ubicacionRepository.save(ubicacion);
        UbicacionDTO ubicacionActualizadaDTO = new UbicacionDTO();
        BeanUtils.copyProperties(ubicacionActualizada, ubicacionActualizadaDTO);
        ubicacionActualizadaDTO.setArea(ubicacionActualizada.getArea().getId());
        return ubicacionActualizadaDTO;

    }

    /**
     * Elimina (deshabilita) una ubicación existente, marcándola como no activa.
     * 
     * @param id El ID de la ubicación a eliminar.
     * @throws LocationNotFoundException Si no se encuentra la ubicación.
     * @throws DeleteNotAllowedException Si la ubicación ya está marcada como
     *                                   eliminada.
     */
    @Override
    public void eliminarUbicacion(Integer id) throws LocationNotFoundException, DeleteNotAllowedException {
        Ubicacion ubicacion = ubicacionRepository.findById(id).orElse(null);

        if (ubicacion == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());

        }
        if (ubicacion.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR ESTA UBICACION").toUpperCase());

        }
        ubicacion.setDeleteFlag(true);
        ubicacionRepository.save(ubicacion);
    }

    // Lista la informacion de una ubicacion especifica de acuerdo a su id
    public UbicacionResponse listarUbicacionById(Integer id) throws LocationNotFoundException {
        Ubicacion ubicacion = ubicacionRepository.findById(id).orElse(null);
        if (ubicacion == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());
        }
        UbicacionResponse ubicacionResponse = new UbicacionResponse();
        BeanUtils.copyProperties(ubicacion, ubicacionResponse);
        ubicacionResponse.setArea(ubicacion.getArea().getNombre());
        return ubicacionResponse;
    }

    @Override
    public List<Ubicacion> listarUbicacionesPorArea(Integer areaId) throws LocationNotFoundException, SelectNotAllowedException {
        AreaPC areaPC = areaRepository.findById(areaId).orElse(null);

        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());
        }

        if (areaPC.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA AREA").toUpperCase());
        }

        List<Ubicacion> ubicaciones = ubicacionRepository.findByArea(areaPC);

        if(ubicaciones.isEmpty()){
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());
        }
        return ubicaciones;
    }

    @Override
    public void activarUbicacion(Integer id) throws LocationNotFoundException, ActivateNotAllowedException {
        Ubicacion ubicacion = ubicacionRepository.findById(id).orElse(null);

        if (ubicacion == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());

        }
        if (ubicacion.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVAR ESTA UBICACION").toUpperCase());

        }
        ubicacion.setDeleteFlag(false);
        ubicacionRepository.save(ubicacion);
    }
}
