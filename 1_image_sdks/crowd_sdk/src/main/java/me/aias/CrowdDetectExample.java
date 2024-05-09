package me.aias;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.util.CrowdDetect;
import me.aias.util.ImagePlotter;
import me.aias.util.NDArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 人群密度检测
 * density map detection
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website http://www.aias.top/
 */
public final class CrowdDetectExample {

  private static final Logger logger = LoggerFactory.getLogger(CrowdDetectExample.class);

  private CrowdDetectExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/crowd1.jpg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    Criteria<Image, NDList> criteria = new CrowdDetect().criteria();

    try (ZooModel model = ModelZoo.loadModel(criteria);
        Predictor<Image, NDList> predictor = model.newPredictor()) {
      NDList list = predictor.predict(image);

      //quantity 为人数
      float q = list.get(1).toFloatArray()[0];
      int quantity = (int)(Math.abs(q) + 0.5);
      logger.info("人数 quantity: {}", quantity);
      
      // density 为密度图
      NDArray densityArray = list.get(0);
      densityArray = densityArray.squeeze(0);
      densityArray = densityArray.squeeze(0);
      densityArray = densityArray.transpose();

      logger.info("密度图 density: {}", densityArray.toDebugString(1000000000, 1000, 1000, 1000));

      float[][] densityArr = NDArrayUtils.floatNDArrayToArray(densityArray);
      ImagePlotter.plotDensity(densityArr);
    }
  }
}
