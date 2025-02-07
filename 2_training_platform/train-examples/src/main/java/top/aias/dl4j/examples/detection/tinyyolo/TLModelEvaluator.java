package top.aias.dl4j.examples.detection.tinyyolo;

import org.datavec.image.recordreader.objdetect.ImageObjectLabelProvider;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.common.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.dl4j.examples.detection.dataHelpers.LabelProvider;
import top.aias.dl4j.examples.detection.dataHelpers.YoloEvaluator;
import top.aias.dl4j.examples.detection.models.TinyYoloModel;

import java.io.File;
/**
 * 模型评估
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class TLModelEvaluator {
    private static final Logger log = LoggerFactory.getLogger(TLModelEvaluator.class);

    public static void main(String[] args) throws Exception {
        TinyYoloModel model = new TinyYoloModel();
        // 指定数据目录位置
        File trainDir = new ClassPathResource("facemask_data/train").getFile();
        File testDir = new ClassPathResource("facemask_data/test").getFile();
        model.setTrainDir(trainDir);
        model.setTestDir(testDir);

        ImageObjectLabelProvider trainLabelProvider = new LabelProvider(trainDir);
        ImageObjectLabelProvider testLabelProvider = new LabelProvider(testDir);

        // 加载数据
        model.loadData(trainLabelProvider, testLabelProvider);

        String modelFilePath = new File(".").getAbsolutePath() + "/generated-models/";
        String modelFileName = modelFilePath + "TinyYOLO_facemask_81.zip";

        System.out.println("加载模型...");
        ComputationGraph computationGraph = model.loadModel(new File(modelFileName));

        System.out.println("评估模型...");
        YoloEvaluator yoloEvaluator = new YoloEvaluator(computationGraph, model.getTest(), testLabelProvider, 0.2, model.getWidth(), model.getHeight());
        yoloEvaluator.print();
    }
}