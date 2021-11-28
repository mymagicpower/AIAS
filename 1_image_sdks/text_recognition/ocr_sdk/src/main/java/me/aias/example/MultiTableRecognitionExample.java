package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
                                   
/**
 * 表格识别.
 *
 * @author Calvin
 * @date 2021-10-05
 * @email 179209347@qq.com
 */
public final class MultiTableRecognitionExample {

  private static final Logger logger = LoggerFactory.getLogger(MultiTableRecognitionExample.class);

  private MultiTableRecognitionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/tables.jpeg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    int height = image.getHeight();
    int width = image.getWidth();

    // 表单布局检测
    LayoutDetection layoutDetection = new LayoutDetection();
    // 表格单元检测
    TableDetection tableDetection = new TableDetection();
    // 文本框检测
    MobileOcrRecognition recognition = new MobileOcrRecognition();

    try (ZooModel tableModel = ModelZoo.loadModel(tableDetection.criteria());
         Predictor<Image, TableResult> tableDetector = tableModel.newPredictor();
         ZooModel detectionModel = ModelZoo.loadModel(recognition.detectCriteria());
         Predictor<Image, DetectedObjects> detector = detectionModel.newPredictor();
         ZooModel recognitionModel = ModelZoo.loadModel(recognition.recognizeCriteria());
         Predictor<Image, String> recognizer = recognitionModel.newPredictor();
         ZooModel layoutModel = ModelZoo.loadModel(layoutDetection.criteria());
         Predictor<Image, DetectedObjects> layoutDetector = layoutModel.newPredictor()) {

      DetectedObjects layoutDetections = layoutDetector.predict(image);

      List<DetectedObjects.DetectedObject> boxes = layoutDetections.items();
      for (int i = 0; i < boxes.size(); i++) {
        // TODO 模型需优化, 页面出现多个表不准，但是当前可以用于表单中一个表格自动检测识别
        if (boxes.get(i).getClassName().equals("Table")) {
          Image subImage = getSubImage(image, boxes.get(i).getBoundingBox());
          // 表格单元检测
          TableResult result = tableDetector.predict(subImage);

          List<BoundingBox> cells = result.getBoxes();
          List<String> pred_structures = result.getStructure_str_list();
          List<String> names = new ArrayList<>();
          List<Double> probs = new ArrayList<>();
          for (int j = 0; j < cells.size(); j++) {
            names.add("" + j);
            probs.add(-1.0);
          }

          DetectedObjects tableDetections = new DetectedObjects(names, probs, cells);
          ImageUtils.saveBoundingBoxImage(
              subImage, tableDetections, "table_cells_detected_" + i + ".png", "build/output");

          DetectedObjects textDetections = recognition.predict(subImage, detector, recognizer);
          ImageUtils.saveBoundingBoxImage(
              subImage, textDetections, "table_texts_detected_" + i + ".png", "build/output");

          List<String> cell_contents =
              tableDetection.cellContents(textDetections, cells, width, height);
          DetectedObjects cellsDetections = new DetectedObjects(cell_contents, probs, cells);
          ImageUtils.saveBoundingBoxImage(
              subImage, cellsDetections, "table_cells_content_" + i + ".png", "build/output");

          String html = tableDetection.get_pred_html(pred_structures, cell_contents);
          System.out.println(html);

          // 创建一个Excel文件
          html = html.replace("<html><body>", "");
          html = html.replace("</body></html>", "");
          HSSFWorkbook workbook = ConvertHtml2Excel.table2Excel(html);
          workbook.write(new File("build/output/table_" + i + ".xls"));
        }
      }
    }
  }

  private static Image getSubImage(Image img, BoundingBox box) {
    Rectangle rect = box.getBounds();
    int width = img.getWidth();
    int height = img.getHeight();
    int[] recovered = {
      (int) (rect.getX() * width),
      (int) (rect.getY() * height),
      (int) (rect.getWidth() * width),
      (int) (rect.getHeight() * height)
    };
    return img.getSubImage(recovered[0], recovered[1], recovered[2], recovered[3]);
  }
}
