package com.rch.download.utils;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 异常工具类，提供异常信息的格式化输出和处理
 */
public class ExceptionUtils {

    /**
     * 获取异常的完整信息，包括异常的类名、消息、根因和堆栈跟踪
     *
     * @param e 异常对象
     * @return 格式化后的异常信息
     */
    public static String getFullExceptionMessage(Throwable e) {
        if (e == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        // 添加异常类名和消息
        sb.append("异常类型: ").append(e.getClass().getName())
          .append("\n消息内容: ").append(e.getMessage())
          .append("\n\n=== 完整堆栈信息 ===\n")
          .append(ExceptionUtil.stacktraceToString(e, 2000))
          .append("\n\n=== 根因分析 ===\n")
          .append(ExceptionUtil.getRootCauseMessage(e));

        // 获取所有的嵌套异常
        Throwable current = e;
        int nestedCount = 0;
        while (current.getCause() != null && current.getCause() != current) {
            nestedCount++;
            current = current.getCause();
            sb.append("\n\n[嵌套异常 ").append(nestedCount).append("]")
              .append("\n类型: ").append(current.getClass().getName())
              .append("\n消息: ").append(current.getMessage());
        }

        return sb.toString();
    }

    /**
     * 获取异常的简要信息，包括异常类名、消息和根因
     *
     * @param e 异常对象
     * @return 简要的异常信息
     */
    public static String getBriefExceptionMessage(Throwable e) {
        if (e == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("异常类型: ").append(e.getClass().getName())
          .append("\n消息内容: ").append(e.getMessage())
          .append("\n根因: ").append(ExceptionUtil.getRootCauseMessage(e));
        return sb.toString();
    }

    /**
     * 获取异常的堆栈跟踪信息，可以指定最大行数
     *
     * @param e        异常对象
     * @param maxLines 最大行数，如果小于1则返回完整堆栈
     * @return 堆栈跟踪信息
     */
    public static String getStackTrace(Throwable e, int maxLines) {
        if (e == null) {
            return "";
        }
        String stackTrace = ExceptionUtil.stacktraceToString(e);
        if (maxLines < 1) {
            return stackTrace;
        }
        // 按行分割并限制行数
        String[] lines = stackTrace.split("\n");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(lines.length, maxLines); i++) {
            sb.append(lines[i]).append("\n");
        }
        if (lines.length > maxLines) {
            sb.append("... (还有 ").append(lines.length - maxLines).append(" 行未显示)");
        }
        return sb.toString();
    }

    /**
     * 格式化异常信息，支持自定义格式
     *
     * @param e           异常对象
     * @param format      格式字符串，支持的占位符：
     *                   {class}: 异常类名
     *                   {message}: 异常消息
     *                   {root}: 根因
     *                   {stack}: 完整堆栈
     * @return 格式化后的异常信息
     */
    public static String format(Throwable e, String format) {
        if (e == null || StrUtil.isBlank(format)) {
            return "";
        }
        return format.replace("{class}", e.getClass().getName())
                     .replace("{message}", StrUtil.nullToEmpty(e.getMessage()))
                     .replace("{root}", ExceptionUtil.getRootCauseMessage(e))
                     .replace("{stack}", ExceptionUtil.stacktraceToString(e));
    }
}