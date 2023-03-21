package me.aias.example;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;

/**
 * JAVA 播放wav,mp3,flac,ape格式音频文件
 * JAVA code for playing wav, mp3, flac, and ape audio files.
 *
 * @author calvin
 * @mail 179209347@qq.com
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
            // Get the encoding format of the audio file
            System.out.println("Audio format: " + audioFormat.getEncoding());
            System.out.println("Frames played per second: " + audioFormat.getSampleRate());
            System.out.println("Total frames: " + audioInputStream.getFrameLength());
            System.out.println("Audio length in seconds: " + audioInputStream.getFrameLength() / audioFormat.getSampleRate());

            // 转换文件编码
            // Convert file encoding
            if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
                // 将数据流转换成指定编码
                // Convert the data stream to the specified encoding
                audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
            }

            // 打开输出设备
            // Open the output device
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
            // 得到一个播放设备
            // Get a playback device
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            // 指定编码打开
            // Open with specified encoding
            sourceDataLine.open(audioFormat);
            // 开始播放
            // Start playing
            sourceDataLine.start();
            int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
            // 将流数据写入数据行,边写边播
            // Write stream data to data line, write and play
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