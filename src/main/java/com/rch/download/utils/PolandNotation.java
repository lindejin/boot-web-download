package com.rch.download.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * 波兰表达式（逆波兰表达式）计算工具类
 */
public class PolandNotation {
    
    /**
     * 计算表达式结果
     * @param expression 表达式字符串
     * @param <T> 返回类型
     * @return 计算结果
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T calculate(String expression) {
        // 将中缀表达式转换成后缀表达式
        List<String> suffixExpression = parseSuffixExpression(expression);
        
        // 创建栈
        Stack<String> stack = new Stack<>();
        
        // 循环遍历
        for (String item : suffixExpression) {
            String regex = getRegex();
            if (Pattern.matches(regex, item)) {
                // 如果是数字直接入栈
                stack.push(item);
            } else {
                // 如果是操作符
                // 出栈两个数字，并运算，再入栈
                Number num1 = parseNumber(stack.pop());
                Number num2 = parseNumber(stack.pop());
                
                Number result;
                
                switch (item) {
                    case "+":
                        result = add(num2, num1);
                        break;
                    case "*":
                        result = multiply(num2, num1);
                        break;
                    case "/":
                        result = divide(num2, num1);
                        break;
                    case "-":
                        result = subtract(num2, num1);
                        break;
                    default:
                        throw new IllegalArgumentException("无法识别符号: " + item);
                }
                
                stack.push(result.toString());
            }
        }
        
        // 最后把stack中数据返回
        return (T) parseNumber(stack.pop());
    }
    
    /**
     * 字符串转中缀表达式的List
     */
    private static List<String> toInfixExpression(String expression) {
        List<String> list = new ArrayList<>();
        int index = 0;
        String str;
        
        while (index < expression.length()) {
            char ch = expression.charAt(index);
            // 如果不是数字直接入链表
            if (ch < '0' || ch > '9') {
                list.add(String.valueOf(ch));
                index++;
            } else {
                str = "";
                // 多位数判断
                while (index < expression.length() && 
                      ((expression.charAt(index) >= '0' && expression.charAt(index) <= '9') || 
                       expression.charAt(index) == '.')) {
                    str += expression.charAt(index);
                    index++;
                }
                list.add(str);
            }
        }
        
        return list;
    }
    
    /**
     * 中缀转后缀
     */
    private static List<String> parseSuffixExpression(String expression) {
        List<String> expressionList = toInfixExpression(expression);
        
        // 存储中间结果
        List<String> list = new ArrayList<>();
        // 符号栈
        Stack<String> stack = new Stack<>();
        
        for (String item : expressionList) {
            // 如果是数字直接加入list
            if (Pattern.matches(getRegex(), item)) {
                list.add(item);
            }
            // 如果是左括号，直接入符号栈
            else if (item.equals("(")) {
                stack.push(item);
            }
            // 如果是右括号
            else if (item.equals(")")) {
                // 依次弹出stack栈顶的运算符，并存入list,直到遇到左括号为止
                while (!stack.peek().equals("(")) {
                    list.add(stack.pop());
                }
                // 将(也出栈
                stack.pop();
            }
            // 如果是运算符
            else {
                // 循环判断item的优先级小于或者等于stack栈顶运算符，将stack栈顶的运算符出栈并加入到list中
                while (!stack.isEmpty() && Operation.getValue(stack.peek()) >= Operation.getValue(item)) {
                    list.add(stack.pop());
                }
                // 将item入栈
                stack.push(item);
            }
        }
        
        // 将stack剩余的运算符依次入list
        while (!stack.isEmpty()) {
            list.add(stack.pop());
        }
        
        return list;
    }
    
    /**
     * 获取数字正则表达式
     */
    private static String getRegex() {
        return "(\\d+\\.\\d+|\\d+)";
    }
    
    /**
     * 解析数字
     */
    private static Number parseNumber(String str) {
        if (str.contains(".")) {
            return Double.parseDouble(str);
        } else {
            return Long.parseLong(str);
        }
    }
    
    /**
     * 加法运算
     */
    private static Number add(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            BigDecimal bd1 = BigDecimal.valueOf(a.doubleValue());
            BigDecimal bd2 = BigDecimal.valueOf(b.doubleValue());
            return bd1.add(bd2).doubleValue();
        }
        return a.longValue() + b.longValue();
    }
    
    /**
     * 减法运算
     */
    private static Number subtract(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            BigDecimal bd1 = BigDecimal.valueOf(a.doubleValue());
            BigDecimal bd2 = BigDecimal.valueOf(b.doubleValue());
            return bd1.subtract(bd2).doubleValue();
        }
        return a.longValue() - b.longValue();
    }
    
    /**
     * 乘法运算
     */
    private static Number multiply(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            BigDecimal bd1 = BigDecimal.valueOf(a.doubleValue());
            BigDecimal bd2 = BigDecimal.valueOf(b.doubleValue());
            return bd1.multiply(bd2).doubleValue();
        }
        return a.longValue() * b.longValue();
    }
    
    /**
     * 除法运算
     */
    private static Number divide(Number a, Number b) {
        if (a instanceof Double || b instanceof Double) {
            if (b.doubleValue() == 0) {
                throw new IllegalArgumentException("除数不能为0");
            }
            BigDecimal bd1 = BigDecimal.valueOf(a.doubleValue());
            BigDecimal bd2 = BigDecimal.valueOf(b.doubleValue());
            return bd1.divide(bd2, 10, RoundingMode.HALF_UP).doubleValue();
        }
        if (b.longValue() == 0) {
            throw new IllegalArgumentException("除数不能为0");
        }
        return a.longValue() / b.longValue();
    }
}