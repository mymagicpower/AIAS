package me.aias.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import me.aias.domain.DataBean;
import me.aias.domain.Point;
import me.aias.infer.ocr.RecognitionModel;
import me.aias.service.OcrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Calvin
 * @date Jun 12, 2021
 */
@Service
public class OcrServiceImpl implements OcrService {
    private Logger logger = LoggerFactory.getLogger(OcrServiceImpl.class);
    @Autowired
    private RecognitionModel recognitionModel;

    public List<DataBean> getGeneralInfo(Image image) {
        try {
            List<DataBean> dataList = new ArrayList<>();
            DetectedObjects detectedObjects = recognitionModel.predict(image);
            List<DetectedObjects.DetectedObject> list = detectedObjects.items();
            for (DetectedObjects.DetectedObject result : list) {
                DataBean dataBean = new DataBean();
                List<Point> points = new ArrayList<>();
                String className = result.getClassName();
                dataBean.setValue(className);
                BoundingBox box = result.getBoundingBox();
                Rectangle rectangle = box.getBounds();

                Iterable<ai.djl.modality.cv.output.Point> pathIterator = rectangle.getPath();
                for (Iterator iter = pathIterator.iterator(); iter.hasNext(); ) {
                    Point point = new Point();
                    ai.djl.modality.cv.output.Point djlPoint = (ai.djl.modality.cv.output.Point) iter.next();
                    point.setX((int) (djlPoint.getX() * image.getWidth()));
                    point.setY((int) (djlPoint.getY() * image.getHeight()));
                    points.add(point);
                }

                dataBean.setPoints(points);
                dataList.add(dataBean);
            }
            return dataList;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
