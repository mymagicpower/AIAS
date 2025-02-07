package top.aias.dl4j.exercises.modelio;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;

/**
 * 保存多层神经网络  MultiLayerNetwork
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class MultiLayerNetworkSave {

    public static void main(String[] args) throws Exception {
        //定义一个简单的多层神经网络
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .weightInit(WeightInit.XAVIER)
            .updater(new Nesterovs(0.1, 0.9))
            .list()
            .layer(new DenseLayer.Builder().nIn(4).nOut(3).activation(Activation.TANH).build())
            .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).activation(Activation.SOFTMAX).nIn(3).nOut(3).build())
            .build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();

        //保存模型
        File locationToSave = new File("MyMultiLayerNetwork.zip");
        boolean saveUpdater = true;
        net.save(locationToSave, saveUpdater);

        System.out.println("模型参数:      " + net.params());
        System.out.println("模型配置:  " + net.getLayerWiseConfigurations());
    }

}
