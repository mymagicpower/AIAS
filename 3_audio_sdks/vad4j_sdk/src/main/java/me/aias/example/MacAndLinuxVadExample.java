package me.aias.example;

import com.orctom.vad4j.VAD;
import me.aias.example.utils.SoundUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public class MacAndLinuxVadExample {

    public static void main(String[] args) throws Exception {
        String os = System.getProperty("os.name");
        System.out.println(os);
        
        File audioFile = new File("src/test/resources/audio.mp3");
        AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
        byte[] bytes = SoundUtils.convertAsByteArray(ais, SoundUtils.WAV_PCM_SIGNED);
        try (VAD vad = new VAD()) {
            // Arrays.copyOfRange(bytes, window_start,window_end)
            boolean isSpeech = vad.isSpeech(bytes);
            System.out.println(isSpeech);
        }

        // or use threshold of your choise
        try (VAD vad = new VAD()) {
            float score = vad.speechProbability(bytes);
            boolean isSpeech = score >= VAD.THRESHOLD;
            System.out.println(isSpeech);
        }
    }

}
