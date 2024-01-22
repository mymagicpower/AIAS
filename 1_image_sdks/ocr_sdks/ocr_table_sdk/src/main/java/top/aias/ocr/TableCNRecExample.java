package top.aias.ocr;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import top.aias.ocr.utils.common.ImageUtils;
import top.aias.ocr.utils.recognition.OcrV4AlignedRecognition;
import top.aias.ocr.utils.table.ConvertHtml2Excel;
import top.aias.ocr.utils.table.TableResult;
import top.aias.ocr.utils.table.V4TableRecognition;
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
 * Single Table Recognition
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TableCNRecExample {

    private static final Logger logger = LoggerFactory.getLogger(TableCNRecExample.class);

    private TableCNRecExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/img.png");
        Image image = ImageFactory.getInstance().fromFile(imageFile);
        int height = image.getHeight();
        int width = image.getWidth();
        try (
                V4TableRecognition tableDetector = new V4TableRecognition(Device.cpu());
                OcrV4AlignedRecognition recognizer = new OcrV4AlignedRecognition(Device.cpu());
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
            ImageUtils.saveBoundingBoxImage(image.duplicate(), tableDetections, "table_cells_detected.png", "build/output");

            DetectedObjects textDetections = recognizer.predict(image);
            ImageUtils.saveBoundingBoxImage(image.duplicate(), textDetections, "table_texts_detected.png", "build/output");

            List<String> cell_contents = tableDetector.cellContents(textDetections, cells, width, height);
            DetectedObjects cellsDetections = new DetectedObjects(cell_contents, probs, cells);
            ImageUtils.saveBoundingBoxImage(image, cellsDetections, "table_cells_content.png", "build/output");

            String html = tableDetector.get_pred_html(pred_structures, cell_contents);
            System.out.println(html);
            bufferedWriter.write(html);

            // 创建一个Excel文件
            // Create an Excel file
            html = html.replace("<html><body>", "");
            html = html.replace("</body></html>", "");
            HSSFWorkbook workbook = ConvertHtml2Excel.table2Excel(html);
            workbook.write(new File("build/output/table.xls"));
        }
    }
}
