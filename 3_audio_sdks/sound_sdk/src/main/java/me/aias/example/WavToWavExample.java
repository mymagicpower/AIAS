package me.aias.example;

import me.aias.example.utils.SoundUtils;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

/**
 * JAVA wav文件格式转换
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class WavToWavExample {
    public static void main(String [] args){
        try{
            AudioFileFormat inputFileFormat = AudioSystem.getAudioFileFormat(new File("src/test/resources/audio.wav"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("src/test/resources/audio.wav"));
 
            AudioFormat audioFormat = ais.getFormat();
 
            System.out.println("File Format Type: "+inputFileFormat.getType());
            System.out.println("File Format String: "+inputFileFormat.toString());
            System.out.println("File lenght: "+inputFileFormat.getByteLength());
            System.out.println("Frame length: "+inputFileFormat.getFrameLength());
            System.out.println("Channels: "+audioFormat.getChannels());
            System.out.println("Encoding: "+audioFormat.getEncoding());
            System.out.println("Frame Rate: "+audioFormat.getFrameRate());
            System.out.println("Frame Size: "+audioFormat.getFrameSize());
            System.out.println("Sample Rate: "+audioFormat.getSampleRate());
            System.out.println("Sample size (bits): "+audioFormat.getSampleSizeInBits());
            System.out.println("Big endian: "+audioFormat.isBigEndian());
            System.out.println("Audio Format String: "+audioFormat.toString());
 
            AudioInputStream encodedASI = SoundUtils.convertAsStream(ais, SoundUtils.WAV);
            try{
                int i = AudioSystem.write(encodedASI, AudioFileFormat.Type.WAVE, new File("build/output/wav_converted.wav"));
                System.out.println("Bytes Written: "+i);
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}