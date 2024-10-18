package com.inventarios.pc.inventarios_pc_be.services.implementations;

import com.inventarios.pc.inventarios_pc_be.entities.TipoPC;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.ChangeNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComponentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MarcaNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MiscellaneousNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypePcNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Componente;
import com.inventarios.pc.inventarios_pc_be.entities.Computador;
import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.entities.Marca;
import com.inventarios.pc.inventarios_pc_be.entities.TipoAlmacenamientoRam;
import com.inventarios.pc.inventarios_pc_be.repositories.ComponenteRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.EstadoDispositivoRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.MarcaRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoAlmacenamientoRamRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoPcRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UbicacionRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.UsuarioRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IComputadorService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.ComputadorDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComputadorIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComputadoresResponse;

@Service
public class ComputadorServiceImplementation implements IComputadorService {

    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_ALLOWED = "%s no esta permitido";
    public static final String IS_NOT_VALID = "%s no es valido";
    public static final String ARE_NOT_EQUALS = "%s no son iguales";
    public static final String IS_NOT_CORRECT = "%s no es correcto";

    
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
    private TipoAlmacenamientoRamRepository tipoAlmacenamientoRamRepository;

    @Override
    public ComputadorDTO crearComputador(ComputadorDTO computadorDTO)
            throws TypePcNotFoundException, SelectNotAllowedException, UserNotFoundException,
            LocationNotFoundException, ComponentNotFoundException, MiscellaneousNotFoundException,
            StateNotFoundException, MarcaNotFoundException {
        Computador computador = new Computador();
        BeanUtils.copyProperties(computadorDTO, computador);

        TipoPC tipoPC = tipoPcRepository.findById(computadorDTO.getTipoPC()).orElse(null);
        if (tipoPC == null) {
            throw new TypePcNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE PC").toUpperCase());
        }

        if (tipoPC.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE TIPO DE PC").toUpperCase());
        }

        computador.setTipoPC(tipoPC);

        Usuario usuario = usuarioRepository.findById(computadorDTO.getResponsable()).orElse(null);

        if (usuario == null) {
            throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO").toUpperCase());
        }

        if (usuario.getDeleteFlag() == true) {
            throw new

            SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE USUARIO").toUpperCase());
        }

        computador.setResponsable(usuario);

        Ubicacion ubicacion = ubicacionRepository.findById(computadorDTO.getUbicacion()).orElse(null);

        if (ubicacion == null) {
            throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());
        }

        if (ubicacion.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA UBICACION").toUpperCase());
        }

        computador.setUbicacion(ubicacion);

        Marca marca = marcaRepository.findById(computadorDTO.getMarca()).orElse(null);

        if (marca == null) {
            throw new MarcaNotFoundException(String.format(IS_NOT_FOUND, "MARCA").toUpperCase());
        }

        if (marca.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA MARCA").toUpperCase());
        }

        computador.setMarca(marca);

        Componente procesador = componenteRepository.findById(computadorDTO.getProcesador()).orElse(null);

        if (procesador == null) {
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "COMPONENTE").toUpperCase());

        }

        if (!procesador.getTipoComponente().getNombre().equals("Procesador")) {
            throw new SelectNotAllowedException(String.format(IS_NOT_VALID, "COMPONENTE SELECCIONADO").toUpperCase());
        }

        if (procesador.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE PROCESADOR").toUpperCase());
        }

        computador.setProcesador(procesador);

        Componente ram = componenteRepository.findById(computadorDTO.getRam()).orElse(null);

        if (ram == null) {
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "COMPONENTE").toUpperCase());
        }

        if (!ram.getTipoComponente().getNombre().equals("Memoria RAM")) {
            throw new SelectNotAllowedException(String.format(IS_NOT_VALID, "COMPONENTE SELECCIONADO").toUpperCase());
        }

        if (ram.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA RAM").toUpperCase());
        }

        computador.setRam(ram);

        Componente almacenamiento = componenteRepository.findById(computadorDTO.getAlmacenamiento()).orElse(null);

        if (almacenamiento == null) {
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "COMPONENTE").toUpperCase());
        }

        if (!almacenamiento.getTipoComponente().getNombre().equals("Almacenamiento")) {
            throw new SelectNotAllowedException(String.format(IS_NOT_VALID, "COMPONENTE SELECCIONADO").toUpperCase());
        }

        if (almacenamiento.getDeleteFlag() == true) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE ALMACENAMIENTO").toUpperCase());
        }

        computador.setAlmacenamiento(almacenamiento);

        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("Disponible").orElse(null);
        if (estadoDispositivo == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DE PC").toUpperCase());
        }

        computador.setEstadoDispositivo(estadoDispositivo);

        TipoAlmacenamientoRam tipoAlmacenamiento = tipoAlmacenamientoRamRepository
                .findById(computadorDTO.getTipoAlmacenamiento()).orElse(null);

        if (tipoAlmacenamiento == null) {
            throw new MiscellaneousNotFoundException(
                    String.format(IS_NOT_FOUND, "TIPO DE ALMACENAMIENTO").toUpperCase());
        }

        if (tipoAlmacenamiento.getId() > 3 && tipoAlmacenamiento.getId() <= 6) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_VALID, "ESTA SELECCION ES UN TIPO DE RAM Y ").toUpperCase());
        }

        computador.setTipoAlmacenamiento(tipoAlmacenamiento);

        TipoAlmacenamientoRam tipoRam = tipoAlmacenamientoRamRepository.findById(computadorDTO.getTipoRam())
                .orElse(null);

        if (tipoRam == null) {
            throw new MiscellaneousNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE RAM").toUpperCase());
        }

        if (tipoRam.getId() > 0 && tipoRam.getId() <= 3) {
            throw new SelectNotAllowedException(
                    String.format(IS_NOT_VALID, "ESTA SELECCION ES UN TIPO DE ALMACENAMIENTO Y ").toUpperCase());
        }

        computador.setTipoRam(tipoRam);

        Computador computadorCreado = computadorRepository.save(computador);

        ComputadorDTO computadorCreadoDto = new ComputadorDTO();

        BeanUtils.copyProperties(computadorCreado, computadorCreadoDto);
        computadorCreadoDto.setTipoPC(computadorCreado.getTipoPC().getId());
        computadorCreadoDto.setResponsable(computadorCreado.getResponsable().getId());
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
    public List<ComputadoresResponse> listarComputadores() {
        List<Computador> computadores = computadorRepository.findAll();

        List<ComputadoresResponse> computadoresResponses = new ArrayList<>();

        for (Computador computador : computadores) {
            ComputadoresResponse computadorResponse = new ComputadoresResponse();
            BeanUtils.copyProperties(computador, computadorResponse);
            computadorResponse.setResponsable(
                    computador.getResponsable().getPrimerNombre() + " " + computador.getResponsable().getSegundoNombre()
                            + " " + computador.getResponsable().getPrimerApellido() + " "
                            + computador.getResponsable().getSegundoApellido());
            computadorResponse.setTipoPC(computador.getTipoPC().getNombre());
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
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "COMPUTADOR").toUpperCase());
        }

        ComputadorIdResponse computadorIdResponse = new ComputadorIdResponse();

        BeanUtils.copyProperties(computador, computadorIdResponse);
        computadorIdResponse.setTipoPC(computador.getTipoPC().getNombre());
        computadorIdResponse.setResponsable(computador.getResponsable().getPrimerNombre() + " "
                + computador.getResponsable().getSegundoNombre() + " " + computador.getResponsable().getPrimerApellido()
                + " " + computador.getResponsable().getSegundoApellido());
        computadorIdResponse.setUbicacion(computador.getUbicacion().getNombre());
        computadorIdResponse.setMarca(computador.getMarca().getNombre());
        computadorIdResponse.setProcesador(computador.getProcesador().getNombre());
        computadorIdResponse.setRam(computador.getRam().getNombre());
        computadorIdResponse.setAlmacenamiento(computador.getAlmacenamiento().getNombre());
        computadorIdResponse.setEstadoDispositivo(computador.getEstadoDispositivo().getNombre());
        computadorIdResponse.setTipoAlmacenamiento(computador.getTipoAlmacenamiento().getNombre());
        computadorIdResponse.setTipoRam(computador.getTipoRam().getNombre());
        computadorIdResponse.setResPrimerNombre(computador.getResponsable().getPrimerNombre());

        return computadorIdResponse;
    }

    @Override
    public void darBajaComputador(Integer id) throws ComputerNotFoundException, DeleteNotAllowedException {
        Computador computador = computadorRepository.findById(id).orElse(null);

        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "COMPUTADOR").toUpperCase());
        }

        if (computador.getEstadoDispositivo().getNombre().equals("Baja")) {
            throw new DeleteNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "DAR DE BAJA ESTE COMPUTADOR").toUpperCase());
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
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "COMPUTADOR").toUpperCase());
        }

        EstadoDispositivo nuevoEstadoDispositivo = estadoDispositivoRepository.findById(nuevoEstadoDispositivoId)
                .orElse(null);
        if (nuevoEstadoDispositivo == null) {
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DEL COMPUTADOR").toUpperCase());
        }

        String estadoActual = computador.getEstadoDispositivo().getNombre();

        // Lógica de validación basada en las reglas de cambio de estado
        switch (nuevoEstadoDispositivoId) {
            case 1: // En uso
                if (!estadoActual.equals("Disponible") && !estadoActual.equals("En reparacion")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "CAMBIO DE ESTADO DEL COMPUTADOR").toUpperCase());
                }
                break;

            case 3: // Averiado
                if (!estadoActual.equals("En uso")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "CAMBIO DE ESTADO DEL COMPUTADOR").toUpperCase());
                }
                break;

            case 2: // En reparacion
                if (!estadoActual.equals("Ninguno") && !estadoActual.equals("Averiado")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "CAMBIO DE ESTADO DEL COMPUTADOR").toUpperCase());
                }
                break;

            case 4: // Disponible
                if (!estadoActual.equals("Disponible") && !estadoActual.equals("En reparacion")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "CAMBIO DE ESTADO DEL COMPUTADOR").toUpperCase());
                }
                break;

            case 5: // Baja
                if (!estadoActual.equals("Averiado") && !estadoActual.equals("Disponible")) {
                    throw new ChangeNotAllowedException(
                            String.format(IS_NOT_ALLOWED, "CAMBIO DE ESTADO DEL COMPUTADOR").toUpperCase());
                }
                break;

            default:
                throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DEL COMPUTADOR").toUpperCase());

        }

        computador.setEstadoDispositivo(nuevoEstadoDispositivo);
        computadorRepository.save(computador);
    }

    
    public ComputadorDTO actualizarComputador(Integer computadorId, ComputadorDTO computadorDTO)
            throws TypePcNotFoundException, SelectNotAllowedException, UserNotFoundException,
            LocationNotFoundException, ComponentNotFoundException, MiscellaneousNotFoundException,
            StateNotFoundException, MarcaNotFoundException, UpdateNotAllowedException, ComputerNotFoundException, ChangeNotAllowedException {

        Computador computador = computadorRepository.findById(computadorId).orElse(null);

        if (computador == null) {
            throw new ComputerNotFoundException(String.format(IS_NOT_FOUND, "COMPUTADOR").toUpperCase());
        }

        if (computador.getEstadoDispositivo().getNombre().equals("Baja")) {
            throw new UpdateNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "ACTUALIZAR ESTE COMPUTADOR").toUpperCase());
        }

        BeanUtils.copyProperties(computadorDTO, computador);
        computador.setId(computadorId);

        if (computadorDTO.getTipoPC() != null) {
            TipoPC tipoPC = tipoPcRepository.findById(computadorDTO.getTipoPC()).orElse(null);

            if (tipoPC == null) {
                throw new TypePcNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE COMPUTADOR").toUpperCase());
            }

            if (tipoPC.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE TIPO DE PC").toUpperCase());
            }

            computador.setTipoPC(tipoPC);
        } else {
            computador.setTipoPC(computador.getTipoPC());
        }

        if (computadorDTO.getResponsable() != null) {
            Usuario usuario = usuarioRepository.findById(computadorDTO.getResponsable()).orElse(null);

            if (usuario == null) {
                throw new UserNotFoundException(String.format(IS_NOT_FOUND, "USUARIO RESPONSABLE").toUpperCase());
            }

            if (usuario.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE RESPONSABLE").toUpperCase());
            }

            computador.setResponsable(usuario);
        } else {
            computador.setResponsable(computador.getResponsable());
        }

        if (computadorDTO.getUbicacion() != null) {
            Ubicacion ubicacion = ubicacionRepository.findById(computadorDTO.getUbicacion()).orElse(null);

            if (ubicacion == null) {
                throw new LocationNotFoundException(String.format(IS_NOT_FOUND, "UBICACION").toUpperCase());
            }

            if (ubicacion.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA UBICACION").toUpperCase());
            }

            computador.setUbicacion(ubicacion);
        } else {
            computador.setUbicacion(computador.getUbicacion());
        }

        if (computadorDTO.getMarca() != null) {
            Marca marca = marcaRepository.findById(computadorDTO.getMarca()).orElse(null);

            if (marca == null) {
                throw new MarcaNotFoundException(String.format(IS_NOT_FOUND, "MARCA").toUpperCase());

            }

            if (marca.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA MARCA").toUpperCase());
            }

            computador.setMarca(marca);

        } else {
            computador.setMarca(computador.getMarca());
        }

        if (computadorDTO.getProcesador() != null) {
            Componente procesador = componenteRepository.findById(computadorDTO.getProcesador()).orElse(null);

            if (procesador == null) {
                throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "COMPONENTE").toUpperCase());
            }

            if (!procesador.getTipoComponente().getNombre().equals("Procesador")) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_VALID, "COMPONENTE SELECCIONADO").toUpperCase());
            }

            if (procesador.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA PROCESADOR").toUpperCase());
            }

            computador.setProcesador(procesador);

        } else {
            computador.setProcesador(computador.getProcesador());
        }

        if (computadorDTO.getRam() != null) {
            Componente ram = componenteRepository.findById(computadorDTO.getRam()).orElse(null);

            if (ram == null) {
                throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "COMPONENTE").toUpperCase());
            }

            if (!ram.getTipoComponente().getNombre().equals("Memoria RAM")) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_VALID, "COMPONENTE SELECCIONADO").toUpperCase());
            }

            if (ram.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA RAM").toUpperCase());
            }

            computador.setRam(ram);
        } else {
            computador.setRam(computador.getRam());
        }

        if (computadorDTO.getAlmacenamiento() != null) {
            Componente almacenamiento = componenteRepository.findById(computadorDTO.getAlmacenamiento()).orElse(null);
            if (almacenamiento == null) {
                throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "COMPONENTE").toUpperCase());
            }

            if (!almacenamiento.getTipoComponente().getNombre().equals("Almacenamiento")) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_VALID, "COMPONENTE SELECCIONADO").toUpperCase());
            }

            if (almacenamiento.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE ALMACENAMIENTO").toUpperCase());
            }

            computador.setAlmacenamiento(almacenamiento);
        } else {

            computador.setAlmacenamiento(computador.getAlmacenamiento());

        }

        if (computadorDTO.getTipoAlmacenamiento() != null) {
            TipoAlmacenamientoRam tipoAlmacenamiento = tipoAlmacenamientoRamRepository
                    .findById(computadorDTO.getTipoAlmacenamiento()).orElse(null);

            if (tipoAlmacenamiento == null) {
                throw new MiscellaneousNotFoundException(
                        String.format(IS_NOT_FOUND, "TIPO DE ALMACENAMIENTO").toUpperCase());
            }

            if (tipoAlmacenamiento.getId() > 3 && tipoAlmacenamiento.getId() <= 6) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_VALID, "ESTA SELECCION ES UN TIPO DE RAM Y ").toUpperCase());
            }

            computador.setTipoAlmacenamiento(tipoAlmacenamiento);
        } else {
            computador.setTipoAlmacenamiento(computador.getTipoAlmacenamiento());
        }

        if (computadorDTO.getTipoRam() != null) {
            TipoAlmacenamientoRam tipoRam = tipoAlmacenamientoRamRepository.findById(computadorDTO.getTipoRam())
                    .orElse(null);

            if (tipoRam == null) {
                throw new MiscellaneousNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE RAM").toUpperCase());
            }

            if (tipoRam.getId() > 0 && tipoRam.getId() <= 3) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_VALID, "ESTA SELECCION ES UN TIPO DE ALMACENAMIENTO Y ").toUpperCase());
            }

            computador.setTipoRam(tipoRam);
        }else{
            computador.setTipoRam(computador.getTipoRam());
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

        return computadorActualizadoDTO;
    }
}