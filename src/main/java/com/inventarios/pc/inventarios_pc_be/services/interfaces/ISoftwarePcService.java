package com.inventarios.pc.inventarios_pc_be.services.interfaces;


import java.util.*;
import com.inventarios.pc.inventarios_pc_be.entities.SoftwarePC;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.SoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.exceptions.TypeSoftwareNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.SoftwarePcDTO;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SoftwareResponse;

public interface ISoftwarePcService {
    
    public SoftwarePcDTO crearSoftware(SoftwarePcDTO  softwarePcDTO)throws TypeSoftwareNotFoundException;

    public List<SoftwarePC> listarSoftwares();

    public SoftwareResponse listarSoftwareById(Integer id) throws SoftwareNotFoundException;

    public SoftwarePcDTO actualizarSoftware(Integer id, SoftwarePcDTO softwarePcDTO) throws TypeSoftwareNotFoundException, SoftwareNotFoundException;

    public void eliminarSoftware(Integer id) throws SoftwareNotFoundException, DeleteNotAllowedException;

}
