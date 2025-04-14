package com.rch.download.service;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
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
import com.itextpdf.layout.properties.VerticalAlignment;
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
    private static final float INFO_CONTENT_FONT_SIZE = 10f;
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
    private static final float URGENT_ROW_HEIGHT_MM = UnitUtils.mmToPt(17f);
    /**
     * 仓库位置行高度（毫米）
     */
    private static final float LOCATION_ROW_HEIGHT_MM = UnitUtils.mmToPt(33f);
    /**
     * 发货单信息行高度（毫米）
     */
    private static final float INFO_ROW_HEIGHT_MM = UnitUtils.mmToPt(15f);
    /**
     * 配送信息行高度（毫米）
     */
    private static final float DELIVERY_ROW_HEIGHT_MM = UnitUtils.mmToPt(15f);
    /**
     * 注意事项行高度（毫米）
     */
    private static final float NOTE_ROW_HEIGHT_MM = UnitUtils.mmToPt(20f);

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
            document.setMargins(10, 10, 5, 10);

            // 加载不同字重的思源黑体字体
            PdfFont regularFont = PdfFontFactory.createFont(ExpressLabelService.class.getClassLoader().getResource("fonts/SourceHanSansSC-Medium.otf").getPath());
            PdfFont boldFont = PdfFontFactory.createFont(ExpressLabelService.class.getClassLoader().getResource("fonts/SourceHanSansSC-Bold.otf").getPath());
            PdfFont lightFont = PdfFontFactory.createFont(ExpressLabelService.class.getClassLoader().getResource("fonts/SourceHanSansSC-Light.otf").getPath());

            document.setFont(regularFont);

            // 主表格（设置固定高度为95mm）
            Table mainTable = new Table(new float[]{1});
            mainTable.setWidth(UnitValue.createPercentValue(100));
            mainTable.setBorder(new SolidBorder(0.5f));
            mainTable.setHeight(UnitValue.createPointValue(UnitUtils.mmToPt(95f)));


            // 1. 加急标识行
            Table urgentTable = new Table(new float[]{1});
            urgentTable.setWidth(UnitValue.createPercentValue(100));
            urgentTable.setHeight(UnitValue.createPointValue(URGENT_ROW_HEIGHT_MM));
            urgentTable.addCell(new Cell().add(new Paragraph("加急").setFontSize(URGENT_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER).setFont(boldFont))
                    .setBackgroundColor(new DeviceRgb(51, 51, 51))
                    .setFontColor(ColorConstants.WHITE)
                    .setBorder(null).setPadding(0));
            mainTable.addCell(new Cell().add(urgentTable).setBorder(null).setPadding(0));

            // 2. 仓库位置行
            Table locationTable = new Table(new float[]{1});
            locationTable.setWidth(UnitValue.createPercentValue(100));
            locationTable.setHeight(UnitValue.createPointValue(LOCATION_ROW_HEIGHT_MM));
            locationTable.addCell(new Cell().add(new Paragraph(warehouseLocation).setFontSize(LOCATION_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER).setFont(boldFont)
                            .setFixedLeading(LOCATION_FONT_SIZE * 1.2f))
                    .setBorder(null).setPadding(2).setVerticalAlignment(VerticalAlignment.MIDDLE));
            mainTable.addCell(new Cell().add(locationTable).setBorder(null).setPadding(0));

            // 3. 发货单信息行
            Table leftInfoTable = new Table(new float[]{1});
            leftInfoTable.setWidth(UnitValue.createPercentValue(100));
            leftInfoTable.setHeight(UnitValue.createPointValue(INFO_ROW_HEIGHT_MM));
            leftInfoTable.addCell(new Cell().add(new Paragraph("发货单总数")
                            .setFixedLeading(DELIVERY_TITLE_FONT_SIZE * 1.1f)
                            .setFontSize(INFO_TITLE_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER)).setFont(lightFont)
                    .setBorderRight(new SolidBorder(0.5f))
                    .setBorderLeft(Border.NO_BORDER)
                    .setBorderTop(new SolidBorder(0.5f))
                    .setBorderBottom(Border.NO_BORDER)
                    .setPadding(2));
            leftInfoTable.addCell(new Cell().add(new Paragraph("1")
                            .setFixedLeading(DELIVERY_TITLE_FONT_SIZE * 1.1f)
                            .setFontSize(INFO_CONTENT_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER).setFont(boldFont))
                    .setBorderRight(new SolidBorder(0.5f))
                    .setBorderLeft(Border.NO_BORDER)
                    .setBorderTop(Border.NO_BORDER)
                    .setBorderBottom(Border.NO_BORDER)
                    .setPadding(2));

            Table rightInfoTable = new Table(new float[]{1});
            rightInfoTable.setWidth(UnitValue.createPercentValue(100));
            rightInfoTable.setHeight(UnitValue.createPointValue(INFO_ROW_HEIGHT_MM));
            rightInfoTable.addCell(new Cell().add(new Paragraph("PC包裹总数")
                            .setFixedLeading(DELIVERY_TITLE_FONT_SIZE * 1.1f)
                            .setFontSize(INFO_TITLE_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER)).setFont(lightFont)
                    .setBorderLeft(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER)
                    .setBorderTop(new SolidBorder(0.5f))
                    .setBorderBottom(Border.NO_BORDER)
                    .setPadding(2));
            rightInfoTable.addCell(new Cell().add(new Paragraph("1")
                            .setFixedLeading(DELIVERY_TITLE_FONT_SIZE * 1.1f)
                            .setFontSize(INFO_CONTENT_FONT_SIZE)
                            .setTextAlignment(TextAlignment.CENTER).setFont(boldFont))
                    .setBorderLeft(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER)
                    .setBorderTop(Border.NO_BORDER)
                    .setBorderBottom(Border.NO_BORDER)
                    .setPadding(2));

            Table infoContainer = new Table(new float[]{1, 1});
            infoContainer.setWidth(UnitValue.createPercentValue(100));
            infoContainer.addCell(new Cell().add(leftInfoTable).setBorder(null).setPadding(0).setWidth(UnitValue.createPercentValue(50)));
            infoContainer.addCell(new Cell().add(rightInfoTable).setBorder(null).setPadding(0).setWidth(UnitValue.createPercentValue(50)));
            mainTable.addCell(new Cell().add(infoContainer).setBorder(null).setPadding(0));

            // 4. 配送信息行
            Table deliveryTable = new Table(new float[]{1});
            deliveryTable.setWidth(UnitValue.createPercentValue(100));
            deliveryTable.setHeight(UnitValue.createPointValue(DELIVERY_ROW_HEIGHT_MM));
            deliveryTable.addCell(new Cell().add(new Paragraph("自行配送").setFontSize(DELIVERY_TITLE_FONT_SIZE)
                            .setTextAlignment(TextAlignment.LEFT).setFont(boldFont)
                            .setFixedLeading(DELIVERY_TITLE_FONT_SIZE * 1.1f))
                    .setBorder(Border.NO_BORDER)
                    .setPaddings(0,0,0,10));
            deliveryTable.addCell(new Cell().add(new Paragraph("司机：" + driverName).setFontSize(DELIVERY_CONTENT_FONT_SIZE)
                            .setTextAlignment(TextAlignment.LEFT).setFont(lightFont)
                            .setFixedLeading(DELIVERY_CONTENT_FONT_SIZE * 1.1f))
                    .setBorder(Border.NO_BORDER)
                    .setPaddings(0,0,0,10));
            deliveryTable.addCell(new Cell().add(new Paragraph("车牌号：" + plateNumber).setFontSize(DELIVERY_CONTENT_FONT_SIZE)
                            .setTextAlignment(TextAlignment.LEFT).setFont(lightFont)
                            .setFixedLeading(DELIVERY_CONTENT_FONT_SIZE * 1.1f))
                    .setBorder(Border.NO_BORDER)
                    .setPaddings(0,0,0,10));
            deliveryTable.setBorder(new SolidBorder(0.01f));
            mainTable.addCell(new Cell().add(deliveryTable).setBorder(null).setPadding(0));

            // 5. 注意事项行
            Table noteTable = new Table(new float[]{1});
            noteTable.setWidth(UnitValue.createPercentValue(100));
            noteTable.setHeight(UnitValue.createPointValue(NOTE_ROW_HEIGHT_MM));
            noteTable.addCell(new Cell().add(new Paragraph("*此包裹内是JIT/定制品，且加急的订单货物，需优先收货入库\n*请勿在(非JIT/非定制品/非加急订单)外箱张贴加急标识，否则会拒收").setFontSize(NOTE_FONT_SIZE)
                            .setTextAlignment(TextAlignment.LEFT).setFont(lightFont))
                    .setBorder(null).setPadding(2));
            mainTable.addCell(new Cell().add(noteTable).setBorder(null).setPadding(0));

            document.add(mainTable);
            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("生成快递标签失败", e);
        }
    }
}