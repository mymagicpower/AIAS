package top.aias.dl4j.examples.detection.models;

import lombok.Data;
import org.datavec.api.split.FileSplit;
import org.datavec.image.recordreader.objdetect.ImageObjectLabelProvider;
import org.datavec.image.recordreader.objdetect.ObjectDetectionRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * 基础模型
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
@Data
public abstract class BaseModel {
    // 训练数据标签数（分类数）
    protected int nClasses = 2;

    // 标签：模型输出分类
    protected List<String> labels = null;

    // 小批量大小
    protected Integer batchSize = 2;
    // 训练轮数
    protected Integer epochs = 1;

    // 与预训练模型 TinyYOLO 一致的参数设置
    // 图片宽度
    protected int width = 416;
    // 图片高度
    protected int height = 416;
    // 图片通道数
    protected int nChannels = 3;
    // 网格宽度
    protected int gridWidth = 13;
    // 网格高度
    protected int gridHeight = 13;

    // Yolo2OutputLayer 层参数设置
    // 先验框数
    protected int nBoxes = 5;
    // Yolo 损失函数参数说明
    // https://stats.stackexchange.com/questions/287486/yolo-loss-function-explanation
    // 用于控制未包含目标的网格单元（no object）的损失权重
    protected double lambdaNoObj = 0.5;
    // 用于控制坐标回归（bounding box）的损失权重。
    protected double lambdaCoord = 5.0;
    // 先验框
    protected double[][] priorBoxes = {{1.5, 1.5}, {2, 2}, {3, 3}, {3.5, 8}, {4, 9}};
    // 其它
    // double[][] priorBoxes = { { 2, 2 }, { 2, 2 }, { 2, 2 }, { 2, 2 }, { 2, 2 } };
    // double[][] priorBoxes = {{1.5, 1.5}, {2, 2}, {3,3}, {3.5, 8}, {4, 9}};
    // double[][] priorBoxes = {{2, 5}, {2.5, 6}, {3, 7}, {3.5, 8}, {4, 9}};

    // 训练阶段的参数设置
    // 学习率
    protected Double learningRate = 1e-3;
    // 动量
    protected double lrMomentum = 0.9;
    // 随机数
    // private static int seed = 123;
    // System.currentTimeMillis()
    protected Random rng = new Random(System.currentTimeMillis());
    // 训练数据路径
    protected File trainDir = null;
    // 测试数据路径
    protected File testDir = null;

    protected RecordReaderDataSetIterator train;

    protected RecordReaderDataSetIterator test;

    protected ComputationGraph computationGraph;

    public BaseModel() {
    }

    public ComputationGraph loadModel(File model) throws IOException {
        // model = ModelSerializer.restoreComputationGraph(modelPath);
        computationGraph = ComputationGraph.load(model, true);
        return computationGraph;
    }

    public void loadData(ImageObjectLabelProvider trainLabelProvider, ImageObjectLabelProvider testLabelProvider) throws IOException {
//        File trainDirPath = new ClassPathResource(trainDir).getFile();
//        File testDirPath = new ClassPathResource(testDir).getFile();
//        FileSplit trainData = new FileSplit(trainDir, NativeImageLoader.ALLOWED_FORMATS, rng);
//        FileSplit testData = new FileSplit(testDir, NativeImageLoader.ALLOWED_FORMATS, rng);

        FileSplit trainData = new FileSplit(trainDir, new String[]{".png", ".jpg"}, rng);
        FileSplit testData = new FileSplit(testDir, new String[]{".png", ".jpg"}, rng);

        ObjectDetectionRecordReader recordReaderTrain = new ObjectDetectionRecordReader(height, width, nChannels, gridHeight, gridWidth, trainLabelProvider);
        recordReaderTrain.initialize(trainData);

        ObjectDetectionRecordReader recordReaderTest = new ObjectDetectionRecordReader(height, width, nChannels, gridHeight, gridWidth, testLabelProvider);
        recordReaderTest.initialize(testData);

        // tinyYolo 涉及回归，所以设置为true
        DataSetPreProcessor preProcessingScaler = new ImagePreProcessingScaler();
        train = new RecordReaderDataSetIterator(recordReaderTrain, batchSize, 1, 1, true);
        train.setPreProcessor(preProcessingScaler);

        test = new RecordReaderDataSetIterator(recordReaderTest, 1, 1, 1, true);
        test.setPreProcessor(preProcessingScaler);
        test.setCollectMetaData(true);
    }

    public abstract ComputationGraph build() throws IOException;

}