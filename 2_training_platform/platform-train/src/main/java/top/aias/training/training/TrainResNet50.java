package top.aias.training.training;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.training.config.UIServerInstance;
import top.aias.training.domain.TrainArgument;
import top.aias.training.service.TrainArgumentService;
import top.aias.training.training.models.ResNet50Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * ResNet50 模型训练
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TrainResNet50 {
    private static final Logger logger = LoggerFactory.getLogger(TrainResNet50.class);

    public static void main(String[] args) throws ModelException, TranslateException, IOException {
        String modelPath = "/Users/calvin/AIAS/2_training_platform/platform-train/models/resnet50_dl4j_inference.v3.zip";
        String savePath = "/Users/calvin/AIAS/2_training_platform/platform-train/models/";
        String dataRootPath = "/Users/calvin/AIAS/2_training_platform/platform-train/data/animals/";
        TrainArgument trainArgument = new TrainArgument();
        trainArgument.setBatchSize(2);
        trainArgument.setEpoch(1);
        trainArgument.setNClasses(4);

        UIServerInstance uiServer = new UIServerInstance();

//        TrainResNet50.train(uiServer, trainArgument, modelPath, savePath, dataRootPath);
        Path path = Paths.get(savePath);
    }

    public static void train(UIServerInstance uiServer, TrainArgumentService trainArgumentService, String modelPath, String savePath, String dataRootPath)
            throws IOException, ModelException, TranslateException {
//        String dataPath = dataRootPath + File.separator + "train";
//        String testPath = dataRootPath + File.separator + "test";

        TrainArgument trainArgument = trainArgumentService.getTrainArgument();

        // 模型超参数
        int nEpochs = trainArgument.getEpoch();
        int batchSize = trainArgument.getBatchSize();
        int nClasses = trainArgument.getNClasses();

        // 模型实例化
        ResNet50Model model = new ResNet50Model();
        model.setBatchSize(batchSize);
        model.setNClasses(nClasses);

        // 加载数据
        model.loadData(new File(dataRootPath), 70);

        // Print Labels
        RecordReaderDataSetIterator trainIter = model.getTrainIter();
        RecordReaderDataSetIterator testIter = model.getTestIter();

        List<String> labelsList = model.getLabels();
        String labels = Arrays.toString(labelsList.toArray());
        System.out.println(labels);
        // 保存类别标签
        trainArgument.setClassLabels(labels);
        trainArgumentService.update(trainArgument);

        double bestScore = 0.0;
        // 1. 构建模型
        ComputationGraph computationGraph = model.build(new File(modelPath));
        logger.info(computationGraph.summary());

        // 2. 训练模型
        logger.info("训练模型...");
        uiServer.init(computationGraph);

        for (int i = 0; i < nEpochs; i++) {
            trainIter.reset();
            while (trainIter.hasNext()) {
                computationGraph.fit(trainIter.next());
            }
            logger.info("*** epoch {} 完成 ***", i);

            System.out.println("评估模型...");
            System.out.println("当前 F1 score :" + bestScore);
            Evaluation eval = new Evaluation(nClasses);
            testIter.reset();
            while (testIter.hasNext()) {
                DataSet t = testIter.next();
                // 获取特征和标签
                INDArray evalFeatures = t.getFeatures();
                INDArray evalLabels = t.getLabels();
                // 获取模型的预测结果
                INDArray[] predicted = model.getComputationGraph().output(false, evalFeatures);
                // 如果 ComputationGraph 只有一个输出，可以直接使用 predicted[0]
                eval.eval(evalLabels, predicted[0]);
            }
            // 打印评估结果
            System.out.println(eval.stats());
            // 获取 F1 Score
            double tempScore = eval.f1();
            // 3. 保存模型
            if (tempScore > bestScore) {
                System.out.println("保存模型...");
                bestScore = tempScore;
                System.out.println("最新最好的 F1 score: " + bestScore);
                ModelSerializer.writeModel(computationGraph, savePath, true);
            }
        }


    }

}
