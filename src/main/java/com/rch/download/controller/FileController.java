package com.rch.download.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileController {

    @GetMapping("/download")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        

        
        // 创建工作簿，使用SXSSF模式处理大数据量
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {// 设置内存中保留100行数据
            // 如果选择触发异常，则抛出异常
            if ("error".equals(action)) {
                throw new RuntimeException("用户选择触发异常！");
            }
            SXSSFSheet sheet = workbook.createSheet("大数据测试");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Data");

            // 根据action类型生成不同的数据
            int rowCount = "simple".equals(action) ? 100 : 100000;
            // 模拟大量数据写入
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(i);
                row.createCell(1).setCellValue("Name" + i);
                row.createCell(2).setCellValue("Data" + i);
            }

            // 设置响应头
            String filename = "large_data.xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" +
                URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()));

            // 写入响应流
            workbook.write(response.getOutputStream());

            // 清理临时文件
            workbook.dispose();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            String errorJson = String.format("{\"code\":%d,\"message\":\"生成Excel文件时发生错误: %s\"}", 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                e.getMessage().replace("\"", "\\\""));
            writer.write(new String(errorJson.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            writer.flush();
        }
    }

    @PostMapping("/download")
    public void downloadFilePost(@RequestBody DownloadRequest request, HttpServletResponse response) throws IOException {
        String action = request.getAction();

        // 创建工作簿，使用SXSSF模式处理大数据量
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {// 设置内存中保留100行数据
         // 如果选择触发异常，则抛出异常
            if ("error".equals(action)) {
                //加个时间戳
                throw new RuntimeException("用户选择触发异常！"+System.currentTimeMillis());
            }
            SXSSFSheet sheet = workbook.createSheet("数据");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Data");

            // 根据action类型生成不同的数据
            int rowCount = "simple".equals(action) ? 100 : 100000;
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(i);
                row.createCell(1).setCellValue("Name" + i);
                row.createCell(2).setCellValue("Data" + i);
            }

            // 设置响应头
            String filename = "large_data.xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" +
                URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()));

            // 写入响应流
            workbook.write(response.getOutputStream());

            // 清理临时文件
            workbook.dispose();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            String errorJson = String.format("{\"code\":%d,\"message\":\"%s\"}", 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                e.getMessage().replace("\"", "\\\""));
            writer.write(errorJson);
            writer.flush();
        }
    }

    public static class DownloadRequest {
        private String action;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }
}