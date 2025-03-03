package com.codershubham.cms.cms.util;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfGeneratorUtil {

    public static byte[] generatePdfWithTable(String title, List<String[]> tableData) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(200, 750);
                contentStream.showText(title);
                contentStream.endText();

                // Draw table
                float margin = 50;
                float yStart = 700;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float rowHeight = 20;
                int cols = tableData.get(0).length;

                float colWidth = tableWidth / cols;

                // Draw rows
                for (String[] row : tableData) {
                    float xPosition = margin;
                    for (String cell : row) {
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(xPosition + 5, yPosition);
                        contentStream.showText(cell);
                        contentStream.endText();
                        xPosition += colWidth;
                    }
                    yPosition -= rowHeight;
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error while generating PDF", e);
        }
    }
}
