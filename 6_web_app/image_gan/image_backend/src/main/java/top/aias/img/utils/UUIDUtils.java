package top.aias.img.utils;

import java.util.UUID;

/**
 * 生成文件名
 * Generate file name
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class UUIDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}