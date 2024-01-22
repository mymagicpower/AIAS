package top.aias.ocr.utils.layout;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
/**
 * 布局检测
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class LayoutDetection implements AutoCloseable {

  ZooModel model;
  Predictor<Image, DetectedObjects> predictor;
  private Device device;

  public LayoutDetection(Device device) throws ModelException, IOException {
    this.device = device;
    this.model = ModelZoo.loadModel(tableCriteria());
    this.predictor = model.newPredictor();
  }

  public DetectedObjects predict(Image img) throws TranslateException {
    return predictor.predict(img);
  }

  public void close(){
    this.model.close();
    this.predictor.close();
  }

  /**
   * picodet_lcnet_x1_0_fgd_layout_table
   * 表格数据集训练的版面分析模型，支持中英文文档表格区域的检测
   * @return
   */
  private Criteria<Image, DetectedObjects> tableCriteria() {

    Criteria<Image, DetectedObjects> criteria =
            Criteria.builder()
                    .optEngine("OnnxRuntime")
                    .optModelName("inference")
                    .setTypes(Image.class, DetectedObjects.class)
                    .optModelPath(Paths.get("models/picodet_lcnet_x1_0_fgd_layout_table_infer_onnx.zip"))
                    .optDevice(device)
                    .optTranslator(new LayoutDetTranslator())
                    .optProgress(new ProgressBar())
                    .build();

    return criteria;
  }

}
