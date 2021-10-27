package me.aias.example;

import com.jlibrosa.audio.JLibrosa;
import com.jlibrosa.audio.exception.FileFormatNotSupportedException;
import com.jlibrosa.audio.wavFile.WavFileException;
import me.aias.example.utils.FFT;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

public class FFTExample {

    public static void main(String[] args) throws IOException, WavFileException, FileFormatNotSupportedException {

        String audioFilePath = "src/test/resources/audio.wav";
        //-1 value implies the method to use default sample rate
        int defaultSampleRate = -1;
        //-1 value implies the method to process complete audio duration
        int defaultAudioDuration = -1;

        JLibrosa jLibrosa = new JLibrosa();

        // To read the magnitude values of audio files - equivalent to librosa.load('../audio.wav', sr=None)
        float audioFeatureValues[] = jLibrosa.loadAndRead(audioFilePath, defaultSampleRate, defaultAudioDuration);

        double[] arr = IntStream.range(0, audioFeatureValues.length).mapToDouble(i -> audioFeatureValues[i]).toArray();
//        double[] arrTest = {0, 1, 0, 0};
        //Arrays.copy for testing purpose

        double[] fft = FFT.fft(arr);
        float[][] complex = FFT.rfft(fft);

//        int stride_size = 160;
//        int window_size = 320;
//        int truncate_size = (audioFeatureValues.length - window_size) % stride_size;
//        int length = audioFeatureValues.length - truncate_size;

        System.out.println("Real parts: " + Arrays.toString(complex[0]));
        System.out.println("Imaginary parts: " + Arrays.toString(complex[1]));

    }

}