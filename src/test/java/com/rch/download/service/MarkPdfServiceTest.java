package com.rch.download.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MarkPdfServiceTest {

    @Autowired
    private MarkPdfService markPdfService;

    private Path testPdfPath;

    @BeforeEach
    void setUp() {
        testPdfPath = Paths.get("test_mark.pdf");
    }

    @AfterEach
    void tearDown() throws IOException {
        // 保留生成的PDF文件，不再删除
    }

    @Test
    void testGenerateMarkPdf() throws IOException {
        // 准备测试数据
        String boxNo = "A001";
        String totalBoxes = "10";
        String logisticsNo = "LBX0223841992272 3072 3072";
        String warehouseCode = "WH001";
        String inboundNo = "IN001";
        String warehouseName = "菜鸟东莞定制揽收集散7号仓";
        String containerNo = "CN001";
        String appointmentTime = "2024-01-20 10:00:00";
        String skuCount = "5";

        // 生成PDF
        byte[] pdfBytes = markPdfService.generateMarkPdf(
                boxNo, totalBoxes, logisticsNo,warehouseName,
                warehouseCode, inboundNo, containerNo,
                appointmentTime, skuCount
        );

        // 将PDF保存到临时文件
        try (FileOutputStream fos = new FileOutputStream(testPdfPath.toFile())) {
            fos.write(pdfBytes);
        }

        // 验证PDF文件是否生成
        File pdfFile = testPdfPath.toFile();
        assertTrue(pdfFile.exists(), "PDF文件应该被成功生成");
        assertTrue(pdfFile.length() > 0, "PDF文件不应该为空");
    }
}