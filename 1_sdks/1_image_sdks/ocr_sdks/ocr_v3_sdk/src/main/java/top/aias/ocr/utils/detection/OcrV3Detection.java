package top.aias.ocr.utils.detection;

import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.Paths;
/**
 * 文字检测
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class OcrV3Detection {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3Detection.class);

    public OcrV3Detection() {
    }
    /**
     * 中文文本检测
     * @return
     */
    public Criteria<Image, NDList> chDetCriteria() {
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
     * 英文文本检测
     * @return
     */
    public Criteria<Image, NDList> enDetCriteria() {
        Criteria<Image, NDList> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, NDList.class)
                        .optModelPath(Paths.get("models/en_PP-OCRv3_det_infer_onnx.zip"))
                        .optTranslator(new OCRDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    /**
     * 多语言文本检测
     * @return
     */
    public Criteria<Image, NDList> mlDetCriteria() {
        Criteria<Image, NDList> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, NDList.class)
                        .optModelPath(Paths.get("models/Multilingual_PP-OCRv3_det_infer_onnx.zip"))
                        .optTranslator(new OCRDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }
}
