package com.rch.download.service;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class JITReceiptService {

    public byte[] generateJITReceipt(String receiptNo, String warehouseCode, String destinationCity,
                                    String contactPhone, String address, int boxCount, int skuCount) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            PageSize customPageSize = new PageSize(PageSize.A4.getWidth() / 2.97f, PageSize.A4.getWidth() / 2.97f);
            Document document = new Document(pdf, customPageSize);
            document.setMargins(8, 8, 8, 8);

            PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H");
            document.setFont(font);

            // 主表格 - 7行结构
            Table mainTable = new Table(new float[]{1});
            mainTable.setWidth(UnitValue.createPercentValue(100));
            mainTable.setBorder(new SolidBorder(1f));

            // 1. 标题行
            Table titleTable = new Table(new float[]{4, 1});
            titleTable.setWidth(UnitValue.createPercentValue(100));
            titleTable.addCell(new Cell().add(new Paragraph("JIT 揽收面单").setFontSize(10)
                    .setTextAlignment(TextAlignment.LEFT).setBold()).setBorder(null));
            titleTable.addCell(new Cell().add(new Paragraph("第1箱（共1箱）").setFontSize(6)
                    .setTextAlignment(TextAlignment.RIGHT)).setBorder(null));
            mainTable.addCell(new Cell().add(titleTable).setBorder(null).setPadding(1));

            // 2. 上方条码行
            Barcode128 barcode = new Barcode128(pdf);
            barcode.setCode(receiptNo);
            barcode.setBarHeight(20f);
            Image barcodeImage = new Image(barcode.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdf));
            barcodeImage.setWidth(UnitValue.createPercentValue(95));
            barcodeImage.setHeight(25);
            mainTable.addCell(new Cell().add(barcodeImage
                    .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER))
                    .setBorder(null).setPadding(1));

            // 3. 仓库信息行
            Table infoTable = new Table(new float[]{2, 2, 1});
            infoTable.setWidth(UnitValue.createPercentValue(100));
            Cell warehouseCell = new Cell().add(new Paragraph("仓库编码：" + warehouseCode).setFontSize(6));
            Cell cityCell = new Cell().add(new Paragraph("目的城市：" + destinationCity).setFontSize(6));
            Cell priorityCell = new Cell().add(new Paragraph("优").setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER).setBold());
            infoTable.addCell(warehouseCell.setBorder(null));
            infoTable.addCell(cityCell.setBorder(null));
            infoTable.addCell(priorityCell.setBorder(null));
            mainTable.addCell(new Cell().add(infoTable).setBorder(null).setPadding(1));

            // 4. 联系人信息行
            Table contactTable = new Table(new float[]{1, 2});
            contactTable.setWidth(UnitValue.createPercentValue(100));
            contactTable.addCell(new Cell().add(new Paragraph("联系电话：" + contactPhone).setFontSize(7)).setBorder(null));
            mainTable.addCell(new Cell().add(contactTable).setBorder(null).setPadding(1));

            // 5. 地址信息行
            mainTable.addCell(new Cell().add(new Paragraph("仓库地址：" + address)
                    .setFontSize(7)).setBorder(null).setPadding(1));

            // 6. AE单号和数量信息合并行
            Table combinedInfoTable = new Table(new float[]{1});
            combinedInfoTable.setWidth(UnitValue.createPercentValue(100));
            combinedInfoTable.addCell(new Cell().add(new Paragraph("AE揽收单号：" + receiptNo).setFontSize(7)).setBorder(null));
            Table countTable = new Table(new float[]{1, 1});
            countTable.setWidth(UnitValue.createPercentValue(100));
            countTable.addCell(new Cell().add(new Paragraph("LBX小包数量：" + boxCount + " 个").setFontSize(6)).setBorder(null));
            countTable.addCell(new Cell().add(new Paragraph("SKU数量：" + skuCount + " 个").setFontSize(6)).setBorder(null));
            combinedInfoTable.addCell(new Cell().add(countTable).setBorder(null));
            mainTable.addCell(new Cell().add(combinedInfoTable).setBorder(null).setPadding(1));

            // 7. 底部条码行
            Image bottomBarcodeImage = new Image(barcode.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdf));
            bottomBarcodeImage.setWidth(UnitValue.createPercentValue(95));
            bottomBarcodeImage.setHeight(25);
            mainTable.addCell(new Cell().add(bottomBarcodeImage
                    .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER))
                    .setBorder(null).setPadding(2));

            document.add(mainTable);
            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("生成JIT揽收面单失败", e);
        }
    }
}