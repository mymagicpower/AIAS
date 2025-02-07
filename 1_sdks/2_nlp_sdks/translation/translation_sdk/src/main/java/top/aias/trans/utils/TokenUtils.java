package top.aias.trans.utils;

import java.util.Arrays;
import java.util.Map;
/**
 * Token工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TokenUtils {

    private TokenUtils() {
    }

    /**
     * Token 解码
     * 根据语言的类型更新下面的方法
     *
     * @param reverseMap
     * @param outputIds
     * @return
     */
    public static String decode(Map<Long, String> reverseMap, long[] outputIds) {
        int[] intArray = Arrays.stream(outputIds).mapToInt(l -> (int) l).toArray();

        StringBuffer sb = new StringBuffer();
        for (int value : intArray) {
            // 65000 <pad>
            // 0 </s>
            if (value == 65000 || value == 0 || value == 8)
                continue;
            String text = reverseMap.get(Long.valueOf(value));
            sb.append(text);
        }

        String result = sb.toString();
        result = result.replaceAll("▁"," ");
        return result;
    }
}