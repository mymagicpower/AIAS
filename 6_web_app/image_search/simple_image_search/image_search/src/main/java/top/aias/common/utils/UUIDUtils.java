package top.aias.common.utils;

import java.util.UUID;

/**
 * UUID 工具类
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
public class UUIDUtils {
    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
