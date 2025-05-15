package com.file.ch.service;

/**
 * PDF单位转换工具类（符合iText标准）
 * 参考：iText默认单位体系
 */
public class UnitUtils {
    // 预计算转换系数（避免重复计算）
    private static final float MM_TO_PT_FACTOR = 72f / 25.4f;
    private static final float PT_TO_MM_FACTOR = 25.4f / 72f;
    private static final float INCH_TO_PT_FACTOR = 72f;

    /**
     * 毫米转点（精确到小数点后三位）
     *
     * @param mm 毫米值（必须≥0）
     * @return 对应点数
     * @throws IllegalArgumentException 输入负数时抛出
     */
    public static float mmToPt(float mm) {
        validateNonNegative(mm);
        return roundToThreeDecimal(mm * MM_TO_PT_FACTOR);
    }

    /**
     * 点转毫米（精确到小数点后三位）
     *
     * @param pt 点数值（必须≥0）
     * @return 对应毫米数
     * @throws IllegalArgumentException 输入负数时抛出
     */
    public static float ptToMm(float pt) {
        validateNonNegative(pt);
        return roundToThreeDecimal(pt * PT_TO_MM_FACTOR);
    }

    /**
     * 英寸转点（精确到整数）
     *
     * @param inch 英寸值（必须≥0）
     * @return 对应点数
     * @throws IllegalArgumentException 输入负数时抛出
     */
    public static int inchToPt(float inch) {
        validateNonNegative(inch);
        return Math.round(inch * INCH_TO_PT_FACTOR);
    }

    // 参数校验方法
    private static void validateNonNegative(float value) {
        if (value < 0) {
            throw new IllegalArgumentException("单位值不能为负数: " + value);
        }
    }

    // 精度控制方法
    private static float roundToThreeDecimal(float value) {
        return Math.round(value * 1000f) / 1000f;
    }

    /* 扩展方法（可选） */
    // 厘米转点（1厘米=10毫米）
    public static float cmToPt(float cm) {
        return mmToPt(cm * 10);
    }

    // 像素转点（需提供DPI）
    public static float pxToPt(float px, int dpi) {
        return inchToPt(px / dpi);
    }
}
