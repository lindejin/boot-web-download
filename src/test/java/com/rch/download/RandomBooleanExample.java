package com.rch.download;

import java.util.concurrent.ThreadLocalRandom;

public class RandomBooleanExample {
    public static void main(String[] args) {
        boolean randomBoolean = ThreadLocalRandom.current().nextBoolean(); // 随机获取 true 或 false
        System.out.println("Random Boolean: " + randomBoolean);
    }
}
