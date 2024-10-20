package top.aias.tools.face;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 人脸特征
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class FaceFeature implements AutoCloseable {
  ZooModel model;
  Predictor<Image, float[]> predictor;
  private Device device;

  public FaceFeature(Device device) throws ModelException, IOException {
    this.device = device;
    this.model = ModelZoo.loadModel(criteria());
    this.predictor = model.newPredictor();
  }

  public float[] predict(Image img) throws TranslateException {
    return predictor.predict(img);
  }

  public void close(){
    this.model.close();
    this.predictor.close();
  }

  private Criteria<Image, float[]> criteria() {
    Criteria<Image, float[]> criteria =
        Criteria.builder()
            .setTypes(Image.class, float[].class)
            .optModelPath(Paths.get("models/face_feature.onnx"))
            .optModelName("face_feature")
            .optTranslator(new FaceFeatureTranslator())
            .optProgress(new ProgressBar())
            .optEngine("OnnxRuntime")
            .optDevice(device)
            .build();

    return criteria;
  }

  public float calculSimilar(float[] feature1, float[] feature2) {
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
