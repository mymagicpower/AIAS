package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;

/** Sound utility class */
public class SoundUtils {
  public static int samplerate = 0;
  public static int channels = 0;
  // float转为16int需要的 基数
  public static int _int16_max = (int) (Math.pow(2, 15) - 1);

  /**
   * 数据保存成wav文件
   *
   * @param wav
   * @param volume
   * @param outs
   * @throws Exception
   */
  public static void saveWavFile(NDArray wav, float volume, File outs) throws Exception {
    NDArray out = wav.mul(_int16_max * volume).div(NDArrays.maximum(0.01, wav.abs().max()));
    save(out.toFloatArray(), samplerate, channels, outs);
  }

  public static void save(float[] buffer, double sampleRate, int channels, File outs)
      throws Exception {

    // if(sampleRate ==0.0){
    sampleRate = 22050.0;
    // }

    final byte[] byteBuffer = new byte[buffer.length * 2];

    int bufferIndex = 0;
    for (int i = 0; i < byteBuffer.length; i++) {
      final int x = (int) (buffer[bufferIndex++]); // * 32767.0

      byteBuffer[i++] = (byte) x;
      byteBuffer[i] = (byte) (x >>> 8);
    }

    final boolean bigEndian = false;
    final boolean signed = true;

    final int bits = 16;
    if (channels == 0) {
      channels = 1;
    }

    AudioFormat format = new AudioFormat((float) sampleRate, bits, channels, signed, bigEndian);
    ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer);
    AudioInputStream audioInputStream = new AudioInputStream(bais, format, buffer.length);
    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outs);
    bais.close();
    audioInputStream.close();
    audioInputStream.close();
  }
}
