package top.aias.training.training.models;

import lombok.Data;
import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.BaseImageLoader;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.FlipImageTransform;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.PipelineImageTransform;
import org.datavec.image.transform.WarpImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.WorkspaceMode;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.FineTuneConfiguration;
import org.deeplearning4j.nn.transferlearning.TransferLearning;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.ResNet50;
import org.nd4j.common.primitives.Pair;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * ResNet50 模型
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
@Data
public class ResNet50Model {
    // 训练数据标签数（分类数）
    protected int nClasses = 3;
    // 标签：模型输出分类
    protected List<String> labels = null;
    // 小批量大小
    protected Integer batchSize = 10;
    // 训练轮数
    protected Integer epochs = 40;
    // 图片宽度
    protected int width = 224;
    // 图片高度
    protected int height = 224;
    // 图片通道数
    protected int nChannels = 3;

    // 训练阶段的参数设置
    // 学习率
    protected Double learningRate = 1e-3;
    // 动量
    protected double lrMomentum = 0.9;
    // 随机数
    protected Random rng = new Random(13);
    // 训练数据路径
    protected File trainDir = null;
    // 测试数据路径
    protected File testDir = null;

    protected RecordReaderDataSetIterator trainIter;

    protected RecordReaderDataSetIterator testIter;

    protected ComputationGraph computationGraph;

    protected ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();

    public ResNet50Model() {
    }

    public ComputationGraph loadModel(File model) throws IOException {
        // model = ModelSerializer.restoreComputationGraph(modelPath);
        computationGraph = ComputationGraph.load(model, true);
        return computationGraph;
    }

    public void loadData(File parentDir, int trainPerc) throws IOException {
        FileSplit filesInDir = new FileSplit(parentDir, BaseImageLoader.ALLOWED_FORMATS, rng);
        BalancedPathFilter pathFilter = new BalancedPathFilter(rng, BaseImageLoader.ALLOWED_FORMATS, labelMaker);
        if (trainPerc >= 100) {
            throw new IllegalArgumentException("训练百分比应该小于等于 100%.");
        }
        InputSplit[] filesInDirSplit = filesInDir.sample(pathFilter, trainPerc, 100 - trainPerc);
        InputSplit trainData = filesInDirSplit[0];
        InputSplit testData = filesInDirSplit[1];

        // 数据增强
        boolean shuffle = false;

        ImageTransform flipTransform1 = new FlipImageTransform(rng);
        ImageTransform flipTransform2 = new FlipImageTransform(new Random(123));
        ImageTransform warpTransform = new WarpImageTransform(rng, 42);
        List<Pair<ImageTransform, Double>> pipeline = Arrays.asList(
                new Pair<>(flipTransform1, 0.9),
                new Pair<>(flipTransform2, 0.8),
                new Pair<>(warpTransform, 0.5));

//        List<Pair<ImageTransform, Double>> pipeline = Arrays.asList(
//                new Pair<>(new FlipImageTransform(rng), 0.9),
//                new Pair<>(new RotateImageTransform(15), 0.8),
//                new Pair<>(new ScaleImageTransform(rng, 0.8f), 0.5),
//                new Pair<>(new WarpImageTransform(rng, 42), 0.5));

        ImageTransform transform = new PipelineImageTransform(pipeline, shuffle);

        ImageRecordReader recordReaderTrain = new ImageRecordReader(height, width, nChannels, labelMaker);
        recordReaderTrain.initialize(trainData, transform);
        trainIter = new RecordReaderDataSetIterator(recordReaderTrain, batchSize, 1, nClasses);
        trainIter.setPreProcessor(new VGG16ImagePreProcessor());

        ImageRecordReader recordReaderTest = new ImageRecordReader(height, width, nChannels, labelMaker);
        recordReaderTest.initialize(testData);
        testIter = new RecordReaderDataSetIterator(recordReaderTest, 1, 1, nClasses);
        testIter.setPreProcessor(new VGG16ImagePreProcessor());

        labels = trainIter.getLabels();
    }

    public void loadData(File trainDir, File testDir) throws IOException {
        FileSplit trainData = new FileSplit(trainDir, NativeImageLoader.ALLOWED_FORMATS, rng);
        FileSplit testData = new FileSplit(testDir, NativeImageLoader.ALLOWED_FORMATS, rng);

        // 数据增强
        boolean shuffle = false;

        ImageTransform flipTransform1 = new FlipImageTransform(rng);
        ImageTransform flipTransform2 = new FlipImageTransform(new Random(123));
        ImageTransform warpTransform = new WarpImageTransform(rng, 42);
        List<Pair<ImageTransform, Double>> pipeline = Arrays.asList(
                new Pair<>(flipTransform1, 0.9),
                new Pair<>(flipTransform2, 0.8),
                new Pair<>(warpTransform, 0.5));

//        List<Pair<ImageTransform, Double>> pipeline = Arrays.asList(
//                new Pair<>(new FlipImageTransform(rng), 0.9),
//                new Pair<>(new RotateImageTransform(15), 0.8),
//                new Pair<>(new ScaleImageTransform(rng, 0.8f), 0.5),
//                new Pair<>(new WarpImageTransform(rng, 42), 0.5));

        ImageTransform transform = new PipelineImageTransform(pipeline, shuffle);

        ImageRecordReader recordReaderTrain = new ImageRecordReader(height, width, nChannels, labelMaker);
        recordReaderTrain.initialize(trainData, transform);
        trainIter = new RecordReaderDataSetIterator(recordReaderTrain, batchSize, 1, nClasses);
        trainIter.setPreProcessor(new VGG16ImagePreProcessor());

        ImageRecordReader recordReaderTest = new ImageRecordReader(height, width, nChannels, labelMaker);
        recordReaderTest.initialize(testData);
        testIter = new RecordReaderDataSetIterator(recordReaderTest, 1, 1, nClasses);
        testIter.setPreProcessor(new VGG16ImagePreProcessor());

        labels = trainIter.getLabels();
    }

    public ComputationGraph build() throws IOException {

        // 步骤 1: 迁移学习步骤 - 加载 ResNet50 的预构建模型。
        ZooModel zooModel = ResNet50.builder().build();
        ComputationGraph pretrained = (ComputationGraph) zooModel.initPretrained();

        // 步骤 2: 迁移学习步骤 - 模型配置。
        FineTuneConfiguration fineTuneConf = getFineTuneConfiguration();

        // 步骤 3: 迁移学习步骤 - 修改预构建模型的架构。
        ComputationGraph computationGraph = getNewComputationGraph(pretrained, fineTuneConf);
        this.setComputationGraph(computationGraph);

        return computationGraph;
    }

    public ComputationGraph build(File model) throws IOException {

        // 步骤 1: 迁移学习步骤 - 加载本地 ResNet50 的预构建模型。
        ComputationGraph pretrained = this.loadModel(model);

        // 步骤 2: 迁移学习步骤 - 模型配置。
        FineTuneConfiguration fineTuneConf = getFineTuneConfiguration();

        // 步骤 3: 迁移学习步骤 - 修改预构建模型的架构。
        computationGraph = getNewComputationGraph(pretrained, fineTuneConf);
        this.setComputationGraph(computationGraph);

        return computationGraph;
    }

    private FineTuneConfiguration getFineTuneConfiguration() {
        FineTuneConfiguration fineTuneConf = new FineTuneConfiguration.Builder()
                .seed(rng.nextInt())
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer)
                .gradientNormalizationThreshold(1.0)
                .updater(new Nesterovs.Builder().learningRate(learningRate).momentum(Nesterovs.DEFAULT_NESTEROV_MOMENTUM).build())
                .l2(0.00001)
                .activation(Activation.IDENTITY)
                .trainingWorkspaceMode(WorkspaceMode.ENABLED)
                .inferenceWorkspaceMode(WorkspaceMode.ENABLED)
                .build();

        return fineTuneConf;
    }

    private ComputationGraph getNewComputationGraph(ComputationGraph pretrained, FineTuneConfiguration fineTuneConf) {
        // 构建新模型，替换输出层
        // 这是因为 ResNet50 使用了预训练的模型结构，其中 fc1000 是全连接层，用于 Imagenet 分类任务（通常有 1000 类）。
        // 在迁移学习中，当需要对其他任务进行训练时，必须替换这个输出层以匹配新任务的类别数。
        // 使用 TransferLearning 构建新模型
        ComputationGraph ResNet50Transfer = new TransferLearning.GraphBuilder(pretrained)
        //      .setFeatureExtractor("avg_pool") // 冻结到 avg_pool 层 - 用于指定哪个层以下为非冻结层
                .fineTuneConfiguration(fineTuneConf)
                .removeVertexAndConnections("fc1000") // 移除原始输出层
                .addLayer("fc1000", new OutputLayer.Builder()
                        .nIn(2048) // avg_pool 的输出大小
                        .nOut(nClasses) // 新任务的类别数
                        .activation(Activation.SOFTMAX)
                        .lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .build(), "flatten_1") // 将新层连接到 flatten_1
                .setOutputs("fc1000") // 设置 fc1000 为新的输出层
                .build();


        return ResNet50Transfer;
    }

}