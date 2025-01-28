package top.aias.ocr;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.translate.TranslateException;
import top.aias.ocr.utils.common.ImageUtils;
import top.aias.ocr.utils.layout.LayoutDetection;
import top.aias.ocr.utils.recognition.OcrV4AlignedRecognition;
import top.aias.ocr.utils.table.ConvertHtml2Excel;
import top.aias.ocr.utils.table.TableResult;
import top.aias.ocr.utils.table.V4ENTableRecognition;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.ocr.utils.table.V4TableRecognition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 多表格识别.
 * Table Recognition
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class MultiTableRecExample {

    private static final Logger logger = LoggerFactory.getLogger(MultiTableRecExample.class);

    private MultiTableRecExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/tables.jpeg");
        Image image = ImageFactory.getInstance().fromFile(imageFile);
        int height = image.getHeight();
        int width = image.getWidth();

        try (V4TableRecognition tableDetector = new V4TableRecognition(Device.cpu());
             OcrV4AlignedRecognition recognizer = new OcrV4AlignedRecognition(Device.cpu());
             // 表单布局检测
             // Form layout detection
             LayoutDetection layoutDetector = new LayoutDetection(Device.cpu());) {

            DetectedObjects layoutDetections = layoutDetector.predict(image);

            List<DetectedObjects.DetectedObject> boxes = layoutDetections.items();
            for (int i = 0; i < boxes.size(); i++) {
                if (boxes.get(i).getClassName().equalsIgnoreCase("table")) {
                    Image subImage = getSubImage(image, boxes.get(i).getBoundingBox());
                    // 表格单元检测
                    // Table cell detection
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
                    ImageUtils.saveBoundingBoxImage(subImage, tableDetections, "table_cells_detected_" + i + ".png", "build/output");

                    DetectedObjects textDetections = recognizer.predict(subImage);
                    ImageUtils.saveBoundingBoxImage(subImage, textDetections, "table_texts_detected_" + i + ".png", "build/output");

                    List<String> cell_contents =
                            tableDetector.cellContents(textDetections, cells, width, height);
                    DetectedObjects cellsDetections = new DetectedObjects(cell_contents, probs, cells);
                    ImageUtils.saveBoundingBoxImage(subImage, cellsDetections, "table_cells_content_" + i + ".png", "build/output");

                    String html = tableDetector.get_pred_html(pred_structures, cell_contents);
                    System.out.println(html);

                    // 创建一个Excel文件
                    // Create an Excel file
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
