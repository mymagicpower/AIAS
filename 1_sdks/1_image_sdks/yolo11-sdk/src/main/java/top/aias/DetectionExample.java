package top.aias;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.opencv.OpenCVImageFactory;
import ai.onnxruntime.*;
import lombok.extern.slf4j.Slf4j;
import top.aias.models.Yolo11DetModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 目标检测，COCO数据集
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Slf4j
public class DetectionExample {

    public static void main(String[] args) throws OrtException, IOException {
        String modelPath = "src/main/resources/yolov8n.onnx";
        Path path = Paths.get("src/main/resources/bus.jpg");
        Image image = OpenCVImageFactory.getInstance().fromFile(path);

        // COCO数据集
        String[] classNames = {
                "person", "bicycle", "car", "motorcycle", "airplane", "bus", "train", "truck", "boat", "traffic light",
                "fire hydrant", "stop sign", "parking meter", "bench", "bird", "cat", "dog", "horse", "sheep", "cow", "elephant",
                "bear", "zebra", "giraffe", "backpack", "umbrella", "handbag", "tie", "suitcase", "frisbee", "skis", "snowboard",
                "sports ball", "kite", "baseball bat", "baseball glove", "skateboard", "surfboard", "tennis racket", "bottle",
                "wine glass", "cup", "fork", "knife", "spoon", "bowl", "banana", "apple", "sandwich", "orange", "broccoli",
                "carrot", "hot dog", "pizza", "donut", "cake", "chair", "couch", "potted plant", "bed", "dining table", "toilet",
                "tv", "laptop", "mouse", "remote", "keyboard", "cell phone", "microwave", "oven", "toaster", "sink", "refrigerator",
                "book", "clock", "vase", "scissors", "teddy bear", "hair drier", "toothbrush"
        };

        Yolo11DetModel yolo11DetModel = new Yolo11DetModel(modelPath, classNames);
        DetectedObjects detectedObjects = yolo11DetModel.call(image);
        drawBoundingBoxes(image, detectedObjects);

    }

    private static void drawBoundingBoxes(Image image, DetectedObjects detection) throws IOException {
        image.drawBoundingBoxes(detection, 0.8f);
//        for (Rectangle rect : input.getBoxes()) {
//            OpenCVUtils.drawRectangle((Mat) img.getWrappedImage(), rect, 0xff0000, 6);
//        }
        Path outputDir = Paths.get("build/output");
        Files.createDirectories(outputDir);
        Path imagePath = outputDir.resolve("det_result.png");
        image.save(Files.newOutputStream(imagePath), "png");
    }

}