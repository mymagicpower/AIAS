package top.aias.dl4j.exercises.modelio;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import java.io.File;

/**
 * 载入多层神经网络  MultiLayerNetwork
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class MultiLayerNetworkLoad {

    public static void main(String[] args) throws Exception {
        //指定模型路径
        File locationToSave = new File("MyMultiLayerNetwork.zip");

        //载入模型
        MultiLayerNetwork restored = MultiLayerNetwork.load(locationToSave, true);

        System.out.println("模型参数:      " + restored.params());
        System.out.println("模型配置:  " + restored.getLayerWiseConfigurations());
    }

}
