package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;


import com.inventarios.pc.inventarios_pc_be.entities.TipoSoftware;

public interface ITipoSoftwareService {
    /**
     * Obtiene una lista de todos los tipos de software registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoSoftware} que representan todos los tipos de documentos.
     */
    public List<TipoSoftware> listarTipoSoftware();
}
