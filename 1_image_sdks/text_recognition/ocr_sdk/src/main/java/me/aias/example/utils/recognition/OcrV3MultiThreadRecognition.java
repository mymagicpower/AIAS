package me.aias.example.utils.recognition;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.paddlepaddle.zoo.cv.objectdetection.PpWordDetectionTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.common.ImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class OcrV3MultiThreadRecognition {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3MultiThreadRecognition.class);

    public OcrV3MultiThreadRecognition() {
    }

    public DetectedObjects predict(
            Image image, List<ZooModel> recModels, Predictor<Image, DetectedObjects> detector, int threadNum)
            throws TranslateException {
        DetectedObjects detections = detector.predict(image);

        List<DetectedObjects.DetectedObject> boxes = detections.items();

        ConcurrentLinkedQueue<ImageInfo> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < boxes.size(); i++) {
            BoundingBox box = boxes.get(i).getBoundingBox();
            Image subImg = getSubImage(image, box);
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(subImg);
            }
            ImageInfo imageInfo = new ImageInfo(subImg, box);
            queue.add(imageInfo);
        }

        List<InferCallable> callables = new ArrayList<>(threadNum);
        for (int i = 0; i < threadNum; i++) {
            callables.add(new InferCallable(recModels.get(i), queue));
        }

        ExecutorService es = Executors.newFixedThreadPool(threadNum);
        List<ImageInfo> resultList = new ArrayList<>();
        try {
            List<Future<List<ImageInfo>>> futures = new ArrayList<>();
            long timeInferStart = System.currentTimeMillis();
            for (InferCallable callable : callables) {
                futures.add(es.submit(callable));
            }

            for (Future<List<ImageInfo>> future : futures) {
                List<ImageInfo> subList = future.get();
                if (subList != null) {
                    resultList.addAll(subList);
                }
            }

            long timeInferEnd = System.currentTimeMillis();
            System.out.println("time: " + (timeInferEnd - timeInferStart));

            for (InferCallable callable : callables) {
                callable.close();
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("", e);
        } finally {
            es.shutdown();
        }

        List<String> names = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        List<BoundingBox> rect = new ArrayList<>();
        for (ImageInfo imageInfo : resultList) {
            names.add(imageInfo.getName());
            prob.add(imageInfo.getProb());
            rect.add(imageInfo.getBox());
        }
        DetectedObjects detectedObjects = new DetectedObjects(names, prob, rect);

        return detectedObjects;
    }

    public Criteria<Image, DetectedObjects> detectCriteria() {
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optEngine("PaddlePaddle")
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get("models/ch_PP-OCRv3_det_infer.zip"))
                        .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    public Criteria<Image, String> recognizeCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("PaddlePaddle")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/ch_PP-OCRv3_rec_infer.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();

        return criteria;
    }

    private static class InferCallable implements Callable<List<ImageInfo>> {
        private Predictor<Image, String> recognizer;
        private ConcurrentLinkedQueue<ImageInfo> queue;
        private List<ImageInfo> resultList = new ArrayList<>();

        public InferCallable(ZooModel recognitionModel, ConcurrentLinkedQueue<ImageInfo> queue){
            recognizer = recognitionModel.newPredictor();
            this.queue = queue;
        }

        public List<ImageInfo> call() {
            try {
                ImageInfo imageInfo = queue.poll();
                while (imageInfo != null) {
                    String name = recognizer.predict(imageInfo.getImage());
                    imageInfo.setName(name);
                    imageInfo.setProb(-1.0);
                    resultList.add(imageInfo);
                    imageInfo = queue.poll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultList;
        }

        public void close() {
            recognizer.close();
        }
    }

    private Image getSubImage(Image img, BoundingBox box) {
        Rectangle rect = box.getBounds();
        double[] extended = extendRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        int width = img.getWidth();
        int height = img.getHeight();
        int[] recovered = {
                (int) (extended[0] * width),
                (int) (extended[1] * height),
                (int) (extended[2] * width),
                (int) (extended[3] * height)
        };
        return img.getSubImage(recovered[0], recovered[1], recovered[2], recovered[3]);
    }

    private double[] extendRect(double xmin, double ymin, double width, double height) {
        double centerx = xmin + width / 2;
        double centery = ymin + height / 2;
        if (width > height) {
            width += height * 2.0;
            height *= 3.0;
        } else {
            height += width * 2.0;
            width *= 3.0;
        }
        double newX = centerx - width / 2 < 0 ? 0 : centerx - width / 2;
        double newY = centery - height / 2 < 0 ? 0 : centery - height / 2;
        double newWidth = newX + width > 1 ? 1 - newX : width;
        double newHeight = newY + height > 1 ? 1 - newY : height;
        return new double[]{newX, newY, newWidth, newHeight};
    }

    private Image rotateImg(Image image) {
        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
            return ImageFactory.getInstance().fromNDArray(rotated);
        }
    }
}
