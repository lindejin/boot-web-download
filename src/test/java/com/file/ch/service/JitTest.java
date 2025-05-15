package com.file.ch.service;


public class JitTest {
    public static void main(String[] args) {
        System.out.println(UnitUtils.mmToPt(100));

        // 1. 定义页面尺寸（100mm × 100mm）
        float widthMM = 100f;  // 毫米
        float heightMM = 100f;

        // 2. 将毫米转换为磅（1英寸=25.4mm, 1英寸=72磅）
        float widthPt = widthMM / 25.4f * 72f;
        float heightPt = heightMM / 25.4f * 72f;
        System.out.println(widthPt);
        System.out.println(heightPt);
    }
}
