package top.aias;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.util.Utils;
import ai.onnxruntime.OrtException;
import lombok.extern.slf4j.Slf4j;
import top.aias.models.Yolo11ClsModel;
import top.aias.models.Yolo11DetModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片分类，imagenet 数据集
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Slf4j
public class ClsExample {

    public static void main(String[] args) throws OrtException, IOException {
        String modelPath = "src/main/resources/yolo11x-cls.onnx";
        String classesPath = "src/main/resources/classes.txt";
        Path path = Paths.get("src/main/resources/tiger.jpeg");

        Image image = OpenCVImageFactory.getInstance().fromFile(path);

        Yolo11ClsModel model = new Yolo11ClsModel(modelPath, classesPath);

        String result = model.call(image);
        System.out.println(result);
    }

}