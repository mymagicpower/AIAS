package top.aias.training.common.utils;

import java.util.UUID;

/**
 * 唯一ID
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class UUIDUtil {
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
}
