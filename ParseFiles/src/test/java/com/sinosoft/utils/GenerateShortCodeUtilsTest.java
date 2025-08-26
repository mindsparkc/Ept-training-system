package com.sinosoft.utils;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GenerateShortCodeUtilsTest {

    @Test
    void generateShortCode() {
        String[] shortCodes = GenerateShortCodeUtils.generateShortCodes("123", 8);

        System.out.println(Arrays.asList(shortCodes));

        String shortCode = GenerateShortCodeUtils.generateShortCode("123", 6);
        System.out.println(shortCode);

        // 使用布隆过滤器测试 10000 个 重复
        BloomFilter bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf-8")), 10000, 0.000001);
        for (int i=0; i<10000; i++) {
            String s = GenerateShortCodeUtils.generateShortCode("123", 6);
            boolean b = bloomFilter.mightContain(s);
            if (b) {
                System.out.println("重复：" + s);
            }
            bloomFilter.put(s);
        }
    }
}