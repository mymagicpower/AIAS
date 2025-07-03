package me.calvin.modules.search.lmax;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import com.lmax.disruptor.WorkHandler;
import me.calvin.modules.search.model.ImageEncoderModel;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 该消费者执行特征提取操作
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public class ImageEventHandler implements com.lmax.disruptor.EventHandler<ImageEvent>, WorkHandler<ImageEvent> {
    private long threadId;
    private ConcurrentLinkedQueue<ImageInfoDto> queue;
    private ImageEncoderModel imageEncoderModel;
    private Predictor<Image, float[]> predictor;
    private int count;

    public ImageEventHandler(ImageEncoderModel imageEncoderModel, int threadId, ConcurrentLinkedQueue<ImageInfoDto> queue) {
        System.out.println(threadId + ": ImageEventHandler init");
        this.threadId = threadId;
        this.queue = queue;
        this.imageEncoderModel = imageEncoderModel;
//        predictor = imageEncoderModel.getModel().newPredictor();
    }

    @Override
    public void onEvent(ImageEvent event, long sequence, boolean endOfBatch) throws Exception {
        ImageInfoDto imageInfo = event.getImageInfo();
        count++;
        long timeInferStart = System.currentTimeMillis();
        float[] embeddings = imageEncoderModel.predict(imageInfo.getImage());
        long timeInferEnd = System.currentTimeMillis();
        long timeUsed = timeInferEnd - timeInferStart;
        System.out.println("model thread: " + threadId + " extract image - " + count + " - time used - " + timeUsed);
        List<Float> feature = new ArrayList<>();
        for (int j = 0; j < embeddings.length; j++) {
            feature.add(new Float(embeddings[j]));
        }
        imageInfo.setFeature(feature);
        // 收回内存
        ((Mat)imageInfo.getImage().getWrappedImage()).release();
        imageInfo.setImage(null);
        queue.offer(imageInfo);
    }

    @Override
    public void onEvent(ImageEvent event) throws Exception {
        ImageInfoDto imageInfo = event.getImageInfo();
        count++;
        long timeInferStart = System.currentTimeMillis();
        float[] embeddings = imageEncoderModel.predict(imageInfo.getImage());
        long timeInferEnd = System.currentTimeMillis();
        long timeUsed = timeInferEnd - timeInferStart;
        System.out.println("model thread: " + threadId + " extract image - " + count + " - time used - " + timeUsed);
        List<Float> feature = new ArrayList<>();
        for (int j = 0; j < embeddings.length; j++) {
            feature.add(new Float(embeddings[j]));
        }
        imageInfo.setFeature(feature);
        // 收回内存
        ((Mat)imageInfo.getImage().getWrappedImage()).release();
        imageInfo.setImage(null);
        queue.offer(imageInfo);
    }

    public void close() {
//        predictor.close();
    }
}