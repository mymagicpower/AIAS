package me.aias.example;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.example.model.MlsdSquareModel;
import me.aias.example.model.SingleRecognitionModel;
import me.aias.example.utils.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class MlsdSquareCompExample {

    private static final Logger logger = LoggerFactory.getLogger(MlsdSquareCompExample.class);

    private MlsdSquareCompExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/template.png");
        Image template = OpenCVImageFactory.getInstance().fromFile(imageFile);

        imageFile = Paths.get("src/test/resources/ticket_0.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (SingleRecognitionModel recognitionModel = new SingleRecognitionModel();
             MlsdSquareModel mlsdSquareModel = new MlsdSquareModel();
             NDManager manager = NDManager.newBaseManager(Device.cpu(), "PyTorch")) {

            recognitionModel.init("models/ch_PP-OCRv3_det_infer_onnx.zip", "models/ch_PP-OCRv3_rec_infer_onnx.zip");
            mlsdSquareModel.init("models/mlsd_traced_model_onnx.zip");

            Image templateCropImg = mlsdSquareModel.predict(template);
            ImageUtils.saveImage(templateCropImg, "templateCrop.png", "build/output");
            // 模版文本检测 1
            // Text detection area
            List<LabelBean> templateTexts = new ArrayList<>();
            List<RotatedBox> templateTextsDet = recognitionModel.predict(manager, templateCropImg);
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


            Image targetCropImg = mlsdSquareModel.predict(img);
            NDArray array = NDImageUtils.resize(targetCropImg.toNDArray(manager), templateCropImg.getWidth(), templateCropImg.getHeight(), Image.Interpolation.BILINEAR);
            targetCropImg = OpenCVImageFactory.getInstance().fromNDArray(array);
            ImageUtils.saveImage(targetCropImg, "imgCrop.png", "build/output");

            // 目标文本检测 2
            // Text detection area
            List<LabelBean> targetTexts = new ArrayList<>();
            List<RotatedBox> textDetections = recognitionModel.predict(manager, targetCropImg);
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


            Map<String, String> hashMap;
            String distance = "IOU";
            if (distance.equalsIgnoreCase("IOU")) {
                hashMap = DistanceUtils.iou(templateTexts, targetTexts);
            } else {
                hashMap = DistanceUtils.l2Distance(templateTexts, targetTexts);
            }

            Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (entry.getKey().trim().equals("") && entry.getValue().trim().equals(""))
                    continue;
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }
}
