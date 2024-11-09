package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.EstadoDispositivo;
import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.ComputadorDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.UbicarPcRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComputadorIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.ComputadoresResponse;

public interface IComputadorService {

     public ComputadorDTO crearComputador(ComputadorDTO computadorDTO)
             throws TypePcNotFoundException, SelectNotAllowedException, UserNotFoundException,
             LocationNotFoundException, ComponentNotFoundException, MiscellaneousNotFoundException,
             StateNotFoundException, MarcaNotFoundException, TypeDeviceNotFoundException, OwnerNotFoundException;

     public List<ComputadoresResponse> listarComputadores();

     public ComputadorIdResponse listarComputadorById(Integer id) throws ComputerNotFoundException;

     public void darBajaComputador(Integer id) throws ComputerNotFoundException, DeleteNotAllowedException;

     public void cambiarEstadoPc(Integer computadorId, Integer nuevoEstadoDispositivoId)
               throws ComputerNotFoundException, StateNotFoundException, ChangeNotAllowedException;

     public ComputadorDTO actualizarComputador(Integer computadorId, ComputadorDTO computadorDTO)
             throws TypePcNotFoundException, SelectNotAllowedException, UserNotFoundException,
             LocationNotFoundException, ComponentNotFoundException, MiscellaneousNotFoundException,
             StateNotFoundException, MarcaNotFoundException, UpdateNotAllowedException, ComputerNotFoundException,
             ChangeNotAllowedException, TypeDeviceNotFoundException, OwnerNotFoundException;

     public List<ComputadoresResponse> listarComputadoresByUbicacion(Integer ubicacionId)
               throws LocationNotFoundException;

     public List<ComputadoresResponse> listarComputadoresByUsuario(Integer usuarioId) throws UserNotFoundException; 
     public List<ComputadoresResponse> listarComputadoresByEmail(String usuarioEmail) throws UserNotFoundException;       

      public void ubicarPc(UbicarPcRequest ubicarPcRequest) throws ComputerNotFoundException, SelectNotAllowedException,
            UserNotFoundException, LocationNotFoundException, StateNotFoundException ;

}
