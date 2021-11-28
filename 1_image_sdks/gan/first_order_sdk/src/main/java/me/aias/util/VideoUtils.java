package me.aias.util;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_AAC;
import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264;
import static org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_YUV420P;

public class VideoUtils {
  public static List<Image> getKeyFrame(String filePath) throws Exception {
    File vf = new File(filePath);
    FFmpegFrameGrabber grabberI = FFmpegFrameGrabber.createDefault(vf);
    grabberI.start();
    Java2DFrameConverter converter = new Java2DFrameConverter();

    // 帧总数
    BufferedImage bImg = null;
    System.out.println("总时长:" + grabberI.getLengthInTime() / 1000 / 60);
    System.out.println("音频帧数:" + grabberI.getLengthInAudioFrames());
    System.out.println("视频帧数:" + grabberI.getLengthInVideoFrames());
    System.out.println("总帧数:" + grabberI.getLengthInFrames());
    int vidoes =
        grabberI.getLengthInVideoFrames() >= Integer.MAX_VALUE
            ? 0
            : grabberI.getLengthInVideoFrames();
    // 获取图片
    int frame_number = vidoes;
    Frame img = null;
    grabberI.flush();
    List<Image> cvImgs = new ArrayList<>();
    for (int i = 0; i < frame_number; i++) {
      if ((img = grabberI.grab()) == null) {
        continue;
      }
      if ((bImg = converter.convert(img)) == null) {
        continue;
      }

      cvImgs.add(ImageFactory.getInstance().fromImage(copyImg(bImg)));
    }
    grabberI.release();
    return cvImgs;
  }

  public static BufferedImage copyImg(BufferedImage img) {
    BufferedImage checkImg =
        new BufferedImage(img.getWidth(), img.getHeight(), img.getType() == 0 ? 5 : img.getType());
    checkImg.setData(img.getData());
    return checkImg;
  }

  public static void save(String src, String des, List<BufferedImage> cimgs, String fileType)
      throws Exception {
    File vf = new File(src);
    FFmpegFrameGrabber grabberI = FFmpegFrameGrabber.createDefault(vf);
    grabberI.start();
    FFmpegFrameRecorder recorder =
        new FFmpegFrameRecorder(des, grabberI.getImageWidth(), grabberI.getImageHeight(), 2);
    recorder.setVideoCodec(AV_CODEC_ID_H264);
    // 音频编/解码器
    recorder.setAudioCodec(AV_CODEC_ID_AAC);
    // rtmp的类型
    recorder.setFormat(fileType);
    recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
    recorder.start();
    //
    OpenCVFrameConverter.ToIplImage conveter = new OpenCVFrameConverter.ToIplImage();
    Java2DFrameConverter converter = new Java2DFrameConverter();
    // 帧总数
    BufferedImage bImg = null;
    System.out.println("总时长:" + grabberI.getLengthInTime() / 1000 / 60);
    System.out.println("音频帧数:" + grabberI.getLengthInAudioFrames());
    System.out.println("视频帧数:" + grabberI.getLengthInVideoFrames());
    System.out.println("总帧数:" + grabberI.getLengthInFrames());
    int audios =
        grabberI.getLengthInAudioFrames() >= Integer.MAX_VALUE
            ? 0
            : grabberI.getLengthInAudioFrames();
    int vidoes =
        grabberI.getLengthInVideoFrames() >= Integer.MAX_VALUE
            ? 0
            : grabberI.getLengthInVideoFrames();
    int frame_number = audios + vidoes;
    int width = grabberI.getImageWidth();
    int height = grabberI.getImageHeight();
    int depth = 0;
    int channels = 0;
    int stride = 0;
    int index = 0;

    grabberI.flush();
    for (int i = 0; i < frame_number; i++) {
      System.out.println("总共：" + frame_number + " 完成：" + i);
      Frame frame = grabberI.grab();

      if (frame == null) {
        continue;
      }
      Buffer[] smples = frame.samples;
      if (smples != null) {
        recorder.recordSamples(smples);
      }
      Buffer[] img = frame.image;

      if (img != null) {
        if ((bImg = converter.convert(frame)) != null) {
          System.out.println("放入图片");
          if (index >= cimgs.size()) break;
          Mat face = OpencvImageUtils.b2M(cimgs.get(index), opencv_core.CV_8UC3);
          opencv_imgproc.resize(face, face, new Size(width, height));
          Frame frame3 = conveter.convert(face);
          img = frame3.image;
          depth = frame3.imageDepth;
          channels = frame3.imageChannels;
          stride = frame3.imageStride;
          index++;
          recorder.recordImage(width, height, depth, channels, stride, -1, img);
          //  opencv_imgcodecs.imwrite("/Users/calvin/first-order-model-java/build" + File.separator
          // + "FACE" + index + ".jpg", face);
          // recorder.setTimestamp(frame.timestamp);
        }
      }
    }
    grabberI.close();
    recorder.close();
  }
}
