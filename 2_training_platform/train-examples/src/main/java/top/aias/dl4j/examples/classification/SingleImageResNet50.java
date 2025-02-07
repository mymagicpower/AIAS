package top.aias.dl4j.examples.classification;

import org.bytedeco.javacv.OpenCVFrameConverter;
import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.dl4j.examples.classification.models.ClassPrediction;
import top.aias.dl4j.examples.classification.models.ResNet50Model;
import top.aias.dl4j.examples.utils.OpenCVUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 单张图片分类
 * 模型：ResNet50
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class SingleImageResNet50 {
    private static final Logger log = LoggerFactory.getLogger(SingleImageResNet50.class);
    private static final OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

    // 模型路径
    private static String modelPath = new File(".").getAbsolutePath() + "/generated-models/ResNet50_Garbage_3.zip";

    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) throws Exception {

        ResNet50Model model = new ResNet50Model();
        // 标签：模型输出分类
        List<String> labels = Arrays.asList(new String[]{"Cardboard", "Glass", "Metal", "Paper", "Plastic", "Trash"});
        model.setLabels(labels);

        if (new File(modelPath).exists()) {
            log.info("加载模型...");
            model.loadModel(new File(modelPath));
        } else {
            log.info("模型没有找到.");
        }

        // 加载图片
        String imageFile = "src/main/resources/paper.jpg";
        Mat image = Imgcodecs.imread(imageFile);

        long st = System.currentTimeMillis();
        ClassPrediction classPrediction = classify(model, image, labels);
        double per = (System.currentTimeMillis() - st) / 1000.0;

        // 在图像上添加文本
        String time = "Time : " + per + " ms";
        Point timeLoc = new Point(10, 25);  // 文本起始点
        OpenCVUtils.putText(image, time, timeLoc);

        String result = "Prediction : " + classPrediction;
        Point resultLoc = new Point(10, 65);  // 文本起始点
        OpenCVUtils.putText(image, result, resultLoc);

        HighGui.imshow("图像分类", image);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }

    public static ClassPrediction classify(ResNet50Model model, Mat image, List<String> labels) {
        NativeImageLoader loader = new NativeImageLoader(model.getWidth(), model.getHeight(), model.getNChannels());
        INDArray ds = null;
        try {
            ds = loader.asMatrix(image);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        // 获取模型预测结果
        INDArray predictions = model.getComputationGraph().outputSingle(ds);
        int predictedClassIndex = predictions.argMax(1).getInt(0);
        String modelPrediction = labels.get(predictedClassIndex);
        float topXProb = predictions.getFloat(0, predictedClassIndex);
        return new ClassPrediction(modelPrediction, topXProb);
    }

}
