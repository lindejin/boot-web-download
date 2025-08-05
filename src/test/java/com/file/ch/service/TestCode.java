package com.file.ch.service;

import java.util.Random;


public class TestCode {

    public static void main(String[] args) {
        TestCode testCode = new TestCode();
        System.out.println(testCode.generateThreeDigitRandom());
        System.out.println(testCode.generateThreeDigitRandom());

        System.out.println(testCode.generateThreeDigitRandom());

        System.out.println(testCode.generateThreeDigitRandom());

        System.out.println(testCode.generateThreeDigitRandom());

    }


    public int generateThreeDigitRandom() {
        Random random = new Random();
        return random.nextInt(900) + 100; // 100-999
    }
}
