package top.aias.dl4j.exercises.inference;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.layers.objdetect.DetectedObject;
import org.deeplearning4j.parallelism.ParallelInference;
import org.deeplearning4j.parallelism.inference.InferenceMode;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.dl4j.examples.detection.dataHelpers.NonMaxSuppression;
import top.aias.dl4j.examples.detection.models.TinyYoloModel;
import top.aias.dl4j.examples.utils.OpenCVUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 本示例展示了 ParallelInference 机制的使用
 * Parallel Inference 从多个线程接收请求，
 * 将它们短时间内收集起来并行推理，资源被充分利用。
 *
 * 更多信息请参考：
 * https://www.dubs.tech/guides/quickstart-with-dl4j/#parallel-inference
 * https://github.com/kgoderis/deeplearning4j/blob/master/deeplearning4j/deeplearning4j-scaleout/deeplearning4j-scaleout-parallelwrapper/src/test/java/org/deeplearning4j/parallelism/ParallelInferenceTest.java
 *
 *  @author Calvin
 * Mail: 179209347@qq.com
 */

public class ParallelInferExample {
    private static final Logger log = LoggerFactory.getLogger(ParallelInferExample.class);
    // 检测阈值
    private static double detectionThreshold = 0.2;
    // 模型路径
    private static String modelPath = new File(".").getAbsolutePath() + "/generated-models/tinyyolo_model.zip";

    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) throws Exception {
        // 在此处使用您的模型路径，或者在其他地方实例化模型
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
        ComputationGraph computationGraph = model.getComputationGraph();

        String imageFile = "src/main/resources/mask.jpg";

        Mat image = Imgcodecs.imread(imageFile);

        // 获取输出层
        org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer yout =
                (org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer) computationGraph.getOutputLayer(0);

        NativeImageLoader loader = new NativeImageLoader(model.getWidth(), model.getHeight(), model.getNChannels());
        INDArray ds = null;
        try {
            ds = loader.asMatrix(image);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(ds);

        // new ParallelInference.Builder(model).inferenceMode(InferenceMode.SEQUENTIAL).workers(2).build();

        ParallelInference modelWrapper = new ParallelInference.Builder(computationGraph)
                // BATCHED 模式是一种优化方式：如果传入请求的数量过高，会将单个查询合并为一个批次处理。
                // 如果请求数量较低，则查询将单独处理，不进行批处理
                .inferenceMode(InferenceMode.BATCHED)

                // BATCHED 模式下批次的最大大小。应根据运行环境（例如 GPU 内存容量）设置此值
                .batchLimit(5)

                // 设置此值为可用计算设备的数量，CPU 或 GPU
                .workers(2)

                .build();

        // 请注意：这个 output() 调用只是一个占位符，您应传入与训练时相同维度的数据
        INDArray results = modelWrapper.output(ds);

        // 获取检测结果，并根据阈值过滤
        List<DetectedObject> objs = yout.getPredictedObjects(results, detectionThreshold);

        // 非极大值抑制
        List<DetectedObject> objects = NonMaxSuppression.getObjects(objs);

        // 画检测框
        OpenCVUtils.drawBoxes(model, objects, image);

        HighGui.imshow("检测", image);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}
