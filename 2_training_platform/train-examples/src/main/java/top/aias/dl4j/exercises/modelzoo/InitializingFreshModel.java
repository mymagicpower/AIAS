package top.aias.dl4j.exercises.modelzoo;

import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.AlexNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实例化一个全新的、未经训练的 AlexNet 网络
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class InitializingFreshModel {
    private static final Logger log = LoggerFactory.getLogger(InitializingFreshModel.class);

    public static void main(String[] args) throws Exception {
        int numberOfClassesInYourData = 1000;
        int randomSeed = 123;

        // 实例化一个全新的、未经训练的 AlexNet 网络
        ZooModel zooModel = AlexNet.builder()
                .numClasses(numberOfClassesInYourData)
                .seed(randomSeed)
                .build();
        Model net = zooModel.init();

        System.out.println("模型参数量："+ net.numParams());

        // 如果想调整参数或更改优化算法，可以获取底层网络配置的引用
        MultiLayerConfiguration conf = ((AlexNet) zooModel).conf();
    }
}
