package com.example.demo.utils;

import java.util.Random;

public class RandomUtil {
    public static String generateRandomString(int length){
        Random random = new Random();
        // thread safe
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++){
            stringBuffer.append(random.nextInt(9));
        }
        return stringBuffer.toString();
    }
}
