package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.ActivateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.ChangeNotAllowedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Componente;
import com.inventarios.pc.inventarios_pc_be.entities.TipoComponente;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComponentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UpdateNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.ComponenteRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.ComputadorRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoComponenteRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IComponenteService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.ComponenteDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComponenteResponse;

@Service
public class ComponenteServiceImplementation implements IComponenteService {
    public static final String IS_ALREADY_USE = "%s ya esta en uso";
    public static final String IS_NOT_FOUND = "%s no fue encontrado";
    public static final String IS_NOT_FOUND_F = "%s no fue encontrada";
    public static final String IS_NOT_ALLOWED = "no esta permitido %s";

    @Autowired
    private ComputadorRepository computadorRepository;
    @Autowired
    private ComponenteRepository componenteRepository;

    @Autowired
    private TipoComponenteRepository tipoComponenteRepository;

    @Override
    public ComponenteDTO crearComponente(ComponenteDTO componenteDTO)
            throws ComponentNotFoundException, SelectNotAllowedException {
        Componente componente = new Componente();

        BeanUtils.copyProperties(componenteDTO, componente);

        TipoComponente tipoComponente = tipoComponenteRepository.findById(componenteDTO.getTipoComponente())
                .orElse(null);

        if (tipoComponente == null) {
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "EL TIPO DE COMPONENTE").toUpperCase());
        }

        if (tipoComponente.getDeleteFlag() == true) {
            throw new

            SelectNotAllowedException(String.format(IS_NOT_ALLOWED,
                    "SELECCIONAR EL TIPO DE COMPONENTE " + tipoComponente.getNombre() + " PORQUE SE ENCUENTRA INACTIVO")
                    .toUpperCase());
        }
        componente.setTipoComponente(tipoComponente);
        componente.setDeleteFlag(false);

        Componente componenteCreado = componenteRepository.save(componente);
        ComponenteDTO componenteCreadoDTO = new ComponenteDTO();
        BeanUtils.copyProperties(componenteCreado, componenteCreadoDTO);
        return componenteCreadoDTO;
    }

    @Override
    public List<Componente> listarComponentes() {
        return (List<Componente>) componenteRepository.findAll();
    }

    @Override
    public ComponenteResponse listarComponenteById(Integer id) throws ComponentNotFoundException {
        Componente componente = componenteRepository.findById(id).orElse(null);
        if (componente == null) {
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "EL COMPONENTE").toUpperCase());
        }
        ComponenteResponse componenteResponse = new ComponenteResponse();
        BeanUtils.copyProperties(componente, componenteResponse);
        componenteResponse.setNombre(componente.getNombre()+" "+componente.getCantidad());
        componenteResponse.setTipoComponente(componente.getTipoComponente().getNombre());
        return componenteResponse;
    }

    public List<ComponenteDTO> listarComponentePorTipoComponente(Integer tipoComponenteId) {
        return null;
    }

    @Override
    public ComponenteDTO actualizarComponente(Integer id, ComponenteDTO componenteDTO)
            throws SelectNotAllowedException, UpdateNotAllowedException, ComponentNotFoundException {
        Componente componente = componenteRepository.findById(id).orElse(null);
        if (componente == null) {
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "EL COMPONENTE").toUpperCase());
        }

        if (componente.getDeleteFlag() == true) {
            throw new UpdateNotAllowedException(String
                    .format(IS_NOT_ALLOWED,
                            "ACTUALIZAR EL COMPONENTE " + componente.getNombre() + " PORQUE SE ENCUENTRA INACTIVO")
                    .toUpperCase());
        }
        BeanUtils.copyProperties(componenteDTO, componente);
        componente.setDeleteFlag(false);

        if (componenteDTO.getTipoComponente() != null) {
            TipoComponente tipoComponente = tipoComponenteRepository.findById(componenteDTO.getTipoComponente())
                    .orElse(null);
            if (tipoComponente == null) {
                throw new ComponentNotFoundException(
                        String.format(IS_NOT_FOUND, "EL TIPO DE COMPONENTE").toUpperCase());

            }
            if (tipoComponente.getDeleteFlag() == true) {
                throw new SelectNotAllowedException(
                        String.format(IS_NOT_ALLOWED, "SELECCIONAR EL TIPO DE COMPONENTE " + componente.getNombre()
                                + " PORQUE SE ENCUENTRA INACTIVO").toUpperCase());
            }
            componente.setTipoComponente(tipoComponente);
        } else {
            componente.setTipoComponente(componente.getTipoComponente());
        }

        Componente componenteActualizado = componenteRepository.save(componente);
        ComponenteDTO componenteActualizadoDTO = new ComponenteDTO();
        componenteActualizadoDTO.setTipoComponente(componenteActualizado.getTipoComponente().getId());
        BeanUtils.copyProperties(componenteActualizado, componenteActualizadoDTO);
        return componenteActualizadoDTO;
    }

    @Override
    public void eliminarComponente(Integer id) throws ComponentNotFoundException, DeleteNotAllowedException {
        
        Componente componente = componenteRepository.findById(id).orElse(null);

        
        if (componente == null) {
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "EL COMPONENTE").toUpperCase());
        }

        
        if (Boolean.TRUE.equals(componente.getDeleteFlag())) {
            throw new DeleteNotAllowedException(
                    String.format(IS_NOT_ALLOWED, "DESACTIVAR EL COMPONENTE " + componente.getNombre()
                            + " PORQUE YA SE ENCUENTRA DESACTIVADO").toUpperCase());
        }

        boolean estaVinculado = computadorRepository.existsByProcesadorOrRamOrAlmacenamiento(
                componente, componente, componente);

        if (!estaVinculado) {
            componenteRepository.delete(componente);
        } else {
            componente.setDeleteFlag(true);
            componenteRepository.save(componente);
        }
    }

    @Override
    public void activarComponente(Integer id) throws ComponentNotFoundException, ActivateNotAllowedException {
        Componente componente = componenteRepository.findById(id).orElse(null);
        if (componente == null) {
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "EL COMPONENTE").toUpperCase());
        }

        if (componente.getDeleteFlag() == false) {
            throw new ActivateNotAllowedException(
                    String.format(IS_NOT_ALLOWED,
                            "ACTIVAR EL COMPONENTE " + componente.getNombre() + " PORQUE YA SE ENCUENTRA ACTIVADO")
                            .toUpperCase());
        }
        componente.setDeleteFlag(false);
        componenteRepository.save(componente);
    }
}
