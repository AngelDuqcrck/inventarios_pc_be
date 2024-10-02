package com.inventarios.pc.inventarios_pc_be.services.interfaces;

import java.util.List;


import com.inventarios.pc.inventarios_pc_be.entities.TipoDocumento;

public interface ITipoDocumentoService {
    /**
     * Obtiene una lista de todos los tipos de documentos (DNI) registrados en el sistema.
     * 
     * @return Una lista de objetos {@link TipoDocumento} que representan todos los tipos de documentos.
     */
    public List<TipoDocumento> listarTipoDocumentos();
}
