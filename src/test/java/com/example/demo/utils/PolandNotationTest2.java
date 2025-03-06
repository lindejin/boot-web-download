package com.example.demo.utils;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PolandNotationTest2 {

    private final JexlEngine jexl = new JexlBuilder().create();

    private Number calculateWithJexl(String expression) {
        // 替换所有的乘除符号为JEXL支持的格式
        expression = expression.replace('×', '*').replace('÷', '/');
        JexlExpression jexlExpression = jexl.createExpression(expression);
        return (Number) jexlExpression.evaluate(null);
    }

    private void assertExpressionEquals(String expression, Number expected) {
        Number polandResult = PolandNotation.calculate(expression);
        Number jexlResult = calculateWithJexl(expression);
        assertEquals(expected.doubleValue(), polandResult.doubleValue(), 0.0001,
                String.format("PolandNotation计算结果: %s, JEXL计算结果: %s", polandResult, jexlResult));
        assertEquals(expected.doubleValue(), jexlResult.doubleValue(), 0.0001,
                String.format("期望结果: %s, JEXL计算结果: %s", expected, jexlResult));
    }

    @Test
    public void testBasicOperations() {
        // 基本四则运算测试
        assertExpressionEquals("2+3", 5L);
        assertExpressionEquals("2-3", -1L);
        assertExpressionEquals("2*3", 6L);
        assertExpressionEquals("6/3", 2L);
    }

    @Test
    public void testComplexExpressions() {
        // 复杂混合运算测试
        assertExpressionEquals("2+3*4", 14L);
        assertExpressionEquals("(2+3)*4", 20L);
        assertExpressionEquals("2+3*4+3", 17L);
        assertExpressionEquals("(2+3)*(4+1)", 25L);
    }

    @Test
    public void testComplexNestedParentheses() {
        // 复杂嵌套括号测试（至少5个括号，3层嵌套）
        assertExpressionEquals("((2+3)*(4+(1*2)))+(3*(1+2))", 39L);
        assertExpressionEquals("((5+3)*(4+(2*3)))*(1+(2+3))", 480L);
        assertExpressionEquals("((2.5+1)*(3+(0.5*2)))+(1*(2+1))", 17L);
        assertExpressionEquals("(2*(3+(4*(1+2))))+(3*(2+1))", 39L);
        assertExpressionEquals("((2+3)*(4+(5*(1+1))))+(2*(1+2))", 76L);
    }

    @Test
    public void testNestedParentheses() {
        // 嵌套括号测试
        assertExpressionEquals("2*(3*(4+1))", 30L);
        assertExpressionEquals("(2+3)*(4+5*(2+1))", 95L);
        assertExpressionEquals("((2+3)*2)+11", 21L);
    }

    @Test
    public void testFloatingPointNumbers() {
        // 浮点数测试
        assertExpressionEquals("2.5+3*1.5+1", 8.0);
        assertExpressionEquals("2.5+3", 5.5);
        assertExpressionEquals("2.5*3", 7.5);
        assertExpressionEquals("4.5/3.111", 1.5);

    }

    @Test
    public void testMixedNumberTypes() {
        // 混合数字类型测试
        assertExpressionEquals("2.5+3", 5.5);
        assertExpressionEquals("2*(2+2)", 8.0);
        assertExpressionEquals("2.5+3.3+2", 7.8);
    }

    @Test
    public void testLargeNumbers() {
        // 大数测试
        assertExpressionEquals("1000*1000", 1000000L);
        assertExpressionEquals("1000000+0.5", 1000000.5);
    }

    @Test
    public void testPrecedence() {
        // 运算符优先级测试
        assertExpressionEquals("2+3*4", 14L);
        assertExpressionEquals("(2+3)*4", 20L);
        assertExpressionEquals("2+3*4-3", 13L);
    }

    @Test
    public void testExceptionCases() {
        // 异常情况测试
        assertThrows(IllegalArgumentException.class, () -> PolandNotation.calculate("2++3"));
        assertThrows(IllegalArgumentException.class, () -> PolandNotation.calculate("2+3)"));
        assertThrows(IllegalArgumentException.class, () -> PolandNotation.calculate("2+3/0"));
        assertThrows(IllegalArgumentException.class, () -> PolandNotation.calculate("a+b"));
    }
}
