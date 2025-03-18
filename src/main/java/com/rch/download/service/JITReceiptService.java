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

        // 1. 定义页面尺寸（100mm × 100mm）
        float widthMM = 100f;  // 毫米
        float heightMM = 100f;

        // 2. 将毫米转换为磅（1英寸=25.4mm, 1英寸=72磅）
        float widthPt = widthMM / 25.4f * 72f;
        float heightPt = heightMM / 25.4f * 72f;
        PageSize customPageSize = new PageSize(widthPt, heightPt);

        // 3. 创建 PDF 文档
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, customPageSize);
            document.setMargins(2, 2, 2, 2);

            PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H");
            document.setFont(font);

            // 主表格 - 7行结构
            Table mainTable = new Table(new float[]{1});
            mainTable.setWidth(UnitValue.createPercentValue(100));
            mainTable.setBorder(new SolidBorder(0.5f));

            // 1. 标题行
            Table titleTable = new Table(new float[]{7, 3});
            titleTable.setWidth(UnitValue.createPercentValue(100));
            titleTable.setBorder(new SolidBorder(0.5f));
            titleTable.addCell(new Cell().add(new Paragraph("JIT 揽收面单").setFontSize(14)
                    .setTextAlignment(TextAlignment.LEFT).setBold()).setBorder(new SolidBorder(0.5f)).setPadding(4));
            titleTable.addCell(new Cell().add(new Paragraph("第1箱（共1箱）").setFontSize(9)
                    .setTextAlignment(TextAlignment.RIGHT)).setBorder(new SolidBorder(0.5f)).setPadding(4));
            mainTable.addCell(new Cell().add(titleTable).setBorder(null).setPadding(2));

            // 2. 上方条码行
            Barcode128 barcode = new Barcode128(pdf);
            barcode.setCode(receiptNo);
            barcode.setBarHeight(30f);
            barcode.setX(1.5f);
            Image barcodeImage = new Image(barcode.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdf));
            barcodeImage.setWidth(UnitValue.createPercentValue(98));
            barcodeImage.setHeight(35);
            mainTable.addCell(new Cell().add(barcodeImage
                    .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER))
                    .setBorder(null).setPadding(1));

            // 3. 仓库信息行
            // 3. 仓库信息行
            Table containerTable = new Table(new float[]{5, 1});
            containerTable.setWidth(UnitValue.createPercentValue(100));
            containerTable.setBorder(new SolidBorder(0.5f));

            // 左侧信息表格
            Table leftInfoTable = new Table(new float[]{1, 1});
            leftInfoTable.setWidth(UnitValue.createPercentValue(100));
            
            // 添加四个单元格
            leftInfoTable.addCell(new Cell().add(new Paragraph("仓库编码：" + warehouseCode)
                    .setFontSize(9).setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(4));
            leftInfoTable.addCell(new Cell().add(new Paragraph("目的城市：" + destinationCity)
                    .setFontSize(9).setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(4));
            leftInfoTable.addCell(new Cell().add(new Paragraph("仓库联系人：" + contactPhone)
                    .setFontSize(9).setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(4));
            leftInfoTable.addCell(new Cell().add(new Paragraph("联系电话：" + contactPhone)
                    .setFontSize(9).setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(4));

            // 右侧优先级标识
            Cell priorityCell = new Cell().add(new Paragraph("优").setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER).setBold());
            priorityCell.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
            
            containerTable.addCell(new Cell().add(leftInfoTable).setBorder(new SolidBorder(0.5f)).setPadding(0));
            containerTable.addCell(priorityCell.setBorder(new SolidBorder(0.5f)).setPadding(4));
            
            mainTable.addCell(new Cell().add(containerTable).setBorder(null).setPadding(2));

            // 5. 地址信息行
            Table addressTable = new Table(new float[]{1});
            addressTable.setWidth(UnitValue.createPercentValue(100));
            addressTable.setBorder(new SolidBorder(0.5f));
            addressTable.addCell(new Cell().add(new Paragraph("仓库地址：" + address).setFontSize(9)).setBorder(new SolidBorder(0.5f)).setPadding(4));
            mainTable.addCell(new Cell().add(addressTable).setBorder(null).setPadding(2));

            // 6. AE单号和数量信息合并行
            Table combinedInfoTable = new Table(new float[]{1});
            combinedInfoTable.setWidth(UnitValue.createPercentValue(100));
            combinedInfoTable.setBorder(new SolidBorder(0.5f));
            combinedInfoTable.addCell(new Cell().add(new Paragraph("AE揽收单号：" + receiptNo).setFontSize(9)).setBorder(new SolidBorder(0.5f)).setPadding(4));
            Table countTable = new Table(new float[]{1});
            countTable.setWidth(UnitValue.createPercentValue(100));
            countTable.addCell(new Cell().add(new Paragraph("LBX小包数量：" + boxCount + " 个").setFontSize(9)).setBorder(new SolidBorder(0.5f)).setPadding(4));
            countTable.addCell(new Cell().add(new Paragraph("SKU数量：" + skuCount + " 个").setFontSize(9)).setBorder(new SolidBorder(0.5f)).setPadding(4));
            combinedInfoTable.addCell(new Cell().add(countTable).setBorder(new SolidBorder(0.5f)).setPadding(4));
            mainTable.addCell(new Cell().add(combinedInfoTable).setBorder(null).setPadding(2));

            // 7. 底部条码行
            Image bottomBarcodeImage = new Image(barcode.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdf));
            bottomBarcodeImage.setWidth(UnitValue.createPercentValue(98));
            bottomBarcodeImage.setHeight(35);
            mainTable.addCell(new Cell().add(bottomBarcodeImage
                    .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER))
                    .setBorder(null).setPadding(1));

            document.add(mainTable);
            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("生成JIT揽收面单失败", e);
        }
    }
}