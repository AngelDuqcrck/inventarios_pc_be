package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.AreaPC;
import com.inventarios.pc.inventarios_pc_be.exceptions.DeleteNotAllowedException;
import com.inventarios.pc.inventarios_pc_be.exceptions.LocationNotFoundException;
import com.inventarios.pc.inventarios_pc_be.shared.DTOs.AreaDTO;

public interface IAreaService {
    
    public AreaDTO crearArea(AreaDTO areaDTO) throws LocationNotFoundException;

    public List<AreaPC> listarAreas() throws LocationNotFoundException;

    public AreaDTO actualizarArea(Integer id, AreaDTO areaDTO) throws LocationNotFoundException;

    public void eliminarArea(Integer id)throws LocationNotFoundException, DeleteNotAllowedException;
}
