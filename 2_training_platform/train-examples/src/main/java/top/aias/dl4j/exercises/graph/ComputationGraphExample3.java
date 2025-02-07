package top.aias.dl4j.exercises.graph;

import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 示例 3：多任务学习
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class ComputationGraphExample3 {
    private static final Logger log = LoggerFactory.getLogger(ComputationGraphExample3.class);

    public static void main(String[] args) throws Exception {

        // 在多任务学习中，神经网络被用于做出多个独立的预测。
        // 例如，考虑一个同时用于分类和回归的简单网络。
        // 在这种情况下，我们有两个输出层，“out1”用于分类，“out2”用于回归。
        ComputationGraphConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(new Sgd(0.01))
                .graphBuilder()
                .addInputs("input")
                .addLayer("L1", new DenseLayer.Builder().nIn(3).nOut(4).build(), "input")
                .addLayer("out1", new OutputLayer.Builder()
                        .lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(4).nOut(3).build(), "L1")
                .addLayer("out2", new OutputLayer.Builder()
                        .lossFunction(LossFunctions.LossFunction.MSE)
                        .nIn(4).nOut(2).build(), "L1")
                .setOutputs("out1","out2")
                .build();

        ComputationGraph net = new ComputationGraph(conf);
        net.init();

        System.out.println("模型参数量："+ net.numParams());

    }
}
