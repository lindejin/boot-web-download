package com.rch.download.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JITReceiptServiceTest {

    @Autowired
    private JITReceiptService jitReceiptService;

    @Test
    public void testGenerateJITReceipt() throws IOException {
        // 准备测试数据
        String receiptNo = "JIT20240101001";
        String warehouseCode = "WH001";
        String destinationCity = "上海";
        String contactPhone = "13800138000";
        String address = "上海市浦东新区张江高科技园区";
        int boxCount = 5;
        int skuCount = 10;

        // 生成PDF
        byte[] pdfBytes = jitReceiptService.generateJITReceipt(
                receiptNo,
                warehouseCode,
                destinationCity,
                contactPhone,
                address,
                boxCount,
                skuCount
        );

        // 验证PDF生成是否成功
        assertNotNull(pdfBytes, "生成的PDF不应为空");
        assertTrue(pdfBytes.length > 0, "PDF文件大小应大于0");

        // 保存PDF文件到项目目录
        // 获取项目目录路径
        Path projectDir = Paths.get(System.getProperty("user.dir"));

        // 在项目目录下创建 PDF 文件路径
        Path pdfPath = projectDir.resolve("test_receipt.pdf");

        try (FileOutputStream fos = new FileOutputStream(pdfPath.toFile())) {
            fos.write(pdfBytes);
        }

        // 验证文件是否成功保存
        assertTrue(Files.exists(pdfPath), "PDF文件应该已经保存到临时目录");
        assertTrue(Files.size(pdfPath) > 0, "保存的PDF文件大小应大于0");

        // 清理临时文件
//        Files.delete(pdfPath);
//        Files.delete(tempDir);
    }
}