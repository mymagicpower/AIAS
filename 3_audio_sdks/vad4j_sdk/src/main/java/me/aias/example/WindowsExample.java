package me.aias.example;

import me.aias.example.utils.IntegerConversion;
import me.aias.example.utils.SoundUtils;
import org.jitsi.webrtcvadwrapper.WebRTCVad;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public class WindowsExample {

    public static void main(String[] args) throws Exception {
        String os = System.getProperty("os.name");
        System.out.println(os);

        File audioFile = new File("src/test/resources/audio.mp3");
        AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
        byte[] bytes = SoundUtils.convertAsByteArray(ais, SoundUtils.WAV_PCM_SIGNED);

        int size = bytes.length / 2;
        int[] pcm_wave = new int[size];
        for (int i = 0; i < size; i++) {
            pcm_wave[i] = IntegerConversion.convertTwoBytesToInt1(bytes[2 * i], bytes[2 * i + 1]);
        }

        WebRTCVad vad = new WebRTCVad(16000, 3);

        boolean isSpeech = vad.isSpeech(pcm_wave);
        System.out.println(isSpeech);
    }

}
