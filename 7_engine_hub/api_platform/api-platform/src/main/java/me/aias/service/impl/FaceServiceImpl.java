package me.aias.service.impl;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.translate.TranslateException;
import me.aias.domain.DataBean;
import me.aias.domain.Point;
import me.aias.infer.face.FaceDetectionModel;
import me.aias.infer.face.FaceFeatureModel;
import me.aias.service.FaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Calvin
 * @date Jun 12, 2021
 */
@Service
public class FaceServiceImpl implements FaceService {
    private Logger logger = LoggerFactory.getLogger(FaceServiceImpl.class);
    @Autowired
    private FaceDetectionModel faceDetectionModel;

    @Autowired
    private FaceFeatureModel faceFeatureModel;

    public List<DataBean> detect(Image image) {
        try {
            List<DataBean> dataList = new ArrayList<>();
            DetectedObjects detectedObjects = faceDetectionModel.predict(image);
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
                    point.setX((int) (djlPoint.getX()));//* image.getWidth()
                    point.setY((int) (djlPoint.getY()));// * image.getHeight()
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

    public float[] feature(Image image) {
        try {
            float[] feature = faceFeatureModel.predict(image);
            return feature;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String compare(Image img1, Image img2) throws TranslateException {
        float[] feature1 = faceFeatureModel.predict(img1);
        float[] feature2 = faceFeatureModel.predict(img2);
        return Float.toString(calculSimilar(feature1, feature2));
    }

    private float calculSimilar(float[] feature1, float[] feature2) {
        float ret = 0.0f;
        float mod1 = 0.0f;
        float mod2 = 0.0f;
        int length = feature1.length;
        for (int i = 0; i < length; ++i) {
            ret += feature1[i] * feature2[i];
            mod1 += feature1[i] * feature1[i];
            mod2 += feature2[i] * feature2[i];
        }
        return (float) ((ret / Math.sqrt(mod1) / Math.sqrt(mod2) + 1) / 2.0f);
    }
}
