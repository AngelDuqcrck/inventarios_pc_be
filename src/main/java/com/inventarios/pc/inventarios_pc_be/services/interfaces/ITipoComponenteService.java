package com.inventarios.pc.inventarios_pc_be.services.interfaces;



import java.util.List;

import com.inventarios.pc.inventarios_pc_be.entities.TipoComponente;

public interface ITipoComponenteService {
    /**
     * Obtiene una lista de todos los tipos de software registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoComponente} que representan todos los tipos de documentos.
     */
    public List<TipoComponente> listarTipoComponente();
}
