package com.sinosoft.utils;


import org.springframework.cglib.core.Converter;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerateShortCodeUtils {

    private static List<String> sources = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "m", "n", "o", "p","q", "r", "s", "t", "u", "v", "w","x", "y", "z", "0",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");


    public static String generateShortCode(String str, int len) {
        Random random = new Random();
        int index = random.nextInt(4);
        return generateShortCodes(str, len)[index];
    }

    public static String[] generateShortCodes(String str, int len) {
        String[] result = new String[4];

        try {
            String reverse = (new StringBuilder(str)).reverse().toString();
            String md5 = DigestUtils.md5DigestAsHex(reverse.getBytes("UTF-8"));
            List<String> charList = listRandom();

            for (int i=0; i<4; i++) {
                String s = md5.substring(i * 8, i * 8 + 8);
                Long hexLong = 0x3FFFFFFF & Long.parseLong (s, 16);
                String oChar = "";
                for (int j=0; j<len; j++) {

                    Long index = 0x0000003D & hexLong;
                    oChar += charList.get(index.intValue());
                    hexLong = hexLong >> (30/len);
                }
                result[i] = oChar;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取一个随机数组
     *
     * @return
     */
    private static List<String> listRandom() {
        Random rd = new Random();
        int index = 0;
        String temp;
        for (int i = 0; i < sources.size(); i++)
        {
            index = rd.nextInt(sources.size() - 1);
            if (index != i)
            {
                temp = sources.get(i);
                sources.set(i, sources.get(index));
                sources.set(index, temp);
            }
        }
        return sources;
    }
}
