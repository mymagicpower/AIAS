package me.aias.utils;

import java.util.UUID;

/**
 * 生成文件名
 * generate file name
 */
public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}