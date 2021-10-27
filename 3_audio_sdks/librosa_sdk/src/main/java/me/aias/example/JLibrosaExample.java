package me.aias.example;

import java.io.IOException;
import java.util.ArrayList;

import com.jlibrosa.audio.JLibrosa;
import org.apache.commons.math3.complex.Complex;

import com.jlibrosa.audio.exception.FileFormatNotSupportedException;
import com.jlibrosa.audio.wavFile.WavFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Java Librosa
 * <p>
 * 加载音频文件，读取幅值(magnitude)
 * 梅尔频率倒谱系数
 * 从wav提取mel(MelSpectrogram)特征值
 * 短时傅里叶变换
 * 短时傅立叶逆变换（ISTFT）
 */
public class JLibrosaExample {
    private static final Logger logger = LoggerFactory.getLogger(JLibrosaExample.class);

    public static void main(String[] args) throws IOException, WavFileException, FileFormatNotSupportedException {
        String audioFilePath = "src/test/resources/test.wav";
        int defaultSampleRate = -1;        //-1 value implies the method to use default sample rate
        int defaultAudioDuration = -1;    //-1 value implies the method to process complete audio duration

        JLibrosa jLibrosa = new JLibrosa();

        // To read the magnitude values of audio files - equivalent to librosa.load('../audioFiles/audio.wav', sr=None) function
        float audioFeatureValues[] = jLibrosa.loadAndRead(audioFilePath, defaultSampleRate, defaultAudioDuration);

        ArrayList<Float> audioFeatureValuesList = jLibrosa.loadAndReadAsList(audioFilePath, defaultSampleRate, defaultAudioDuration);


        System.out.println("Audio Feature Values：");
        for (int i = 0; i < 10; i++) {
            System.out.printf("%.6f%n", audioFeatureValues[i]);
        }

        // To read the no of frames present in audio file
        int nNoOfFrames = jLibrosa.getNoOfFrames();


        // To read sample rate of audio file
        int sampleRate = jLibrosa.getSampleRate();
        System.out.println("Sample Rate: " + sampleRate);
        
        // To read number of channels in audio file
        int noOfChannels = jLibrosa.getNoOfChannels();

        Complex[][] stftComplexValues = jLibrosa.generateSTFTFeatures(audioFeatureValues, sampleRate, 40);

        float[] invSTFTValues = jLibrosa.generateInvSTFTFeatures(stftComplexValues, sampleRate, 40);
        System.out.println("/n/n");

        float[][] melSpectrogram = jLibrosa.generateMelSpectroGram(audioFeatureValues, sampleRate, 2048, 128, 256);

        /* To read the MFCC values of an audio file
         *equivalent to librosa.feature.mfcc(x, sr, n_mfcc=40) in python
         * */

        float[][] mfccValues = jLibrosa.generateMFCCFeatures(audioFeatureValues, sampleRate, 40);

        float[] meanMFCCValues = jLibrosa.generateMeanMFCCFeatures(mfccValues, mfccValues.length, mfccValues[0].length);

        System.out.println(".......");
        System.out.println("Size of MFCC Feature Values: (" + mfccValues.length + " , " + mfccValues[0].length + " )");

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.printf("%.6f%n", mfccValues[i][j]);
            }
        }

        /* To read the STFT values of an audio file
         *equivalent to librosa.core.stft(x, sr, n_mfcc=40) in python
         *Note STFT values return would be complex in nature with real and imaginary values.
         * */

        Complex[][] stftComplexValues1 = jLibrosa.generateSTFTFeatures(audioFeatureValues, sampleRate, 40);


        float[] invSTFTValues1 = jLibrosa.generateInvSTFTFeatures(stftComplexValues, sampleRate, 40);

        System.out.println(".......");
        System.out.println("Size of STFT Feature Values: (" + stftComplexValues.length + " , " + stftComplexValues[0].length + " )");


        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 10; j++) {
                double realValue = stftComplexValues[i][j].getReal();
                double imagValue = stftComplexValues[i][j].getImaginary();
                System.out.println("Real and Imag values of STFT are " + realValue + "," + imagValue);
            }

        }

    }


}