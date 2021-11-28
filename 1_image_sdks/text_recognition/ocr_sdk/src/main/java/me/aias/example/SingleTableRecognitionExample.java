package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
public final class SingleTableRecognitionExample {

  private static final Logger logger = LoggerFactory.getLogger(SingleTableRecognitionExample.class);

  private SingleTableRecognitionExample() {}

  public static void main(String[] args) throws IOException, ModelException, TranslateException {
    Path imageFile = Paths.get("src/test/resources/table.jpg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);
    int height = image.getHeight();
    int width = image.getWidth();

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
         BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("build/output/table.html"))) {

      // 表格单元检测
      TableResult result = tableDetector.predict(image);

      List<BoundingBox> cells = result.getBoxes();
      List<String> pred_structures = result.getStructure_str_list();
      List<String> names = new ArrayList<>();
      List<Double> probs = new ArrayList<>();
      for (int i = 0; i < cells.size(); i++) {
        names.add("" + i);
        probs.add(-1.0);
      }

      DetectedObjects tableDetections = new DetectedObjects(names, probs, cells);
      ImageUtils.saveBoundingBoxImage(
          image, tableDetections, "table_cells_detected.png", "build/output");

      DetectedObjects textDetections = recognition.predict(image, detector, recognizer);
      ImageUtils.saveBoundingBoxImage(
          image, textDetections, "table_texts_detected.png", "build/output");

      List<String> cell_contents =
          tableDetection.cellContents(textDetections, cells, width, height);
      DetectedObjects cellsDetections = new DetectedObjects(cell_contents, probs, cells);
      ImageUtils.saveBoundingBoxImage(
          image, cellsDetections, "table_cells_content.png", "build/output");

      String html = tableDetection.get_pred_html(pred_structures, cell_contents);
      System.out.println(html);
      bufferedWriter.write(html);
      
      // 创建一个Excel文件
      html = html.replace("<html><body>", "");
      html = html.replace("</body></html>", "");
      HSSFWorkbook workbook = ConvertHtml2Excel.table2Excel(html);
      workbook.write(new File("build/output/table.xls"));
    }
  }
}
