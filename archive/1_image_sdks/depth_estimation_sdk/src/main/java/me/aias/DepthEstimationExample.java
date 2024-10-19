package me.aias;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.util.DepthEstimation;
import me.aias.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class DepthEstimationExample {

  private static final Logger logger = LoggerFactory.getLogger(DepthEstimationExample.class);

  private DepthEstimationExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    String imagePath = "src/test/resources/depth.jpeg";
    BufferedImage image = ImageIO.read(new File(imagePath));
    Image img = ImageFactory.getInstance().fromImage(image);

    DepthEstimation depthEstimation = new DepthEstimation();
    try (ZooModel model = ModelZoo.loadModel(depthEstimation.criteria());
        Predictor<Image, float[][]> predictor = model.newPredictor();
        NDManager manager = NDManager.newBaseManager(); ) {
      float[][] matrix = predictor.predict(img);
      NDArray array = manager.create(matrix);
      array = array.expandDims(2);
      array =
          NDImageUtils.resize(array, img.getWidth(), img.getHeight(), Image.Interpolation.BILINEAR);
      float[] arr = array.toFloatArray();
      float[][] mat = new float[img.getHeight()][img.getWidth()];
      for (int i = 0; i < img.getHeight(); i++) {
        for (int j = 0; j < img.getWidth(); j++) {
          mat[i][j] = arr[i * img.getWidth() + j];
        }
      }

      float min = array.min().toFloatArray()[0];
      float max = array.max().toFloatArray()[0];

      BufferedImage gray =
          new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

      for (int i = 0; i < img.getHeight(); i++) {
        for (int j = 0; j < img.getWidth(); j++) {
          int value = (int) (65535 * ((mat[i][j] - min) / (max - min)));
          gray.setRGB(j, i, value);
        }
      }

      Image grayImg = ImageFactory.getInstance().fromImage(gray);
      ImageUtils.saveImage(grayImg, "depth.png", "build/output");
    }
  }
}
