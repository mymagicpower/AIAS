package top.aias.dl4j.examples.classification;

import com.google.common.primitives.Ints;
import org.datavec.api.records.metadata.RecordMetaDataURI;
import org.deeplearning4j.core.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.model.stats.StatsListener;
import org.deeplearning4j.ui.model.storage.InMemoryStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.dl4j.examples.classification.models.ClassPrediction;
import top.aias.dl4j.examples.classification.models.ResNet50Model;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * ResNet50 模型训练
 * 数据集：动物
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class ResNet50Animals {
    private static final Logger log = LoggerFactory.getLogger(ResNet50Animals.class);
    private static String modelFilePath = new File(".").getAbsolutePath() + "/generated-models/";
    private static String modelFileName = modelFilePath + "ResNet50_Animals.zip";
    // 训练参数
    private static int batchSize = 5;
    private static int nEpochs = 15;
    // 训练数据标签数（分类数）
    protected static int nClasses = 4;
    private static List<String> labels;

    public static void main(String[] args) throws Exception {
        ResNet50Model model = new ResNet50Model();
        model.setBatchSize(batchSize);
        model.setNClasses(nClasses);

        // 加载数据
//        File trainDir = new ClassPathResource("cars/train").getFile();
//        File testDir = new ClassPathResource("cars/test").getFile();
//        vgg16Model.loadData(trainDir, testDir);
        // 加载数据
        model.loadData(new ClassPathResource("animals").getFile(), 70);

        // Print Labels
        RecordReaderDataSetIterator trainIter = model.getTrainIter();
        labels = model.getLabels();
        System.out.println(Arrays.toString(labels.toArray()));

        if (new File(modelFileName).exists()) {
            // 加载模型
            log.info("加载模型...");
            model.loadModel(new File(modelFileName));

        } else {
            // 1. 构建模型
            ComputationGraph computationGraph = model.build();
            log.info(computationGraph.summary());

            // 2. 训练模型
            log.info("训练模型...");
            UIServer server = UIServer.getInstance();
            StatsStorage storage = new InMemoryStatsStorage();
            server.attach(storage);
            computationGraph.setListeners(new ScoreIterationListener(1), new StatsListener(storage));

            for (int i = 0; i < nEpochs; i++) {
                trainIter.reset();
                while (trainIter.hasNext()) {
                    computationGraph.fit(trainIter.next());
                }
                log.info("*** epoch {} 完成 ***", i);
                // 3. 保存模型
                ModelSerializer.writeModel(computationGraph, modelFilePath + "ResNet50_Animals_" + i + ".zip", true);
            }
        }

        // 4. 验证模型
        RecordReaderDataSetIterator testIter = model.getTestIter();
//        validationTestDataset(model, testIter);

        System.out.println("Evaluate model....");
        // 假设 testIter 是 DataSetIterator，并且 numOutputs 是输出类别的数量
        Evaluation eval = new Evaluation(nClasses);

        while (testIter.hasNext()) {
            DataSet t = testIter.next();
            // 获取特征和标签
            INDArray features = t.getFeatures();
            INDArray labels = t.getLabels();
            // 获取模型的预测结果
            INDArray[] predicted = model.getComputationGraph().output(false, features);

            // 如果 ComputationGraph 只有一个输出，可以直接使用 predicted[0]
            eval.eval(labels, predicted[0]);
        }
        // 打印评估结果
        System.out.println(eval.stats());
    }

    private static void validationTestDataset(ResNet50Model model, RecordReaderDataSetIterator test) {
        test.setCollectMetaData(true);
        while (test.hasNext()) {
            DataSet ds = test.next();
            RecordMetaDataURI metadata = (RecordMetaDataURI) ds.getExampleMetaData().get(0);
            INDArray image = ds.getFeatures();
            // 获取实际标签
            System.out.println("实际标签: " + labels.get(Ints.asList(ds.getLabels().toIntVector()).indexOf(1)));
            System.out.println(metadata.getURI());
            ClassPrediction classPrediction = getPredictions(model, image);
            System.out.println("预测结果:  " + classPrediction);
        }
    }

    private static ClassPrediction getPredictions(ResNet50Model model, INDArray image) {
        // 获取模型预测结果
        INDArray predictions = model.getComputationGraph().outputSingle(image);
        int predictedClassIndex = predictions.argMax(1).getInt(0);
        String modelPrediction = labels.get(predictedClassIndex);
        float topXProb = predictions.getFloat(0, predictedClassIndex);
        return new ClassPrediction(modelPrediction, topXProb);
    }
}
