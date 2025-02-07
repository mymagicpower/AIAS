package top.aias.dl4j.examples.classification;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.TransferLearning;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.dl4j.examples.classification.models.ResNet50Model;

import java.io.File;
/**
 * 模型架构修改，保存
 *
 * @author Calvin
 * Mail: 179209347@qq.com
 */
public class ModelModify {
    private static final Logger log = LoggerFactory.getLogger(ModelModify.class);

    static {
        nu.pattern.OpenCV.loadLocally();
        if (System.getProperty("apple.awt.UIElement") == null) {
            // disables coffee cup image showing up on macOS
            System.setProperty("apple.awt.UIElement", "true");
        }
    }

    public static void main(String[] args) throws Exception {

        String modelFilePath = new File(".").getAbsolutePath() + "/generated-models/";
        String modelFileName = modelFilePath + "ResNet50_Animals.zip";

        // 加载图片
        String imageFile1 = "src/main/resources/Brown_bear.jpg";
        Mat image1 = Imgcodecs.imread(imageFile1);
        String imageFile2 = "src/main/resources/Polar_Bear.jpg";
        Mat image2 = Imgcodecs.imread(imageFile2);
        String imageFile3 = "src/main/resources/mask.jpg";
        Mat image3 = Imgcodecs.imread(imageFile3);

        System.out.println("Loading model...");
        ResNet50Model resNet50Model = new ResNet50Model();
        // 1. 加载模型
        ComputationGraph model = resNet50Model.loadModel(new File(modelFileName));

        // 2. 更新模型架构
        ComputationGraph ResNet50Embedding = new TransferLearning.GraphBuilder(model)
                .removeVertexAndConnections("fc1000") // 移除原始输出层
                .setOutputs("flatten_1") // 设置 flatten_1 为新的输出层 2048
                .build();

        // 3. 保存模型
        ModelSerializer.writeModel(ResNet50Embedding, modelFilePath + "ResNet50Embedding.zip", true);

        // 4. 测试模型
        NativeImageLoader loader = new NativeImageLoader(resNet50Model.getWidth(), resNet50Model.getHeight(), resNet50Model.getNChannels());
        INDArray ds1 = loader.asMatrix(image1);
        INDArray ds2 = loader.asMatrix(image2);
        INDArray ds3 = loader.asMatrix(image3);

        // 获取模型预测结果
        INDArray embedding1 = ResNet50Embedding.outputSingle(ds1);
        INDArray embedding2 = ResNet50Embedding.outputSingle(ds2);
        INDArray embedding3 = ResNet50Embedding.outputSingle(ds3);

        // 转换为 float[]
        float[] feature1 = embedding1.toFloatVector();
        float[] feature2 = embedding2.toFloatVector();
        float[] feature3 = embedding3.toFloatVector();

        // 输出结果
//        System.out.println(Arrays.toString(floatArray1));  // 打印展平后的 float[]
        System.out.println(calculSimilar(feature1, feature2));
        System.out.println(calculSimilar(feature1, feature3));
    }

    public static float calculSimilar(float[] feature1, float[] feature2) {
        float ret = 0.0f;
        float mod1 = 0.0f;
        float mod2 = 0.0f;
        int length = feature1.length;
        for (int i = 0; i < length; ++i) {
            ret += feature1[i] * feature2[i];
            mod1 += feature1[i] * feature1[i];
            mod2 += feature2[i] * feature2[i];
        }
        return (float) ((ret / Math.sqrt(mod1) / Math.sqrt(mod2) + 1) / 2.0f);
    }
}