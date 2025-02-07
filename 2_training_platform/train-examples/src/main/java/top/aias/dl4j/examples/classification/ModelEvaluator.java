package top.aias.dl4j.examples.classification;

import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.dl4j.examples.classification.models.ResNet50Model;

import java.io.File;
/**
 * 分类模型评估
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class ModelEvaluator {
    private static final Logger log = LoggerFactory.getLogger(ModelEvaluator.class);

    public static void main(String[] args) throws Exception {
        // 训练数据标签数（分类数）
        int nClasses = 6;
        String modelFilePath = new File(".").getAbsolutePath() + "/generated-models/";
        String modelFileName = modelFilePath + "ResNet50_Garbage.zip";

        // 1. 加载模型
        System.out.println("加载模型...");
        ResNet50Model resNet50Model = new ResNet50Model();
        resNet50Model.setNClasses(nClasses);

        // 2. 加载数据
        System.out.println("加载数据...");
        resNet50Model.loadData(new ClassPathResource("garbage").getFile(), 70);
        resNet50Model.loadModel(new File(modelFileName));

        // 3. 评估模型
        System.out.println("评估模型...");
        Evaluation eval = new Evaluation(nClasses);
        RecordReaderDataSetIterator testIter = resNet50Model.getTestIter();

        while (testIter.hasNext()) {
            DataSet t = testIter.next();
            // 获取特征和标签
            INDArray features = t.getFeatures();
            INDArray labels = t.getLabels();
            // 获取模型的预测结果
            INDArray[] predicted = resNet50Model.getComputationGraph().output(false, features);
            // 如果 ComputationGraph 只有一个输出，可以直接使用 predicted[0]
            eval.eval(labels, predicted[0]);
        }

        // 打印评估结果
        System.out.println(eval.stats());
    }
}