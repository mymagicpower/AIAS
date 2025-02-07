package top.aias.dl4j.examples.detection.tinyyolo;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.datavec.api.records.metadata.RecordMetaDataImageURI;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.core.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.layers.objdetect.DetectedObject;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.model.stats.StatsListener;
import org.deeplearning4j.ui.model.storage.InMemoryStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.dl4j.examples.detection.dataHelpers.LabelImgVocProvider;
import top.aias.dl4j.examples.detection.dataHelpers.NonMaxSuppression;
import top.aias.dl4j.examples.detection.dataHelpers.YoloEvaluator;
import top.aias.dl4j.examples.detection.models.TinyYoloModel;
import top.aias.dl4j.examples.utils.OpenCVUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_core.CV_8U;

/**
 * 使用 Tiny YOLO 在ImageNet， Pascal VOC数据集上预训练的模型
 * 口罩检测训练数据使用 labelimg 标注
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class TLFaceMasks {
    private static final Logger log = LoggerFactory.getLogger(TLFaceMasks.class);

    private static double detectionThreshold = 0.4;
    // 训练参数
    private static int batchSize = 3;
    private static int nEpochs = 200;

    private static String modelFilePath = new File(".").getAbsolutePath() + "/generated-models/";
    private static String modelFileName = modelFilePath + "TinyYOLO_facemask_xxx.zip";

    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) throws Exception {

        TinyYoloModel tinyYoloModel = new TinyYoloModel();
        // 指定数据目录位置
        tinyYoloModel.setBatchSize(batchSize);
        double[][] priorBoxes = { { 2, 2 }, { 2, 2 }, { 2, 2 }, { 2, 2 }, { 2, 2 } };
        tinyYoloModel.setPriorBoxes(priorBoxes);
        File trainDir = new ClassPathResource("facemask_small/train").getFile();
        File testDir = new ClassPathResource("facemask_small/test").getFile();
        tinyYoloModel.setTrainDir(trainDir);
        tinyYoloModel.setTestDir(testDir);

        LabelImgVocProvider trainLabelProvider = new LabelImgVocProvider(trainDir);
        LabelImgVocProvider testLabelProvider = new LabelImgVocProvider(testDir);

        // 加载数据
        tinyYoloModel.loadData(trainLabelProvider, testLabelProvider);

        // 打印标签
        RecordReaderDataSetIterator train = tinyYoloModel.getTrain();
        List<String> labels = tinyYoloModel.getTrain().getLabels();
        tinyYoloModel.setLabels(labels);
        System.out.println(Arrays.toString(labels.toArray()));

        double bestScore = 0.0;
        if (new File(modelFileName).exists()) {
            // 加载模型
            log.info("加载模型...");
            tinyYoloModel.loadModel(new File(modelFileName));
        } else {
            // 1. 构建模型
            ComputationGraph computationGraph = tinyYoloModel.build();
            System.out.println(computationGraph.summary(InputType.convolutional(tinyYoloModel.getHeight(), tinyYoloModel.getWidth(), tinyYoloModel.getNChannels())));

            // 2. 训练模型
            log.info("训练模型...");
            UIServer server = UIServer.getInstance();
            StatsStorage storage = new InMemoryStatsStorage();
            server.attach(storage);
            computationGraph.setListeners(new ScoreIterationListener(1), new StatsListener(storage));
            for (int i = 0; i < nEpochs; i++) {
//                train.reset();
//                while (train.hasNext()) {
//                    computationGraph.fit(train.next());
//                }
                computationGraph.fit(train);
                System.out.println("完成 epoch: " + i);

                System.out.println("评估模型...");
                YoloEvaluator yoloEvaluator = new YoloEvaluator(tinyYoloModel.getComputationGraph(), tinyYoloModel.getTest(), testLabelProvider, tinyYoloModel.getWidth(), tinyYoloModel.getHeight());
                double tempScore = yoloEvaluator.getF1();
                System.out.println("当前 F1 score :" + bestScore);
                yoloEvaluator.print();

                // 3. 保存模型
                if (tempScore > bestScore) {
                    System.out.println("保存模型...");
                    bestScore = tempScore;
                    System.out.println("最新最好的 F1 score: " + bestScore);
                    ModelSerializer.writeModel(computationGraph, modelFilePath + "TinyYOLO_facemask_voc_" + i + ".zip", true);
                }
            }
        }

        // 4. 验证模型
        RecordReaderDataSetIterator test = tinyYoloModel.getTest();
        test.reset();
        validation(tinyYoloModel, test);
    }

    private static void validation(TinyYoloModel tinyYoloModel, RecordReaderDataSetIterator test) throws InterruptedException {

        // 可视化测试
        NativeImageLoader imageLoader = new NativeImageLoader();
        CanvasFrame frame = new CanvasFrame("验证");
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer yout = (org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer) tinyYoloModel.getComputationGraph().getOutputLayer(0);

        test.setCollectMetaData(true);
        while (test.hasNext() && frame.isVisible()) {
            org.nd4j.linalg.dataset.DataSet ds = test.next();
            RecordMetaDataImageURI metadata = (RecordMetaDataImageURI) ds.getExampleMetaData().get(0);
            INDArray features = ds.getFeatures();
            INDArray results = tinyYoloModel.getComputationGraph().outputSingle(features);
            List<DetectedObject> objs = yout.getPredictedObjects(results, detectionThreshold);
            List<DetectedObject> objects = NonMaxSuppression.getObjects(objs);

            File file = new File(metadata.getURI());
            log.info(file.getName() + ": " + objects);

            Mat mat = imageLoader.asMat(features);
            Mat convertedMat = new Mat();
            mat.convertTo(convertedMat, CV_8U, 255, 0);

            org.opencv.core.Mat image = OpenCVUtils.convert(convertedMat);
            Imgproc.resize(image, image, new org.opencv.core.Size(tinyYoloModel.getWidth(), tinyYoloModel.getHeight()));
            // 画检测框
            OpenCVUtils.drawBoxes(tinyYoloModel, objects, image);
            frame.setTitle(new File(metadata.getURI()).getName() + " - 验证");
            frame.setCanvasSize(tinyYoloModel.getWidth(), tinyYoloModel.getHeight());
            frame.showImage(converter.convert(image));
            frame.waitKey();
        }
        frame.dispose();
    }


}
