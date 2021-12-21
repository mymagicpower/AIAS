package me.aias.common.utils;

import java.util.UUID;

/**
 * 获取UUID
 *
 * @author Calvin
 * @date 2021-12-12
 **/
public class UUIDUtil {
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
