package me.aias.example;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.example.model.SingleRecognitionModel;
import me.aias.example.utils.common.*;
import me.aias.example.utils.opencv.OpenCVUtils;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class OcrV3RecExample {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3RecExample.class);

    private OcrV3RecExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/template.png");
        Image templateImg = OpenCVImageFactory.getInstance().fromFile(imageFile);

        imageFile = Paths.get("src/test/resources/perspectiveTransform2.png");
        Image targetImg = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (SingleRecognitionModel recognitionModel = new SingleRecognitionModel();
             NDManager manager = NDManager.newBaseManager(Device.cpu(), "PyTorch")) {
            recognitionModel.init("models/ch_PP-OCRv3_det_infer_onnx.zip", "models/ch_PP-OCRv3_rec_infer_onnx.zip");

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

            // 转 BufferedImage 解决 Imgproc.putText 中文乱码问题
            Mat wrappedImage = (Mat) templateImg.getWrappedImage();
            BufferedImage bufferedImage = OpenCVUtils.mat2Image(wrappedImage);
            for (RotatedBox result : templateTextsDet) {
                ImageUtils.drawImageRectWithText(bufferedImage, result.getBox(), result.getText());
            }

            Mat image2Mat = OpenCVUtils.image2Mat(bufferedImage);
            templateImg = OpenCVImageFactory.getInstance().fromImage(image2Mat);
            ImageUtils.saveImage(templateImg, "ocr_result.png", "build/output");

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
//            // 转 BufferedImage 解决 Imgproc.putText 中文乱码问题
//            Mat matImage = (Mat) targetImg.getWrappedImage();
//            BufferedImage buffImage = OpenCVUtils.mat2Image(matImage);
//            for (int i = 0; i < 4; i++) {
//                DJLImageUtils.drawImageRect(buffImage, (int) dstArr[i][0], (int) dstArr[i][1], 4, 4);
//
//            }
//            Mat pointMat = OpenCVUtils.image2Mat(buffImage);
//            Image pointImg = OpenCVImageFactory.getInstance().fromImage(pointMat);
//            ImageUtils.saveImage(pointImg, "points_result.png", "build/output");


            List<Point> srcQuadPoints = new ArrayList<>();
            List<Point> dstQuadPoints = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                double x = srcArr[i][0];
                double y = srcArr[i][1];
                Point point = new Point((int) x, (int) y);
                srcQuadPoints.add(point);
            }

            for (int i = 0; i < 4; i++) {
                double x = dstArr[i][0];
                double y = dstArr[i][1];
                Point point = new Point((int) x, (int) y);
                dstQuadPoints.add(point);
            }

            org.opencv.core.Mat srcPoint2f = OpenCVUtils.toMat(srcQuadPoints);
            org.opencv.core.Mat dstPoint2f = OpenCVUtils.toMat(dstQuadPoints);

            //4点透视变换
            // 4-point perspective transformation
            org.opencv.core.Mat mat = OpenCVUtils.perspectiveTransform((org.opencv.core.Mat) targetImg.getWrappedImage(), (org.opencv.core.Mat) templateImg.getWrappedImage(), srcPoint2f, dstPoint2f);
            Image newImg = OpenCVImageFactory.getInstance().fromImage(mat);
            ImageUtils.saveImage(newImg, "perspectiveTransform.png", "build/output");


            System.out.println("end");


        }
    }
}
