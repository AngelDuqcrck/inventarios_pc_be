package com.inventarios.pc.inventarios_pc_be.services.implementations;

import com.inventarios.pc.inventarios_pc_be.entities.TipoPC;
import com.inventarios.pc.inventarios_pc_be.entities.TipoRam;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.ChangeNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComponentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DuplicateEntityException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MarcaNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MiscellaneousNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.OwnerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeDeviceNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypePcNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.controllers.NotificationController;
import com.inventarios.pc.inventarios_pc_be.entities.CambioUbicacionPc;
import com.inventarios.pc.inventarios_pc_be.entities.Componente;
import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.DispositivoPC;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.HistorialDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.Marca;
import com.inventarios.pc.inventarios_pc_be.entities.Propietario;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwareCSA;
import com.inventarios.pc.inventarios_pc_be.entities.TipoAlmacenamiento;
import com.inventarios.pc.inventarios_pc_be.entities.TipoAlmacenamientoRam;
import com.inventarios.pc.inventarios_pc_be.entities.TipoDispositivo;
import com.inventarios.pc.inventarios_pc_be.repositories.CambioUbicacionPcRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.ComponenteRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.DispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.HistorialDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.MarcaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.PropietarioRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.SoftwareCsaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoAlmacenamientoRamRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoAlmacenamientoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoPcRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoRamRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UbicacionRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UsuarioRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IComputadorService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.ComputadorDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.UbicarPcRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComputadorIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComputadoresResponse;

@Service
public class ComputadorServiceImplementation implements IComputadorService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "no esta permitido %s ";
    public static final String IS_NOT_VALID = "no es valido %s";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";

    @Autowired
    private SoftwareCsaRepository softwareCSARepository;
    @Autowired
    private PropietarioRepository propietarioRepository;

    @Autowired
    private TipoRamRepository tipoRamRepository;

    @Autowired
    private TipoAlmacenamientoRepository tipoAlmacenamientoRepository;

    @Autowired
    private HistorialDispositivoRepository historialDispositivoRepository;

    @Autowired
    private DispositivoRepository dispositivoRepository;
    @Autowired
    private TipoDispositivoRepository tipoDispositivoRepository;

    @Autowired
    private ComputadorRepository computadorRepository;

    @Autowired
    private TipoPcRepository tipoPcRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ComponenteRepository componenteRepository;

    @Autowired
    private EstadoDispositivoRepository estadoDispositivoRepository;

    @Autowired
    private CambioUbicacionPcRepository cambioUbicacionPcRepository;

    @Autowired
    private NotificationController notificationController;

    @Override
    public ComputadorDTO crearComputador(ComputadorDTO computadorDTO)
            throws TypePcNotFoundException, SelectNotAllowedException, UserNotFoundException,
            LocationNotFoundException, ComponentNotFoundException, MiscellaneousNotFoundException,
            StateNotFoundException, MarcaNotFoundException, TypeDeviceNotFoundException, OwnerNotFoundException, DuplicateEntityException {
        Computador computador = new Computador();
        if(computadorRepository.existsByNombreIgnoreCase(computadorDTO.getNombre())){
            throw new DuplicateEntityException("Ya existe un computador registrado con el nombre "+computadorDTO.getNombre());
        }

        if(computadorRepository.existsByPlacaIgnoreCase(computadorDTO.getPlaca())){
            throw new DuplicateEntityException("Ya existe un computador registrado con la placa "+computadorDTO.getPlaca());
        }

        if(computadorRepository.existsByIpAsignadaIgnoreCase(computadorDTO.getIpAsignada())){
            throw new DuplicateEntityException("Ya existe un computador registrado con la ip "+computadorDTO.getIpAsignada());
        }
        
        BeanUtils.copyProperties(computadorDTO, computador);

        TipoPC tipoPC = tipoPcRepository.findById(computadorDTO.getTipoPC()).orElse(null);
        if (tipoPC == null) {
            throw new TypePcNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE PC").toUpperCase());
        }

        if (tipoPC.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED,
                            "SELECCIONAR EL TIPO DE PC " + tipoPC.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADO")
                            .toUpperCase());
        }

        computador.setTipoPC(tipoPC);

        computador.setResponsable(null);

        Ubicacion bodegaSistemas = ubicacionRepository.findById(4).orElse(null);

        if (bodegaSistemas == null) {
            throw new StateNotFoundException(String
                    .format(IS_NOT_FOUND, "LA BODEGA DE SISTEMAS DE LA SEDE PRINCIPAL").toUpperCase());
        }
        computador.setUbicacion(bodegaSistemas);

        Marca marca = marcaRepository.findById(computadorDTO.getMarca()).orElse(null);

        if (marca == null) {
            throw new MarcaNotFoundException(String.format(IS_NOT_FOUND_F, "LA MARCA").toUpperCase());
        }

        if (marca.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String
                    .format(IS_NOT_ALLOWED,
                            "SELECCIONAR LA MARCA " + marca.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADA")
                    .toUpperCase());
        }

        computador.setMarca(marca);

        Componente procesador = componenteRepository.findById(computadorDTO.getProcesador()).orElse(null);

        if (procesador == null) {
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "EL PROCESADOR").toUpperCase());

        }

        if (!procesador.getTipoComponente().getNombre().equals("Procesador")) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_VALID, "EL COMPONENTE SELECCIONADO PORQUE NO ES UN PROCESADOR").toUpperCase());
        }

        if (procesador.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED,
                            "SELECCIONAR EL PROCESADOR " + procesador.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADO")
                            .toUpperCase());
        }

        computador.setProcesador(procesador);

        Componente ram = componenteRepository.findById(computadorDTO.getRam()).orElse(null);

        if (ram == null) {
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND_F, "LA MEMORIA RAM").toUpperCase());
        }

        if (!ram.getTipoComponente().getNombre().equals("Cantidad de memoria RAM")) {
            throw new SelectNotAllowedException(String
                    .format(IS_NOT_VALID, "EL COMPONENTE SELECCIONADO PORQUE NO ES UNA MEMORIA RAM").toUpperCase());
        }

        if (ram.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String
                    .format(IS_NOT_ALLOWED,
                            "SELECCIONAR LA MEMORIA RAM " + ram.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADA")
                    .toUpperCase());
        }

        computador.setRam(ram);

        Componente almacenamiento = componenteRepository.findById(computadorDTO.getAlmacenamiento()).orElse(null);

        if (almacenamiento == null) {
            throw new ComponentNotFoundException(
                    String.format(IS_NOT_FOUND, "DISPOSITIVO DE ALMACENAMIENTO").toUpperCase());
        }

        if (!almacenamiento.getTipoComponente().getNombre().equals("Cantidad de disco duro")) {
            throw new SelectNotAllowedException(String
                    .format(IS_NOT_VALID, "EL COMPONENTE SELECCIONADO PORQUE NO ES UN DISPOSITIVO DE ALMACENAMIENTO")
                    .toUpperCase());
        }

        if (almacenamiento.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED,
                            "SELECCIONAR EL DISPOSITIVO DE ALMACENAMIENTO PORQUE ESTA DESACTIVADO").toUpperCase());
        }

        computador.setAlmacenamiento(almacenamiento);

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("Disponible").orElse(null);
        if (estadoDispositivo == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO DEL COMPUTADOR").toUpperCase());
        }

        computador.setEstadoDispositivo(estadoDispositivo);

        TipoAlmacenamiento tipoAlmacenamiento = tipoAlmacenamientoRepository
                .findById(computadorDTO.getTipoAlmacenamiento()).orElse(null);

        if (tipoAlmacenamiento == null) {
            throw new MiscellaneousNotFoundException(
                    String.format(IS_NOT_FOUND, "EL TIPO DE ALMACENAMIENTO").toUpperCase());
        }

        if (tipoAlmacenamiento.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_VALID,
                            "SELECCIONAR EL TIPO DE ALMACENAMIENTO " + tipoAlmacenamiento.getNombre()
                                    + " PORQUE SE ENCUENTRA INACTIVO")
                            .toUpperCase());
        }

        computador.setTipoAlmacenamiento(tipoAlmacenamiento);

        TipoRam tipoRam = tipoRamRepository.findById(computadorDTO.getTipoRam())
                .orElse(null);

        if (tipoRam == null) {
            throw new MiscellaneousNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE RAM").toUpperCase());
        }

        if (tipoRam.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED,
                            "SELECCIONAR EL TIPO DE RAM " + tipoRam.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADA")
                            .toUpperCase());
        }

        computador.setTipoRam(tipoRam);

        Propietario propietario = propietarioRepository.findById(computadorDTO.getPropietario()).orElse(null);

        if (propietario == null) {
            throw new OwnerNotFoundException(String.format(IS_NOT_FOUND, "EL PROPIETARIO").toUpperCase());
        }

        if (propietario.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE PROPIETARIO PORQUE SE ENCUENTRA DESACTIVADO")
                            .toUpperCase());
        }

        computador.setPropietario(propietario);

        Computador computadorCreado = computadorRepository.save(computador);
        DispositivoPC dispositivoPc = new DispositivoPC();

        EstadoDispositivo estadoTorre = estadoDispositivoRepository.findByNombre("En uso").orElse(null);
        if (estadoTorre == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO DE LA TORRE").toUpperCase());
        }

        TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(8).orElse(null);

        if (tipoDispositivo == null) {
            throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE DISPOSITIVO").toUpperCase());
        }

        dispositivoPc.setEstadoDispositivo(estadoTorre);
        dispositivoPc.setTipoDispositivo(tipoDispositivo);
        dispositivoPc.setMarca(computadorCreado.getMarca());
        dispositivoPc.setModelo(computadorCreado.getModelo());
        dispositivoPc.setNombre(computadorCreado.getNombre());
        dispositivoPc.setPlaca(computadorCreado.getPlaca());
        dispositivoPc.setSerial(computadorCreado.getSerial());
        dispositivoPc.setPropietario(computadorCreado.getPropietario());
        dispositivoRepository.save(dispositivoPc);

        HistorialDispositivo historialDispositivo = new HistorialDispositivo();
        historialDispositivo.setComputador(computadorCreado);
        boolean existeDispositivoMismoTipo = historialDispositivoRepository
                .existsByComputadorAndDispositivoPC_TipoDispositivoAndFechaDesvinculacionIsNull(
                        computador, dispositivoPc.getTipoDispositivo());

        if (existeDispositivoMismoTipo) {
            throw new SelectNotAllowedException(
                    String.format(IS_ALREADY_USE,
                            dispositivoPc.getTipoDispositivo().getNombre() + " DE ESTE COMPUTADOR")
                            .toUpperCase());
        }

        historialDispositivo.setDispositivoPC(dispositivoPc);

        historialDispositivo.setFechaCambio(new Date());

        historialDispositivoRepository.save(historialDispositivo);

        crearCambioUbicacionPc(computadorCreado, bodegaSistemas, null,
                "Se ha registrado el equipo " + computadorCreado.getNombre() + " en la bodega de sistemas");
        ComputadorDTO computadorCreadoDto = new ComputadorDTO();

        BeanUtils.copyProperties(computadorCreado, computadorCreadoDto);
        computadorCreadoDto.setTipoPC(computadorCreado.getTipoPC().getId());
        computadorCreadoDto.setUbicacion(computadorCreado.getUbicacion().getId());
        computadorCreadoDto.setMarca(computadorCreado.getMarca().getId());
        computadorCreadoDto.setProcesador(computadorCreado.getProcesador().getId());
        computadorCreadoDto.setRam(computadorCreado.getRam().getId());
        computadorCreadoDto.setAlmacenamiento(computadorCreado.getAlmacenamiento().getId());
        computadorCreadoDto.setEstadoDispositivo(computadorCreado.getEstadoDispositivo().getId());
        computadorCreadoDto.setTipoAlmacenamiento(computadorCreado.getTipoAlmacenamiento().getId());
        computadorCreadoDto.setTipoRam(computadorCreado.getTipoRam().getId());

        return computadorCreadoDto;

    }

    @Override
    public void ubicarPc(UbicarPcRequest ubicarPcRequest) throws ComputerNotFoundException, SelectNotAllowedException,
            UserNotFoundException, LocationNotFoundException, StateNotFoundException {

        Computador computador = computadorRepository.findById(ubicarPcRequest.getComputadorId()).orElse(null);

        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        if (!computador.getEstadoDispositivo().getNombre().equals("Disponible")
                && !computador.getEstadoDispositivo().getNombre().equals("En uso")) {
            throw new SelectNotAllowedException(String
                    .format(IS_NOT_ALLOWED,
                            "SELECCIONAR ESTE COMPUTADOR PORQUE SU ESTADO ES DIFERENTE A DISPONIBLE O EN USO")
                    .toUpperCase());
        }
        Ubicacion ubicacionActual = computador.getUbicacion();

        Usuario usuario = usuarioRepository.findById(ubicarPcRequest.getUsuarioId()).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "EL USUARIO").toUpperCase());
        }

        if (usuario.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String
                    .format(IS_NOT_ALLOWED, "SELECCIONAR ESTE USUARIO PORQUE SE ENCUENTRA DESACTIVADO").toUpperCase());
        }

        computador.setResponsable(usuario);

        Ubicacion ubicacion = ubicacionRepository.findById(ubicarPcRequest.getUbicacionId()).orElse(null);

        if (ubicacion == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND_F, "LA UBICACION").toUpperCase());
        }

        if (ubicacion.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED,
                            "SELECCIONAR LA UBICACION " + ubicacion.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADA")
                            .toUpperCase());
        }

        computador.setUbicacion(ubicacion);
        crearCambioUbicacionPc(computador, ubicacion, ubicacionActual, ubicarPcRequest.getJustificacion());

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("En uso").orElse(null);
        if (estadoDispositivo == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO EN USO").toUpperCase());
        }

        computador.setEstadoDispositivo(estadoDispositivo);

        computadorRepository.save(computador);
    }

    @Override
    public List<ComputadoresResponse> listarComputadoresByUsuario(Integer usuarioId) throws UserNotFoundException {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "EL USUARIO").toUpperCase());
        }

        List<Computador> computadores = computadorRepository.findByResponsable(usuario);
        List<ComputadoresResponse> computadoresResponses = new ArrayList<>();

        for (Computador computador : computadores) {
            ComputadoresResponse computadorResponse = new ComputadoresResponse();
            BeanUtils.copyProperties(computador, computadorResponse);
            if (computador.getResponsable() != null) {
                computadorResponse.setResponsable(
                        computador.getResponsable().getPrimerNombre() + " "
                                + computador.getResponsable().getSegundoNombre()
                                + " " + computador.getResponsable().getPrimerApellido() + " "
                                + computador.getResponsable().getSegundoApellido());
            } else {
                computadorResponse.setResponsable(null);
            }
            computadorResponse.setTipoPC(computador.getTipoPC().getNombre());
            computadorResponse.setUbicacion(computador.getUbicacion().getNombre());
            computadorResponse.setEstadoDispositivo(computador.getEstadoDispositivo().getNombre());

            computadoresResponses.add(computadorResponse);
        }

        return computadoresResponses;
    }

    @Override
    public List<ComputadoresResponse> listarComputadoresByEmail(String usuarioEmail) throws UserNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(usuarioEmail).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "EL USUARIO").toUpperCase());
        }

        List<Computador> computadores = computadorRepository.findByResponsable(usuario);
        List<ComputadoresResponse> computadoresResponses = new ArrayList<>();

        for (Computador computador : computadores) {
            ComputadoresResponse computadorResponse = new ComputadoresResponse();
            BeanUtils.copyProperties(computador, computadorResponse);
            if (computador.getResponsable() != null) {
                computadorResponse.setResponsable(
                        computador.getResponsable().getPrimerNombre() + " "
                                + computador.getResponsable().getSegundoNombre()
                                + " " + computador.getResponsable().getPrimerApellido() + " "
                                + computador.getResponsable().getSegundoApellido());
            } else {
                computadorResponse.setResponsable(null);
            }

            computadorResponse.setTipoPC(computador.getTipoPC().getNombre());
            computadorResponse.setUbicacion(computador.getUbicacion().getNombre());
            computadorResponse.setArea(computador.getUbicacion().getArea().getNombre());
            computadorResponse.setSede(computador.getUbicacion().getArea().getSede().getNombre());
            computadorResponse.setEstadoDispositivo(computador.getEstadoDispositivo().getNombre());
            computadorResponse.setMarca(computador.getMarca().getNombre());

            computadoresResponses.add(computadorResponse);
        }

        return computadoresResponses;
    }

    @Override
    public List<ComputadoresResponse> listarComputadoresByUbicacion(Integer ubicacionId)
            throws LocationNotFoundException {
        Ubicacion ubicacion = ubicacionRepository.findById(ubicacionId).orElse(null);

        if (ubicacion == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND_F, "LA UBICACION").toUpperCase());
        }

        List<Computador> computadores = computadorRepository.findByUbicacion(ubicacion);
        List<ComputadoresResponse> computadoresResponses = new ArrayList<>();

        for (Computador computador : computadores) {
            ComputadoresResponse computadorResponse = new ComputadoresResponse();
            BeanUtils.copyProperties(computador, computadorResponse);
            if (computador.getResponsable() != null) {
                computadorResponse.setResponsable(
                        computador.getResponsable().getPrimerNombre() + " "
                                + computador.getResponsable().getSegundoNombre()
                                + " " + computador.getResponsable().getPrimerApellido() + " "
                                + computador.getResponsable().getSegundoApellido());
            } else {
                computadorResponse.setResponsable(null);
            }
            computadorResponse.setTipoPC(computador.getTipoPC().getNombre());
            computadorResponse.setMarca(computador.getMarca().getNombre());
            computadorResponse.setModelo(computador.getModelo());
            computadorResponse.setSerial(computador.getSerial());
            computadorResponse.setPlaca(computador.getPlaca());
            computadorResponse.setUbicacion(computador.getUbicacion().getNombre());
            computadorResponse.setEstadoDispositivo(computador.getEstadoDispositivo().getNombre());

            computadoresResponses.add(computadorResponse);
        }

        return computadoresResponses;
    }

    @Override
    public List<ComputadoresResponse>  listarComputadoresByEstadoId(Integer estadoId){
        List<Computador> computadores = computadorRepository.findByEstadoDispositivo_Id(estadoId);
        List<ComputadoresResponse> computadoresResponses = new ArrayList<>();

        for (Computador computador : computadores) {
            ComputadoresResponse computadorResponse = new ComputadoresResponse();
            BeanUtils.copyProperties(computador, computadorResponse);
            if (computador.getResponsable() != null) {
                computadorResponse.setResponsable(
                        computador.getResponsable().getPrimerNombre() + " "
                                + computador.getResponsable().getSegundoNombre()
                                + " " + computador.getResponsable().getPrimerApellido() + " "
                                + computador.getResponsable().getSegundoApellido());
                // campo añadido para ubicar al usuario al momento de autocompletar el dropdown
                // usuarios al cambiar de lugar un pc
                computadorResponse.setPrimerNombreUser(computador.getResponsable().getPrimerNombre());
            } else {
                computadorResponse.setResponsable(null);
                computadorResponse.setPrimerNombreUser(null);
            }
            computadorResponse.setMarca(computador.getMarca().getNombre());
            computadorResponse.setTipoPC(computador.getTipoPC().getNombre());
            computadorResponse.setSede(computador.getUbicacion().getArea().getSede().getNombre());
            computadorResponse.setArea(computador.getUbicacion().getArea().getNombre());
            computadorResponse.setUbicacion(computador.getUbicacion().getNombre());
            computadorResponse.setEstadoDispositivo(computador.getEstadoDispositivo().getNombre());

            computadoresResponses.add(computadorResponse);
        }
        return computadoresResponses;
    }
    @Override
    public List<ComputadoresResponse> listarComputadores() {
        List<Computador> computadores = computadorRepository.findAll();

        List<ComputadoresResponse> computadoresResponses = new ArrayList<>();

        for (Computador computador : computadores) {
            ComputadoresResponse computadorResponse = new ComputadoresResponse();
            BeanUtils.copyProperties(computador, computadorResponse);
            if (computador.getResponsable() != null) {
                computadorResponse.setResponsable(
                        computador.getResponsable().getPrimerNombre() + " "
                                + computador.getResponsable().getSegundoNombre()
                                + " " + computador.getResponsable().getPrimerApellido() + " "
                                + computador.getResponsable().getSegundoApellido());
                // campo añadido para ubicar al usuario al momento de autocompletar el dropdown
                // usuarios al cambiar de lugar un pc
                computadorResponse.setPrimerNombreUser(computador.getResponsable().getPrimerNombre());
            } else {
                computadorResponse.setResponsable(null);
                computadorResponse.setPrimerNombreUser(null);
            }
            computadorResponse.setTipoPC(computador.getTipoPC().getNombre());
            computadorResponse.setSede(computador.getUbicacion().getArea().getSede().getNombre());
            computadorResponse.setArea(computador.getUbicacion().getArea().getNombre());
            computadorResponse.setUbicacion(computador.getUbicacion().getNombre());
            computadorResponse.setEstadoDispositivo(computador.getEstadoDispositivo().getNombre());

            computadoresResponses.add(computadorResponse);
        }

        return computadoresResponses;
    }

    @Override
    public ComputadorIdResponse listarComputadorById(Integer id) throws ComputerNotFoundException {
        Computador computador = computadorRepository.findById(id).orElse(null);

        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        ComputadorIdResponse computadorIdResponse = new ComputadorIdResponse();

        BeanUtils.copyProperties(computador, computadorIdResponse);
        computadorIdResponse.setTipoPC(computador.getTipoPC().getNombre());
        if (computador.getResponsable() != null) {
            computadorIdResponse.setResponsable(
                    computador.getResponsable().getPrimerNombre() + " " +
                            computador.getResponsable().getSegundoNombre() + " " +
                            computador.getResponsable().getPrimerApellido() + " " +
                            computador.getResponsable().getSegundoApellido());
            computadorIdResponse.setResPrimerNombre(computador.getResponsable().getPrimerNombre());
        } else {
            computadorIdResponse.setResponsable(null);
            computadorIdResponse.setResPrimerNombre(null);
        }
        computadorIdResponse.setPropietario(computador.getPropietario().getNombre());
        computadorIdResponse.setUbicacion(computador.getUbicacion().getNombre());
        computadorIdResponse.setMarca(computador.getMarca().getNombre());
        computadorIdResponse.setProcesador(computador.getProcesador().getNombre());
        computadorIdResponse.setRam(computador.getRam().getNombre());
        computadorIdResponse.setAlmacenamiento(computador.getAlmacenamiento().getNombre());
        computadorIdResponse.setEstadoDispositivo(computador.getEstadoDispositivo().getNombre());
        computadorIdResponse.setTipoAlmacenamiento(computador.getTipoAlmacenamiento().getNombre());
        computadorIdResponse.setTipoRam(computador.getTipoRam().getNombre());
        computadorIdResponse.setSede(computador.getUbicacion().getArea().getSede().getNombre());
        computadorIdResponse.setArea(computador.getUbicacion().getArea().getNombre());

        return computadorIdResponse;
    }

    @Override
    public void darBajaComputador(Integer id) throws ComputerNotFoundException, DeleteNotAllowedException {
        Computador computador = computadorRepository.findById(id).orElse(null);

        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        if (computador.getEstadoDispositivo().getNombre().equals("Baja")) {
            throw new DeleteNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "DAR DE BAJA ESTE COMPUTADOR PORQUE YA TIENE ESE ESTADO")
                            .toUpperCase());
        }

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("Baja").get();
        computador.setEstadoDispositivo(estadoDispositivo);
        computadorRepository.save(computador);

    }

    @Override
    public void cambiarEstadoPc(Integer computadorId, Integer nuevoEstadoDispositivoId)
            throws ComputerNotFoundException, StateNotFoundException, ChangeNotAllowedException {
        Computador computador = computadorRepository.findById(computadorId).orElse(null);

        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        EstadoDispositivo nuevoEstadoDispositivo = estadoDispositivoRepository.findById(nuevoEstadoDispositivoId)
                .orElse(null);
        if (nuevoEstadoDispositivo == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO DEL COMPUTADOR").toUpperCase());
        }
        List<HistorialDispositivo> historialDispositivos = historialDispositivoRepository
                .findByComputadorAndFechaDesvinculacionIsNull(computador);
        String estadoActual = computador.getEstadoDispositivo().getNombre();

        // Lógica de validación basada en las reglas de cambio de estado
        switch (nuevoEstadoDispositivoId) {
            case 1: // En uso
                if (!estadoActual.equals("Disponible") && !estadoActual.equals("En reparacion")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "EL ESTADO EN USO PORQUE SU ESTADO ACTUAL ES DIFERENTE A DISPONIBLE O EN REPARACIÓN")
                                    .toUpperCase());

                }

                for (HistorialDispositivo historialDispositivo : historialDispositivos) {
                    DispositivoPC dispositivoPC = historialDispositivo.getDispositivoPC();

                    if (dispositivoPC.getTipoDispositivo().getId() == 8) {
                        dispositivoPC.setEstadoDispositivo(nuevoEstadoDispositivo);
                        dispositivoRepository.save(dispositivoPC);
                    }
                }
                break;

            case 3: // Averiado
                if (!estadoActual.equals("En uso")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "EL ESTADO AVERIADO PORQUE SU ESTADO ACTUAL ES DIFERENTE A EN USO").toUpperCase());
                }

                EstadoDispositivo estadoDisponible = estadoDispositivoRepository.findById(4).orElse(null);
                if (estadoDisponible == null) {
                    throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO DISPONIBLE").toUpperCase());
                }
                for (HistorialDispositivo historialDispositivo : historialDispositivos) {

                    DispositivoPC dispositivoPC = historialDispositivo.getDispositivoPC();
                    if (dispositivoPC.getTipoDispositivo().getId() != 8) {
                        historialDispositivo.setFechaDesvinculacion(new Date());
                        historialDispositivo
                                .setJustificacion("El dispositivo fue desvinculado porque estaba el computador "
                                        + computador.getNombre().toLowerCase() + " se encuentra averiado");
                        historialDispositivoRepository.save(historialDispositivo);
                        dispositivoPC.setEstadoDispositivo(estadoDisponible);
                    } else {
                        dispositivoPC.setEstadoDispositivo(nuevoEstadoDispositivo);
                    }

                    dispositivoRepository.save(dispositivoPC);
                }
                break;

            case 2: // En reparacion
                if (!estadoActual.equals("Ninguno") && !estadoActual.equals("Averiado")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "EL ESTADO EN REPARACIÓN PORQUE SU ESTADO ACTUAL ES DIFERENTE A AVERIADO")
                                    .toUpperCase());
                }

               

                for (HistorialDispositivo historialDispositivo : historialDispositivos) {
                    DispositivoPC dispositivoPC = historialDispositivo.getDispositivoPC();

                    if (dispositivoPC.getTipoDispositivo().getId() == 8) {
                        dispositivoPC.setEstadoDispositivo(nuevoEstadoDispositivo);
                        dispositivoRepository.save(dispositivoPC);
                    }
                }
                break;

            case 4: // Disponible
                if (!estadoActual.equals("Disponible") && !estadoActual.equals("En reparacion")
                        && !estadoActual.equals("En uso")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "EL ESTADO DISPONIBLE PORQUE SU ESTADO ACTUAL ES DIFERENTE A EN REPARACION O EN USO")
                                    .toUpperCase());

                }

                Ubicacion ubicacion = ubicacionRepository.findById(4).orElse(null); // bodega sistemas
                Ubicacion antiguaUbicacion = computador.getUbicacion();
                if (ubicacion == null) {
                    throw new StateNotFoundException(
                            String.format(IS_NOT_FOUND, "LA BODEGA DE SISTEMAS DE LA SEDE PRINCIPAL").toUpperCase());
                }

                computador.setUbicacion(ubicacion);
                computador.setResponsable(null);
                crearCambioUbicacionPc(computador, ubicacion, antiguaUbicacion,
                        "El computador fue movido a la bodega de sistemas de la sede principal porque su estado cambio a disponible");

                if (estadoActual.equals("En uso")) {
                   
                    for (HistorialDispositivo historialDispositivo : historialDispositivos) {

                        DispositivoPC dispositivoPC = historialDispositivo.getDispositivoPC();
                        if (dispositivoPC.getTipoDispositivo().getId() != 8) {
                            historialDispositivo.setFechaDesvinculacion(new Date());
                            historialDispositivo
                                    .setJustificacion("El dispositivo fue desvinculado porque estaba el computador "
                                            + computador.getNombre().toLowerCase()
                                            + " fue movido a la bodega de sistemas");
                            historialDispositivoRepository.save(historialDispositivo);
                        }
                        dispositivoPC.setEstadoDispositivo(nuevoEstadoDispositivo);

                        dispositivoRepository.save(dispositivoPC);
                    }
                }
                break;

            case 5: // Baja
                if (!estadoActual.equals("Averiado") && !estadoActual.equals("Disponible")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED,
                                    "EL ESTADO DADO DE BAJA PORQUE SU ESTADO ACTUAL ES DIFERENTE A AVERIADO O DISPONIBLE")
                                    .toUpperCase());
                }
                for (HistorialDispositivo historialDispositivo : historialDispositivos) {
                    DispositivoPC dispositivoPC = historialDispositivo.getDispositivoPC();

                    if (dispositivoPC.getTipoDispositivo().getId() == 8) {
                        dispositivoPC.setEstadoDispositivo(nuevoEstadoDispositivo);
                        dispositivoRepository.save(dispositivoPC);
                    }
                }
                List<SoftwareCSA> softwareCSAs = softwareCSARepository
                        .findByComputadorAndFechaDesvinculacionIsNull(computador);

                for (SoftwareCSA softwareCSA : softwareCSAs) {
                    softwareCSA.setFechaDesvinculacion(new Date());
                    softwareCSA.setJustificacion("El software fue desvinculado porque estaba el computador "
                            + computador.getNombre().toLowerCase() + " fue dado de baja");
                    softwareCSARepository.save(softwareCSA);
                }
                break;

            default:
                throw new StateNotFoundException(String.format(IS_NOT_FOUND, "EL ESTADO DEL COMPUTADOR").toUpperCase());

        }

        computador.setEstadoDispositivo(nuevoEstadoDispositivo);
        computadorRepository.save(computador);
        notificationController.notifyStatusUpdate("COMPUTADOR", computador.getId(), computador.getEstadoDispositivo().getNombre(), null);
    }

    @Override
    public ComputadorDTO actualizarComputador(Integer computadorId, ComputadorDTO computadorDTO)
            throws TypePcNotFoundException, SelectNotAllowedException, UserNotFoundException,
            LocationNotFoundException, ComponentNotFoundException, MiscellaneousNotFoundException,
            StateNotFoundException, MarcaNotFoundException, UpdateNotAllowedException, ComputerNotFoundException,
            ChangeNotAllowedException, TypeDeviceNotFoundException, OwnerNotFoundException, DuplicateEntityException {

        Computador computador = computadorRepository.findById(computadorId).orElse(null);

        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "EL COMPUTADOR").toUpperCase());
        }

        if (computador.getEstadoDispositivo().getNombre().equals("Baja")) {
            throw new UpdateNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTE COMPUTADOR PORQUE ESTA EN ESTADO DADO DE BAJA")
                            .toUpperCase());
        }

        if(computadorRepository.existsByNombreIgnoreCaseAndIdNot(computadorDTO.getNombre(), computadorId)){
            throw new DuplicateEntityException("Ya existe un computador registrado con el nombre "+computadorDTO.getNombre());
        }
        if(computadorRepository.existsByPlacaIgnoreCaseAndIdNot(computadorDTO.getPlaca(), computadorId)){
            throw new DuplicateEntityException("Ya existe un computador registrado con la placa "+computadorDTO.getPlaca());
        }
        if(computadorRepository.existsByIpAsignadaIgnoreCaseAndIdNot(computadorDTO.getIpAsignada(), computadorId)){
            throw new DuplicateEntityException("Ya existe un computador registrado con la ip "+computadorDTO.getIpAsignada());
        }
        Ubicacion ubicacionActual = computador.getUbicacion();
        BeanUtils.copyProperties(computadorDTO, computador);
        computador.setId(computadorId);

        if (computadorDTO.getTipoPC() != null) {
            TipoPC tipoPC = tipoPcRepository.findById(computadorDTO.getTipoPC()).orElse(null);

            if (tipoPC == null) {
                throw new TypePcNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE COMPUTADOR").toUpperCase());
            }

            if (tipoPC.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED,
                                "SELECCIONAR EL TIPO DE COMPUTADOR " + tipoPC.getNombre() + " PORQUE ESTA DESACTIVADO")
                                .toUpperCase());
            }

            computador.setTipoPC(tipoPC);
        } else {
            computador.setTipoPC(computador.getTipoPC());
        }

        if (computadorDTO.getResponsable() != null) {
            Usuario usuario = usuarioRepository.findById(computadorDTO.getResponsable()).orElse(null);

            if (usuario == null) {
                throw new UserNotFoundException(String.format(IS_NOT_FOUND, "EL USUARIO RESPONSABLE").toUpperCase());
            }

            if (usuario.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE USUARIO PORQUE SE ENCUENTRA DESACTIVADO")
                                .toUpperCase());
            }

            computador.setResponsable(usuario);
        } else {
            computador.setResponsable(computador.getResponsable());
        }

        if (computadorDTO.getUbicacion() != null) {
            Ubicacion ubicacion = ubicacionRepository.findById(computadorDTO.getUbicacion()).orElse(null);

            if (ubicacion == null) {
                throw new LocationNotFoundException(String.format(IS_NOT_FOUND_F, "LA UBICACIÓN").toUpperCase());
            }

            if (ubicacion.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR LA UBICACIÓN " + ubicacion.getNombre()
                                + " PORQUE SE ENCUENTRA DESACTIVADA").toUpperCase());
            }

            computador.setUbicacion(ubicacion);

            crearCambioUbicacionPc(computador, ubicacion, ubicacionActual, computadorDTO.getJustificacion());
        } else {
            computador.setUbicacion(computador.getUbicacion());
        }

        if (computadorDTO.getMarca() != null) {
            Marca marca = marcaRepository.findById(computadorDTO.getMarca()).orElse(null);

            if (marca == null) {
                throw new MarcaNotFoundException(String.format(IS_NOT_FOUND_F, "LA MARCA").toUpperCase());

            }

            if (marca.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED,
                                "SELECCIONAR LA MARCA " + marca.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADA")
                                .toUpperCase());
            }

            computador.setMarca(marca);

        } else {
            computador.setMarca(computador.getMarca());
        }

        if (computadorDTO.getProcesador() != null) {
            Componente procesador = componenteRepository.findById(computadorDTO.getProcesador()).orElse(null);

            if (procesador == null) {
                throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "EL PROCESADOR").toUpperCase());
            }

            if (!procesador.getTipoComponente().getNombre().equals("Procesador")) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_VALID, "EL COMPONENTE SELECCIONADO PORQUE NO ES UN PROCESADOR")
                                .toUpperCase());
            }

            if (procesador.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR EL PROCESADOR " + procesador.getNombre()
                                + " PORQUE SE ENCUENTRA DESACTIVADO").toUpperCase());
            }

            computador.setProcesador(procesador);

        } else {
            computador.setProcesador(computador.getProcesador());
        }

        if (computadorDTO.getRam() != null) {
            Componente ram = componenteRepository.findById(computadorDTO.getRam()).orElse(null);

            if (ram == null) {
                throw new ComponentNotFoundException(String.format(IS_NOT_FOUND_F, "LA MEMORIA RAM").toUpperCase());
            }

            if (!ram.getTipoComponente().getNombre().equals("Cantidad de memoria RAM")) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_VALID, "EL COMPONENTE SELECCIONADO PORQUE NO ES UNA MEMORIA RAM")
                                .toUpperCase());
            }

            if (ram.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED,
                                "SELECCIONAR LA RAM " + ram.getNombre() + " PORQUE SE ENCUENTRA DESACTIVADA")
                                .toUpperCase());
            }

            computador.setRam(ram);
        } else {
            computador.setRam(computador.getRam());
        }

        if (computadorDTO.getAlmacenamiento() != null) {
            Componente almacenamiento = componenteRepository.findById(computadorDTO.getAlmacenamiento()).orElse(null);
            if (almacenamiento == null) {
                throw new ComponentNotFoundException(
                        String.format(IS_NOT_FOUND, "EL DISPOSITIVO DE ALMACENAMIENTO").toUpperCase());
            }

            if (!almacenamiento.getTipoComponente().getNombre().equals("Cantidad de disco duro")) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_VALID,
                                "EL COMPONENTE SELECCIONADO PORQUE NO ES UN DISPOSITIVO DE ALMACENAMIENTO")
                                .toUpperCase());

            }
            if (almacenamiento.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED,
                                "SELECCIONAR ESTE DISPOSITIVO DE ALMACENAMIENTO PORQUE ESTA DESACTIVADO")
                                .toUpperCase());
            }

            computador.setAlmacenamiento(almacenamiento);
        } else {

            computador.setAlmacenamiento(computador.getAlmacenamiento());

        }

        if (computadorDTO.getTipoAlmacenamiento() != null) {
            TipoAlmacenamiento tipoAlmacenamiento = tipoAlmacenamientoRepository
                    .findById(computadorDTO.getTipoAlmacenamiento()).orElse(null);

            if (tipoAlmacenamiento == null) {
                throw new MiscellaneousNotFoundException(
                        String.format(IS_NOT_FOUND, " EL TIPO DE ALMACENAMIENTO").toUpperCase());
            }

            if (tipoAlmacenamiento.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED,
                                "SELECCIONAR EL TIPO DE ALMACENAMIENTO " + tipoAlmacenamiento.getNombre()
                                        + " PORQUE SE ENCUENTRA DESACTIVADO")
                                .toUpperCase());
            }

            computador.setTipoAlmacenamiento(tipoAlmacenamiento);
        } else {
            computador.setTipoAlmacenamiento(computador.getTipoAlmacenamiento());
        }

        if (computadorDTO.getTipoRam() != null) {
            TipoRam tipoRam = tipoRamRepository.findById(computadorDTO.getTipoRam())
                    .orElse(null);

            if (tipoRam == null) {
                throw new MiscellaneousNotFoundException(
                        String.format(IS_NOT_FOUND, "EL TIPO DE MEMORIA RAM").toUpperCase());
            }

            if (tipoRam.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED,
                                "SELECCIONAR EL TIPO DE RAM " + tipoRam.getNombre()
                                        + " PORQUE SE ENCUENTRA DESACTIVADA")
                                .toUpperCase());
            }

            computador.setTipoRam(tipoRam);
        } else {
            computador.setTipoRam(computador.getTipoRam());
        }

        if (computadorDTO.getPropietario() != null) {
            Propietario propietario = propietarioRepository.findById(computadorDTO.getPropietario()).orElse(null);

            if (propietario == null) {
                throw new OwnerNotFoundException(String.format(IS_NOT_FOUND, "EL PROPIETARIO").toUpperCase());
            }

            if (propietario.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE PROPIETARIO PORQUE SE ENCUENTRA DESACTIVADO")
                                .toUpperCase());
            }

            computador.setPropietario(propietario);
        } else {
            computador.setPropietario(computador.getPropietario());
        }
        computador.setEstadoDispositivo(computador.getEstadoDispositivo());

        Computador computadorActualizado = computadorRepository.save(computador);
        ComputadorDTO computadorActualizadoDTO = new ComputadorDTO();
        BeanUtils.copyProperties(computadorActualizado, computadorActualizadoDTO);
        computadorActualizadoDTO.setTipoPC(computadorActualizado.getTipoPC().getId());
        computadorActualizadoDTO.setResponsable(computadorActualizado.getResponsable().getId());
        computadorActualizadoDTO.setUbicacion(computadorActualizado.getUbicacion().getId());
        computadorActualizadoDTO.setMarca(computadorActualizado.getMarca().getId());
        computadorActualizadoDTO.setProcesador(computadorActualizado.getProcesador().getId());
        computadorActualizadoDTO.setRam(computadorActualizado.getRam().getId());
        computadorActualizadoDTO.setAlmacenamiento(computadorActualizado.getAlmacenamiento().getId());
        computadorActualizadoDTO.setEstadoDispositivo(computadorActualizado.getEstadoDispositivo().getId());
        computadorActualizadoDTO.setTipoAlmacenamiento(computadorActualizado.getTipoAlmacenamiento().getId());
        computadorActualizadoDTO.setTipoRam(computadorActualizado.getTipoRam().getId());

        TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(8).orElse(null);

        if (tipoDispositivo == null) {
            throw new TypeDeviceNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE DISPOSITIVO").toUpperCase());
        }

        HistorialDispositivo historialDispositivo = historialDispositivoRepository
                .findFirstByComputadorAndDispositivoPC_TipoDispositivoAndFechaDesvinculacionIsNull(computador,
                        tipoDispositivo);
        if (historialDispositivo != null) {

            DispositivoPC torre = historialDispositivo.getDispositivoPC();

            torre.setMarca(computadorActualizado.getMarca());
            torre.setModelo(computadorActualizado.getModelo());
            torre.setNombre(computadorActualizado.getNombre());
            torre.setPlaca(computadorActualizado.getPlaca());
            torre.setSerial(computadorActualizado.getPlaca());

            dispositivoRepository.save(torre);
        }
        return computadorActualizadoDTO;
    }

    public void crearCambioUbicacionPc(Computador computador, Ubicacion ubicacionNueva, Ubicacion ubicacionActual,
            String justificacion) {

        CambioUbicacionPc cambioUbicacionPc = new CambioUbicacionPc();
        cambioUbicacionPc.setComputador(computador);
        cambioUbicacionPc.setUbicacion(ubicacionNueva);
        cambioUbicacionPc.setFechaIngreso(new Date());
        cambioUbicacionPc.setJustificacion(justificacion);

        if (ubicacionNueva != null) {
            CambioUbicacionPc ultimauUbicacionPc = cambioUbicacionPcRepository
                    .findTopByComputadorAndAndUbicacionOrderByFechaIngresoDesc(computador, ubicacionActual);
            if (ultimauUbicacionPc != null) {
                ultimauUbicacionPc.setFechaCambio(new Date());
                cambioUbicacionPcRepository.save(ultimauUbicacionPc);

            }

        }
        cambioUbicacionPcRepository.save(cambioUbicacionPc);
    }
}
