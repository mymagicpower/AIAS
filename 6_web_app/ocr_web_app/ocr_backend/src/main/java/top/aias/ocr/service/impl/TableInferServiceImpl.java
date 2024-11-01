package top.aias.ocr.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.translate.TranslateException;
import top.aias.ocr.model.LayoutDetectionModel;
import top.aias.ocr.model.RecognitionModel;
import top.aias.ocr.model.TableRecognitionModel;
import top.aias.ocr.service.TableInferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格识别服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Service
public class TableInferServiceImpl implements TableInferService {
    private Logger logger = LoggerFactory.getLogger(TableInferServiceImpl.class);

    @Autowired
    private RecognitionModel recognitionModel;

    @Autowired
    private TableRecognitionModel tableDetectionModel;

    @Autowired
    private LayoutDetectionModel layoutDetectionModel;

    public String getTableHtml(Image image) throws TranslateException {
        DetectedObjects textDetections = recognitionModel.predict(image);
        String tableHtml = tableDetectionModel.getTableHtml(image, textDetections);
        return tableHtml;
    }

    public List<String> getTableHtmlList(Image image) throws TranslateException {
        List<String> tableHtmlList = new ArrayList<>();
        DetectedObjects layoutDetections = layoutDetectionModel.predict(image);

        List<DetectedObjects.DetectedObject> boxes = layoutDetections.items();
        for (int i = 0; i < boxes.size(); i++) {
            if (boxes.get(i).getClassName().equalsIgnoreCase("table")) {
                Image subImage = getSubImage(image, boxes.get(i).getBoundingBox());
                // 表格单元检测
                // Table cell detection
                DetectedObjects textDetections = recognitionModel.predict(subImage);
                String tableHtml = tableDetectionModel.getTableHtml(subImage, textDetections);
                tableHtmlList.add(tableHtml);
            }
        }
        return tableHtmlList;
    }

    private Image getSubImage(Image img, BoundingBox box) {
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
