package me.aias.common.utils;

import java.util.UUID;

/**
 * @author Calvin
 * @date 2021-06-20
 **/
public class UUIDUtil {
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
