package me.aias.example.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.*;
import org.apache.commons.compress.utils.IOUtils;

public class MD5Utils {

  public static String getMd5ByFile(Path audioFile) throws FileNotFoundException {
    File file = new File(audioFile.toString());
    String value = null;
    FileInputStream in = new FileInputStream(file);
    try {
      MappedByteBuffer byteBuffer =
          in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(byteBuffer);
      BigInteger bi = new BigInteger(1, md5.digest());
      value = bi.toString(16);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (null != in) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return value;
  }

  static String getMd5ByFile(File file) throws FileNotFoundException {
    String value = null;
    FileInputStream in = new FileInputStream(file);
    try {
      MappedByteBuffer byteBuffer =
          in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(byteBuffer);
      BigInteger bi = new BigInteger(1, md5.digest());
      value = bi.toString(16);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (null != in) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return value;
  }

  public static void main(String[] args) throws IOException {
    // ad6db392b94b4ce50a6e6a3e6aff13dd
    String path = "src/test/resources/biaobei-009502.mp3";
    String v = getMd5ByFile(new File(path));
    System.out.println("MD5:" + v);

    FileInputStream fis = new FileInputStream(path);
    String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
    IOUtils.closeQuietly(fis);
    System.out.println("MD5:" + md5);
    // System.out.println("MD5:"+DigestUtils.md5Hex("WANGQIUYUN"));
  }
}
