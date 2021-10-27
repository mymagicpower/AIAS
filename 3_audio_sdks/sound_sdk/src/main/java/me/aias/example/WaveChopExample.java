package me.aias.example;

import me.aias.example.utils.SoundUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * JAVA 截取部分wav文件
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class WaveChopExample {
    private static final Logger logger = LoggerFactory.getLogger(WaveChopExample.class);

    public static void main(String[] args) throws Exception {
        WaveChopExample waveChop = new WaveChopExample();
        waveChop.run();
    }

    public void run() throws Exception {
        File sourceFile = new File("build/output/wav_converted.wav");
        File chopResult = new File("build/output/wav_chop_result.wav");

        int length = SoundUtils.getWavLengthSeconds(sourceFile);
        logger.info("Source wave file: {}", sourceFile);
        logger.info("Wave Length: {} seconds",length);
        SoundUtils.createChop(sourceFile, chopResult, 0, 3);
        logger.info("Wave chopped: {}", chopResult);
    }
}