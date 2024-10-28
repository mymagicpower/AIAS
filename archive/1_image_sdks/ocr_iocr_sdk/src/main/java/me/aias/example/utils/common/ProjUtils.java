package me.aias.example.utils.common;

import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import ai.djl.util.Pair;
import me.aias.example.model.SingleRecognitionModel;
import me.aias.example.utils.opencv.OpenCVUtils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Calvin
 * @date Jun 18, 2023
 */
public class ProjUtils {

    /**
     * 获取图片对应2个4变形4对顶点
     *
     * @return
     */

    public static Pair<List<Point>, List<Point>> projPointsPair(NDManager manager, SingleRecognitionModel recognitionModel, Image templateImg, Image targetImg) throws TranslateException {
        // 模版文本检测 1
        // Text detection area
        List<LabelBean> templateTexts = new ArrayList<>();
        List<RotatedBox> templateTextsDet = recognitionModel.predict(manager, templateImg);
        for (RotatedBox rotatedBox : templateTextsDet) {
            LabelBean labelBean = new LabelBean();
            List<Point> points = new ArrayList<>();
            labelBean.setValue(rotatedBox.getText());
            labelBean.setField(rotatedBox.getText());

            float[] pointsArr = rotatedBox.getBox().toFloatArray();
            for (int i = 0; i < 4; i++) {
                Point point = new Point((int) pointsArr[2 * i], (int) pointsArr[2 * i + 1]);
                points.add(point);
            }

            labelBean.setPoints(points);
            labelBean.setCenterPoint(PointUtils.getCenterPoint(points));
            templateTexts.add(labelBean);
        }

//        // 转 BufferedImage 解决 Imgproc.putText 中文乱码问题
//        Mat wrappedImage = (Mat) templateImg.getWrappedImage();
//        BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
//        for (RotatedBox result : templateTextsDet) {
//            ImageUtils.drawImageRectWithText(bufferedImage, result.getBox(), result.getText());
//        }
//
//        Mat image2Mat = OpenCVUtils.image2Mat(bufferedImage);
//        templateImg = OpenCVImageFactory.getInstance().fromImage(image2Mat);
//        ImageUtils.saveImage(templateImg, "ocr_result.png", "build/output");

        // 目标文本检测 2
        // Text detection area
        List<LabelBean> targetTexts = new ArrayList<>();
        List<RotatedBox> textDetections = recognitionModel.predict(manager, targetImg);
        for (RotatedBox rotatedBox : textDetections) {
            LabelBean labelBean = new LabelBean();
            List<Point> points = new ArrayList<>();
            labelBean.setValue(rotatedBox.getText());

            float[] pointsArr = rotatedBox.getBox().toFloatArray();
            for (int i = 0; i < 4; i++) {
                Point point = new Point((int) pointsArr[2 * i], (int) pointsArr[2 * i + 1]);
                points.add(point);
            }

            labelBean.setPoints(points);
            labelBean.setCenterPoint(PointUtils.getCenterPoint(points));
            targetTexts.add(labelBean);
        }

        List<LabelBean> srcPoints = new ArrayList<>();
        List<LabelBean> dstPoints = new ArrayList<>();
        for (int i = 0; i < templateTexts.size(); i++) {
            String anchorText = templateTexts.get(i).getValue();
            for (int j = 0; j < targetTexts.size(); j++) {
                String detectedText = targetTexts.get(j).getValue();
                if (detectedText.equals(anchorText)) {
                    dstPoints.add(templateTexts.get(i));
                    srcPoints.add(targetTexts.get(j));
                }
            }
        }

        List<double[][]> srcPointsList = new ArrayList<>();
        List<double[][]> dstPointsList = new ArrayList<>();

        for (int i = 0; i < srcPoints.size(); i++) {
            for (int j = i + 1; j < srcPoints.size(); j++) {
                for (int k = j + 1; k < srcPoints.size(); k++) {
                    for (int l = k + 1; l < srcPoints.size(); l++) {
                        double[][] srcArr = new double[4][2];
                        srcArr[0][0] = srcPoints.get(i).getCenterPoint().getX();
                        srcArr[0][1] = srcPoints.get(i).getCenterPoint().getY();
                        srcArr[1][0] = srcPoints.get(j).getCenterPoint().getX();
                        srcArr[1][1] = srcPoints.get(j).getCenterPoint().getY();
                        srcArr[2][0] = srcPoints.get(k).getCenterPoint().getX();
                        srcArr[2][1] = srcPoints.get(k).getCenterPoint().getY();
                        srcArr[3][0] = srcPoints.get(l).getCenterPoint().getX();
                        srcArr[3][1] = srcPoints.get(l).getCenterPoint().getY();
                        srcPointsList.add(srcArr);

                        double[][] dstArr = new double[4][2];
                        dstArr[0][0] = dstPoints.get(i).getCenterPoint().getX();
                        dstArr[0][1] = dstPoints.get(i).getCenterPoint().getY();
                        dstArr[1][0] = dstPoints.get(j).getCenterPoint().getX();
                        dstArr[1][1] = dstPoints.get(j).getCenterPoint().getY();
                        dstArr[2][0] = dstPoints.get(k).getCenterPoint().getX();
                        dstArr[2][1] = dstPoints.get(k).getCenterPoint().getY();
                        dstArr[3][0] = dstPoints.get(l).getCenterPoint().getX();
                        dstArr[3][1] = dstPoints.get(l).getCenterPoint().getY();
                        dstPointsList.add(dstArr);
                    }
                }
            }
        }

        // 根据海伦公式（Heron's formula）计算4边形面积
        double maxArea = 0;
        int index = -1;
        for (int i = 0; i < dstPointsList.size(); i++) {
            double[][] dstArr = dstPointsList.get(i);
            double area = PointUtils.getQuadArea(manager, dstArr);
            if (area > maxArea) {
                maxArea = area;
                index = i;
            }

        }

        double[][] srcArr = srcPointsList.get(index);
        double[][] dstArr = dstPointsList.get(index);

        List<Point> srcQuadPoints = new ArrayList<>();
        List<Point> dstQuadPoints = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            double x = srcArr[i][0];
            double y = srcArr[i][1];
            Point point1 = new Point((int) x, (int) y);
            srcQuadPoints.add(point1);

            x = dstArr[i][0];
            y = dstArr[i][1];
            Point point2 = new Point((int) x, (int) y);
            dstQuadPoints.add(point2);
        }

        return new Pair<>(srcQuadPoints, dstQuadPoints);
    }

    /**
     * 透视变换
     *
     * @return
     */

    public static ProjItemBean projTransform(List<Point> srcQuadPoints, List<Point> dstQuadPoints, Image templateImg, Image targetImg) {
        Mat srcPoint2f = OpenCVUtils.toMat(srcQuadPoints);
        Mat dstPoint2f = OpenCVUtils.toMat(dstQuadPoints);

        // 透视变换矩阵
        // perspective transformation
        org.opencv.core.Mat warp_mat = Imgproc.getPerspectiveTransform(srcPoint2f, dstPoint2f);

        // 透视变换
        // perspective transformation
        Mat mat = OpenCVUtils.perspectiveTransform((Mat) targetImg.getWrappedImage(), (Mat) templateImg.getWrappedImage(), srcPoint2f, dstPoint2f);
        Image newImg = OpenCVImageFactory.getInstance().fromImage(mat);
        ProjItemBean projItemBean = new ProjItemBean();
        projItemBean.setImage(newImg);
        projItemBean.setWarpMat(warp_mat);

        return projItemBean;
    }

}
