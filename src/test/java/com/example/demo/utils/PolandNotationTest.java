package com.example.demo.utils;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PolandNotationTest {

    @Test
    public void testBasicOperations() {
        // 基本四则运算测试
        assertEquals(5L, PolandNotation.<Long>calculate("2+3"));
        assertEquals(-1L, PolandNotation.<Long>calculate("2-3"));
        assertEquals(6L, PolandNotation.<Long>calculate("2*3"));
        assertEquals(2L, PolandNotation.<Long>calculate("6/3"));
    }

    @Test
    public void testComplexExpressions() {
        // 复杂混合运算测试
        assertEquals(14L, PolandNotation.<Long>calculate("2+3*4"));
        assertEquals(20L, PolandNotation.<Long>calculate("(2+3)*4"));
        assertEquals(17L, PolandNotation.<Long>calculate("2+3*4+3"));
        assertEquals(25L, PolandNotation.<Long>calculate("(2+3)*(4+1)"));
    }

    @Test
    public void testComplexNestedParentheses() {
        // 复杂嵌套括号测试（至少5个括号，3层嵌套）
        assertEquals(39L, PolandNotation.<Long>calculate("((2+3)*(4+(1*2)))+(3*(1+2))"));
        assertEquals(480L, PolandNotation.<Long>calculate("((5+3)*(4+(2*3)))*(1+(2+3))"));
        assertEquals(17D, PolandNotation.<Double>calculate("((2.5+1)*(3+(0.5*2)))+(1*(2+1))"), 0.0001);
        assertEquals(39L, PolandNotation.<Long>calculate("(2*(3+(4*(1+2))))+(3*(2+1))"));
        assertEquals(76L, PolandNotation.<Long>calculate("((2+3)*(4+(5*(1+1))))+(2*(1+2))"));
    }

    @Test
    public void testNestedParentheses() {
        // 嵌套括号测试
        assertEquals(30L, PolandNotation.<Long>calculate("2*(3*(4+1))"));
        assertEquals(95L, PolandNotation.<Long>calculate("(2+3)*(4+5*(2+1))"));
        assertEquals(21L, PolandNotation.<Long>calculate("((2+3)*2)+11"));
    }

    @Test
    public void testFloatingPointNumbers() {
        // 浮点数测试
        assertEquals(5.5, PolandNotation.<Double>calculate("2.5+3"), 5.5D);
        assertEquals(7.5, PolandNotation.<Double>calculate("2.5*3"), 7.5D);
        assertEquals(1.5, PolandNotation.<Double>calculate("4.5/3"), 1.5D);
        assertEquals(8.0, PolandNotation.<Double>calculate("2.5+3*1.5+1"), 8D);
    }

    @Test
    public void testFloatingPointNumbers2() {
        // 浮点数测试
        assertEquals(5.5, PolandNotation.<Double>calculate("2.5+3.2222"), 5.5D);
        assertEquals(7.5, PolandNotation.<Double>calculate("2.5*3.99999"), 7.5D);
        assertEquals(1.5, PolandNotation.<Double>calculate("4.5/3.215"), 1.5D);
        assertEquals(8.0, PolandNotation.<Double>calculate("2.5+3*1.5+1"), 8D);
    }

    @Test
    public void testMixedNumberTypes() {
        // 混合数字类型测试
        assertEquals(5.5, PolandNotation.<Double>calculate("2.5+3"), 0.0001);
        assertEquals(8.0, PolandNotation.<Double>calculate("2*(2+2)"), 0.0001);
        assertEquals(7.8, PolandNotation.<Double>calculate("2.5+3.3+2"), 0.0001);
    }

    @Test
    public void testLargeNumbers() {
        // 大数测试
        assertEquals(1000000L, PolandNotation.<Long>calculate("1000*1000"));
        assertEquals(1000000.5, PolandNotation.<Double>calculate("1000000+0.5"), 0.0001);
    }

    @Test
    public void testPrecedence() {
        // 运算符优先级测试
        assertEquals(20L, PolandNotation.<Long>calculate("(2+3)*4"));
        assertEquals(14L, PolandNotation.<Long>calculate("2+3*4"));
        assertEquals(11L, PolandNotation.<Long>calculate("2+3*4-3"));
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
