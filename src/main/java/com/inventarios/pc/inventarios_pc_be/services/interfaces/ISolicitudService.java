package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.RequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeRequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SolicitudDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudesResponse;

public interface ISolicitudService {
    public SolicitudDTO crearSolicitud(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
            throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException, LocationNotFoundException,
            TypeRequestNotFoundException;

    public List<SolicitudesResponse> listarSolicitudes();

    public List<SolicitudesResponse> listarSolicitudesByUsuario(Integer usuarioId) throws UserNotFoundException;

    public SolicitudIdResponse listarSolicitudById(Integer solicitudId)throws RequestNotFoundException;
}
