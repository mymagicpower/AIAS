package top.aias.dl4j.exercises.graph;

import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.learning.config.Sgd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 示例 1：具有跳过连接的循环网络
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class ComputationGraphExample1 {
    private static final Logger log = LoggerFactory.getLogger(ComputationGraphExample1.class);

    public static void main(String[] args) throws Exception {

        // 示例：假设我们的输入数据大小为 5
        ComputationGraphConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(new Sgd(0.01))
                .graphBuilder()
                .addInputs("input") //can use any label for this
                .addLayer("L1", new GravesLSTM.Builder().nIn(5).nOut(5).build(), "input")
                .addLayer("L2",new RnnOutputLayer.Builder().nIn(5+5).nOut(5).build(), "input", "L1")
                .setOutputs("L2")    //We need to specify the network outputs and their order
                .build();

        ComputationGraph net = new ComputationGraph(conf);
        net.init();

        System.out.println("模型参数量："+ net.numParams());

    }
}
