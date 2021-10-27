package me.aias.example.utils;

import java.math.BigDecimal;

public class AudioDoubleConverter {

    public static double[] float2Double(float[] fs) {
        int size = fs.length;
        double[] db = new double[size];
        for (int i = 0; i < size; i++) {
            BigDecimal b = new BigDecimal(String.valueOf(fs[i]));
            db[i] = b.doubleValue();
        }
        return db;
    }
}