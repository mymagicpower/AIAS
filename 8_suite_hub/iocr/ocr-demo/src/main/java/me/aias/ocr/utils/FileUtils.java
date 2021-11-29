package me.aias.ocr.utils;

import ai.djl.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.aias.ocr.configuration.FileProperties;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 文件上传工具包
 */
public class FileUtils {
    /**
     * @param file     文件
     * @param path     文件存放路径
     * @param fileName 源文件名
     * @return
     */
    public static boolean upload(MultipartFile file, String path, String fileName) {
        // 生成新的文件名
        //String realPath = path + "/" + FileNameUtils.getFileName(fileName);
        Path filePath = Paths.get(path + fileName);
        File dest = filePath.toAbsolutePath().toFile();

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     *
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName) {
        return UUIDUtils.getUUID() + getSuffix(fileOriginName);
    }

    /**
     * 读取json文件
     *
     * @param path     文件路径信息
     * @param fileName 文件名
     * @return
     */
    public static String readFile(String path, String fileName) throws IOException {
        StringBuilder json = new StringBuilder();
        Path filePath = Paths.get(path + fileName);
        List<String> lines = Utils.readLines(filePath, true);
        lines.stream()
                .filter(line -> (line != null && line != ""))
                .forEach(
                        line -> {
                            json.append(line);
                        });

        return json.toString();
    }

    /**
     * 保存json文件
     *
     * @param path     文件路径信息
     * @param fileName 文件名
     * @param json     json信息
     * @return
     */
    public static void saveFile(String path, String fileName, String json) throws IOException {
        Path filePath = Paths.get(path + fileName);
        try (PrintStream ps = new PrintStream(new FileOutputStream(filePath.toFile()))) {
            ps.print(json);
        }
    }

    /**
     * 删除json文件
     *
     * @param path     文件路径信息
     * @param fileName 文件名
     * @return
     */
    public static void removeFile(String path, String fileName) {
        Path filePath = Paths.get(path + fileName);
        filePath.toFile().delete();
    }

    /**
     * Check & create file path
     *
     * @param fileRelativePath 文件路径信息
     * @return
     */
    public static void checkAndCreatePath(String fileRelativePath) {
        //Check & create file path
        Path filePath = Paths.get(fileRelativePath).toAbsolutePath();
        File file = filePath.toFile();
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }
}