package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.repositories.AreaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SedeRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UbicacionRepository;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.UbicacionDTO;

/**
 * Servicio que implementa las operaciones CRUD para la gestión de ubicaciones en el sistema.
 */
@Service
public class UbicacionServiceImplementation {
    
     public static final String IS_ALREADY_USE = "The %s is already use";
    public static final String IS_NOT_FOUND = "The %s is not found";
    public static final String IS_NOT_ALLOWED = "The %s is not allowed";


    @Autowired
    AreaRepository areaRepository;

    @Autowired
    SedeRepository sedeRepository;

    @Autowired
    UbicacionRepository ubicacionRepository;

    
    /**
     * Crea una nueva ubicación en el sistema a partir de los datos proporcionados.
     * 
     * @param ubicacionDTO Un objeto {@link UbicacionDTO} con los datos de la ubicación a crear.
     * @return Un objeto {@link UbicacionDTO} con los datos de la ubicación recién creada.
     * @throws LocationNotFoundException Si no se encuentra el área asociada a la ubicación.
     */
     public UbicacionDTO crearUbicacion(UbicacionDTO ubicacionDTO) throws LocationNotFoundException{
        Ubicacion ubicacion = new Ubicacion();
        BeanUtils.copyProperties(ubicacionDTO, ubicacion);
        //Con el id del area, llamamos al llamamos al repositorio para consultar y traernos la info, si no lo consigue manda nulo y manda la excepcion
        AreaPC areaPC = areaRepository.findById(ubicacionDTO.getArea().getId()).orElse(null);

        if (areaPC == null){
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());
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
     * @return Una lista de objetos {@link Ubicacion} que representan todas las ubicaciones.
     */
    public List<Ubicacion> listarUbicaciones(){
        return (List<Ubicacion>) ubicacionRepository.findAll();
    }

    /**
     * Actualiza los datos de una ubicación existente en el sistema.
     * 
     * @param id El ID de la ubicación a actualizar.
     * @param ubicacionDTO Un objeto {@link UbicacionDTO} con los nuevos datos de la ubicación.
     * @return Un objeto {@link UbicacionDTO} con los datos de la ubicación actualizada.
     * @throws LocationNotFoundException Si no se encuentra la ubicación o el área asociada.
     */
    public UbicacionDTO actualizarUbicacion(Integer id, UbicacionDTO ubicacionDTO) throws LocationNotFoundException{
        Ubicacion ubicacion = ubicacionRepository.findById(id).orElse(null);
        if(ubicacion == null){
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());
                
        }
        BeanUtils.copyProperties(ubicacionDTO, ubicacion);

        if (ubicacionDTO.getArea()!= null) {
            AreaPC areaPC = areaRepository.findById(ubicacionDTO.getArea().getId()).orElse(null);
            if(areaPC == null){
                throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());    }
                ubicacion.setArea(areaPC);
        }
        Ubicacion ubicacionActualizada = ubicacionRepository.save(ubicacion);
        UbicacionDTO ubicacionActualizadaDTO = new UbicacionDTO();
        BeanUtils.copyProperties(ubicacionActualizada, ubicacionActualizadaDTO);
        ubicacionActualizadaDTO.setArea(ubicacionActualizada.getArea());
        return ubicacionActualizadaDTO;

    }

    /**
     * Elimina (deshabilita) una ubicación existente, marcándola como no activa.
     * 
     * @param id El ID de la ubicación a eliminar.
     * @throws LocationNotFoundException Si no se encuentra la ubicación.
     * @throws DeleteNotAllowedException Si la ubicación ya está marcada como eliminada.
     */
    public void eliminarUbicacion(Integer id) throws LocationNotFoundException, DeleteNotAllowedException{
        Ubicacion ubicacion = ubicacionRepository.findById(id).orElse(null);

        if (ubicacion == null){
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());        

        }
        if(ubicacion.getDeleteFlag() ==true){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DELETE LOCATION").toUpperCase());

        }
        ubicacion.setDeleteFlag(true);
        ubicacionRepository.save(ubicacion);
    }
}
