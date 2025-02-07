package top.aias.dl4j.exercises.modelio;

import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;

/**
 * 保存计算图 ComputationGraph
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class ComputationGraphSave {

    public static void main(String[] args) throws Exception {
        // 定义一个简单的计算图 ComputationGraph:
        ComputationGraphConfiguration conf = new NeuralNetConfiguration.Builder()
            .weightInit(WeightInit.XAVIER)
            .updater(new Nesterovs(0.1, 0.9))
            .graphBuilder()
            .addInputs("in")
            .addLayer("layer0", new DenseLayer.Builder().nIn(4).nOut(3).activation(Activation.TANH).build(), "in")
            .addLayer("layer1", new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).activation(Activation.SOFTMAX).nIn(3).nOut(3).build(), "layer0")
            .setOutputs("layer1")
            .build();

        ComputationGraph net = new ComputationGraph(conf);
        net.init();

        // 保存模型
        File locationToSave = new File("MyComputationGraph.zip");
        // 如果需要继续训练保存的网络，则需要保存状态：用于动量（Momentum）、RMSProp、Adagrad 等的状态。
        boolean saveUpdater = true;
        net.save(locationToSave, saveUpdater);

        System.out.println("模型参数:      " + net.params());
        System.out.println("模型配置:  " + net.getConfiguration());
    }

}
