package me.aias.example;

import me.aias.example.utils.SoundUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import javax.sound.sampled.*;

/**
 * JAVA mp3 转 wav
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class MP3ToWAVExample {
    private static final Logger logger = LoggerFactory.getLogger(MP3ToWAVExample.class);
    
    public static void main(String [] args){
        try{
            AudioFileFormat inputFileFormat = AudioSystem.getAudioFileFormat(new File("src/test/resources/audio.mp3"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("src/test/resources/audio.mp3"));
 
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
                int i = AudioSystem.write(encodedASI, AudioFileFormat.Type.WAVE, new File("build/output/converted.wav"));
                System.out.println("Bytes Written: "+i);
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}