package me.aias.ocr.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.translate.TranslateException;
import me.aias.ocr.inference.RecognitionModel;
import me.aias.ocr.model.DataBean;
import me.aias.ocr.model.Point;
import me.aias.ocr.service.InferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
@Service
public class InferServiceImpl implements InferService {
    private Logger logger = LoggerFactory.getLogger(InferServiceImpl.class);

    @Autowired
    private RecognitionModel recognitionModel;

    public List<DataBean> getGeneralInfo(Image image) throws TranslateException {
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
    }


}
