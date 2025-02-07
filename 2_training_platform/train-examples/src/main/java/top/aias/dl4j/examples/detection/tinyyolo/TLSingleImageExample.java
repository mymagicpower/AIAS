package top.aias.dl4j.examples.detection.tinyyolo;

import org.bytedeco.javacv.OpenCVFrameConverter;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.layers.objdetect.DetectedObject;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.dl4j.examples.detection.dataHelpers.NonMaxSuppression;
import top.aias.dl4j.examples.detection.models.BaseModel;
import top.aias.dl4j.examples.detection.models.TinyYoloModel;
import top.aias.dl4j.examples.utils.OpenCVUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 单张图片检测
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class TLSingleImageExample {
    private static final Logger log = LoggerFactory.getLogger(TLSingleImageExample.class);
    private static final OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

    // 模型路径
    private static String modelPath = new File(".").getAbsolutePath() + "/generated-models/tinyyolo_model.zip";

    // 检测阈值
    private static double detectionThreshold = 0.2;

    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) throws Exception {

        TinyYoloModel model = new TinyYoloModel();
        // 标签：模型输出分类
        List<String> labels = Arrays.asList(new String[]{"mask", "no-mask"});
        model.setLabels(labels);

        if (new File(modelPath).exists()) {
            log.info("加载模型...");
            model.loadModel(new File(modelPath));
        } else {
            log.info("模型没有找到.");
        }

        // 加载图片
        String imageFile = "src/main/resources/mask.jpg";
        Mat image = Imgcodecs.imread(imageFile);

        long st = System.currentTimeMillis();
        detect(model, image, detectionThreshold);
        double per = (System.currentTimeMillis() - st) / 1000.0;

        // 在图像上添加文本
        String text = "Time : " + per + " ms";
        Point org = new Point(10, 25);  // 文本起始点
        OpenCVUtils.putText(image, text, org);

        HighGui.imshow("检测", image);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }

    public static void detect(BaseModel model, Mat image, double threshold) {
        ComputationGraph computationGraph = model.getComputationGraph();
        // 获取输出层
        org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer yout =
            (org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer) computationGraph.getOutputLayer(0);
        NativeImageLoader loader = new NativeImageLoader(model.getWidth(), model.getHeight(), model.getNChannels());
        INDArray ds = null;
        try {
            ds = loader.asMatrix(image);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(ds);
        INDArray results = computationGraph.outputSingle(ds);

        // 获取检测结果，并根据阈值过滤
        List<DetectedObject> objs = yout.getPredictedObjects(results, threshold);

        // 非极大值抑制
        List<DetectedObject> objects = NonMaxSuppression.getObjects(objs);

        // 画检测框
        OpenCVUtils.drawBoxes(model, objects, image);
    }

}
