package me.aias.example;

import me.aias.example.utils.SoundUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JAVA wav文件合并
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class WavAppenderExample {
    private static final Logger logger = LoggerFactory.getLogger(WavAppenderExample.class);
    public static void main(String[] args) {
        String wavFile1 = "build/output/wav_converted.wav";
        String wavFile2 = "build/output/wav_converted.wav";
        String destinationFile = "build/output/wav_appended.wav";

        logger.info("wavFile1: {}", wavFile1);
        logger.info("wavFile2: {}", wavFile2);
        SoundUtils.appendStream(wavFile1,wavFile2,destinationFile);
        logger.info("wav File appended: {}", destinationFile);
    }
}