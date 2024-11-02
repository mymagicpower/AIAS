package me.calvin.modules.search.common.utils;

import lombok.extern.slf4j.Slf4j;
import me.calvin.modules.search.common.constant.Constant;
import me.calvin.modules.search.common.exception.BusinessException;
import me.calvin.modules.search.domain.enums.ResEnum;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
/**
 * Zip 工具类
 * Common Constants
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
@Slf4j
public class ZipUtil {

    private static final int BUFFER = 2048;

    /**
     * 解压Zip文件
     */
    public static void unZip(String receivedZipFile, String osName, String filePath) {
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
                if (filename.lastIndexOf("/") != -1) {
                    filename = filename.substring(filename.lastIndexOf("/"), filename.length());
                    ismkdir = false;
                }
                filename = filePath + File.separator + filename;
                if (entry.isDirectory()) {
                    continue;
                }
                File file = new File(filename);
                if (!file.exists()) {
                    if (ismkdir) {
                        new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs();
                    }
                }
                file.createNewFile();
                is = zipFile.getInputStream(entry);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, BUFFER);
                while ((count = is.read(buf)) > -1) {
                    bos.write(buf, 0, count);
                }
                bos.flush();
            }
        } catch (IOException e) {
            log.error("zip file exception {}", e.getMessage());
            throw new BusinessException(
                    ResEnum.PACKAGE_DECOMPRESSION_FAIL.KEY, ResEnum.PACKAGE_DECOMPRESSION_FAIL.VALUE);
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
                log.error("zip file exception {}", e.getMessage());
                throw new BusinessException(
                        ResEnum.PACKAGE_DECOMPRESSION_FAIL.KEY, ResEnum.PACKAGE_DECOMPRESSION_FAIL.VALUE);
            }
        }
    }
}
