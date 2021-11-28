package me.aias;

import me.aias.util.*;
import org.bytedeco.opencv.opencv_core.*;

import java.io.IOException;
import java.util.stream.LongStream;

import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class TestRotation {

    public static void main(String[] args) throws IOException {
        IplImage marcel_ = cvLoadImage("src/test/resources/ticket_r2.png");
        Mat marcel = new Mat(marcel_);

        GeneralUtils.saveImg(marcel, "build/output/srcImg.jpg");

        // 边缘检测
        Mat cannyMat = GeneralUtils.canny(marcel);
        GeneralUtils.saveImg(cannyMat, "build/output/canny.jpg");

        // 获取所有轮廓
        MatVector contours = ContourUtils.getContours(cannyMat);

        Mat resultImage = cannyMat.clone();
        LongStream.range(0, contours.size())
                .mapToObj(contours::get)
                .forEach(
                        contour -> {
                            Mat points = new Mat();
                            approxPolyDP(contour, points, arcLength(contour, true) * 0.02, true);
                            drawContours(resultImage, new MatVector(points), -1, Scalar.BLUE);
                        });
        GeneralUtils.saveImg(resultImage, "build/output/contours.jpg");

        // 获取最大外接矩形
        RotatedRect rect = RectUtils.getMaxRect(contours);
        Mat rectMat = marcel.clone();
        Scalar scalar = new Scalar(255, 0, 0, 1);
        Rect r = rect.boundingRect();
        rectMat = DrawUtils.drawRect(rectMat, r, scalar);
        GeneralUtils.saveImg(rectMat, "build/output/maxRect.jpg");

        // 旋转矩形
        Mat rotatedImg = RotationUtils.rotation(cannyMat, rect);
        GeneralUtils.saveImg(rotatedImg, "build/output/rotatedImg.jpg");

        Mat nativeRotatedImg = RotationUtils.rotation(marcel, rect);
        GeneralUtils.saveImg(nativeRotatedImg, "build/output/nativeRotatedImg.jpg");

        // 裁剪矩形
        Mat cutMat = RectUtils.cutRect(rotatedImg, nativeRotatedImg);
        GeneralUtils.saveImg(cutMat, "build/output/cutRect.jpg");
    }
}
