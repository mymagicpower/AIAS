package top.aias.dl4j.exercises;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import top.aias.dl4j.examples.utils.PlotUtils;

import java.io.File;

/**
 * 数据分类示例
 *
 * 基于Jason Baldridge的数据：
 * 	https://github.com/jasonbaldridge/try-tf/tree/master/simdata
 *
 * @author Josh Patterson
 * @author Alex Black (添加了绘图功能)
 *
 */
public class MLPClassifier {


    public static void main(String[] args) throws Exception {
        int batchSize = 50;
        int seed = 123;
        double learningRate = 0.005;
        double momentum = 0.9;
        // 训练的轮数（数据完整遍历的次数）
        int nEpochs = 30;

        int numInputs = 2;
        int numOutputs = 1;
        int numHiddenNodes = 20;

        final String filenameTrain  = new ClassPathResource("classification/saturn_data_train.csv").getFile().getPath();
        final String filenameTest  = new ClassPathResource("classification/saturn_data_eval.csv").getFile().getPath();

        // 加载训练数据：
        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new File(filenameTrain)));
        DataSetIterator trainIter = new RecordReaderDataSetIterator(rr,batchSize,0,1);

        // 加载测试/评估数据：
        RecordReader rrTest = new CSVRecordReader();
        rrTest.initialize(new FileSplit(new File(filenameTest)));
        DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest,batchSize,0, 1);

        // 构建模型：
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .updater(new Nesterovs(learningRate, momentum))
                .list()
                .layer(0, new DenseLayer.Builder()
                    .nIn(numInputs)
                    .nOut(numHiddenNodes)
                    .weightInit(WeightInit.XAVIER)
                    .activation(Activation.RELU)
                    .build())
                .layer(1, new OutputLayer.Builder(LossFunction.XENT)
                    .weightInit(WeightInit.XAVIER)
                    .activation(Activation.SIGMOID)
                    .nIn(numHiddenNodes).nOut(numOutputs).build())
                .build();


        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        // 每10次参数更新打印一次分数
        model.setListeners(new ScoreIterationListener(10));

        for ( int n = 0; n < nEpochs; n++) {
            model.fit( trainIter );
        }

        System.out.println("评估模型....");
        Evaluation eval = new Evaluation(numOutputs);
        while(testIter.hasNext()){
            DataSet t = testIter.next();
            INDArray features = t.getFeatures();
            INDArray lables = t.getLabels();
            INDArray predicted = model.output(features,false);
            eval.eval(lables, predicted);
        }

        System.out.println(eval.stats());
        //------------------------------------------------------------------------------------
        // 训练完成。以下代码仅用于绘制数据和预测结果

        double xMin = -15;
        double xMax = 15;
        double yMin = -15;
        double yMax = 15;

        // 评估每个点在x/y输入空间的预测，并绘制这些点的背景
        int nPointsPerAxis = 100;
        double[][] evalPoints = new double[nPointsPerAxis*nPointsPerAxis][2];
        int count = 0;
        for( int i=0; i<nPointsPerAxis; i++ ){
            for( int j=0; j<nPointsPerAxis; j++ ){
                double x = i * (xMax-xMin)/(nPointsPerAxis-1) + xMin;
                double y = j * (yMax-yMin)/(nPointsPerAxis-1) + yMin;

                evalPoints[count][0] = x;
                evalPoints[count][1] = y;

                count++;
            }
        }

        INDArray allXYPoints = Nd4j.create(evalPoints);
        INDArray predictionsAtXYPoints = model.output(allXYPoints);

        // 获取所有训练数据并绘制：
        rr.initialize(new FileSplit(new File(filenameTrain)));
        rr.reset();
        int nTrainPoints = 500;
        trainIter = new RecordReaderDataSetIterator(rr,nTrainPoints,0,1);
        DataSet ds = trainIter.next();
        PlotUtils.plotTrainingData(ds.getFeatures(), ds.getLabels(), allXYPoints, predictionsAtXYPoints, nPointsPerAxis);


        // 获取测试数据，将测试数据通过网络生成预测，并绘制预测结果：
        rrTest.initialize(new FileSplit(new File(filenameTest)));
        rrTest.reset();
        int nTestPoints = 100;
        testIter = new RecordReaderDataSetIterator(rrTest,nTestPoints,0,1);
        ds = testIter.next();
        INDArray testPredicted = model.output(ds.getFeatures());
        PlotUtils.plotTestData(ds.getFeatures(), ds.getLabels(), testPredicted, allXYPoints, predictionsAtXYPoints, nPointsPerAxis);

        System.out.println("****************示例结束********************");
    }

}
