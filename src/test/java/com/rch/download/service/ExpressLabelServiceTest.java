package com.rch.download.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ExpressLabelServiceTest {

    @Autowired
    private ExpressLabelService expressLabelService;

    @Test
    public void testGenerateExpressLabel() throws IOException {
        // 准备测试数据
        String warehouseLocation = "义乌宝湾1号子仓（前置收货）";
        String driverName = "张三";
        String plateNumber = "浙G228J6";

        // 生成快递标签
        byte[] pdfBytes = expressLabelService.generateExpressLabel(warehouseLocation, driverName, plateNumber);

        // 验证生成的PDF不为空且大小合理
        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(0);

        // 可选：保存PDF文件用于手动检查
        try (FileOutputStream fos = new FileOutputStream("test_express_label"+System.currentTimeMillis()+".pdf")) {
            fos.write(pdfBytes);
        }
    }
}