package me.aias.example.util;

import org.bytedeco.javacv.*;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 获取音频数组
 *
 * @author Calvin
 */
public class AudioArrayUtils {
    public static void main(String[] args) throws FrameGrabber.Exception {
        System.out.println(Arrays.toString(AudioArrayUtils.frameData("src/test/resources/test.wav")));
    }

    private static final class FrameData {
        public final Buffer[] samples;
        public final Integer sampleRate;
        public final Integer audioChannels;

        public FrameData(Buffer[] samples, Integer sampleRate, Integer audioChannels) {
            this.samples = samples;
            this.sampleRate = sampleRate;
            this.audioChannels = audioChannels;
        }
    }

    public static float[] frameData(String path) throws FrameGrabber.Exception {
        //frameRecorder setup during initialization
//        List<FrameData> audioData = new ArrayList<>();
        float scale = (float) 1.0 / Float.valueOf(1 << ((8 * 2) - 1));
        List<Float> floatList = new ArrayList<>();

        try (FFmpegFrameGrabber audioGrabber = new FFmpegFrameGrabber(path)) {
            try {
                audioGrabber.start();
                Frame frame;
                while ((frame = audioGrabber.grabFrame()) != null) {
                    Buffer[] buffers = frame.samples;

                    Buffer[] copiedBuffers = new Buffer[buffers.length];
                    for (int i = 0; i < buffers.length; i++) {
                        deepCopy((ShortBuffer) buffers[i], (ShortBuffer) copiedBuffers[i]);
                    }

                    ShortBuffer sb = (ShortBuffer) buffers[0];
                    for (int i = 0; i < sb.limit(); i++) {
                        floatList.add(new Float(sb.get() * scale));
                    }
//                    FrameData frameData = new FrameData(copiedBuffers, frame.sampleRate, frame.audioChannels);
//                    audioData.add(frameData);
                }
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }

            float[] floatArray = new float[floatList.size()];
            int i = 0;
            for (Float f : floatList) {
                floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
            }
            return floatArray;
        }
    }

//    public void record() {
//        frameCount++;
//        try {
//            FrameData frameData = audioData.get(frameCount % audioData.size());
//            frameRecorder.record(frameData.sampleRate, frameData.audioChannels, frameData.samples);
//        } catch (FrameRecorder.Exception e) {
//        }
//    }

    // 自动处理大小端序问题
    private static ShortBuffer deepCopy(ShortBuffer source, ShortBuffer target) {

        int sourceP = source.position();
        int sourceL = source.limit();

        if (null == target) {
            target = ShortBuffer.allocate(source.remaining());
        }
        target.put(source);
        target.flip();

        source.position(sourceP);
        source.limit(sourceL);
        return target;
    }

    private static ByteBuffer deepCopy(ByteBuffer source, ByteBuffer target) {

        int sourceP = source.position();
        int sourceL = source.limit();

        if (null == target) {
            target = ByteBuffer.allocate(source.remaining());
        }
        target.put(source);
        target.flip();

        source.position(sourceP);
        source.limit(sourceL);
        return target;
    }
}