package com.inventarios.pc.inventarios_pc_be.controllers;

import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventarios.pc.inventarios_pc_be.exceptions.ComputerNotFoundException;
import com.inventarios.pc.inventarios_pc_be.services.implementations.HistorialComputadorService;
import com.inventarios.pc.inventarios_pc_be.services.implementations.ReporteService;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HojaVidaPcResponse;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private HistorialComputadorService historialComputadorService;
    @Autowired
    private ReporteService reportService;
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/hoja-de-vida/{computadorId}")
    public ResponseEntity<byte[]> downloadHojaDeVida(@PathVariable Integer computadorId) throws IOException, ComputerNotFoundException {
        HojaVidaPcResponse hojaVidaPcResponse = historialComputadorService.hojaDeVidaPc(computadorId);

        XWPFDocument document = reportService.generateHojaDeVidaDoc(hojaVidaPcResponse);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);

        return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=hoja_de_vida_pc_"+hojaVidaPcResponse.getId()+".docx")
        .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
        .body(out.toByteArray());
    }
}
