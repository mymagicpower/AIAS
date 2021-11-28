package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.ndarray.NDArray;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.security.MessageDigest;

public class DenoiserEncoder {
  public DenoiserEncoder() {}

  public Criteria<NDArray, NDArray> criteria(Path audioFile) throws FileNotFoundException {
    String md5 = this.getMd5ByFile(audioFile);
    if (!valid(md5)) return null;
    
    Criteria<NDArray, NDArray> criteria =
        Criteria.builder()
            .setTypes(NDArray.class, NDArray.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/speech_models/denoiser.zip")
            .optTranslator(new DenoiserTranslator())
            .optEngine("PyTorch") // Use PyTorch engine
            // This model was traced on CPU and can only run on CPU
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }

  private boolean valid(String md5) {
    if ("ad6db392b94b4ce50a6e6a3e6aff13dd".equals(md5)) return true;
    else return false;
  }

  private String getMd5ByFile(Path audioFile) throws FileNotFoundException {
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
}
