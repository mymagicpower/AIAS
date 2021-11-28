package me.aias.common.utils;

import lombok.extern.slf4j.Slf4j;
import me.aias.common.constant.Constant;
import me.aias.common.enums.ResEnum;
import me.aias.common.exception.BusinessException;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;

/**
 * Zip文件工具
 *
 * @author Calvin
 * @date 2021-06-20
 **/
@Slf4j
public class ZipUtil {

    private static final int BUFFER = 2048;

    /**
     * 解压Zip文件
     *
     * @param receivedZipFile
     * @param osName
     * @param filePath
     * @return
     * @throws Exception
     */
    public static void unZipTrainingData(String receivedZipFile, String osName, String filePath) {
        int count = -1;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipFile zipFile = null;
        try {
            if (osName.toUpperCase().contains(Constant.PC_WINDOW_TYPE)) {
                zipFile = new ZipFile(receivedZipFile, "gbk");
            } else {
                zipFile = new ZipFile(receivedZipFile, "UTF8");
            }
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                byte[] buf = new byte[BUFFER];
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String filename = entry.getName();
                boolean ismkdir = false;
                if (!osName.toUpperCase().contains(Constant.PC_WINDOW_TYPE)) {
                    if (filename.toUpperCase().contains(Constant.PC_APPLE_TYPE)) {
                        continue;
                    }
                }

                if (entry.isDirectory()) {
                    ismkdir = true;
                }

                filename = filePath + File.separator + filename;
                File file = new File(filename);
                if (!file.exists()) {
                    if (ismkdir) {
                        new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs();
                        continue;
                    }
                } else if (file.exists()) {
                    if (file.isDirectory()) {
                        continue;
                    }
                }

                // 创建文件
                file.createNewFile();
                is = zipFile.getInputStream(entry);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, BUFFER);
                while ((count = is.read(buf)) > -1) {
                    bos.write(buf, 0, count);
                }
                bos.flush();
            }
        } catch (IOException ioe) {
            log.error("File exception {}", ioe.getMessage());
            throw new BusinessException(
                    ResEnum.DECOMPRESSION_FAIL.KEY, ResEnum.DECOMPRESSION_FAIL.VALUE);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (Exception e) {
                log.error("File exception {}", e.getMessage());
                throw new BusinessException(
                        ResEnum.DECOMPRESSION_FAIL.KEY, ResEnum.DECOMPRESSION_FAIL.VALUE);
            }
        }
    }
}
