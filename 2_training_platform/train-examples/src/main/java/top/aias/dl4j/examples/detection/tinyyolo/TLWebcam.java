package top.aias.dl4j.examples.detection.tinyyolo;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.layers.objdetect.DetectedObject;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
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
import java.util.concurrent.atomic.AtomicReference;

import static org.bytedeco.opencv.global.opencv_videoio.CAP_PROP_FRAME_HEIGHT;
import static org.bytedeco.opencv.global.opencv_videoio.CAP_PROP_FRAME_WIDTH;

/**
 * 摄像头实时检测
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class TLWebcam {
    private static final Logger log = LoggerFactory.getLogger(TLWebcam.class);
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

        // 调用摄像头
        final AtomicReference<VideoCapture> capture = new AtomicReference<>(new VideoCapture());

        capture.get().set(CAP_PROP_FRAME_WIDTH, model.getWidth());
        capture.get().set(CAP_PROP_FRAME_HEIGHT, model.getHeight());

        if (!capture.get().open(0)) {
            log.error("不能打开摄像头!");
        }

        Mat colorimg = new Mat();
        CanvasFrame mainframe = new CanvasFrame("实时检测", CanvasFrame.getDefaultGamma() / 2.2);
        mainframe.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        mainframe.setCanvasSize(model.getWidth(), model.getHeight());
        mainframe.setLocationRelativeTo(null);
        mainframe.setVisible(true);

        while (true) {
            int i = 0;
            while (capture.get().read(colorimg) && mainframe.isVisible()) {
                if(i++ < 3 ){
                    continue;
                }
                i = 0;

                long st = System.currentTimeMillis();
                Imgproc.resize(colorimg, colorimg, new Size(model.getWidth(), model.getHeight()));
                detect(model, colorimg, detectionThreshold);
                double per = (System.currentTimeMillis() - st) / 1000.0;
                Scalar green = new Scalar(0, 255, 0); // 绿色文本
                Imgproc.putText(colorimg, "Time : " + per + " ms", new Point(10, 25), 2,.9, green);

                mainframe.showImage(converter.convert(colorimg));
//                try {
//                    Thread.sleep(20);
//                } catch (InterruptedException ex) {
//                    log.error(ex.getMessage());
//                }
            }
        }
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
