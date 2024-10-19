package top.aias.chatglm.tokenizer;

import ai.djl.ModelException;
import ai.djl.ndarray.NDArray;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public final class TokenUtils {

    private TokenUtils() {
    }

    /**
     * 当数组中有特殊字符时，整体decode 会有字符丢失的现象，所以改成逐个decode
     * @param processor
     * @param output
     * @return
     */
    public static String decode(SpProcessor processor, NDArray output) {
        long[] outputIds = output.toLongArray();
        int[] intArray = Arrays.stream(outputIds).mapToInt(l -> (int) l).toArray();

        StringBuffer sb = new StringBuffer();
        for (int value : intArray) {
            int[] arr = new int[]{value};
            String text = processor.decode(arr);
            sb.append(text);
        }

        return sb.toString();
    }
}