package com.file.ch.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PickListPdfService {

    public byte[] generatePickListPdf() {
        // Define page size
        float widthMM = 210f;  // A4 width in mm
        float heightMM = 297f; // A4 height in mm

        // Convert mm to points
        float widthPt = widthMM / 25.4f * 72f;
        float heightPt = heightMM / 25.4f * 72f;
        PageSize pageSize = PageSize.A4;
        System.out.println("widthMM = " + widthMM);
        System.out.println("heightMM = " + heightMM);
        System.out.println("widthPt = " + widthPt);
        System.out.println("heightPt = " + heightPt);
        System.out.println("PageSize A4 = new PageSize(595, 842);");

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, pageSize);
            document.setMargins(20, 20, 20, 20);

            // 加载不同字重的思源黑体字体
            PdfFont regularFont = PdfFontFactory.createFont(ExpressLabelService.class.getClassLoader().getResource("fonts/SourceHanSansSC-Medium.otf").getPath());
            PdfFont boldFont = PdfFontFactory.createFont(ExpressLabelService.class.getClassLoader().getResource("fonts/SourceHanSansSC-Bold.otf").getPath());
            PdfFont lightFont = PdfFontFactory.createFont(ExpressLabelService.class.getClassLoader().getResource("fonts/SourceHanSansSC-Light.otf").getPath());

            document.setFont(regularFont);

            // Create main table
            Table table = new Table(new float[]{1, 2, 1, 1, 1, 1});
            table.setWidth(UnitValue.createPercentValue(100));
            table.setBorder(new SolidBorder(1));

            // Add header row
            table.addHeaderCell(new Cell().add(new Paragraph("序号").setFontSize(10).setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("商品信息").setFontSize(10).setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("属性集").setFontSize(10).setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("SKU ID").setFontSize(10).setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("SKU货号").setFontSize(10).setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("数量").setFontSize(10).setTextAlignment(TextAlignment.CENTER)));

            // Add data row with product information and image
            table.addCell(new Cell().add(new Paragraph("1").setFontSize(10).setTextAlignment(TextAlignment.CENTER)));
            
            // Create product info cell with nested table for image and text
            Cell productCell = new Cell();
            Table innerTable = new Table(new float[]{1, 3});
            innerTable.setWidth(UnitValue.createPercentValue(100));
            
            // Create image cell
            Cell imageCell = new Cell();
            try {
                Image productImage = new Image(ImageDataFactory.create(PickListPdfService.class.getClassLoader().getResource("ttt.png").getPath()));
                productImage.setWidth(100);
                productImage.setHeight(100);
                imageCell.add(productImage);
                imageCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
                imageCell.setBorder(Border.NO_BORDER);

            } catch (IOException e) {
                System.err.println("Warning: Could not load product image: " + e.getMessage());
            }
            
            // Create info cell
            Cell infoCell = new Cell();
            StringBuilder productInfo = new StringBuilder();
            productInfo.append("SKC: 2816481173\n")
                      .append("SKC货号：231-55X7GK\n")
                      .append("备货母单号：WP2503255714867\n")
                      .append("备货单号：WB2503253622146\n")
                      .append("创建时间：2025-03-25 15:27\n")
                      .append("要求发货时间：2025-03-26 15:27\n")
                      .append("[JIT] 【加急】 [VMI]");
            
            infoCell.add(new Paragraph(productInfo.toString())
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.LEFT))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);
            infoCell.setBorder(Border.NO_BORDER);

            // Add cells to inner table
            innerTable.addCell(imageCell);
            innerTable.addCell(infoCell);
            
            // Add inner table to product cell
            productCell.add(innerTable);
            
            table.addCell(productCell);
            
            // Add remaining cells
            table.addCell(new Cell().add(new Paragraph("60*90cm").setFontSize(10).setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().add(new Paragraph("2816481173").setFontSize(10).setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().add(new Paragraph("231-55X7GK").setFontSize(10).setTextAlignment(TextAlignment.CENTER)));
            table.addCell(new Cell().add(new Paragraph("1").setFontSize(10).setTextAlignment(TextAlignment.CENTER)));

            document.add(table);
            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("生成拣货单PDF失败", e);
        }
    }
}