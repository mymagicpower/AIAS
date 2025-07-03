package me.calvin.modules.search.common.utils;

import java.util.UUID;
/**
 * 获取UUID
 * Get UUID
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.tops
 **/
public class UUIDUtil {
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
