package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.repositories.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.entities.CambioUbicacionPc;
import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.Rol;
import com.inventarios.pc.inventarios_pc_be.entities.SedePC;
import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RolNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IAreaService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.AreaDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.AreaResponse;

/**
 * Implementación del servicio para gestionar las áreas del sistema.
 */
@Service
public class AreaServiceImplementation implements IAreaService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND ="%s no fue encontrada";
    public static final String IS_NOT_ALLOWED ="no esta permitido %s ";

    @Autowired
    private ComputadorServiceImplementation computadorServiceImplementation;
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private CambioUbicacionPcRepository cambioUbicacionPcRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private ComputadorRepository computadorRepository;

    @Autowired
    private EstadoDispositivoRepository estadoDispositivoRepository;

    @Autowired
    private HistorialDispositivoRepository historialDispositivoRepository;


    /**
     * Crea una nueva área en el sistema.
     *
     * @param areaDTO El objeto {@link AreaDTO} con los datos del área a crear.
     * @return Un objeto {@link AreaDTO} con los datos del área recién creada.
     * @throws LocationNotFoundException Si no se encuentra la sede especificada.
     */
    @Override
    public AreaDTO crearArea(AreaDTO areaDTO) throws LocationNotFoundException, SelectNotAllowedException, RolNotFoundException {
        AreaPC areaPC = new AreaPC();
        BeanUtils.copyProperties(areaDTO, areaPC);
        // Con el id de la sede llamamos al repositorio para consultar y traernos la
        // info, si no lo consigue manda nulo y manda la excepcion
        SedePC sedePC = sedeRepository.findById(areaDTO.getSede()).orElse(null);

        if (sedePC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "LA SEDE").toUpperCase());
        }

        if (sedePC.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR LA SEDE "+sedePC.getNombre()+" PORQUE ESTA INACTIVA").toUpperCase());
        }

        List<AreaPC> areasExistentes = areaRepository.findBySede(sedePC);
        for (AreaPC areaExistente : areasExistentes) {
            if (areaExistente.getNombre().equalsIgnoreCase(areaDTO.getNombre())) {
                throw new SelectNotAllowedException(String.format("YA EXISTE UN ÁREA CON EL NOMBRE '%s' EN LA SEDE '%s'.",
                    areaDTO.getNombre().toUpperCase(), sedePC.getNombre().toUpperCase()));
            }
        }
        

        Rol rol = rolRepository.findById(areaDTO.getRol()).orElse(null);

        if (rol == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "EL ROL").toUpperCase());
        }

        if(rol.getDeleteFlag()==true){
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR EL ROL "+rol.getNombre()+" PORQUE ESTA INACTIVO").toUpperCase());
        }
        areaPC.setRol(rol);
        areaPC.setSede(sedePC);
        areaPC.setDeleteFlag(false);

        AreaPC areaCreada = areaRepository.save(areaPC);
        AreaDTO areaCreadaDTO = new AreaDTO();
        BeanUtils.copyProperties(areaCreada, areaCreadaDTO);

        return areaCreadaDTO;
    }

    /**
     * Lista todas las áreas registradas en el sistema.
     *
     * @return Una lista de objetos {@link AreaPC} que representan todas las áreas.
     */
    @Override
    public List<AreaPC> listarAreas() {
        return (List<AreaPC>) areaRepository.findAll();
    }

        /**
     * Lista todas las áreas registradas en el sistema.
     *
     * @return Una lista de objetos {@link AreaPC} que representan todas las áreas.
     */
    @Override
    public List<AreaPC> listarAreasPorSede(Integer sedeId) throws LocationNotFoundException, SelectNotAllowedException {
        SedePC sedePC = sedeRepository.findById(sedeId).orElse(null);

        if (sedePC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "LA SEDE").toUpperCase());
        }

        if (sedePC.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR LA SEDE "+sedePC.getNombre()+" PORQUE ESTA INACTIVA").toUpperCase());
        }

        List<AreaPC> areas = areaRepository.findBySede(sedePC);

        if(areas.isEmpty()){
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "EL ÁREA").toUpperCase());
        }
        return areas;
    }

    @Override
    public List<AreaResponse> listarAreasPorRolySede(Integer rol, Integer sedeId) throws LocationNotFoundException, SelectNotAllowedException, RolNotFoundException {
        Rol rolPC = rolRepository.findById(rol).orElse(null);
        SedePC sedePC = sedeRepository.findById(sedeId).orElse(null);
        if (rolPC == null) {
            throw new RolNotFoundException(String.format(IS_NOT_FOUND, "EL ROL").toUpperCase());
        }
        if (sedePC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "LA SEDE").toUpperCase());
        }
        List<AreaPC> areas = areaRepository.findBySedeAndRol(sedePC, rolPC);
        List<AreaResponse> areasResponses = new ArrayList<>();
        for (AreaPC area : areas) {
            AreaResponse areaResponse = new AreaResponse();
            BeanUtils.copyProperties(area, areaResponse);
            areaResponse.setSede(area.getSede().getNombre());
            areaResponse.setRol(area.getRol().getNombre());
            areasResponses.add(areaResponse);
        }

        return areasResponses;
    }

    // Metodo que lista toda la informacion especifica de acuerdo a su id
    @Override
    public AreaResponse listarAreaById(Integer id) throws LocationNotFoundException {
        AreaPC areaPC = areaRepository.findById(id).orElse(null);

        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "EL ÁREA").toUpperCase());
        }
        AreaResponse areaResponse = new AreaResponse();
        BeanUtils.copyProperties(areaPC, areaResponse);
        areaResponse.setSede(areaPC.getSede().getNombre());
        areaResponse.setRol(areaPC.getRol().getNombre());
        return areaResponse;
    }

    /**
     * Actualiza los datos de un área existente.
     *
     * @param id      El ID del área a actualizar.
     * @param areaDTO El objeto {@link AreaDTO} con los nuevos datos del área.
     * @return Un objeto {@link AreaDTO} con los datos del área actualizada.
     * @throws LocationNotFoundException Si no se encuentra el área o la sede
     *                                   especificada.
     */
    @Override
    public AreaDTO actualizarArea(Integer id, AreaDTO areaDTO)
            throws SelectNotAllowedException, LocationNotFoundException, UpdateNotAllowedException, RolNotFoundException {
        AreaPC areaPC = areaRepository.findById(id).orElse(null);
        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "EL ÁREA").toUpperCase());
        }

        if (areaPC.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTUALIZAR EL ÁREA "+areaPC.getNombre()+" PORQUE ESTA INACTIVA").toUpperCase());
        }
        BeanUtils.copyProperties(areaDTO, areaPC);

        if (areaDTO.getSede() != null) {
            SedePC sedePC = sedeRepository.findById(areaDTO.getSede()).orElse(null);
            if (sedePC == null) {
                throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "LA SEDE").toUpperCase());
            }
            if (sedePC.getDeleteFlag() == true) {
                throw new

                SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR LA SEDE "+sedePC.getNombre()+" PORQUE SE ENCUENTRA INACTIVA").toUpperCase());
            }
            areaPC.setSede(sedePC);
        }

        if(areaDTO.getNombre()!= null){
            List<AreaPC> areasExistentes = areaRepository.findBySede(areaPC.getSede());
            for (AreaPC areaExistente : areasExistentes) {
                if (areaExistente.getNombre().equalsIgnoreCase(areaDTO.getNombre())) {
                    throw new SelectNotAllowedException(String.format("YA EXISTE UN ÁREA CON EL NOMBRE '%s' EN LA SEDE '%s'.",
                        areaDTO.getNombre().toUpperCase(), areaPC.getSede().getNombre().toUpperCase()));
                }
            }
        }

        if(areaDTO.getRol()!= null){
            Rol rol = rolRepository.findById(areaDTO.getRol()).orElse(null);

            if (rol == null) {
                throw new RolNotFoundException(String.format(IS_NOT_FOUND, "EL ROL").toUpperCase());
            }

            if(rol.getDeleteFlag()==true){
                throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR EL ROL "+rol.getNombre()+" PORQUE ESTA INACTIVO").toUpperCase());
            }
            areaPC.setRol(rol);
        }else{
            areaPC.setRol(areaPC.getRol());
        }
        AreaPC areaActualizada = areaRepository.save(areaPC);
        AreaDTO areaActualizadaDTO = new AreaDTO();
        BeanUtils.copyProperties(areaActualizada, areaActualizadaDTO);
        areaActualizadaDTO.setSede(areaActualizada.getSede().getId());
        return areaActualizadaDTO;
    }

    /**
     * Elimina (deshabilita) un área del sistema, marcándola como eliminada.
     *
     * @param id El ID del área a eliminar.
     * @throws LocationNotFoundException Si no se encuentra el área especificada.
     * @throws DeleteNotAllowedException Si la eliminación del área no está
     *                                   permitida.
     */
    @Override
    public void eliminarArea(Integer id) throws LocationNotFoundException, DeleteNotAllowedException, StateNotFoundException {
        AreaPC areaPC = areaRepository.findById(id).orElse(null);
        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "EL ÁREA").toUpperCase());
        }

        if(areaPC.getId() == 4){
            throw  new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR EL ÁREA  "+areaPC.getNombre()).toUpperCase());
        }

        if (areaPC.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR EL ÁREA  "+areaPC.getNombre()+" PORQUE YA SE ENCUENTRA INACTIVA").toUpperCase());
        }

        List<Ubicacion> ubicaciones = ubicacionRepository.findByArea(areaPC);

        if (ubicaciones.isEmpty()) {
            areaRepository.delete(areaPC);
        } else {
            areaPC.setDeleteFlag(true);
            areaRepository.save(areaPC);

            for (Ubicacion ubicacion : ubicaciones) {
                ubicacion.setDeleteFlag(true);
                ubicacionRepository.save(ubicacion);

                 List<Computador> computadores = computadorRepository.findByUbicacion(ubicacion);

                 for (Computador computador : computadores) {

                     Ubicacion antiguaUbicacion = computador.getUbicacion();

                        EstadoDispositivo estadoComputador = estadoDispositivoRepository.findById(4).orElse(null); // Estado
                                                                                                                   // Disponible
                        if (estadoComputador == null) {
                            throw new StateNotFoundException(String
                                    .format(IS_NOT_FOUND, "EL ESTADO DISPONIBLE NO FUE ENCONTRADO").toUpperCase());
                        }

                        computador.setEstadoDispositivo(estadoComputador);

                        Ubicacion bodegaSistemas = ubicacionRepository.findById(4).orElse(null);

                        if (bodegaSistemas == null) {
                            throw new StateNotFoundException(String
                                    .format(IS_NOT_FOUND, "LA BODEGA DE SISTEMAS DE LA SEDE PRINCIPAL").toUpperCase());
                        }

                        computadorServiceImplementation.crearCambioUbicacionPc(computador, antiguaUbicacion, bodegaSistemas, "El Área fue desactivada y el computador "+computador.getNombre()+" fue movido a la bodega de sistemas de la sede principal"); 
                        
                        computador.setUbicacion(bodegaSistemas);
                        computador.setResponsable(null);

                        List<HistorialDispositivo> historialDispositivos = historialDispositivoRepository
                                .findByComputadorAndFechaDesvinculacionIsNull(computador);

                        for (HistorialDispositivo historialDispositivo : historialDispositivos) {

                            DispositivoPC dispositivoPC = historialDispositivo.getDispositivoPC();
                            if (dispositivoPC.getTipoDispositivo().getId() != 8) {
                                historialDispositivo.setFechaDesvinculacion(new Date());
                                historialDispositivo.setJustificacion("El dispositivo fue desvinculado, porque el área "+areaPC.getNombre().toLowerCase()+ " fue desactivada y el computador fue movido a la bodega de sistemas"); 
                                historialDispositivoRepository.save(historialDispositivo);

                            }

                            dispositivoPC.setEstadoDispositivo(estadoComputador);
                            dispositivoRepository.save(dispositivoPC);
                        }

                        computadorRepository.save(computador);
                 }
            }
        }
    }

    @Override
    public void activarArea(Integer id) throws LocationNotFoundException, ActivateNotAllowedException {
        AreaPC areaPC = areaRepository.findById(id).orElse(null);
        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "EL ÁREA").toUpperCase());
        }

        if (areaPC.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(String.format(IS_NOT_ALLOWED, "ACTIVAR EL ÁREA "+areaPC.getNombre()+" PORQUE YA SE ENCUENTRA ACTIVADA").toUpperCase());
        }
        areaPC.setDeleteFlag(false);
        areaRepository.save(areaPC);

        List<Ubicacion> ubicaciones = ubicacionRepository.findByArea(areaPC);

        for (Ubicacion ubicacion : ubicaciones) {
            ubicacion.setDeleteFlag(false);
            ubicacionRepository.save(ubicacion);
        }

    }
}
