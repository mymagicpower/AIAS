package top.aias.training.infer;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.training.training.models.ClassPrediction;
import top.aias.training.training.models.ResNet50Model;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 图像分类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class ImageClassification {

    private static final Logger logger = LoggerFactory.getLogger(ImageClassification.class);
    public static int imgSize = 224;

    private ImageClassification() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        String newModelPath = "/Users/calvin/Documents/build/training/modelv2/";
        String imageUrl = "https://car2.autoimg.cn/cardfs/product/g26/M0A/5D/33/1024x0_1_q95_autohomecar__ChsEe15-6YKAULPgAAYVp-IEjZA085.jpg";
        Image img = ImageFactory.getInstance().fromUrl(imageUrl);

        ClassPrediction classifications = predict(newModelPath, img);
        logger.info("Predict result: {}", classifications);
    }

    public static ClassPrediction predict(String newModelPath, Image img) throws IOException {

        ResNet50Model model = new ResNet50Model();
        // 标签：模型输出分类
        List<String> labels = Arrays.asList(new String[]{"bear", "deer", "duck", "trutle"});
        model.setLabels(labels);

        if (new File(newModelPath).exists()) {
            logger.info("加载模型...");
            model.loadModel(new File(newModelPath));
        } else {
            logger.info("模型没有找到.");
        }

        long st = System.currentTimeMillis();
        ClassPrediction classPrediction = classify(model, (Mat) img.getWrappedImage(), labels);
        double per = (System.currentTimeMillis() - st) / 1000.0;

        return classPrediction;

    }

    public static ClassPrediction classify(ResNet50Model model, Mat image, List<String> labels) {
        NativeImageLoader loader = new NativeImageLoader(model.getWidth(), model.getHeight(), model.getNChannels());
        INDArray ds = null;
        try {
            ds = loader.asMatrix(image);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }

        // 获取模型预测结果
        INDArray predictions = model.getComputationGraph().outputSingle(ds);
        int predictedClassIndex = predictions.argMax(1).getInt(0);
        String modelPrediction = labels.get(predictedClassIndex);
        float topXProb = predictions.getFloat(0, predictedClassIndex);
        return new ClassPrediction(modelPrediction, topXProb);
    }
}
