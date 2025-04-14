package com.rch.download.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ExpressLabelService {
    /**
     * 加急标识的字体大小
     */
    private static final float URGENT_FONT_SIZE = 24f;
    /**
     * 仓库位置的字体大小
     */
    private static final float LOCATION_FONT_SIZE = 22.7f;
    /**
     * 发货单信息标题的字体大小
     */
    private static final float INFO_TITLE_FONT_SIZE = 8.5f;
    /**
     * 发货单信息内容的字体大小
     */
    private static final float INFO_CONTENT_FONT_SIZE = 8.5f;
    /**
     * 配送信息标题的字体大小
     */
    private static final float DELIVERY_TITLE_FONT_SIZE = 11.3f;
    /**
     * 配送信息内容的字体大小
     */
    private static final float DELIVERY_CONTENT_FONT_SIZE = 9.9f;
    /**
     * 注意事项的字体大小
     */
    private static final float NOTE_FONT_SIZE = 8.5f;

    /**
     * 加急标识行高度（毫米）
     */
    private static final float URGENT_ROW_HEIGHT_MM = 15f;
    /**
     * 仓库位置行高度（毫米）
     */
    private static final float LOCATION_ROW_HEIGHT_MM = 20f;
    /**
     * 发货单信息行高度（毫米）
     */
    private static final float INFO_ROW_HEIGHT_MM = 15f;
    /**
     * 配送信息行高度（毫米）
     */
    private static final float DELIVERY_ROW_HEIGHT_MM = 25f;
    /**
     * 注意事项行高度（毫米）
     */
    private static final float NOTE_ROW_HEIGHT_MM = 20f;

    public byte[] generateExpressLabel(String warehouseLocation, String driverName, String plateNumber) {
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

            // 加载不同字重的思源黑体字体
            PdfFont regularFont = PdfFontFactory.createFont(ExpressLabelService.class.getClassLoader().getResource("fonts/SourceHanSansSC-Medium.otf").getPath());
            PdfFont boldFont = PdfFontFactory.createFont(ExpressLabelService.class.getClassLoader().getResource("fonts/SourceHanSansSC-Bold.otf").getPath());
            document.setFont(regularFont);

            // 主表格（设置固定高度为95mm）
            Table mainTable = new Table(new float[]{1});
            mainTable.setWidth(UnitValue.createPercentValue(100));
            mainTable.setBorder(new SolidBorder(0.5f));
            mainTable.setHeight(new UnitValue(UnitValue.POINT, 95f / 25.4f * 72f));
            mainTable.setPaddingTop(1)   // 上填充10点
                    .setPaddingBottom(3)// 下填充10点
                    .setPaddingRight(1)  // 右填充5点
                    .setPaddingLeft(1);  // 左填充5点

            // 1. 加急标识行
            Table urgentTable = new Table(new float[]{1});
            urgentTable.setWidth(UnitValue.createPercentValue(100));
            urgentTable.addCell(new Cell().add(new Paragraph("加急").setFontSize(URGENT_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER).setFont(boldFont))
                    .setBackgroundColor(ColorConstants.BLACK)
                    .setFontColor(ColorConstants.WHITE)
                    .setBorder(new SolidBorder(0.5f)).setPadding(4));
            mainTable.addCell(new Cell().add(urgentTable).setBorder(null).setPadding(0));

            // 2. 仓库位置行
            Table locationTable = new Table(new float[]{1});
            locationTable.setWidth(UnitValue.createPercentValue(100));
            locationTable.setHeight(new UnitValue(UnitValue.POINT, LOCATION_ROW_HEIGHT_MM / 25.4f * 72f));
            locationTable.addCell(new Cell().add(new Paragraph(warehouseLocation).setFontSize(LOCATION_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER).setFont(boldFont))
                    .setBorder(new SolidBorder(0.5f)).setPadding(6));
            mainTable.addCell(new Cell().add(locationTable).setBorder(null).setPadding(0));

            // 3. 发货单信息行
            Table infoTable = new Table(new float[]{1, 1});
            infoTable.setWidth(UnitValue.createPercentValue(100));
            infoTable.setHeight(new UnitValue(UnitValue.POINT, INFO_ROW_HEIGHT_MM / 25.4f * 72f));
            infoTable.addCell(new Cell().add(new Paragraph("发货单总数").setFontSize(INFO_TITLE_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBorder(new SolidBorder(0.5f)).setPadding(2));
            infoTable.addCell(new Cell().add(new Paragraph("PC包裹总数").setFontSize(INFO_TITLE_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBorder(new SolidBorder(0.5f)).setPadding(2));
            infoTable.addCell(new Cell().add(new Paragraph("1").setFontSize(INFO_CONTENT_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER).setFont(boldFont))
                    .setBorder(new SolidBorder(0.5f)).setPadding(2));
            infoTable.addCell(new Cell().add(new Paragraph("1").setFontSize(INFO_CONTENT_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER).setFont(boldFont))
                    .setBorder(new SolidBorder(0.5f)).setPadding(2));
            mainTable.addCell(new Cell().add(infoTable).setBorder(null).setPadding(0));

            // 4. 配送信息行
            Table deliveryTable = new Table(new float[]{1});
            deliveryTable.setWidth(UnitValue.createPercentValue(100));
            deliveryTable.setHeight(new UnitValue(UnitValue.POINT, DELIVERY_ROW_HEIGHT_MM / 25.4f * 72f));
            deliveryTable.addCell(new Cell().add(new Paragraph("自行配送").setFontSize(DELIVERY_TITLE_FONT_SIZE)
                            .setTextAlignment(TextAlignment.LEFT).setFont(boldFont))
                    .setBorder(Border.NO_BORDER)
                    .setPadding(2));
            deliveryTable.addCell(new Cell().add(new Paragraph("司机：" + driverName).setFontSize(DELIVERY_CONTENT_FONT_SIZE)
                            .setTextAlignment(TextAlignment.LEFT).setFont(regularFont))
                    .setBorder(Border.NO_BORDER)
                    .setPadding(2));
            deliveryTable.addCell(new Cell().add(new Paragraph("车牌号：" + plateNumber).setFontSize(DELIVERY_CONTENT_FONT_SIZE)
                            .setTextAlignment(TextAlignment.LEFT).setFont(regularFont))
                    .setBorder(Border.NO_BORDER)
                    .setPadding(2));
            deliveryTable.setBorder(new SolidBorder(0.5f));
            mainTable.addCell(new Cell().add(deliveryTable).setBorder(null).setPadding(0));

            // 5. 注意事项行
            Table noteTable = new Table(new float[]{1});
            noteTable.setWidth(UnitValue.createPercentValue(100));
            noteTable.setHeight(new UnitValue(UnitValue.POINT, NOTE_ROW_HEIGHT_MM / 25.4f * 72f));
            noteTable.addCell(new Cell().add(new Paragraph("*此包裹内是JIT/定制品，且加急的订单货物，需优先收货入库\n*请勿在(非JIT/非定制品/非加急订单)外箱张贴加急标识，否则会拒收").setFontSize(NOTE_FONT_SIZE)
                            .setTextAlignment(TextAlignment.LEFT).setFont(regularFont))
                    .setBorder(new SolidBorder(0.5f)).setPadding(2));
            mainTable.addCell(new Cell().add(noteTable).setBorder(null).setPadding(0));

            document.add(mainTable);
            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("生成快递标签失败", e);
        }
    }
}