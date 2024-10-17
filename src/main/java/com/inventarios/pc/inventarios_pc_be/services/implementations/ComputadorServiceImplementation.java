package com.inventarios.pc.inventarios_pc_be.services.implementations;

import com.inventarios.pc.inventarios_pc_be.entities.TipoPC;
import com.inventarios.pc.inventarios_pc_be.entities.Ubicacion;
import com.inventarios.pc.inventarios_pc_be.entities.Usuario;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComponentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MarcaNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.MiscellaneousNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypePcNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;

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
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE TIPO DE PC").toUpperCase());
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
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA UBICACION").toUpperCase());
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

        if(!procesador.getTipoComponente().getNombre().equals("Procesador")){
            throw new SelectNotAllowedException(String.format(IS_NOT_VALID, "COMPONENTE SELECCIONADO").toUpperCase());
        }

        if(procesador.getDeleteFlag()==true){
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE PROCESADOR").toUpperCase());
        }

        computador.setProcesador(procesador);


        Componente ram = componenteRepository.findById(computadorDTO.getRam()).orElse(null);

        if(ram == null){
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "COMPONENTE").toUpperCase());
        }

        if(!ram.getTipoComponente().getNombre().equals("Memoria RAM")){
            throw new SelectNotAllowedException(String.format(IS_NOT_VALID, "COMPONENTE SELECCIONADO").toUpperCase());
        }

        if(ram.getDeleteFlag()==true){
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTA RAM").toUpperCase());
        }

        computador.setRam(ram);


        Componente almacenamiento = componenteRepository.findById(computadorDTO.getAlmacenamiento()).orElse(null);

        if(almacenamiento == null){
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "COMPONENTE").toUpperCase());
        }

        if(!almacenamiento.getTipoComponente().getNombre().equals("Almacenamiento")){
            throw new SelectNotAllowedException(String.format(IS_NOT_VALID, "COMPONENTE SELECCIONADO").toUpperCase());
        }

        if(almacenamiento.getDeleteFlag()==true){
            throw new SelectNotAllowedException(String.format(IS_NOT_ALLOWED, "SELECCIONAR ESTE ALMACENAMIENTO").toUpperCase());
        }

        computador.setAlmacenamiento(almacenamiento);


        EstadoDispositivo estadoDispositivo = estadoDispositivoRepository.findByNombre("Disponible").orElse(null);
        if(estadoDispositivo == null){
            throw new StateNotFoundException(String.format(IS_NOT_FOUND, "ESTADO DE PC").toUpperCase());
        }

        computador.setEstadoDispositivo(estadoDispositivo);

        TipoAlmacenamientoRam tipoAlmacenamiento = tipoAlmacenamientoRamRepository.findById(computadorDTO.getTipoAlmacenamiento()).orElse(null);

        if(tipoAlmacenamiento == null){
            throw new MiscellaneousNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE ALMACENAMIENTO").toUpperCase());
        }

        if(tipoAlmacenamiento.getId()>3 && tipoAlmacenamiento.getId()<=6){
            throw new SelectNotAllowedException(String.format(IS_NOT_VALID, "ESTA SELECCION ES UN TIPO DE RAM Y ").toUpperCase());
        }

        computador.setTipoAlmacenamiento(tipoAlmacenamiento);

        TipoAlmacenamientoRam tipoRam = tipoAlmacenamientoRamRepository.findById(computadorDTO.getTipoRam()).orElse(null);

        if(tipoRam == null){
            throw new MiscellaneousNotFoundException(String.format(IS_NOT_FOUND, "TIPO DE RAM").toUpperCase());
        }

        if(tipoRam.getId()>0 && tipoRam.getId()<=3){
            throw new SelectNotAllowedException(String.format(IS_NOT_VALID, "ESTA SELECCION ES UN TIPO DE ALMACENAMIENTO Y ").toUpperCase());
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


}
