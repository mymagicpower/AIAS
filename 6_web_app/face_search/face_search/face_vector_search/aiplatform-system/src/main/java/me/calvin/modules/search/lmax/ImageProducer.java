package me.calvin.modules.search.lmax;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.opencv.OpenCVImageFactory;
import com.lmax.disruptor.RingBuffer;
import me.calvin.modules.search.service.dto.ImageInfoDto;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * 图片信息生产者
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class ImageProducer implements Runnable {
    private int threadId;
    private ConcurrentLinkedQueue<ImageInfoDto> queue;
    private RingBuffer<ImageEvent> ringBuffer;
    private CountDownLatch latch;
    private int count;

    public ImageProducer(int threadId, ConcurrentLinkedQueue<ImageInfoDto> queue, RingBuffer<ImageEvent> ringBuffer, CountDownLatch latch) {
        this.threadId = threadId;
        this.queue = queue;
        this.ringBuffer = ringBuffer;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            ImageInfoDto imageInfo = queue.poll();
            ImageFactory defFactory = new OpenCVImageFactory();
            while (imageInfo != null) {
                count++;
                System.out.println("reader thread: " + threadId + " read image - " + count);
                Path path = Paths.get(imageInfo.getFullPath());
                Image gold = defFactory.fromFile(path);
                imageInfo.setImage(gold);
                ringBuffer.publishEvent(new ImageEventTranslator(), imageInfo);
                imageInfo = queue.poll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

}