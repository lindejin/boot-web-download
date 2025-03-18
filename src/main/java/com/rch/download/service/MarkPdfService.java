package com.rch.download.service;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
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
public class MarkPdfService {

    public byte[] generateMarkPdf(String boxNo, String totalBoxes, String logisticsNo,String warehouseName,
                                 String warehouseCode, String inboundNo, String containerNo,
                                 String appointmentTime, String skuCount) {

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
            document.setMargins(1, 1, 1, 1);

            PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H");
            document.setFont(font);

            // 主表格
            Table mainTable = new Table(new float[]{1});
            mainTable.setWidth(UnitValue.createPercentValue(100));
            mainTable.setBorder(new SolidBorder(0.5f));

            // 1. 顶部行（包含箱号/箱数、条码和美标识）
            Table topTable = new Table(new float[]{1, 3.5f, 0.5f});
            topTable.setFixedLayout(); // 关键代码：强制固定列宽
            topTable.setWidth(UnitValue.createPercentValue(100));
            topTable.setBorder(new SolidBorder(0.5f));

            // 箱号/箱数列
            Table boxInfoTable = new Table(new float[]{1});
            boxInfoTable.setWidth(UnitValue.createPercentValue(100));
            boxInfoTable.addCell(new Cell().add(new Paragraph("箱号/" + boxNo).setFontSize(10)
                    .setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(2));
            boxInfoTable.addCell(new Cell().add(new Paragraph("箱数/" + totalBoxes).setFontSize(10)
                    .setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(2));
            topTable.addCell(new Cell().add(boxInfoTable).setBorder(new SolidBorder(0.5f)).setPadding(2));

            // 条码列
            Barcode128 barcode = new Barcode128(pdf);
            barcode.setCode(logisticsNo);
            barcode.setBarHeight(35f);
            barcode.setX(1.2f);
            barcode.setAltText("");
            Image barcodeImage = new Image(barcode.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdf));
            barcodeImage.setWidth(UnitValue.createPercentValue(90));
            barcodeImage.setHeight(35);
            Table barcodeTable = new Table(new float[]{1});
            barcodeTable.setWidth(UnitValue.createPercentValue(100));
            barcodeTable.addCell(new Cell().add(barcodeImage
                    .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER))
                    .setBorder(null).setPadding(1));
            barcodeTable.addCell(new Cell().add(new Paragraph("物流单号：" + logisticsNo)
                    .setFontSize(8).setTextAlignment(TextAlignment.CENTER))
                    .setBorder(null).setPadding(2));
            Cell barcodeCell = new Cell().add(barcodeTable);
            barcodeCell.setBorder(new SolidBorder(0.5f));
            barcodeCell.setPadding(2);
            topTable.addCell(barcodeCell);

            // 美标识列
            Cell beautyCell = new Cell();
            Table beautyTable = new Table(new float[]{1});
            beautyTable.setWidth(UnitValue.createPercentValue(100));
            beautyTable.addCell(new Cell().add(new Paragraph("").setFontSize(10)).setBorder(null).setPadding(1));
            beautyTable.addCell(new Cell().add(new Paragraph("美").setFontSize(21).setTextAlignment(TextAlignment.CENTER).setBold()).setBorder(null).setPadding(1));
            beautyCell.add(beautyTable);
            beautyCell.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
            topTable.addCell(beautyCell.setBorder(new SolidBorder(0.5f)).setPadding(2));

            // 将顶部表格添加到主表格
            mainTable.addCell(new Cell().add(topTable).setBorder(null).setPadding(1));

            // 仓库信息行
            Table warehouseTable = new Table(new float[]{1});
            warehouseTable.setWidth(UnitValue.createPercentValue(100));
            warehouseTable.setBorder(new SolidBorder(0.5f));
            warehouseTable.addCell(new Cell().add(new Paragraph("(JIT)" + warehouseName)
                    .setFontSize(10).setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(3));
            mainTable.addCell(new Cell().add(warehouseTable).setBorder(null).setPadding(1));

            // 商家信息和入库单号行
            Table merchantTable = new Table(new float[]{1});
            merchantTable.setWidth(UnitValue.createPercentValue(100));
            merchantTable.setBorder(new SolidBorder(0.5f));
            merchantTable.addCell(new Cell().add(new Paragraph("商家编码：" + warehouseCode)
                    .setFontSize(10).setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(3));
            merchantTable.addCell(new Cell().add(new Paragraph("入库单号：" + inboundNo)
                    .setFontSize(10).setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(3));
            mainTable.addCell(new Cell().add(merchantTable).setBorder(null).setPadding(1));

            // 创建两列布局表格
            Table infoTable = new Table(new float[]{1, 1});
            topTable.setFixedLayout(); // 关键代码：强制固定列宽
            infoTable.setWidth(UnitValue.createPercentValue(100));
            infoTable.setBorder(new SolidBorder(0.5f));

            // 第一列：仓库编码和预约时间
            Table leftColumn = new Table(new float[]{1});
            leftColumn.setWidth(UnitValue.createPercentValue(100));
            leftColumn.addCell(new Cell().add(new Paragraph("仓库编码：" + containerNo)
                    .setFontSize(10).setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(3));
            leftColumn.addCell(new Cell().add(new Paragraph("预约时间：" )
                    .setFontSize(10).setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(3));
            infoTable.addCell(new Cell().add(leftColumn).setBorder(new SolidBorder(0.5f)).setPadding(2));

            // 第二列：SKU数量和物流单号
            Table rightColumn = new Table(new float[]{1});
            rightColumn.setWidth(UnitValue.createPercentValue(100));
            rightColumn.addCell(new Cell().add(new Paragraph("SKU数量/总件数：" + skuCount + "/1")
                    .setFontSize(10).setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(3));
            rightColumn.addCell(new Cell().add(new Paragraph("物流单号：" )
                    .setFontSize(10).setTextAlignment(TextAlignment.LEFT)).setBorder(new SolidBorder(0.5f)).setPadding(3));
            infoTable.addCell(new Cell().add(rightColumn).setBorder(new SolidBorder(0.5f)).setPadding(2));

            mainTable.addCell(new Cell().add(infoTable).setBorder(null).setPadding(1));

            // 6. 底部条码行
            Table bottomBarcodeTable = new Table(new float[]{1});
            bottomBarcodeTable.setWidth(UnitValue.createPercentValue(100));
            Image bottomBarcodeImage = new Image(barcode.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdf));
            bottomBarcodeImage.setWidth(UnitValue.createPercentValue(90));
            bottomBarcodeImage.setHeight(35);
            bottomBarcodeTable.addCell(new Cell().add(bottomBarcodeImage
                    .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER))
                    .setBorder(null).setPadding(1));
            bottomBarcodeTable.addCell(new Cell().add(new Paragraph("物流单号：" + logisticsNo)
                    .setFontSize(8).setTextAlignment(TextAlignment.CENTER))
                    .setBorder(null).setPadding(2));
            mainTable.addCell(new Cell().add(bottomBarcodeTable)
                    .setBorder(null).setPadding(0));

            document.add(mainTable);
            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("生成箱唛PDF失败", e);
        }
    }
}