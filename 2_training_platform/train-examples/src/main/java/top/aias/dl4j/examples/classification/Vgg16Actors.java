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
import org.deeplearning4j.zoo.PretrainedType;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.dl4j.examples.classification.models.Vgg16Model;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 演员图片分类
 * 模型：VGG16 (预训练模型数据 VGGFace)
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class Vgg16Actors {
    private static final Logger log = LoggerFactory.getLogger(Vgg16Actors.class);
    private static String modelFilePath = new File(".").getAbsolutePath() + "/generated-models/";
    private static String modelFileName = modelFilePath + "VGG16_Actorsxx.zip";
    // 训练参数
    private static int batchSize = 8;
    private static int nEpochs = 20;
    private static List<String> labels;
    // 训练数据标签数（分类数）
    private static int nClasses = 3;

    public static void main(String[] args) throws Exception {
        Vgg16Model vgg16Model = new Vgg16Model();
        vgg16Model.setBatchSize(batchSize);
        vgg16Model.setNClasses(nClasses);
        // 加载数据
        vgg16Model.loadData(new ClassPathResource("actors").getFile(), 70);

        // 输出分类标签
        RecordReaderDataSetIterator trainIter = vgg16Model.getTrainIter();
        labels = vgg16Model.getLabels();
        System.out.println(Arrays.toString(labels.toArray()));

        if (new File(modelFileName).exists()) {
            // 加载模型
            log.info("加载模型...");
            vgg16Model.loadModel(new File(modelFileName));

        } else {
            // 1. 构建模型
            ComputationGraph computationGraph = vgg16Model.build(PretrainedType.VGGFACE);
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
                if (i > (nEpochs - 5)) {
                    ModelSerializer.writeModel(computationGraph, modelFilePath + "VGG16_Actors_" + i + ".zip", true);
                }
            }
        }

        // 4. 验证模型
        RecordReaderDataSetIterator testIter = vgg16Model.getTestIter();
//        validationTestDataset(vgg16Model, testIter);

        System.out.println("Evaluate model....");
        // 假设 testIter 是 DataSetIterator，并且 numOutputs 是输出类别的数量
        Evaluation eval = new Evaluation(nClasses);

        while (testIter.hasNext()) {
            DataSet t = testIter.next();
            // 获取特征和标签
            INDArray features = t.getFeatures();
            INDArray labels = t.getLabels();
            // 获取模型的预测结果
            INDArray[] predicted = vgg16Model.getComputationGraph().output(false, features);

            // 如果 ComputationGraph 只有一个输出，可以直接使用 predicted[0]
            eval.eval(labels, predicted[0]);
        }

        // 打印评估结果
        System.out.println(eval.stats());
    }

    private static void validationTestDataset(Vgg16Model vgg16Model, RecordReaderDataSetIterator test) {
        test.setCollectMetaData(true);
        while (test.hasNext()) {
            DataSet ds = test.next();
            RecordMetaDataURI metadata = (RecordMetaDataURI) ds.getExampleMetaData().get(0);
            INDArray image = ds.getFeatures();
            // 获取实际标签
            System.out.println("实际标签: " + labels.get(Ints.asList(ds.getLabels().toIntVector()).indexOf(1)));
            System.out.println(metadata.getURI());
            Prediction prediction = getPredictions(vgg16Model, image);
            System.out.println("预测结果:  " + prediction);
        }
    }

    private static Prediction getPredictions(Vgg16Model vgg16Model, INDArray image) {
        // 获取模型预测结果
        INDArray predictions = vgg16Model.getComputationGraph().outputSingle(image);
        int predictedClassIndex = predictions.argMax(1).getInt(0);
        String modelPrediction = labels.get(predictedClassIndex);
        float topXProb = predictions.getFloat(0, predictedClassIndex);
        return new Prediction(modelPrediction, topXProb);
    }


    public static class Prediction {

        private String label;
        private double percentage;

        public Prediction(String label, double percentage) {
            this.label = label;
            this.percentage = percentage;
        }

        public void setLabel(final String label) {
            this.label = label;
        }

        public String toString() {
            return String.format("%s: %.2f ", this.label, this.percentage);
        }
    }

}
