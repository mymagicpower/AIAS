package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import com.jlibrosa.audio.JLibrosa;
import com.jlibrosa.audio.exception.FileFormatNotSupportedException;
import com.jlibrosa.audio.wavFile.WavFileException;
import org.apache.commons.math3.complex.Complex;

import java.io.IOException;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public class JLibrasaEx {
  public static float[][] magnitude(NDManager manager, String audioFilePath)
      throws FileFormatNotSupportedException, IOException, WavFileException {
    int defaultAudioDuration = -1; // -1 value implies the method to process complete audio duration

    JLibrosa jLibrosa = new JLibrosa();

    // 读取音频数据
    // Reading audio data
    float audioFeatureValues[] = jLibrosa.loadAndRead(audioFilePath, 16000, defaultAudioDuration);

    float[] reverseArray = new float[audioFeatureValues.length];
    for (int i = 0; i < audioFeatureValues.length; i++) {
      reverseArray[i] = audioFeatureValues[audioFeatureValues.length - i - 1];
    }
    NDArray reverse = manager.create(reverseArray);

    // 数据拼接
    // Data concatenation
    NDArray extended_wav = manager.create(audioFeatureValues).concat(reverse);

    Complex[][] stftComplexValues =
        jLibrosa.generateSTFTFeatures(extended_wav.toFloatArray(), -1, -1, 512, -1, 160);
    float[][] mag = JLibrasaEx.magnitude(stftComplexValues, 1);
    return mag;
  }

  public static float[][] magnitude(Complex[][] stftComplexValues, float power) {
    int rows = stftComplexValues.length;
    int cols = stftComplexValues[0].length;
    float[][] mag = new float[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        mag[i][j] = (float) stftComplexValues[i][j].abs();
        mag[i][j] = (float) Math.pow(mag[i][j], power);
      }
    }

    return mag;
  }

  // 计算全局标准差
  // Calculating global standard deviation
  public static float[] std(NDArray array, NDArray mean) {
    // 按列减去均值
    // Subtracting mean by column
    array = array.sub(mean);

    // 计算全局标准差
    // Calculating global standard deviation
    int cols = (int) array.getShape().get(1);
    float[] stds = new float[cols];
    for (int i = 0; i < cols; i++) {
      NDArray col = array.get(":," + i);
      stds[i] = std(col);
    }
    return stds;
  }

  // 计算全局标准差
  // Calculating global standard deviation
  public static float std(NDArray array) {
    array = array.square();
    float[] doubleResult = array.toFloatArray();
    float std = 0;
    for (int i = 0; i < doubleResult.length; i++) {
      std = std + doubleResult[i];
    }
    std = (float) Math.sqrt(std / doubleResult.length);
    return std;
  }
}
