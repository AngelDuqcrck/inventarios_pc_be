package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.entities.CambioUbicacionPc;
import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.AreaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.CambioUbicacionPcRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.HistorialDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SedeRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UbicacionRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UsuarioRepository;
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
    private ComputadorServiceImplementation computadorServiceImplementation;

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

    @Autowired
    private UsuarioRepository usuarioRepository;

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

        List<Ubicacion> ubicacionesExistentes = ubicacionRepository.findByArea(areaPC);

        for (Ubicacion ubicacionExistente : ubicacionesExistentes) {
            if (ubicacionExistente.getNombre().equalsIgnoreCase(ubicacionDTO.getNombre())) {
                throw new SelectNotAllowedException(
                        String.format("YA EXISTE UNA UBICACIÓN CON EL NOMBRE '%s' EN EL ÁREA '%s'.",
                                ubicacionDTO.getNombre().toUpperCase(), areaPC.getNombre().toUpperCase()));
            }
        }
        ubicacion.setArea(areaPC);
        ubicacion.setDeleteFlag(false);
        ubicacion.setEstaOcupada(false);

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
            throw new UpdateNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTA UBICACION").toUpperCase());
        }

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

        if (ubicacionDTO.getNombre() != null) {
            List<Ubicacion> ubicacionesExistentes = ubicacionRepository.findByArea(ubicacion.getArea());

            for (Ubicacion ubicacionExistente : ubicacionesExistentes) {
                if (ubicacionExistente.getNombre().equalsIgnoreCase(ubicacionDTO.getNombre())) {
                    throw new SelectNotAllowedException(String.format(
                            "YA EXISTE UNA UBICACIÓN CON EL NOMBRE '%s' EN EL ÁREA '%s'.",
                            ubicacionDTO.getNombre().toUpperCase(), ubicacion.getArea().getNombre().toUpperCase()));
                }
            }
        }

        BeanUtils.copyProperties(ubicacionDTO, ubicacion);
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
    public void eliminarUbicacion(Integer id)
            throws LocationNotFoundException, DeleteNotAllowedException, StateNotFoundException {
        Ubicacion ubicacion = ubicacionRepository.findById(id).orElse(null);

        if (ubicacion == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());

        }
        if (ubicacion.getDeleteFlag() == true) {
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "ELIMINAR ESTA UBICACION").toUpperCase());

        }

        if (ubicacion.getId() == 4) {
            throw new DeleteNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ELIMINAR LA UBICACION " + ubicacion.getNombre()).toUpperCase());
        }

        List<Usuario> usuarios = usuarioRepository.findByUbicacionId(ubicacion);
        List<Computador> computadors = computadorRepository.findByUbicacion(ubicacion);
        List<CambioUbicacionPc> cambioUbicacionPcs = cambioUbicacionPcRepository.findByUbicacion(ubicacion);

        if (usuarios.isEmpty() && computadors.isEmpty() && cambioUbicacionPcs.isEmpty()) {
            ubicacionRepository.delete(ubicacion);
        } else {
            ubicacion.setDeleteFlag(true);
            ubicacionRepository.save(ubicacion);

            for (Computador computador : computadors) {
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

                computadorServiceImplementation.crearCambioUbicacionPc(computador, antiguaUbicacion, bodegaSistemas, "La ubicacion fue desactivada y el computador "+computador.getNombre()+" fue movido a la bodega de sistemas de la sede principal"); 

                computador.setUbicacion(bodegaSistemas);
                computador.setResponsable(null);

                List<HistorialDispositivo> historialDispositivos = historialDispositivoRepository
                        .findByComputadorAndFechaDesvinculacionIsNull(computador);

                for (HistorialDispositivo historialDispositivo : historialDispositivos) {

                    DispositivoPC dispositivoPC = historialDispositivo.getDispositivoPC();
                    if (dispositivoPC.getTipoDispositivo().getId() != 8) {
                        historialDispositivo.setFechaDesvinculacion(new Date());
                        historialDispositivo.setJustificacion("El dispositivo fue desvinculado, porque la ubicacion "+ubicacion.getNombre().toLowerCase()+ " fue eliminada y el computador fue movido a la bodega de sistemas"); 
                        historialDispositivoRepository.save(historialDispositivo);

                    }

                    dispositivoPC.setEstadoDispositivo(estadoComputador);
                    dispositivoRepository.save(dispositivoPC);
                }

                computadorRepository.save(computador);
            }
        }
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
    public List<UbicacionResponse> listarUbicacionesPorAreaYOcupadaIsFalse(Integer areaId) throws LocationNotFoundException {
        AreaPC areaPC = areaRepository.findById(areaId).orElse(null);
        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "EL AREA").toUpperCase());
        }
        List<Ubicacion> ubicaciones = ubicacionRepository.findByAreaAndEstaOcupadaIsFalse(areaPC);
        List<UbicacionResponse> ubicacionesResponses = new ArrayList<>();
        for (Ubicacion ubicacion : ubicaciones) {
            UbicacionResponse ubicacionResponse = new UbicacionResponse();
            BeanUtils.copyProperties(ubicacion, ubicacionResponse);
            ubicacionResponse.setArea(ubicacion.getArea().getNombre());
            ubicacionResponse.setSede(ubicacion.getArea().getSede().getNombre());
            ubicacionResponse.setEstaOcupada(ubicacion.getEstaOcupada());
            ubicacionesResponses.add(ubicacionResponse);
        }
        return ubicacionesResponses;
    }
    @Override
    public List<Ubicacion> listarUbicacionesPorArea(Integer areaId)
            throws LocationNotFoundException, SelectNotAllowedException {
        AreaPC areaPC = areaRepository.findById(areaId).orElse(null);

        if (areaPC == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "AREA").toUpperCase());
        }

        if (areaPC.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA AREA").toUpperCase());
        }

        List<Ubicacion> ubicaciones = ubicacionRepository.findByArea(areaPC);

        if (ubicaciones.isEmpty()) {
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
            throw new ActivateNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ACTIVAR ESTA UBICACION").toUpperCase());

        }
        ubicacion.setDeleteFlag(false);
        ubicacionRepository.save(ubicacion);
    }
}
