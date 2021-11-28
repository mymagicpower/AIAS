package me.aias.example.utils;

import org.bytedeco.javacv.*;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 获取音频数组
 *
 * @author Calvin
 */
public class AudioArrayUtils {
  public static void main(String[] args) throws FrameGrabber.Exception {
    System.out.println(
        Arrays.toString(AudioArrayUtils.audioSegment("src/test/resources/test.wav").samples));
  }

  public static final class AudioSegment {
    public final float[] samples;
    public final Integer sampleRate;
    public final Integer audioChannels;

    public AudioSegment(float[] samples, Integer sampleRate, Integer audioChannels) {
      this.samples = samples;
      this.sampleRate = sampleRate;
      this.audioChannels = audioChannels;
    }
  }

  public static final class FrameData {
    public final Buffer[] samples;
    public final Integer sampleRate;
    public final Integer audioChannels;

    public FrameData(Buffer[] samples, Integer sampleRate, Integer audioChannels) {
      this.samples = samples;
      this.sampleRate = sampleRate;
      this.audioChannels = audioChannels;
    }
  }

  /**
   * 获取音频文件的float数组,sampleRate,audioChannels
   *
   * @param path
   * @return
   * @throws FrameGrabber.Exception
   */
  public static AudioSegment audioSegment(String path) throws FrameGrabber.Exception {
    AudioSegment audioSegment = null;
    int sampleRate = -1;
    int audioChannels = -1;
    //  Audio sample type is usually integer or float-point.
    //  Integers will be scaled to [-1, 1] in float32.
    float scale = (float) 1.0 / Float.valueOf(1 << ((8 * 2) - 1));
    List<Float> floatList = new ArrayList<>();

    try (FFmpegFrameGrabber audioGrabber = new FFmpegFrameGrabber(path)) {
      try {
        audioGrabber.start();
        sampleRate = audioGrabber.getSampleRate();
        audioChannels = audioGrabber.getAudioChannels();
        Frame frame;
        while ((frame = audioGrabber.grabFrame()) != null) {
          Buffer[] buffers = frame.samples;

          Buffer[] copiedBuffers = new Buffer[buffers.length];
          for (int i = 0; i < buffers.length; i++) {
            deepCopy((ShortBuffer) buffers[i], (ShortBuffer) copiedBuffers[i]);
          }

          ShortBuffer sb = (ShortBuffer) buffers[0];
          for (int i = 0; i < sb.limit(); i++) {
            floatList.add(new Float(sb.get() * scale));
          }
        }
      } catch (FrameGrabber.Exception e) {
        e.printStackTrace();
      }

      float[] floatArray = new float[floatList.size()];
      int i = 0;
      for (Float f : floatList) {
        floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
      }
      audioSegment = new AudioSegment(floatArray, sampleRate, audioChannels);
      return audioSegment;
    }
  }

  /**
   * Deep copy - shortBuffer
   *
   * @param source
   * @param target
   * @return
   */
  private static ShortBuffer deepCopy(ShortBuffer source, ShortBuffer target) {

    int sourceP = source.position();
    int sourceL = source.limit();

    if (null == target) {
      target = ShortBuffer.allocate(source.remaining());
    }
    target.put(source);
    target.flip();

    source.position(sourceP);
    source.limit(sourceL);
    return target;
  }

  /**
   * Deep copy - byteBuffer
   *
   * @param source
   * @param target
   * @return
   */
  private static ByteBuffer deepCopy(ByteBuffer source, ByteBuffer target) {

    int sourceP = source.position();
    int sourceL = source.limit();

    if (null == target) {
      target = ByteBuffer.allocate(source.remaining());
    }
    target.put(source);
    target.flip();

    source.position(sourceP);
    source.limit(sourceL);
    return target;
  }

  /**
   * 获取音频文件的FrameData列表
   *
   * @param path
   * @return
   * @throws FrameGrabber.Exception
   */
  public static List<FrameData> frameData(String path) throws FrameGrabber.Exception {
    // frameRecorder setup during initialization
    List<FrameData> audioData = new ArrayList<>();

    try (FFmpegFrameGrabber audioGrabber = new FFmpegFrameGrabber(path)) {
      try {
        audioGrabber.start();
        Frame frame;
        while ((frame = audioGrabber.grabFrame()) != null) {
          Buffer[] buffers = frame.samples;

          Buffer[] copiedBuffers = new Buffer[buffers.length];
          for (int i = 0; i < buffers.length; i++) {
            deepCopy((ShortBuffer) buffers[i], (ShortBuffer) copiedBuffers[i]);
          }

          FrameData frameData = new FrameData(copiedBuffers, frame.sampleRate, frame.audioChannels);
          audioData.add(frameData);
        }
      } catch (FrameGrabber.Exception e) {
        e.printStackTrace();
      }
      return audioData;
    }
  }

  /**
   * 保存音频文件
   *
   * @param buffer
   * @param sampleRate
   * @param audioChannels
   * @param outs
   * @throws Exception
   */
  public static void toWavFile(float[] buffer, float sampleRate, int audioChannels, File outs)
      throws Exception {
    if (sampleRate == 0.0) {
      sampleRate = 22050;
    }

    if (audioChannels == 0) {
      audioChannels = 1;
    }

    final byte[] byteBuffer = new byte[buffer.length * 2];

    int bufferIndex = 0;
    for (int i = 0; i < byteBuffer.length; i++) {
      final int x = (int) (buffer[bufferIndex++]); // * 32767.0

      byteBuffer[i++] = (byte) x;
      byteBuffer[i] = (byte) (x >>> 8);
    }

    AudioFormat format = new AudioFormat(sampleRate, 16, audioChannels, true, false);
    try (ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer);
        AudioInputStream audioInputStream = new AudioInputStream(bais, format, buffer.length)) {
      AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outs);
    }
  }

  /**
   * 保存音频文件
   *
   * @param audioData
   * @param path
   * @param audioChannels
   */
  public void toWavFile(List<FrameData> audioData, String path, int audioChannels) {
    try (FFmpegFrameRecorder audioGrabber = new FFmpegFrameRecorder(path, audioChannels)) {
      for (FrameData frameData : audioData) {
        Frame frame = new Frame();
        frame.sampleRate = frameData.sampleRate;
        frame.audioChannels = frameData.audioChannels;
        frame.samples = frameData.samples;
        audioGrabber.record(frame);
      }
    } catch (FrameRecorder.Exception e) {
    }
  }
}
