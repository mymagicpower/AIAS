package top.aias.dl4j.exercises.graph;

import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.learning.config.Sgd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 示例 2：多个输入和合并顶点
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class ComputationGraphExample2 {
    private static final Logger log = LoggerFactory.getLogger(ComputationGraphExample2.class);

    public static void main(String[] args) throws Exception {

        // 示例：合并顶点从层 L1 和 L2 中提取激活，并将它们合并（连接）：
        // 因此，如果层 L1 和 L2 都具有 4 个输出激活（.nOut(4)），
        // 则合并顶点的输出大小为 4+4=8 个激活。
        ComputationGraphConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(new Sgd(0.01))
                .graphBuilder()
                .addInputs("input1", "input2")
                .addLayer("L1", new DenseLayer.Builder().nIn(3).nOut(4).build(), "input1")
                .addLayer("L2", new DenseLayer.Builder().nIn(3).nOut(4).build(), "input2")
                .addVertex("merge", new MergeVertex(), "L1", "L2")
                .addLayer("out", new OutputLayer.Builder().nIn(4+4).nOut(3).build(), "merge")
                .setOutputs("out")
                .build();

        ComputationGraph net = new ComputationGraph(conf);
        net.init();

        System.out.println("模型参数量："+ net.numParams());

    }
}
