package top.aias.iocr.utils;

import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import ai.djl.util.Pair;
import top.aias.iocr.bean.LabelBean;
import top.aias.iocr.bean.Point;
import top.aias.iocr.bean.ProjBean;
import top.aias.iocr.bean.RotatedBox;
import top.aias.iocr.model.RecognitionModel;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * 透视变换工具类
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class ProjUtils {

    /**
     * 获取图片对应2个4变形4对顶点
     *
     * @return
     */

    public static Pair<List<Point>, List<Point>> projPointsPair(NDManager manager, RecognitionModel recognitionModel, List<LabelBean> anchorlabels, Image targetImg) throws TranslateException {
        // 目标文本检测
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
        for (int i = 0; i < anchorlabels.size(); i++) {
            String anchorText = anchorlabels.get(i).getValue();
            for (int j = 0; j < targetTexts.size(); j++) {
                String detectedText = targetTexts.get(j).getValue();
                if (detectedText.equals(anchorText)) { // 根据实际情况，也可以改成 contains 之类的。
                    dstPoints.add(anchorlabels.get(i));
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

    public static ProjBean projTransform(List<Point> srcQuadPoints, List<Point> dstQuadPoints, Image templateImg, Image targetImg) {
        Mat srcPoint2f = OpenCVUtils.toMat(srcQuadPoints);
        Mat dstPoint2f = OpenCVUtils.toMat(dstQuadPoints);

        // 透视变换矩阵
        // perspective transformation
        Mat warp_mat = Imgproc.getPerspectiveTransform(srcPoint2f, dstPoint2f);

        // 透视变换
        // perspective transformation
        Mat mat = OpenCVUtils.perspectiveTransform((Mat) targetImg.getWrappedImage(), (Mat) templateImg.getWrappedImage(), srcPoint2f, dstPoint2f);
        Image newImg = OpenCVImageFactory.getInstance().fromImage(mat);
        ProjBean projItemBean = new ProjBean();
        projItemBean.setImage(newImg);
        projItemBean.setWarpMat(warp_mat);

        return projItemBean;
    }

}
