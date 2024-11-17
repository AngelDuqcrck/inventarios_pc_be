package com.inventarios.pc.inventarios_pc_be.services.implementations;

import com.inventarios.pc.inventarios_pc_be.shared.responses.DispositivosVinculadosResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.HojaVidaPcResponse;
import com.inventarios.pc.inventarios_pc_be.shared.responses.SoftwareVinculadosResponse;

import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;

@Service
public class ReporteService {

    public XWPFDocument generateHojaDeVidaDoc(HojaVidaPcResponse hojaVidaPcResponse) throws Exception {
        XWPFDocument document = new XWPFDocument();

        // Crear la sección de encabezado
        XWPFHeader header = document.createHeader(HeaderFooterType.DEFAULT);

        // Crear la tabla del encabezado
        XWPFTable headerTable = header.createTable(3, 4);
        headerTable.setWidth("100%");

        // Configurar los bordes de la tabla del encabezado
        CTTblBorders borders = headerTable.getCTTbl().getTblPr().addNewTblBorders();
        borders.addNewTop().setVal(STBorder.SINGLE);
        borders.addNewBottom().setVal(STBorder.SINGLE);
        borders.addNewLeft().setVal(STBorder.SINGLE);
        borders.addNewRight().setVal(STBorder.SINGLE);
        borders.addNewInsideH().setVal(STBorder.SINGLE);
        borders.addNewInsideV().setVal(STBorder.SINGLE);

        // Ajustar el ancho de las columnas del encabezado
        CTTblGrid grid = headerTable.getCTTbl().addNewTblGrid();
        grid.addNewGridCol().setW(BigInteger.valueOf(3000)); // Logo
        grid.addNewGridCol().setW(BigInteger.valueOf(2600)); // Título
        grid.addNewGridCol().setW(BigInteger.valueOf(1700)); // Labels
        grid.addNewGridCol().setW(BigInteger.valueOf(1700)); // Valores

        // Primera columna: Logo (combinar tres filas)
        XWPFTableCell logoCell = headerTable.getRow(0).getCell(0);
        logoCell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
        headerTable.getRow(1).getCell(0).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
        headerTable.getRow(2).getCell(0).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);

        // Insertar logo
        XWPFParagraph logoParagraph = logoCell.getParagraphArray(0);
        XWPFRun logoRun = logoParagraph.createRun();
        String projectRoot = System.getProperty("user.dir");
        String logoPath = projectRoot + "/src/main/resources/reportes/images/logo_csa.png";
        FileInputStream logoStream = new FileInputStream(new File(logoPath));
        logoRun.addPicture(logoStream, XWPFDocument.PICTURE_TYPE_PNG, "logo_csa.png",
                Units.toEMU(126.2362), Units.toEMU(75.5906));
        logoParagraph.setAlignment(ParagraphAlignment.CENTER);
        logoStream.close();

        // Segunda columna: Título
        XWPFTableCell titleCell = headerTable.getRow(0).getCell(1);
        titleCell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
        headerTable.getRow(1).getCell(1).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
        headerTable.getRow(2).getCell(1).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
        XWPFParagraph titleParagraph = titleCell.getParagraphArray(0);
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        titleParagraph.setSpacingBefore(550);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText("HOJA DE VIDA");
        titleRun.addBreak();
        titleRun.setText("EQUIPOS DE COMPUTO");
        titleRun.setBold(true);
        titleRun.setFontSize(12);
        titleRun.setFontFamily("Tahoma");

        // Tercera y cuarta columna
        setCellContent(headerTable.getRow(0).getCell(2), "CÓDIGO", true);
        setCellContent(headerTable.getRow(1).getCell(2), "VERSIÓN", true);
        setCellContent(headerTable.getRow(2).getCell(2), "PÁGINA", true);
        setCellContent(headerTable.getRow(0).getCell(3), "FT-SI-SM010", false);
        setCellContent(headerTable.getRow(1).getCell(3), "001", false);

        // Configuración de la numeración de página
        XWPFTableCell pageCell = headerTable.getRow(2).getCell(3);
        XWPFParagraph pageParagraph = pageCell.getParagraphArray(0);
        pageParagraph.setAlignment(ParagraphAlignment.CENTER);

        // Crear el campo para el número de página actual
        CTSimpleField currentPageField = pageParagraph.getCTP().addNewFldSimple();
        currentPageField.setInstr("PAGE \\* MERGEFORMAT");

        // Agregar el texto "de"
        XWPFRun textRun = pageParagraph.createRun();
        textRun.setText(" de ");
        textRun.setFontSize(10);
        textRun.setFontFamily("Tahoma");

        // Crear el campo para el total de páginas
        CTSimpleField totalPagesField = pageParagraph.getCTP().addNewFldSimple();
        totalPagesField.setInstr("NUMPAGES \\* MERGEFORMAT");

        // Crear pie de página
        XWPFFooter footer = document.createFooter(HeaderFooterType.DEFAULT);
        XWPFTable footerTable = footer.createTable(2, 3);
        footerTable.setWidth("100%");

        // Ajustar el ancho de las columnas del pie de página
        CTTblGrid footerGrid = footerTable.getCTTbl().addNewTblGrid();
        footerGrid.addNewGridCol().setW(BigInteger.valueOf(2800));
        footerGrid.addNewGridCol().setW(BigInteger.valueOf(2800));
        footerGrid.addNewGridCol().setW(BigInteger.valueOf(3600));

        // Primera fila del pie de página
        XWPFTableRow footerHeaderRow = footerTable.getRow(0);
        setCellContent(footerHeaderRow.getCell(0), "ELABORADO POR", true);
        setCellContent(footerHeaderRow.getCell(1), "APROBADO POR", true);
        setCellContent(footerHeaderRow.getCell(2), "FECHA DE APROBACIÓN", true);

        // Segunda fila del pie de página
        XWPFTableRow footerValueRow = footerTable.getRow(1);
        footerValueRow.setHeight(600);
        setCellContent(footerValueRow.getCell(0), "INVENTARIOS PC", false);
        setCellContent(footerValueRow.getCell(1), "SISTEMAS", false);
        setCellContent(footerValueRow.getCell(2), new SimpleDateFormat("dd/MM/yyyy").format(new Date()), false);

        // Contenido del documento
        createGeneralInfoSection(document, hojaVidaPcResponse);
        createPeripheralsSection(document, hojaVidaPcResponse.getDispositivosVinculados());
        createSoftwareSection(document, hojaVidaPcResponse.getSoftwareVinculados());
        responsableSection(document, hojaVidaPcResponse);

        return document;
    }

    private void setCellContent(XWPFTableCell cell, String text, boolean isBold) {
        XWPFParagraph paragraph = cell.getParagraphArray(0);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setSpacingAfter(0);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(isBold);
        run.setFontSize(10);

        run.setFontFamily("Tahoma");
        paragraph.setSpacingBefore(0);

    }

    private void createGeneralInfoSection(XWPFDocument document, HojaVidaPcResponse hojaVida) {
        XWPFParagraph title = document.createParagraph();
        title.setSpacingBefore(500);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("INFORMACIÓN GENERAL DEL EQUIPO");
        titleRun.setBold(true);
        titleRun.setFontSize(12);
        titleRun.setFontFamily("Tahoma");
        title.setAlignment(ParagraphAlignment.CENTER); // Centrar el título
        title.setSpacingAfter(0); // Eliminar espaciado después del título

        XWPFTable table = document.createTable();

        // Quitar bordes de la tabla
        table.setInsideHBorder(XWPFTable.XWPFBorderType.NONE, 0, 0, "FFFFFF");
        table.setInsideVBorder(XWPFTable.XWPFBorderType.NONE, 0, 0, "FFFFFF");
        table.getCTTbl().getTblPr().unsetTblBorders();

        // Configuración de las celdas en la tabla de 4 columnas
        String[][] data = {
                { "Nombre:", hojaVida.getNombre(), "Dirección IP:", hojaVida.getIpAsignada() },
                { "Marca:", hojaVida.getMarca(), "Modelo:", hojaVida.getModelo() },
                { "Tipo:", hojaVida.getTipoPC(), "Placa:", hojaVida.getPlaca() },
                { "Serial:", hojaVida.getSerial(), "RAM:", hojaVida.getTipoRam() + " " + hojaVida.getRam() },
                { "Almacenamiento:", hojaVida.getTipoAlmacenamiento() + " " + hojaVida.getAlmacenamiento(),
                        "Procesador:", hojaVida.getProcesador() },
                { "Propietario:", hojaVida.getPropietario(), "Sede:", hojaVida.getSede() },
                { "Área:", hojaVida.getArea(), "Ubicación:", hojaVida.getUbicacion() }
        };

        for (String[] rowContent : data) {
            XWPFTableRow row = table.createRow();
            for (int i = 0; i < rowContent.length; i++) {
                // Asegurarse de que la celda en la posición 'i' existe, de lo contrario,
                // agregarla
                if (row.getCell(i) == null) {
                    row.addNewTableCell();
                }

                XWPFParagraph cellParagraph = row.getCell(i).getParagraphArray(0);
                cellParagraph.setIndentationLeft(200); // Margen de inicio para la celda (en unidades de TWIP)
                cellParagraph.setAlignment(ParagraphAlignment.LEFT); // Alinear el contenido a la izquierda
                cellParagraph.setVerticalAlignment(TextAlignment.CENTER); // Centrar verticalmente el contenido

                XWPFRun run = cellParagraph.createRun();

                // Si es la primera celda (nombre del dato), establecerla en negrilla
                if (i % 2 == 0) {
                    run.setBold(true);
                } else {
                    run.setBold(false);
                }

                run.setText(rowContent[i]);
                run.setFontFamily("Tahoma");
                run.setFontSize(11);
                cellParagraph.setSpacingAfter(0); // Eliminar espaciado después de cada celda
            }
        }
    }

    private void createPeripheralsSection(XWPFDocument document, List<DispositivosVinculadosResponse> dispositivos) {
        // Crear el párrafo para el título y configurarlo
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingBefore(500);
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText("PERIFÉRICOS VINCULADOS");
        paragraphRun.setBold(true);
        paragraphRun.setFontFamily("Tahoma");
        paragraphRun.setFontSize(11);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setSpacingAfter(300); // Eliminar espaciado después del título

        // Crear la tabla para los dispositivos
        XWPFTable table = document.createTable();
        table.setTableAlignment(TableRowAlign.CENTER); // Centrar la tabla en el documento

        // Crear la primera fila con los títulos y configurarlos
        XWPFTableRow row = table.getRow(0);
        String[] titles = { "Dispositivo", "Marca", "Modelo", "Serial", "Placa" };

        for (int i = 0; i < titles.length; i++) {
            if (row.getCell(i) == null) {
                row.addNewTableCell(); // Crear celda si no existe
            }
            XWPFRun titleRun = row.getCell(i).getParagraphArray(0).createRun();
            titleRun.setText(titles[i]);
            titleRun.setBold(true);
            titleRun.setFontFamily("Tahoma");
            titleRun.setFontSize(11);
            row.getCell(i).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER); // Centrar título de la columna
            row.getCell(i).getCTTc().addNewTcPr().addNewVAlign().setVal(STVerticalJc.CENTER); // Alinear verticalmente
                                                                                              // al centro
            row.getCell(i).getParagraphArray(0).setSpacingAfter(0); // Eliminar espaciado después del párrafo en los
                                                                    // títulos
        }

        // Filtrar dispositivos y agregar solo los que tienen dispositivo, marca y placa
        for (DispositivosVinculadosResponse dispositivo : dispositivos) {
            if (dispositivo.getTipoDispositivo() != null && !dispositivo.getTipoDispositivo().isEmpty() &&
                    dispositivo.getMarca() != null && !dispositivo.getMarca().isEmpty() &&
                    dispositivo.getPlaca() != null && !dispositivo.getPlaca().isEmpty()) {

                row = table.createRow();

                // Agregar datos en las celdas
                String[] deviceData = {
                        dispositivo.getTipoDispositivo(),
                        dispositivo.getMarca(),
                        dispositivo.getModelo(),
                        dispositivo.getSerial(),
                        dispositivo.getPlaca()
                };

                for (int i = 0; i < deviceData.length; i++) {
                    if (row.getCell(i) == null) {
                        row.addNewTableCell(); // Crear celda si no existe
                    }
                    XWPFRun dataRun = row.getCell(i).getParagraphArray(0).createRun();
                    dataRun.setText(deviceData[i]);
                    dataRun.setFontFamily("Tahoma");
                    dataRun.setFontSize(11);
                    row.getCell(i).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER); // Centrar datos de la
                                                                                                 // columna
                    row.getCell(i).getCTTc().addNewTcPr().addNewVAlign().setVal(STVerticalJc.CENTER); // Alinear
                                                                                                      // verticalmente
                                                                                                      // al centro
                    row.getCell(i).getParagraphArray(0).setSpacingAfter(0); // Eliminar espaciado después del párrafo en
                                                                            // los datos
                }
            }
        }

        // Configurar bordes para la tabla
        table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 0, "000000");
        table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 0, "000000");
        BigInteger borderSize = BigInteger.valueOf(8); // Tamaño del borde en unidades
        table.getCTTbl().getTblPr().getTblBorders().getTop().setSz(borderSize);
        table.getCTTbl().getTblPr().getTblBorders().getBottom().setSz(borderSize);
        table.getCTTbl().getTblPr().getTblBorders().getLeft().setSz(borderSize);
        table.getCTTbl().getTblPr().getTblBorders().getRight().setSz(borderSize);
        table.getCTTbl().getTblPr().getTblBorders().getInsideH().setSz(borderSize);
        table.getCTTbl().getTblPr().getTblBorders().getInsideV().setSz(borderSize);
    }

    private void createSoftwareSection(XWPFDocument document, List<SoftwareVinculadosResponse> softwares) {
        // Crear párrafo para el título
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingBefore(500);
        XWPFRun paragraphRun = paragraph.createRun();
        paragraphRun.setText("SOFTWARE INSTALADO");
        paragraphRun.setBold(true);
        paragraphRun.setFontFamily("Tahoma");
        paragraphRun.setFontSize(11);
        paragraph.setAlignment(ParagraphAlignment.CENTER); // Centrar el título
        paragraph.setSpacingAfter(300); // Eliminar espaciado después del título

        // Crear la tabla
        XWPFTable table = document.createTable();
        table.setTableAlignment(TableRowAlign.CENTER); // Centrar la tabla
        // Crear la fila de títulos de columnas y configurarlos
        XWPFTableRow row = table.getRow(0);
        String[] titles = { "Nombre", "Versión", "Tipo", "Empresa" };

        for (int i = 0; i < titles.length; i++) {
            if (row.getCell(i) == null) {
                row.addNewTableCell();
            }
            XWPFRun titleRun = row.getCell(i).getParagraphArray(0).createRun();
            titleRun.setText(titles[i]);
            titleRun.setBold(true); // Títulos en negrilla
            titleRun.setFontFamily("Tahoma");
            titleRun.setFontSize(11);
            row.getCell(i).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER); // Centrar contenido de los
                                                                                         // títulos

            // Alinear verticalmente en el centro
            row.getCell(i).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            // Eliminar espaciado después del párrafo en los títulos
            row.getCell(i).getParagraphArray(0).setSpacingAfter(0);
        }

        // Rellenar la tabla con el contenido de los softwares
        for (SoftwareVinculadosResponse software : softwares) {
            row = table.createRow();
            String[] softwareData = {
                    software.getNombre(),
                    software.getVersion(),
                    software.getTipoSoftware(),
                    software.getEmpresa()
            };

            for (int i = 0; i < softwareData.length; i++) {
                if (row.getCell(i) == null) {
                    row.addNewTableCell();
                }
                XWPFRun dataRun = row.getCell(i).getParagraphArray(0).createRun();
                dataRun.setText(softwareData[i]);
                dataRun.setFontFamily("Tahoma");
                dataRun.setFontSize(11);
                row.getCell(i).getParagraphArray(0).setAlignment(ParagraphAlignment.CENTER); // Centrar el contenido de
                                                                                             // cada celda

                // Alinear verticalmente en el centro
                row.getCell(i).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                // Eliminar espaciado después del párrafo en los datos
                row.getCell(i).getParagraphArray(0).setSpacingAfter(0);
            }
        }
    }

    private void responsableSection(XWPFDocument document, HojaVidaPcResponse hojaVida) {
        XWPFParagraph title = document.createParagraph();
        title.setSpacingBefore(550);
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Responsable: ");
        titleRun.setBold(true);
        titleRun.setFontSize(11);
        titleRun.setFontFamily("Tahoma");

        XWPFRun dataRun = title.createRun();
        dataRun.setBold(false); // Sin negrita
        dataRun.setText(hojaVida.getResponsable());
        dataRun.setFontFamily("Tahoma");
        dataRun.setFontSize(11);
        title.setSpacingAfter(300);
    }

}
