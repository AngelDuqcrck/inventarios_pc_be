package com.inventarios.pc.inventarios_pc_be.services.implementations;


import com.inventarios.pc.inventarios_pc_be.shared.responses.DispositivosVinculadosResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HojaVidaPcResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SoftwareVinculadosResponse;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;



@Service
public class ReporteService {
    
    public XWPFDocument generateHojaDeVidaDoc(HojaVidaPcResponse hojaVidaPcResponse) {
        XWPFDocument document = new XWPFDocument();
        
        // Crear párrafos y tablas según el formato de la plantilla
        XWPFParagraph title = document.createParagraph();
        XWPFRun run = title.createRun();
        run.setText("HOJA DE VIDA DE EQUIPO DE COMPUTO");
        run.setBold(true);
        run.setFontSize(12);
        run.setFontFamily("Tahoma");

        // Información general
        createGeneralInfoSection(document, hojaVidaPcResponse);

        // Periféricos vinculados
        createPeripheralsSection(document, hojaVidaPcResponse.getDispositivosVinculados());

        // Software instalado
        createSoftwareSection(document, hojaVidaPcResponse.getSoftwareVinculados());

        // Footer con la información de aprobación
        createFooterSection(document, hojaVidaPcResponse);

        return document;
    }

    private void createGeneralInfoSection(XWPFDocument document, HojaVidaPcResponse hojaVida) {
        XWPFParagraph title = document.createParagraph();
        XWPFRun titleRun = title.createRun();
        titleRun.setText("INFORMACIÓN GENERAL DEL EQUIPO");
        titleRun.setBold(true);
        titleRun.setFontSize(12);
        titleRun.setFontFamily("Tahoma");
        title.setSpacingAfter(200); // Espaciado después del título
    
        XWPFTable table = document.createTable();
    
        // Quitar bordes de la tabla
        table.setInsideHBorder(XWPFTable.XWPFBorderType.NONE, 0, 0, "FFFFFF");
        table.setInsideVBorder(XWPFTable.XWPFBorderType.NONE, 0, 0, "FFFFFF");
        table.getCTTbl().getTblPr().unsetTblBorders();
    
        // Configuración de las celdas en la tabla de 4 columnas
        String[][] data = {
            {"Nombre:", hojaVida.getNombre(), "Dirección IP:", hojaVida.getIpAsignada()},
            {"Marca:", hojaVida.getMarca(), "Modelo:", hojaVida.getModelo()},
            {"Tipo:", hojaVida.getTipoPC(), "Placa:", hojaVida.getPlaca()},
            {"Serial:", hojaVida.getSerial(), "RAM:", hojaVida.getTipoRam()+" "+hojaVida.getRam()},
            {"Almacenamiento:", hojaVida.getTipoAlmacenamiento()+" "+hojaVida.getAlmacenamiento(), "Procesador:", hojaVida.getProcesador()},
            {"Propietario:", hojaVida.getResponsable(), "Sede:", hojaVida.getSede()},
            {"Área:", hojaVida.getArea(), "Ubicación:", hojaVida.getUbicacion()}
        };
    
        for (String[] rowContent : data) {
            XWPFTableRow row = table.createRow();
            for (int i = 0; i < rowContent.length; i++) {
                // Asegurarse de que la celda en la posición 'i' existe, de lo contrario, agregarla
                if (row.getCell(i) == null) {
                    row.addNewTableCell();
                }
    
                XWPFRun run = row.getCell(i).getParagraphArray(0).createRun();
                
                // Si es la primera celda (nombre del dato), establecerla en negrilla
                if (i % 2 == 0) {
                    run.setBold(true);
                } else {
                    run.setBold(false);
                }
                
                run.setText(rowContent[i]);
                run.setFontFamily("Tahoma");
                run.setFontSize(11);
                row.getCell(i).getParagraphArray(0).setSpacingAfter(0); // Sin espaciado después de cada celda
            }
        }
    }

    private void createPeripheralsSection(XWPFDocument document, List<DispositivosVinculadosResponse> dispositivos) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingBefore(500);
        paragraph.createRun().setText("PERIFÉRICOS VINCULADOS");

        XWPFTable table = document.createTable();
        XWPFTableRow row = table.getRow(0);
        row.getCell(0).setText("Dispositivo");
        row.addNewTableCell().setText("Marca");
        row.addNewTableCell().setText("Modelo");
        row.addNewTableCell().setText("Serial");
        row.addNewTableCell().setText("Placa");

        for (DispositivosVinculadosResponse dispositivo : dispositivos) {
            row = table.createRow();
            row.getCell(0).setText(dispositivo.getTipoDispositivo());
            row.getCell(1).setText(dispositivo.getMarca());
            row.getCell(2).setText(dispositivo.getModelo());
            row.getCell(3).setText(dispositivo.getSerial());
            row.getCell(4).setText(dispositivo.getPlaca());
        }
    }

    private void createSoftwareSection(XWPFDocument document, List<SoftwareVinculadosResponse> softwares) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingBefore(500);
        paragraph.createRun().setText("SOFTWARE INSTALADO");
        

        XWPFTable table = document.createTable();
        XWPFTableRow row = table.getRow(0);
        row.getCell(0).setText("Nombre");
        row.addNewTableCell().setText("Versión");
        row.addNewTableCell().setText("Tipo");
        row.addNewTableCell().setText("Empresa");

        for (SoftwareVinculadosResponse software : softwares) {
            row = table.createRow();
            row.getCell(0).setText(software.getNombre());
            row.getCell(1).setText(software.getVersion());
            row.getCell(2).setText(software.getTipoSoftware());
            row.getCell(3).setText(software.getEmpresa());
        }
    }

    private void createFooterSection(XWPFDocument document, HojaVidaPcResponse hojaVida) {
        XWPFParagraph footer = document.createParagraph();
        XWPFRun run = footer.createRun();
        run.setText("Responsable: " + hojaVida.getResponsable());
        run.addBreak();
        run.setText("Fecha de Aprobación: " + new Date().toString());
    }
}
