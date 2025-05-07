package top.aias.face.detection;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 人脸检测
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public final class FaceDetection implements AutoCloseable {
    // topk值 - topk value
    private int topK = 200;
    // 置信度阈值 - confidence threshold
    private double confThresh = 0.85;
    // 非极大值抑制阈值 - non-maximum suppression threshold
    private double nmsThresh = 0.45;
    private double[] variance = {0.1, 0.2};
    private int[][] scales = {{16, 32}, {64, 128}, {256, 512}};
    private int[] steps = {8, 16, 32};

    ZooModel model;
    Predictor<Image, DetectedObjects> predictor;
    private Device device;

    public FaceDetection(Device device) throws ModelException, IOException {
        this.device = device;
        this.model = ModelZoo.loadModel(onnxCriteria());
        this.predictor = model.newPredictor();
    }

    public DetectedObjects predict(Image img) throws TranslateException {
        return predictor.predict(img);
    }

    public void close(){
        this.model.close();
        this.predictor.close();
    }

    private Criteria<Image, DetectedObjects> onnxCriteria() {

        FaceDetTranslator translator =
                new FaceDetTranslator(confThresh, nmsThresh, variance, topK, scales, steps);

        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get("models/retinaface.onnx"))
                        .optTranslator(translator)
                        .optProgress(new ProgressBar())
                        .optEngine("OnnxRuntime") // Use ONNX engine
                        .build();

        return criteria;
    }

    public Criteria<Image, DetectedObjects> ptCriteria() {

        FaceDetTranslator translator =
                new FaceDetTranslator(confThresh, nmsThresh, variance, topK, scales, steps);

        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get("models/retinaface.pt"))
                        .optModelName("retinaface") // specify model file prefix
                        .optTranslator(translator)
                        .optProgress(new ProgressBar())
                        .optEngine("PyTorch") // Use PyTorch engine
                        .build();

        return criteria;
    }
}
