package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.exceptions.*;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SolicitudDTO;
import com.inventarios.pc.inventarios_pc_be.shared.requests.ActualizarSolicitudRequest;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudIdResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SolicitudesResponse;

public interface ISolicitudService {
        public SolicitudDTO crearSolicitudAsistencial(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
                        throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException,
                        LocationNotFoundException,
                        TypeRequestNotFoundException, SoftwareNotFoundException;

        public List<SolicitudesResponse> listarSolicitudes();

        public List<SolicitudesResponse> listarSolicitudesByUsuario(String correo) throws UserNotFoundException;

        public SolicitudIdResponse listarSolicitudById(Integer solicitudId, String correo, Boolean editar)
                throws RequestNotFoundException, UserNotFoundException, StateNotFoundException, SelectNotAllowedException;

        public SolicitudDTO crearSolicitudAdministrativo(SolicitudDTO solicitudDTO, Integer tipoSolicitudId)
                        throws StateNotFoundException, SelectNotAllowedException, UserNotFoundException,
                        LocationNotFoundException,
                        TypeRequestNotFoundException, SoftwareNotFoundException;

        public void rechazarSolicitud(Integer solicitudId)
                        throws RequestNotFoundException, SelectNotAllowedException, StateNotFoundException;

        public void cancelarSolicitud(Integer solicitudId)
                        throws RequestNotFoundException, SelectNotAllowedException, StateNotFoundException;

        public SolicitudDTO editarSolicitud(Integer solicitudId, ActualizarSolicitudRequest solicitudRequest)
            throws RequestNotFoundException, SelectNotAllowedException, UpdateNotAllowedException,
            ComputerNotFoundException, StateNotFoundException, LocationNotFoundException, DeviceNotFoundException;

        public void retornarSolicitudPendiente(Integer solicitudId) throws RequestNotFoundException, SelectNotAllowedException, StateNotFoundException;
        
}
