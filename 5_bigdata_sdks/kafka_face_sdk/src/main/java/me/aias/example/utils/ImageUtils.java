package me.aias.example.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;

/**
 * 常规图片处理工具
 */
public class ImageUtils {
    /**
     * base64 编码转换为 BufferedImage
     *
     * @param base64
     * @return
     */
    public static BufferedImage base64ToBufferedImage(String base64) {
        Decoder decoder = Base64.getDecoder();
        try {
            byte[] bytes = decoder.decode(base64);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BufferedImage 编码转换为 base64
     *
     * @param image
     * @return
     */
    public static String bufferedImageToBase64(BufferedImage image) {
        String base64 = null;
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            base64 = encoder.encodeToString(stream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
    }
}
