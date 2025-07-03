package me.calvin.modules.search.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
/**
 * 图片工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class ImageUtil {

    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * multipartFile 编码转换为 BufferedImage
     */
    public static BufferedImage multipartFileToBufImage(MultipartFile imageFile) {
        try {
            InputStream ins = imageFile.getInputStream();
            byte[] bytes = ImageUtil.readInputStream(ins);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return ImageIO.read(bais);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MultipartFile对象转字节数组
     */
    public static byte[] multipartFileToBytes(MultipartFile file) {
        InputStream ins = null;
        try {
            ins = file.getInputStream();
            byte[] bytes = ImageUtil.readInputStream(ins);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * file 转 byte数组
     */
    public static byte[] file2Byte(File file) throws IOException {
        byte[] buffer = null;
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1) {
            bos.write(b, 0, n);
        }
        fis.close();
        bos.close();
        buffer = bos.toByteArray();
        return buffer;
    }

    /**
     * 根据地址获得数据的字节流
     */
    public static byte[] getImageByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream(); // 通过输入流获取图片数据
            byte[] bytes = readInputStream(inStream); // 得到图片的二进制数据
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * bytes 编码转换为 BufferedImage
     */
    public static BufferedImage bytesToBufferedImage(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存字节数组图片到指定path
     */
    public static void bytesToImageFile(byte[] bs, String filePath) throws IOException {
        FileOutputStream os = new FileOutputStream(filePath);
        os.write(bs);
        os.close();
    }

    /**
     * 根据日期生成本地图片相对保存路径
     */
    public static String generatePath(String fileRoot) {
        Date date = new Date();
        // yyyy/MM/dd/
        String relativePath = DateUtil.YYYY_MM_dd.get().format(date);
        String filePath = fileRoot + relativePath;
        // 如果不存在,创建文件夹
        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        return relativePath;
    }

    /**
     * 获得图片的格式，例如：JPEG、GIF等
     */
    public static String getImageFormat(Object obj) {
        ImageInputStream iis = null;
        try {
            iis = ImageIO.createImageInputStream(obj);
            Iterator<ImageReader> iterator = ImageIO.getImageReaders(iis);
            while (iterator.hasNext()) {
                ImageReader reader = (ImageReader) iterator.next();
                String suffix = reader.getFormatName();
                return suffix.toLowerCase();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (iis != null) {
                try {
                    iis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
