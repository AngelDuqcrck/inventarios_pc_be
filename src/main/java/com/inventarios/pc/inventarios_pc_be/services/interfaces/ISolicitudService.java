package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SelectNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.StateNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeRequestNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.UserNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SolicitudDTO;

public interface ISolicitudService {
    public SolicitudDTO crearSolicitud(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
            throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException, LocationNotFoundException,
            TypeRequestNotFoundException;
}
