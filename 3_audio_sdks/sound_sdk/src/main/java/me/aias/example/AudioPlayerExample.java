package me.aias.example;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;

/**
 * JAVA 播放wav,mp3,flac,ape格式音频文件
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class AudioPlayerExample {


    public static void main(String[] args) {
        final AudioPlayerExample player = new AudioPlayerExample();
        player.play("src/test/resources/audio.mp3");
//        player.play("build/output/converted.wav");
    }

    public void play(String filePath) {
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            // 获取音频文件编码格式
            AudioFormat audioFormat = audioInputStream.getFormat();
            System.out.println("音频格式：" + audioFormat.getEncoding());
            System.out.println("每秒播放帧数：" + audioFormat.getSampleRate());
            System.out.println("总帧数：" + audioInputStream.getFrameLength());
            System.out.println("音频时长（秒）：" + audioInputStream.getFrameLength() / audioFormat.getSampleRate());

            // 转换文件编码
            if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
                // 将数据流转换成指定编码
                audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
            }

            // 打开输出设备
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
            // 得到一个播放设备
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            // 指定编码打开
            sourceDataLine.open(audioFormat);
            // 开始播放
            sourceDataLine.start();
            int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
            // 将流数据写入数据行,边写边播
            int numBytes = 1024 * bytesPerFrame;
            byte[] audioBytes = new byte[numBytes];
            while (audioInputStream.read(audioBytes) != -1) {
                sourceDataLine.write(audioBytes, 0, audioBytes.length);
            }
            sourceDataLine.drain();
            sourceDataLine.stop();
            sourceDataLine.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                audioInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}