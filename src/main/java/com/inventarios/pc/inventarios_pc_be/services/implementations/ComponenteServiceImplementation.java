package com.inventarios.pc.inventarios_pc_be.services.implementations;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventarios.pc.inventarios_pc_be.entities.Componente;
import com.inventarios.pc.inventarios_pc_be.entities.TipoComponente;
import com.inventarios.pc.inventarios_pc_be.exceptions.ComponentNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.repositories.ComponenteRepository;
import com.inventarios.pc.inventarios_pc_be.repositories.TipoComponenteRepository;
import com.inventarios.pc.inventarios_pc_be.services.interfaces.IComponenteService;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.ComponenteDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComponenteResponse;

@Service
public class ComponenteServiceImplementation implements IComponenteService {
    public static final String IS_ALREADY_USE = "The %s is already use";
    public static final String IS_NOT_FOUND = "The %s is not found";
    public static final String IS_NOT_ALLOWED = "The %s is not allowed";

    @Autowired
    private ComponenteRepository componenteRepository;

    @Autowired
    private TipoComponenteRepository tipoComponenteRepository;

    @Override
    public ComponenteDTO crearComponente(ComponenteDTO componenteDTO)throws ComponentNotFoundException{
        Componente componente = new Componente();

        BeanUtils.copyProperties(componenteDTO, componente);

        TipoComponente tipoComponente = tipoComponenteRepository.findById(componenteDTO.getTipoComponente()).orElse(null);

        if(tipoComponente == null){
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "TYPE COMPONENT").toUpperCase());
        }

        componente.setTipoComponente(tipoComponente);
        componente.setDeleteFlag(false);
        Componente componenteCreado = componenteRepository.save(componente);
        ComponenteDTO componenteCreadoDTO = new ComponenteDTO();
        componenteCreadoDTO.setTipoComponente(componenteCreado.getTipoComponente().getId());
        BeanUtils.copyProperties(componenteCreado, componenteCreadoDTO);
        return componenteCreadoDTO;
    }

    @Override
    public List<Componente> listarComponentes(){
        return (List<Componente>) componenteRepository.findAll();
    }

    @Override
    public ComponenteResponse listarComponenteById(Integer id)throws ComponentNotFoundException{
        Componente componente = componenteRepository.findById(id).orElse(null);
        if(componente == null){
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "COMPONENT").toUpperCase());
        }
        ComponenteResponse componenteResponse = new ComponenteResponse();
        BeanUtils.copyProperties(componente, componenteResponse);
        componenteResponse.setTipoComponente(componente.getTipoComponente().getNombre());
        return componenteResponse;
    }

    public ComponenteDTO listarComponentePorTipoComponente(Integer tipoComponenteId){
        return null;
    }

    @Override
    public ComponenteDTO actualizarComponente(Integer id, ComponenteDTO componenteDTO)throws ComponentNotFoundException{
        Componente componente = componenteRepository.findById(id).orElse(null);
        if(componente == null){
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "COMPONENT").toUpperCase());
        }

        BeanUtils.copyProperties(componenteDTO, componente);
        componente.setDeleteFlag(false);
    

        if(componenteDTO.getDeleteFlag()!= null){
            TipoComponente tipoComponente = tipoComponenteRepository.findById(componenteDTO.getTipoComponente()).orElse(null);
            if(tipoComponente == null){
                throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "TYPE COMPONENT").toUpperCase());

            }
            componente.setTipoComponente(tipoComponente);
        }else{
            componente.setTipoComponente(componente.getTipoComponente());
        }

        Componente componenteActualizado = componenteRepository.save(componente);
        ComponenteDTO componenteActualizadoDTO = new ComponenteDTO();
        componenteActualizadoDTO.setTipoComponente(componenteActualizado.getTipoComponente().getId());
        BeanUtils.copyProperties(componenteActualizado, componenteActualizadoDTO);
        return componenteActualizadoDTO;
    }

    @Override
    public void eliminarComponente (Integer id) throws ComponentNotFoundException, DeleteNotAllowedException{
        Componente componente = componenteRepository.findById(id).orElse(null);
        if(componente == null){
            throw new ComponentNotFoundException(String.format(IS_NOT_FOUND, "COMPONENT").toUpperCase());
        }

        if(componente.getDeleteFlag() == true){
            throw new DeleteNotAllowedException(String.format(IS_NOT_ALLOWED, "DELETE COMPONENT").toUpperCase());
        }
        componente.setDeleteFlag(true);
        componenteRepository.save(componente);
    }
}
