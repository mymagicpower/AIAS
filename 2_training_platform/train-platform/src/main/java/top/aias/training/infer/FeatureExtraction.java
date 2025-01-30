package top.aias.training.infer;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.TransferLearning;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.training.training.models.ResNet50Model;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 特征提取
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class FeatureExtraction {

    private static final Logger logger = LoggerFactory.getLogger(FeatureExtraction.class);
    public static int imgSize = 224;

    private FeatureExtraction() {
    }

    public static void main(String[] args) throws IOException{
        String newModelPath = "/Users/calvin/Documents/build/training/modelv2/";
        Image img = ImageFactory.getInstance().fromUrl("/Users/calvin/Documents/Data_Faces_0/5.jpg");

        float[] feature = predict(newModelPath, img);
        System.out.println(feature.length);
        if (feature != null) {
            logger.info(Arrays.toString(feature));
        }

    }

    public static float[] predict(String newModelPath, Image img) throws IOException{

        ResNet50Model resNet50Model = new ResNet50Model();
        // 1. 加载模型
        ComputationGraph model = resNet50Model.loadModel(new File(newModelPath));

        // 2. 更新模型架构
        ComputationGraph ResNet50Embedding = new TransferLearning.GraphBuilder(model)
                .removeVertexAndConnections("fc1000") // 移除原始输出层
                .setOutputs("flatten_1") // 设置 flatten_1 为新的输出层 2048
                .build();

        // 2. 测试模型
        NativeImageLoader loader = new NativeImageLoader(resNet50Model.getWidth(), resNet50Model.getHeight(), resNet50Model.getNChannels());

        INDArray ds = loader.asMatrix((Mat)img.getWrappedImage());
        // 获取模型预测结果
        INDArray embedding = ResNet50Embedding.outputSingle(ds);
        // 转换为 float[]
        float[] feature = embedding.toFloatVector();
        return feature;
    }

}
