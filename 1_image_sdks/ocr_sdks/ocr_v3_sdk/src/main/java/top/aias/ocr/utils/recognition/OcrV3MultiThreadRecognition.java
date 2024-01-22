package top.aias.ocr.utils.recognition;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import top.aias.ocr.utils.common.ImageInfo;
import top.aias.ocr.utils.common.RotatedBox;
import top.aias.ocr.utils.detection.OCRDetectionTranslator;
import top.aias.ocr.utils.opencv.NDArrayUtils;
import top.aias.ocr.utils.opencv.OpenCVUtils;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
/**
 * 多线程文字识别
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class OcrV3MultiThreadRecognition {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3MultiThreadRecognition.class);

    public OcrV3MultiThreadRecognition() {
    }

    /**
     * 图片推理
     *
     * @param manager
     * @param image
     * @param recognitionModel
     * @param detector
     * @param threadNum
     * @return
     * @throws TranslateException
     */
    public List<RotatedBox> predict(
            NDManager manager, Image image, ZooModel recognitionModel, Predictor<Image, NDList> detector, int threadNum)
            throws TranslateException {
        NDList boxes = detector.predict(image);
        // 交给 NDManager自动管理内存
        // attach to manager for automatic memory management
        boxes.attach(manager);

        ConcurrentLinkedQueue<ImageInfo> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < boxes.size(); i++) {

            NDArray box = boxes.get(i);

            float[] pointsArr = box.toFloatArray();
            float[] lt = java.util.Arrays.copyOfRange(pointsArr, 0, 2);
            float[] rt = java.util.Arrays.copyOfRange(pointsArr, 2, 4);
            float[] rb = java.util.Arrays.copyOfRange(pointsArr, 4, 6);
            float[] lb = java.util.Arrays.copyOfRange(pointsArr, 6, 8);
            int img_crop_width = (int) Math.max(distance(lt, rt), distance(rb, lb));
            int img_crop_height = (int) Math.max(distance(lt, lb), distance(rt, rb));
            List<Point> srcPoints = new ArrayList<>();
            srcPoints.add(new Point(lt[0], lt[1]));
            srcPoints.add(new Point(rt[0], rt[1]));
            srcPoints.add(new Point(rb[0], rb[1]));
            srcPoints.add(new Point(lb[0], lb[1]));
            List<Point> dstPoints = new ArrayList<>();
            dstPoints.add(new Point(0, 0));
            dstPoints.add(new Point(img_crop_width, 0));
            dstPoints.add(new Point(img_crop_width, img_crop_height));
            dstPoints.add(new Point(0, img_crop_height));

            Mat srcPoint2f = NDArrayUtils.toMat(srcPoints);
            Mat dstPoint2f = NDArrayUtils.toMat(dstPoints);

            Mat cvMat = OpenCVUtils.perspectiveTransform((Mat) image.getWrappedImage(), srcPoint2f, dstPoint2f);

            Image subImg = OpenCVImageFactory.getInstance().fromImage(cvMat);
//            ImageUtils.saveImage(subImg, i + ".png", "build/output");

            subImg = subImg.getSubImage(0, 0, img_crop_width, img_crop_height);
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(manager, subImg);
            }


            ImageInfo imageInfo = new ImageInfo(subImg, box);
            queue.add(imageInfo);
        }

        List<InferCallable> callables = new ArrayList<>(threadNum);
        for (int i = 0; i < threadNum; i++) {
            callables.add(new InferCallable(recognitionModel, queue));
        }

        ExecutorService es = Executors.newFixedThreadPool(threadNum);
        List<ImageInfo> resultList = new ArrayList<>();
        try {
            List<Future<List<ImageInfo>>> futures = new ArrayList<>();
            for (InferCallable callable : callables) {
                futures.add(es.submit(callable));
            }

            for (Future<List<ImageInfo>> future : futures) {
                List<ImageInfo> subList = future.get();
                if (subList != null) {
                    resultList.addAll(subList);
                }
            }

            for (InferCallable callable : callables) {
                callable.close();
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("", e);
        } finally {
            es.shutdown();
        }

        List<RotatedBox> rotatedBoxes = new ArrayList<>();
        for (ImageInfo imageInfo : resultList) {
            RotatedBox rotatedBox = new RotatedBox(imageInfo.getBox(), imageInfo.getName());
            rotatedBoxes.add(rotatedBox);

            // 不确定是否需要主动释放，nice to have ...
            Mat wrappedImage = (Mat) imageInfo.getImage().getWrappedImage();
            wrappedImage.release();
        }

        return rotatedBoxes;
    }

    /**
     * 中文检测模型
     *
     * @return
     */
    public Criteria<Image, NDList> detectCriteria() {
        Criteria<Image, NDList> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, NDList.class)
                        .optModelPath(Paths.get("models/ch_PP-OCRv3_det_infer_onnx.zip"))
                        .optTranslator(new OCRDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    /**
     * 中文识别模型
     *
     * @return
     */
    public Criteria<Image, String> recognizeCriteria() {
        ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<>();

        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/ch_PP-OCRv3_rec_infer_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecTranslator(hashMap))
                        .build();

        return criteria;
    }

    private static class InferCallable implements Callable<List<ImageInfo>> {
        private Predictor<Image, String> recognizer;
        private ConcurrentLinkedQueue<ImageInfo> queue;
        private List<ImageInfo> resultList = new ArrayList<>();

        public InferCallable(ZooModel recognitionModel, ConcurrentLinkedQueue<ImageInfo> queue) {
            recognizer = recognitionModel.newPredictor();
            this.queue = queue;
        }

        public List<ImageInfo> call() {
            ImageInfo imageInfo = queue.poll();
            try {
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

    /**
     * 欧式距离计算
     *
     * @param point1
     * @param point2
     * @return
     */
    private float distance(float[] point1, float[] point2) {
        float disX = point1[0] - point2[0];
        float disY = point1[1] - point2[1];
        float dis = (float) Math.sqrt(disX * disX + disY * disY);
        return dis;
    }

    /**
     * 图片旋转
     *
     * @param manager
     * @param image
     * @return
     */
    private Image rotateImg(NDManager manager, Image image) {
        NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
        return ImageFactory.getInstance().fromNDArray(rotated);
    }
}
