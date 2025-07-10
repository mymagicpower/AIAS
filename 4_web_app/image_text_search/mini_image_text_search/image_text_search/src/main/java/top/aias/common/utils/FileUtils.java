/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package top.aias.common.utils;

import cn.hutool.core.util.IdUtil;
import top.aias.common.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * File工具类，扩展 hutool 工具包
 * File tool class, extending hutool tool package
 *
 * @author Zheng Jie
 * @date 2018-12-27
 */
public class FileUtils extends cn.hutool.core.io.FileUtil {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 系统临时目录
     * System temporary directory
     * <br>
     * windows 包含路径分割符，但Linux 不包含,
     * 在windows \\==\ 前提下，
     * 为安全起见 同意拼装 路径分割符，
     * - Windows contains path separators, but Linux does not,
     * - under the premise of windows \\==\,
     * - for safety, agree to assemble path separators,
     * <pre>
     *       java.io.tmpdir
     *       windows : C:\Users/xxx\AppData\Local\Temp\
     *       linux: /temp
     * </pre>
     */
    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;
    /**
     * 定义GB的计算常量
     * Define GB calculation constants
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * 定义MB的计算常量
     * Define MB calculation constants
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     * Define KB calculation constants
     */
    private static final int KB = 1024;

    /**
     * 格式化小数
     * Format decimal
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public static final String IMAGE = "IMAGE";
    public static final String TXT = "TXT";
    public static final String MUSIC = "MUSIC";
    public static final String VIDEO = "VIDEO";
    public static final String OTHER = "OTHER";

    /**
     * 根据日期生成本地图片相对保存路径
     * Generate local image relative storage path based on date
     */
    public static String generatePath(String fileRoot) {
        Date date = new Date();
        // yyyy/MM/dd/"
        String relativePath = DateUtils.YYYY_MM_dd.get().format(date);
        String filePath = fileRoot + relativePath;
        // 如果不存在,创建文件夹
        // If it does not exist, create a folder
        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        return relativePath;
    }

    /**
     * MultipartFile转File
     */
    public static File toFile(MultipartFile multipartFile) {
        // 获取文件名
        // Get file name
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        // Get file extension
        String prefix = "." + getExtensionName(fileName);
        File file = null;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            // Use uuid as file name to prevent duplicate temporary files from being generated
            file = new File(SYS_TEM_DIR + IdUtil.simpleUUID() + prefix);
            // MultipartFile to File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return file;
    }

    /**
     * 获取文件扩展名，不带 .
     * Get file extension name without .
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     * Java file operation, get the file name without extension
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 文件大小转换
     * File size conversion
     */
    public static String getSize(long size) {
        String resultSize;
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            // If the current Byte value is greater than or equal to 1GB
            resultSize = DF.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            // If the current Byte value is greater than or equal to 1MB
            resultSize = DF.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            // If the current Byte value is greater than or equal to 1KB
            resultSize = DF.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }

    /**
     * inputStream 转 File
     * InputStream to File
     */
    static File inputStreamToFile(InputStream ins, String name) throws Exception {
        File file = new File(SYS_TEM_DIR + name);
        if (file.exists()) {
            return file;
        }
        OutputStream os = new FileOutputStream(file);
        int bytesRead;
        int len = 8192;
        byte[] buffer = new byte[len];
        while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        return file;
    }

    /**
     * 将文件名解析成文件的上传路径
     * Parse the file name into the upload path of the file
     */
    public static File upload(MultipartFile file, String filePath) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssS");
        String name = getFileNameNoEx(file.getOriginalFilename());
        String suffix = getExtensionName(file.getOriginalFilename());
        String nowStr = "-" + format.format(date);
        try {
            String fileName = name + nowStr + "." + suffix;
            String path = filePath + fileName;
            // getCanonicalFile 可解析正确各种路径
            // getCanonicalFile can correctly parse various paths
            File dest = new File(path).getCanonicalFile();
            // 检测是否存在目录
            // Check if the directory exists
            if (!dest.getParentFile().exists()) {
                if (!dest.getParentFile().mkdirs()) {
                    System.out.println("was not successful.");
                }
            }
            // 文件写入
            // File write
            file.transferTo(dest);
            return dest;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String getFileType(String type) {
        String documents = "txt doc pdf ppt pps xlsx xls docx";
        String music = "mp3 wav wma mpa ram ra aac aif m4a";
        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        if (image.contains(type)) {
            return IMAGE;
        } else if (documents.contains(type)) {
            return TXT;
        } else if (music.contains(type)) {
            return MUSIC;
        } else if (video.contains(type)) {
            return VIDEO;
        } else {
            return OTHER;
        }
    }

    public static void checkSize(long maxSize, long size) {
        // 1M
        int len = 1024 * 1024;
        if (size > (maxSize * len)) {
            throw new BadRequestException("max size limitted");
        }
    }

    /**
     * 判断两个文件是否相同
     * Determine whether two files are the same
     */
    public static boolean check(File file1, File file2) {
        String img1Md5 = getMd5(file1);
        String img2Md5 = getMd5(file2);
        return img1Md5.equals(img2Md5);
    }

    /**
     * 判断两个文件是否相同
     * Determine whether two files are the same
     */
    public static boolean check(String file1Md5, String file2Md5) {
        return file1Md5.equals(file2Md5);
    }

    public static byte[] getByte(File file) {
        // 得到文件长度
        // Get file length
        byte[] b = new byte[(int) file.length()];
        try {
            InputStream in = new FileInputStream(file);
            try {
                System.out.println(in.read(b));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return b;
    }

    /**
     * 保存字节数组图片到指定path
     * Save byte array picture to specified path
     */
    public static void bytesToFile(byte[] bs, String filePath) throws IOException {
        FileOutputStream os = new FileOutputStream(filePath);
        os.write(bs);
        os.close();
    }
    
    private static String getMd5(byte[] bytes) {
        // 16进制字符
        // 16 hexadecimal characters
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(bytes);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            // 移位 输出字符串
            // Move output string
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String getMd5(File file) {
        return getMd5(getByte(file));
    }


    public static boolean delete(String path) {
        boolean flag = false;
        File file = new File(path);
        // 判断目录或文件是否存在
        // Check if the directory or file exists
        if (!file.exists()) { // 不存在返回 false
            //Does not exist returns false
            return flag;
        } else {
            // 判断是否为文件
            // If it is a file, call the delete file method
            if (file.isFile()) { // 为文件时调用删除文件方法
                // If it is a file, call the delete file method
                return deleteFile(path);
            } else { // 为目录时调用删除目录方法
                // If it is a directory, call the delete directory method
                return deleteDirectory(path);
            }
        }
    }

    /**
     * 删除单个文件
     *Delete single file
     *
     * @param path 被删除文件的文件名 - path file name to be deleted
     * @return 单个文件删除成功返回true，否则返回false - Return true if the single file is deleted successfully, otherwise return false
     */
    public static boolean deleteFile(String path) {
        boolean flag = false;
        File file = new File(path);
        // 路径为文件且不为空则进行删除
        // If the path is a file and is not empty, delete it
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * Delete directory (file) and files under the directory
     *
     * @param path 被删除目录的文件路径 - path file path of the directory to be deleted
     * @return 目录删除成功返回true，否则返回false
     * Return true if the directory is deleted successfully, otherwise return false
     */
    public static boolean deleteDirectory(String path) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        // If sPath does not end with a file separator, automatically add a file separator
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        File dirFile = new File(path);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        // If dir does not exist or is not a directory, exit
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        // Delete all files in the folder (including subdirectories)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            // Delete sub files
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } // 删除子目录
            // Delete subdirectories
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        // 删除当前目录
        // Delete the current directory
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

}
