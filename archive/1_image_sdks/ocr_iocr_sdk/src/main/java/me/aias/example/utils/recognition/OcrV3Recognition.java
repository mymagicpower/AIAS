package me.aias.example.utils.recognition;

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
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.common.RotatedBox;
import me.aias.example.utils.opencv.NDArrayUtils;
import me.aias.example.utils.opencv.OpenCVUtils;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * https://github.com/PaddlePaddle/PaddleOCR/blob/release/2.6/doc/doc_ch/models_list.md
 */
public final class OcrV3Recognition {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3Recognition.class);


    public OcrV3Recognition() {
    }

    /**
     * 中文简体
     * @return
     */
    public Criteria<Image, String> chRecCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/ch_PP-OCRv3_rec_infer_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator(new ConcurrentHashMap<String, String>()))
                        .build();
        return criteria;
    }

    /**
     * 中文繁体
     * @return
     */
    public Criteria<Image, String> chtRecCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/chinese_cht_PP-OCRv3_rec_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator(new ConcurrentHashMap<String, String>()))
                        .build();
        return criteria;
    }

    /**
     * 英文
     * @return
     */
    public Criteria<Image, String> enRecCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/en_PP-OCRv3_rec_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();
        return criteria;
    }

    /**
     * 韩语
     * @return
     */
    public Criteria<Image, String> koreanRecCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/korean_PP-OCRv3_rec_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();
        return criteria;
    }

    /**
     * 日语
     * @return
     */
    public Criteria<Image, String> japanRecCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/japan_PP-OCRv3_rec_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();
        return criteria;
    }


    /**
     * 泰米尔语
     * @return
     */
    public Criteria<Image, String> taRecCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/ta_PP-OCRv3_rec_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();
        return criteria;
    }

    /**
     * 泰卢固语
     * @return
     */
    public Criteria<Image, String> teRecCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/te_PP-OCRv3_rec_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();
        return criteria;
    }

    /**
     * 卡纳达文
     * @return
     */
    public Criteria<Image, String> kaRecCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/ka_PP-OCRv3_rec_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();
        return criteria;
    }

    /**
     * 阿拉伯
     *
     * arabic_lang = ['ar', 'fa', 'ug', 'ur']
     *
     * @return
     */
    public Criteria<Image, String> arabicRecCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/arabic_PP-OCRv3_rec_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();
        return criteria;
    }

    /**
     * 斯拉夫
     * 西里尔字母（英：Cyrillic，俄：Кириллица）源于希腊字母，普遍认为是由基督教传教士西里尔（827年–869年）
     * 在9世纪为了方便在斯拉夫民族传播东正教所创立的，被斯拉夫民族广泛采用
     *
     * cyrillic_lang = [
     *         'ru', 'rs_cyrillic', 'be', 'bg', 'uk', 'mn', 'abq', 'ady', 'kbd', 'ava',
     *         'dar', 'inh', 'che', 'lbe', 'lez', 'tab'
     *     ]
     *
     * @return
     */
    public Criteria<Image, String> cyrillicRecCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/cyrillic_PP-OCRv3_rec_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();
        return criteria;
    }

    /**
     * 梵文
     *
     * devanagari_lang = [
     *         'hi', 'mr', 'ne', 'bh', 'mai', 'ang', 'bho', 'mah', 'sck', 'new', 'gom',
     *         'sa', 'bgc'
     *     ]
     *
     * @return
     */
    public Criteria<Image, String> devanagariRecCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/devanagari_PP-OCRv3_rec_onnx.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();
        return criteria;
    }


    public List<RotatedBox> predict(NDManager manager,
                                    Image image, Predictor<Image, NDList> detector, Predictor<Image, String> recognizer)
            throws TranslateException {
        NDList boxes = detector.predict(image);
        // 交给 NDManager自动管理内存
        // attach to manager for automatic memory management
        boxes.attach(manager);

        List<RotatedBox> result = new ArrayList<>();
        long timeInferStart = System.currentTimeMillis();

        Mat mat = (Mat) image.getWrappedImage();

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

            Mat cvMat = OpenCVUtils.perspectiveTransform(mat, srcPoint2f, dstPoint2f);

            Image subImg = OpenCVImageFactory.getInstance().fromImage(cvMat);
//            ImageUtils.saveImage(subImg, i + ".png", "build/output");

            subImg = subImg.getSubImage(0, 0, img_crop_width, img_crop_height);
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(manager, subImg);
            }

            String name = recognizer.predict(subImg);
            RotatedBox rotatedBox = new RotatedBox(box, name);
            result.add(rotatedBox);

            cvMat.release();
            srcPoint2f.release();
            dstPoint2f.release();

        }

        long timeInferEnd = System.currentTimeMillis();
        System.out.println("time: " + (timeInferEnd - timeInferStart));

        return result;
    }

    private BufferedImage get_rotate_crop_image(Image image, NDArray box) {
        return null;
    }

    private float distance(float[] point1, float[] point2) {
        float disX = point1[0] - point2[0];
        float disY = point1[1] - point2[1];
        float dis = (float) Math.sqrt(disX * disX + disY * disY);
        return dis;
    }

    private Image rotateImg(NDManager manager, Image image) {
        NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
        return ImageFactory.getInstance().fromNDArray(rotated);
    }
}
