package top.aias.iocr.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import ai.djl.util.Pair;
import top.aias.iocr.bean.LabelBean;
import top.aias.iocr.bean.Point;
import top.aias.iocr.bean.ProjBean;
import top.aias.iocr.model.RecognitionModel;
import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

/**
 * 文本转正，根据四边形顶点的距离，多轮透视变换
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class PerspectiveTransform {
    /**
     *  文本检测,锚点识别区,内容识别区 - 计算中心点坐标，用于透视变换及距离计算
     *
     * @param manager
     * @param templateImg
     * @param recognitionModel
     * @param image
     * @param anchorlabels
     * @param contentLabels
     * @param fileRelativePath
     * @param distanceType
     * @param maxNum
     * @param disThreshold
     * @param save
     * @return
     * @throws TranslateException
     */
    public static Map<String, String> recognize(NDManager manager, Image templateImg, RecognitionModel recognitionModel, Image image, List<LabelBean> anchorlabels, List<LabelBean> contentLabels, String fileRelativePath, String distanceType, int maxNum, double disThreshold, boolean save) throws TranslateException {
        // 锚点识别区 - 计算中心点坐标，用于透视变换及距离计算
        // Anchor recognition area - calculating the center point coordinates for perspective transformation
        for (int i = 0; i < anchorlabels.size(); i++) {
            List<Point> points = anchorlabels.get(i).getPoints();
            anchorlabels.get(i).setCenterPoint(PointUtils.getCenterPoint(points));
        }

        // 文本转正，根据四边形顶点的距离，多轮透视变换，直到符合最小距离要求，或者达到最大次数上限
        Image finalImg = finalImg(manager, recognitionModel, templateImg, image, anchorlabels, fileRelativePath, maxNum, disThreshold, save);

        // 转 BufferedImage 解决 Imgproc.putText 中文乱码问题
        org.opencv.core.Mat wrappedImage = (Mat) finalImg.getWrappedImage();
        BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
        Color c = new Color(0, 255, 0);

        // 文本检测 - 计算中心点坐标，用于距离计算
        // Text detection area
        List<LabelBean> detectedTexts = new ArrayList<>();
        DetectedObjects textDetections = recognitionModel.predict(finalImg);
        List<DetectedObjects.DetectedObject> dt_boxes = textDetections.items();
        for (DetectedObjects.DetectedObject item : dt_boxes) {
            LabelBean labelBean = new LabelBean();
            List<Point> points = new ArrayList<>();
            labelBean.setValue(item.getClassName());
            Rectangle rectangle = item.getBoundingBox().getBounds();

            Iterable<ai.djl.modality.cv.output.Point> pathIterator = rectangle.getPath();
            for (Iterator iter = pathIterator.iterator(); iter.hasNext(); ) {
                Point point = new Point();
                ai.djl.modality.cv.output.Point djlPoint = (ai.djl.modality.cv.output.Point) iter.next();
                point.setX((int) (djlPoint.getX() * finalImg.getWidth()));
                point.setY((int) (djlPoint.getY() * finalImg.getHeight()));
                points.add(point);
            }

            labelBean.setPoints(points);
            ai.djl.modality.cv.output.Point point = PointUtils.getCenterPoint(points);
            labelBean.setCenterPoint(point);
            detectedTexts.add(labelBean);

            if (save) {
                DJLImageUtils.drawImageRect(bufferedImage, (int) point.getX(), (int) point.getY(), 4, 4);
            }
        }

        // 内容识别区 - 计算中心点坐标，用于距离计算
        // Content recognition area - calculating the center point coordinates for distance calculation
        for (int i = 0; i < contentLabels.size(); i++) {
            List<Point> points = contentLabels.get(i).getPoints();
            ai.djl.modality.cv.output.Point point = PointUtils.getCenterPoint(points);
            contentLabels.get(i).setCenterPoint(point);

            if (save) {
                DJLImageUtils.drawImageRect(bufferedImage, (int) point.getX(), (int) point.getY(), 4, 4, c);
            }
        }

        if (save) {
            ImageUtils.saveImage(bufferedImage, "center_points_result.png", fileRelativePath);
        }

        Map<String, String> hashMap;
        if (distanceType.equalsIgnoreCase("IoU")) {
            hashMap = DistanceUtils.iou(contentLabels, detectedTexts);
        } else {
            hashMap = DistanceUtils.l2Distance(contentLabels, detectedTexts);
        }

        return hashMap;
    }

    /**
     * 多轮透视变换，获得最后符合距离阈值的图片
     *
     * @param manager
     * @param recognitionModel
     * @param templateImg
     * @param targetImg
     * @param anchorlabels
     * @param fileRelativePath
     * @param maxNum
     * @param disThreshold
     * @param save
     * @return
     * @throws TranslateException
     */
    public static Image finalImg(NDManager manager, RecognitionModel recognitionModel, Image templateImg, Image targetImg, List<LabelBean> anchorlabels, String fileRelativePath, int maxNum, double disThreshold, boolean save) throws TranslateException {
        List<ProjBean> projList = new ArrayList<>();
        Image origTargetImg = targetImg.duplicate();

        for (int num = 0; num < maxNum; num++) {
            Pair pair = ProjUtils.projPointsPair(manager, recognitionModel, anchorlabels, targetImg);
            List<Point> srcQuadPoints = (List<Point>) pair.getKey();
            List<Point> dstQuadPoints = (List<Point>) pair.getValue();

            // 计算距离
            double[] distances = new double[4];
            for (int i = 0; i < 4; i++) {
                distances[i] = PointUtils.distance(srcQuadPoints.get(i), dstQuadPoints.get(i));
            }

            System.out.println(Arrays.toString(distances));

            boolean pass = true;
            for (int i = 0; i < 4; i++) {
                if (distances[i] > disThreshold) {
                    pass = false;
                    break;
                }
            }

            if (!pass) {
                ProjBean projItemBean = ProjUtils.projTransform(srcQuadPoints, dstQuadPoints, templateImg, targetImg);
                targetImg = projItemBean.getImage();
                projList.add(projItemBean);

                if (save) {
                    ImageUtils.saveImage(projItemBean.getImage(), "perspectiveTransform_" + num + ".png", fileRelativePath);
                }
            } else {
                break;
            }
        }
        if (projList.size() > 0) {
            org.opencv.core.Mat warp_mat = projList.get(projList.size() - 1).getWarpMat();
            if (projList.size() > 1) {
                for (int i = projList.size() - 2; i >= 0; i--) {
                    org.opencv.core.Mat matItem = projList.get(i).getWarpMat();
                    warp_mat = warp_mat.matMul(matItem);
                }
            }
            org.opencv.core.Mat mat = OpenCVUtils.warpPerspective((Mat) origTargetImg.getWrappedImage(), (Mat) templateImg.getWrappedImage(), warp_mat);
            Image finalImg = OpenCVImageFactory.getInstance().fromImage(mat);

            if (save) {
                ImageUtils.saveImage(finalImg, "perspectiveTransform_final.png", fileRelativePath);
            }

            return finalImg;
        }else {
            return targetImg;
        }
    }
}
