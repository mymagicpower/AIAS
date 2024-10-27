package me.aias.example;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import ai.djl.util.Pair;
import me.aias.example.model.SingleRecognitionModel;
import me.aias.example.utils.common.*;
import me.aias.example.utils.common.Point;
import me.aias.example.utils.opencv.OpenCVUtils;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class OcrV3ProjStackRecExample {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3ProjStackRecExample.class);

    private OcrV3ProjStackRecExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/template.png");
        Image templateImg = OpenCVImageFactory.getInstance().fromFile(imageFile);

        imageFile = Paths.get("src/test/resources/warp1.png");
        Image targetImg = OpenCVImageFactory.getInstance().fromFile(imageFile);
        Image origTargetImg = targetImg.duplicate();
        try (SingleRecognitionModel recognitionModel = new SingleRecognitionModel();
             NDManager manager = NDManager.newBaseManager(Device.cpu(), "PyTorch")) {
            recognitionModel.init("models/ch_PP-OCRv3_det_infer_onnx.zip", "models/ch_PP-OCRv3_rec_infer_onnx.zip");

            List<ProjItemBean> projList = new ArrayList<>();

            for (int num = 0; num < 4; num++) {
                Pair pair = ProjUtils.projPointsPair(manager, recognitionModel, templateImg, targetImg);
                List<Point> srcQuadPoints = (List<Point>) pair.getKey();
                List<Point> dstQuadPoints = (List<Point>) pair.getValue();

                // [516.74072265625, 367.02178955078125, 335.10894775390625, 578.5404052734375]
                // [1.0, 1.0, 2.2360680103302, 1.4142135381698608]

                // 计算距离
                double[] distances = new double[4];
                for (int i = 0; i < 4; i++) {
                    distances[i] = PointUtils.distance(srcQuadPoints.get(i), dstQuadPoints.get(i));
                }

                System.out.println(Arrays.toString(distances));

                boolean pass = true;
                for (int i = 0; i < 4; i++) {
                    if (distances[i] > 2) {
                        pass = false;
                        break;
                    }
                }

                if (!pass) {
                    ProjItemBean projItemBean = ProjUtils.projTransform(srcQuadPoints, dstQuadPoints, templateImg, targetImg);
                    targetImg = projItemBean.getImage();
                    ImageUtils.saveImage(projItemBean.getImage(), "perspectiveTransform_" + num + ".png", "build/output");

                    projList.add(projItemBean);

                } else {
                    break;
                }
            }
            if (projList.size() > 0) {
                org.opencv.core.Mat warp_mat = projList.get(projList.size() - 1).getWarpMat();
                if(projList.size() > 1){
                    for (int i = projList.size() - 2; i >= 0; i--) {
                        org.opencv.core.Mat matItem = projList.get(i).getWarpMat();
                        warp_mat = warp_mat.matMul(matItem);
                    }
                }

                org.opencv.core.Mat mat = OpenCVUtils.warpPerspective((Mat) origTargetImg.getWrappedImage(), (Mat) templateImg.getWrappedImage(), warp_mat);
                Image finalImg = OpenCVImageFactory.getInstance().fromImage(mat);
                ImageUtils.saveImage(finalImg, "perspectiveTransform_final.png", "build/output");
            }

        }
    }

    public static void save(Image image, List<Point> srcQuadPoints, List<Point> dstQuadPoints) {
        // 转 BufferedImage 解决 Imgproc.putText 中文乱码问题
        Mat matImage = (Mat) image.getWrappedImage();
        BufferedImage buffImage = OpenCVUtils.mat2Image(matImage);
        Color c = new Color(0, 255, 0);
        for (int i = 0; i < 4; i++) {
            DJLImageUtils.drawImageRect(buffImage, dstQuadPoints.get(i).getX(), dstQuadPoints.get(i).getY(), 6, 6, c);
            DJLImageUtils.drawImageRect(buffImage, srcQuadPoints.get(i).getX(), srcQuadPoints.get(i).getY(), 6, 6);

        }
        Mat pointMat = OpenCVUtils.image2Mat(buffImage);
        Image pointImg = OpenCVImageFactory.getInstance().fromImage(pointMat);
        ImageUtils.saveImage(pointImg, "points_result.png", "build/output");
    }

}
