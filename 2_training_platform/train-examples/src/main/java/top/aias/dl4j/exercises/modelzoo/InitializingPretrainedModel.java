package top.aias.dl4j.exercises.modelzoo;

import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.zoo.PretrainedType;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 初始化一个 VGG-16 模型，并使用 ImageNet 权重
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class InitializingPretrainedModel {
    private static final Logger log = LoggerFactory.getLogger(InitializingPretrainedModel.class);

    public static void main(String[] args) throws Exception {

        // 初始化一个 VGG-16 模型，并使用 ImageNet 权重
        ZooModel zooModel = VGG16.builder().build();;
        Model net = zooModel.initPretrained(PretrainedType.IMAGENET);

        System.out.println("模型参数量："+ net.numParams());

    }
}
