package com.rch.download.service;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PickListPdfServiceTest {

    @Test
    public void testGeneratePickListPdf() {
        PickListPdfService service = new PickListPdfService();
        byte[] pdfBytes = service.generatePickListPdf();
        assertNotNull(pdfBytes, "生成的PDF字节数组不应为空");
        // 可以在此处添加更多断言来验证PDF内容
        // 验证生成的PDF不为空且大小合理
        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(0);

        // 可选：保存PDF文件用于手动检查
        try (FileOutputStream fos = new FileOutputStream("pick_label"+System.currentTimeMillis()+".pdf")) {
            fos.write(pdfBytes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}