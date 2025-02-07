package top.aias.dl4j.examples.detection.yolo2;

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
import top.aias.dl4j.examples.detection.models.Yolo2Model;
import top.aias.dl4j.examples.utils.OpenCVUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_core.CV_8U;

/**
 * 使用 YOLO2 在ImageNet， Pascal VOC数据集上预训练的模型
 * 口罩检测训练数据使用 labelimg 标注
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class Yolo2FireSmoke {
    private static final Logger log = LoggerFactory.getLogger(Yolo2FireSmoke.class);

    private static double detectionThreshold = 0.2;

    // 训练参数
    private static int batchSize = 20;
    private static int nEpochs = 50;

    private static String modelFilePath = new File(".").getAbsolutePath() + "/generated-models/";
    private static String modelFileName = modelFilePath + "fire_smoke_yolov2.zip";

    public static void main(String[] args) throws Exception {
        Yolo2Model yolo2Model = new Yolo2Model();
        double[][] priorBoxes = {{1, 4}, {2.5, 6}, {3, 1}, {3.5, 8}, {4, 9}};
        yolo2Model.setPriorBoxes(priorBoxes);
        yolo2Model.setBatchSize(batchSize);
        // [fire, smoke]
        yolo2Model.setNClasses(2);
        yolo2Model.setLambdaCoord(0.7);
        yolo2Model.setLambdaNoObj(0.7);

        // 指定数据目录位置
        File trainDir = new ClassPathResource("fire_smoke/train").getFile();
        File testDir = new ClassPathResource("fire_smoke/test").getFile();
        yolo2Model.setTrainDir(trainDir);
        yolo2Model.setTestDir(testDir);

        LabelImgVocProvider trainLabelProvider = new LabelImgVocProvider(trainDir);
        LabelImgVocProvider testLabelProvider = new LabelImgVocProvider(testDir);

        // 加载数据
        yolo2Model.loadData(trainLabelProvider, testLabelProvider);

        // 打印标签
        RecordReaderDataSetIterator train = yolo2Model.getTrain();
        List<String> labels = yolo2Model.getTrain().getLabels();
        System.out.println(Arrays.toString(labels.toArray()));

        if (new File(modelFileName).exists()) {
            // 加载模型
            log.info("加载模型...");
            yolo2Model.loadModel(new File(modelFileName));
        } else {
            // 1. 构建模型
            ComputationGraph computationGraph = yolo2Model.build();
            System.out.println(computationGraph.summary(InputType.convolutional(yolo2Model.getHeight(), yolo2Model.getWidth(), yolo2Model.getNChannels())));

            // 2. 训练模型
            log.info("训练模型...");
            UIServer server = UIServer.getInstance();
            StatsStorage storage = new InMemoryStatsStorage();
            server.attach(storage);
            computationGraph.setListeners(new ScoreIterationListener(1), new StatsListener(storage));
            for (int i = 0; i < nEpochs; i++) {
                train.reset();
                while (train.hasNext()) {
                    computationGraph.fit(train.next());
                }
                log.info("*** epoch {} 完成 ***", i);
                // 3. 保存模型
                ModelSerializer.writeModel(computationGraph, modelFilePath + "fire_smoke_yolov2_" + i + ".zip", true);
            }
        }

        // 4. 验证模型
        RecordReaderDataSetIterator test = yolo2Model.getTest();
        test.reset();
        validation(yolo2Model, test);
    }

    private static void validation(Yolo2Model model, RecordReaderDataSetIterator test) throws InterruptedException {

        // 可视化测试
        NativeImageLoader imageLoader = new NativeImageLoader();
        CanvasFrame frame = new CanvasFrame("验证");
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer yout = (org.deeplearning4j.nn.layers.objdetect.Yolo2OutputLayer) model.getComputationGraph().getOutputLayer(0);

        test.setCollectMetaData(true);
        while (test.hasNext() && frame.isVisible()) {
            org.nd4j.linalg.dataset.DataSet ds = test.next();
            RecordMetaDataImageURI metadata = (RecordMetaDataImageURI) ds.getExampleMetaData().get(0);
            INDArray features = ds.getFeatures();
            INDArray results = model.getComputationGraph().outputSingle(features);
            List<DetectedObject> objs = yout.getPredictedObjects(results, detectionThreshold);
            List<DetectedObject> objects = NonMaxSuppression.getObjects(objs);

            File file = new File(metadata.getURI());
            log.info(file.getName() + ": " + objects);

            Mat mat = imageLoader.asMat(features);
            Mat convertedMat = new Mat();
            mat.convertTo(convertedMat, CV_8U, 255, 0);

            org.opencv.core.Mat image = OpenCVUtils.convert(convertedMat);
            Imgproc.resize(image, image, new org.opencv.core.Size(model.getWidth(), model.getHeight()));
            // 画检测框
            OpenCVUtils.drawBoxes(model, objects, image);
            frame.setTitle(new File(metadata.getURI()).getName() + " - 验证");
            frame.setCanvasSize(model.getWidth(), model.getHeight());
            frame.showImage(converter.convert(image));
            frame.waitKey();
        }
        frame.dispose();
    }
}
