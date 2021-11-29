package me.aias.ocr.inference;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import me.aias.ocr.model.LabelBean;
import me.aias.ocr.utils.*;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point2f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
public class PerspectiveRecogition {
    public static Map<String, String> recognize(NDManager manager, BufferedImage templateImg, RecognitionModel recognitionModel, Image image, List<LabelBean> anchorlabels, List<LabelBean> contentLabels, String fileRelativePath, String distance, boolean save) throws TranslateException {

        // 锚点识别区 - 计算中心点坐标，用于透视变换
        for (int i = 0; i < anchorlabels.size(); i++) {
            List<me.aias.ocr.model.Point> points = anchorlabels.get(i).getPoints();
            anchorlabels.get(i).setCenterPoint(PointUtils.getCenterPoint(points));
        }

        // 文本检测区
        List<LabelBean> detectedTexts = new ArrayList<>();
        DetectedObjects textDetections = recognitionModel.predict(image);
        List<DetectedObjects.DetectedObject> dt_boxes = textDetections.items();
        for (DetectedObjects.DetectedObject item : dt_boxes) {
            LabelBean labelBean = new LabelBean();
            List<me.aias.ocr.model.Point> points = new ArrayList<>();
            labelBean.setValue(item.getClassName());
            Rectangle rectangle = item.getBoundingBox().getBounds();

            Iterable<ai.djl.modality.cv.output.Point> pathIterator = rectangle.getPath();
            for (Iterator iter = pathIterator.iterator(); iter.hasNext(); ) {
                me.aias.ocr.model.Point point = new me.aias.ocr.model.Point();
                ai.djl.modality.cv.output.Point djlPoint = (ai.djl.modality.cv.output.Point) iter.next();
                point.setX((int) (djlPoint.getX() * image.getWidth()));
                point.setY((int) (djlPoint.getY() * image.getHeight()));
                points.add(point);
            }

            labelBean.setPoints(points);
            labelBean.setCenterPoint(PointUtils.getCenterPoint(points));
            detectedTexts.add(labelBean);
        }

        List<ai.djl.modality.cv.output.Point> srcPoints = new ArrayList<>();
        List<ai.djl.modality.cv.output.Point> dstPoints = new ArrayList<>();
        for (int i = 0; i < anchorlabels.size(); i++) {
            String anchorText = anchorlabels.get(i).getValue();
            for (int j = 0; j < detectedTexts.size(); j++) {
                String detectedText = detectedTexts.get(j).getValue();
                if (detectedText.equals(anchorText)) {
                    double x = anchorlabels.get(i).getCenterPoint().getX();
                    double y = anchorlabels.get(i).getCenterPoint().getY();
                    ai.djl.modality.cv.output.Point point = new ai.djl.modality.cv.output.Point(x, y);
                    srcPoints.add(point);

                    x = detectedTexts.get(j).getCenterPoint().getX();
                    y = detectedTexts.get(j).getCenterPoint().getY();
                    point = new ai.djl.modality.cv.output.Point(x, y);
                    dstPoints.add(point);
                }
            }
        }

        Point2f srcPoint2f = null;
        Point2f dstPoint2f = null;
        Mat warp_mat = null;

        if (dstPoints.size() == 3) {
            srcPoint2f = NDArrayUtils.toOpenCVPoint2f(srcPoints, 3);
            dstPoint2f = NDArrayUtils.toOpenCVPoint2f(dstPoints, 3);
            //3点仿射变换
            warp_mat = opencv_imgproc.getAffineTransform(srcPoint2f.position(0), dstPoint2f.position(0));
        } else if (dstPoints.size() >= 4) {
            srcPoint2f = NDArrayUtils.toOpenCVPoint2f(srcPoints, 4);
            dstPoint2f = NDArrayUtils.toOpenCVPoint2f(dstPoints, 4);
            //4点透视变换
            warp_mat = opencv_imgproc.getPerspectiveTransform(srcPoint2f.position(0), dstPoint2f.position(0));
        }


        if (dstPoints.size() >= 3 && save) {
            // 模板图片透视变换，跟上传图片对齐
            Mat mat = Java2DFrameUtils.toMat(templateImg);
            if (dstPoints.size() == 3) { //3点仿射变换
                mat = OpenCVUtils.affineTransform(mat, srcPoint2f, dstPoint2f);
                templateImg = Java2DFrameUtils.toBufferedImage(mat);
                DJLImageUtils.saveImage(templateImg, "affineTransform.png", fileRelativePath);
            } else if (dstPoints.size() >= 4) { //4点透视变换
                mat = OpenCVUtils.perspectiveTransform(mat, srcPoint2f, dstPoint2f);
                templateImg = Java2DFrameUtils.toBufferedImage(mat);
                DJLImageUtils.saveImage(templateImg, "perspectiveTransform.png", fileRelativePath);
            }
        }

        // 内容识别区 - 计算中心点坐标，用于距离计算
        for (int i = 0; i < contentLabels.size(); i++) {
            List<me.aias.ocr.model.Point> points = contentLabels.get(i).getPoints();

            //根据变换矩阵，对所有点坐标进行坐标变换
            if (dstPoints.size() >= 3) {
                points = PointUtils.transformPoints(manager, warp_mat, points);
            }
            ai.djl.modality.cv.output.Point point = PointUtils.getCenterPoint(points);

            if (dstPoints.size() < 3) { //无坐标变换，则将坐标归一化（坐标变为占整张图片的百分比）
                double x = point.getX() / templateImg.getHeight();
                double y = point.getY() / templateImg.getWidth();
                ai.djl.modality.cv.output.Point djlPoint = new ai.djl.modality.cv.output.Point(x, y);
                contentLabels.get(i).setCenterPoint(djlPoint);
            } else {
                contentLabels.get(i).setCenterPoint(point);
            }
            if (save) {
                Color c = new Color(0, 255, 0);
                DJLImageUtils.drawImageRect((BufferedImage) image.getWrappedImage(), (int) point.getX(), (int) point.getY(), 4, 4, c);
            }
        }

        // 文本检测区 - 中心点坐标根据透视变换矩阵进行坐标变换
        for (int i = 0; i < detectedTexts.size(); i++) {
            List<me.aias.ocr.model.Point> points = detectedTexts.get(i).getPoints();
            ai.djl.modality.cv.output.Point point = PointUtils.getCenterPoint(points);

            if (dstPoints.size() < 3) { //无坐标变换，则将坐标归一化（坐标变为占整张图片的百分比）
                double x = point.getX() / image.getHeight();
                double y = point.getY() / image.getWidth();
                ai.djl.modality.cv.output.Point djlPoint = new ai.djl.modality.cv.output.Point(x, y);
                detectedTexts.get(i).setCenterPoint(djlPoint);
            } else {
                detectedTexts.get(i).setCenterPoint(point);
            }

            if (save) {
                DJLImageUtils.drawImageRect((BufferedImage) image.getWrappedImage(), (int) point.getX(), (int) point.getY(), 4, 4);
            }
        }


        Map<String, String> hashMap = new ConcurrentHashMap<>();
        if (distance.equalsIgnoreCase("IOU")) {
            hashMap = DistanceUtils.iou(contentLabels, detectedTexts);
        } else {
            hashMap = DistanceUtils.l2Distance(contentLabels, detectedTexts);
        }

        if (save) {
            // 保存图
            DJLImageUtils.saveImage((BufferedImage) image.getWrappedImage(), "points_result.png", fileRelativePath);
        }

        return hashMap;
    }

}
