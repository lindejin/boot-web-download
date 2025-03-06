package com.example.demo.utils;

/**
 * 运算符优先级
 */
public class Operation {
    private static final int ADD = 1;
    private static final int SUB = 1;
    private static final int MUL = 2;
    private static final int DIV = 2;
    
    /**
     * 获取运算符优先级
     * @param operation 运算符
     * @return 优先级值
     */
    public static int getValue(String operation) {
        switch (operation) {
            case "+":
                return ADD;
            case "-":
                return SUB;
            case "*":
                return MUL;
            case "/":
                return DIV;
            default:
                return 0;
        }
    }
}