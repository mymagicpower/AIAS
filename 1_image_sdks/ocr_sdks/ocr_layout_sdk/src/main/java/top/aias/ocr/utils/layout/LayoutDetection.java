package top.aias.ocr.utils.layout;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
/**
 * 布局检测
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class LayoutDetection {

  private static final Logger logger = LoggerFactory.getLogger(LayoutDetection.class);

  public LayoutDetection() {}

  /**
   * picodet_lcnet_x1_0_fgd_layout_cdla
   * CDLA数据集训练的中文版面分析模型，可以划分为表格、图片、图片标题、表格、表格标题、页眉、脚本、引用、公式10类区域
   * @return
   */
  public Criteria<Image, DetectedObjects> cnCriteria() {
    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("OnnxRuntime")
            .optModelName("inference")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelPath(Paths.get("models/picodet_lcnet_x1_0_fgd_layout_cdla_infer_onnx.zip"))
            .optTranslator(new LayoutDetectionTranslator())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }

  /**
   * picodet_lcnet_x1_0_fgd_layout
   * 基于PicoDet LCNet_x1_0和FGD蒸馏在PubLayNet 数据集训练的英文版面分析模型，可以划分文字、标题、表格、图片以及列表5类区域
   * @return
   */
  public Criteria<Image, DetectedObjects> enCriteria() {

    Criteria<Image, DetectedObjects> criteria =
            Criteria.builder()
                    .optEngine("OnnxRuntime")
                    .optModelName("inference")
                    .setTypes(Image.class, DetectedObjects.class)
                    .optModelPath(Paths.get("models/picodet_lcnet_x1_0_fgd_layout_infer_onnx.zip"))
                    .optTranslator(new LayoutDetectionTranslator())
                    .optProgress(new ProgressBar())
                    .build();

    return criteria;
  }

  /**
   * picodet_lcnet_x1_0_fgd_layout_table
   * 表格数据集训练的版面分析模型，支持中英文文档表格区域的检测
   * @return
   */
  public Criteria<Image, DetectedObjects> tableCriteria() {

    Criteria<Image, DetectedObjects> criteria =
            Criteria.builder()
                    .optEngine("OnnxRuntime")
                    .optModelName("inference")
                    .setTypes(Image.class, DetectedObjects.class)
                    .optModelPath(Paths.get("models/picodet_lcnet_x1_0_fgd_layout_table_infer_onnx.zip"))
                    .optTranslator(new LayoutDetectionTranslator())
                    .optProgress(new ProgressBar())
                    .build();

    return criteria;
  }

}
