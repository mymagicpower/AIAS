package top.aias.models;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.Mat;
import top.aias.beans.IntermediateResult;
import top.aias.beans.KeyPointsDetResult;
import top.aias.utils.Dl4jUtils;

import java.util.*;

import ai.djl.modality.cv.output.Joints;

public class Yolo11DetKeyPointsModel {
    // Define private variable OrtSession
    private final OrtSession session;
    private double confThreshold = 0.5;
    private double nmsThreshold = 0.8;
    private final List<String> classes = Arrays.asList(new String[]{"a", "b", "c"});

    // Constructor
    public Yolo11DetKeyPointsModel(String modelPath) throws OrtException {
        // 获取ONNX运行时环境
        OrtEnvironment env = OrtEnvironment.getEnvironment();
        // 创建一个ONNX会话选项对象
        OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
        // 将InterOp线程数设置为1，InterOp线程用于并行处理不同计算图操作
        opts.setInterOpNumThreads(1);
        // 将IntraOp线程数设置为1，IntraOp线程用于单个操作内的并行处理
        opts.setIntraOpNumThreads(1);
        // 添加CPU设备，设置为false将用禁CPU执行优化
        opts.addCPU(true);
        // 使用环境、模型路径和选项创建一个ONNX会话
        session = env.createSession(modelPath, opts);
    }

    public void close() throws OrtException {
        session.close();
    }

    /**
     * Method to call the ONNX model
     */
    public KeyPointsDetResult call(Image image) throws OrtException {
        OrtEnvironment env = OrtEnvironment.getEnvironment();

        OnnxTensor inputTensor = null;
        OrtSession.Result ortOutputs = null;
        int width = image.getWidth();
        int height = image.getHeight();

        double wRatio = width / 640.0;
        double hRatio = height / 640.0;

        try {
            image = image.resize(640, 640, false);

            Mat mat = (Mat) image.getWrappedImage();

            INDArray array = Dl4jUtils.matToRgbINDArray(mat);

            // 对图片进行归一化
            normalizeImage(array);

            INDArray expandedImage = array.reshape(1, 3, 640, 640);

            float[][][][] arrayTo4DArray = Dl4jUtils.convertINDArrayTo4DArray(expandedImage);
            // Create input tensors
            inputTensor = OnnxTensor.createTensor(env, arrayTo4DArray);

            Map<String, OnnxTensor> inputs = new HashMap<>();
            inputs.put("images", inputTensor);

            // Call the ONNX model for calculation
            ortOutputs = session.run(inputs);

            float[][][] output = (float[][][]) ortOutputs.get(0).getValue();
            INDArray indArray = Nd4j.create(output[0]);
            INDArray transposeArr = indArray.transpose();

            ArrayList<IntermediateResult> intermediateResults = new ArrayList<>();
//            Joints.Joint joint = new Joints.Joint();

            for (int i = 0; i < transposeArr.rows(); i++) {
                double score = transposeArr.getDouble(i, 4);
                if (score >= confThreshold) {
                    double cx = transposeArr.getDouble(i, 0);
                    double cy = transposeArr.getDouble(i, 1);
                    double w = transposeArr.getDouble(i, 2);
                    double h = transposeArr.getDouble(i, 3);
                    Rectangle rect = new Rectangle(Math.max(0, cx - w / 2), Math.max(0, cy - h / 2), w, h);

                    ArrayList<Joints.Joint> jointsList = new ArrayList<>();
                    Joints joints = new Joints(jointsList);
                    for (int j = 5; j < transposeArr.columns(); j += 3) {
                        double x = transposeArr.getDouble(i, j);
                        double y = transposeArr.getDouble(i, j + 1);
                        double conf = transposeArr.getDouble(i, j + 2);
                        jointsList.add(new Joints.Joint(x * wRatio, y * hRatio, conf));
                    }

                    intermediateResults.add(
                            new IntermediateResult(score, rect, joints));

                }
            }

            List<IntermediateResult> list = nms(intermediateResults, nmsThreshold);


            List<String> retClasses = new ArrayList<>();
            List<Double> retProbs = new ArrayList<>();
            List<ai.djl.modality.cv.output.BoundingBox> retBB = new ArrayList<>();
            List<Joints> jointsList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                IntermediateResult intermediateResult = list.get(i);

                retClasses.add("");
                retProbs.add(intermediateResult.getConfidence());

                retBB.add(
                        new Rectangle(
                                intermediateResult.getLocation().getX() / 640.0,
                                intermediateResult.getLocation().getY() / 640.0,
                                intermediateResult.getLocation().getWidth() / 640.0,
                                intermediateResult.getLocation().getHeight() / 640.0));

                jointsList.add(intermediateResult.getJoints());
            }

            DetectedObjects detectedObjects = new DetectedObjects(retClasses, retProbs, retBB);

            array.close();
            expandedImage.close();
            indArray.close();
            transposeArr.close();

            return new KeyPointsDetResult(detectedObjects, jointsList);

        } finally {
            if (inputTensor != null) {
                inputTensor.close();
            }
            if (ortOutputs != null) {
                ortOutputs.close();
            }
        }
    }

    private void normalizeImage(INDArray image) {
        // Convert image data to floating-point
        image.divi(255.0); // Scale pixel values to [0, 1]\
    }

    public List<IntermediateResult> nms(List<IntermediateResult> intermediateResults, double threshold) {
        List<IntermediateResult> result = new ArrayList<>();
        Map<Integer, List<IntermediateResult>> groups = new HashMap<>();

        // 将边界框按照类别分组
        for (IntermediateResult item : intermediateResults) {
            groups.computeIfAbsent(item.getId(), k -> new ArrayList<>()).add(item);
        }

        // 对每个类别执行NMS
        for (List<IntermediateResult> categoryBoxes : groups.values()) {
            // 按照置信度降序排序边界框
            categoryBoxes.sort(Comparator.comparingDouble(IntermediateResult::getConfidence).reversed());

            while (!categoryBoxes.isEmpty()) {
                // 选择置信度最高的边界框
                IntermediateResult topBox = categoryBoxes.get(0);
                result.add(topBox);

                // 计算与其他边界框的IoU并移除重叠较多的边界框
                categoryBoxes.removeIf(box -> boxIoU(topBox.getLocation(), box.getLocation()) > threshold);
            }
        }

        return result;
    }

    protected double boxIoU(Rectangle a, Rectangle b) {
        return boxIntersection(a, b) / boxUnion(a, b);
    }

    protected double boxIntersection(Rectangle a, Rectangle b) {
        double w =
                overlap(
                        (a.getX() * 2 + a.getWidth()) / 2,
                        a.getWidth(),
                        (b.getX() * 2 + b.getWidth()) / 2,
                        b.getWidth());
        double h =
                overlap(
                        (a.getY() * 2 + a.getHeight()) / 2,
                        a.getHeight(),
                        (b.getY() * 2 + b.getHeight()) / 2,
                        b.getHeight());
        if (w < 0 || h < 0) {
            return 0;
        }
        return w * h;
    }

    protected double boxUnion(Rectangle a, Rectangle b) {
        double i = boxIntersection(a, b);
        return (a.getWidth()) * (a.getHeight()) + (b.getWidth()) * (b.getHeight()) - i;
    }

    protected double overlap(double x1, double w1, double x2, double w2) {
        double l1 = x1 - w1 / 2;
        double l2 = x2 - w2 / 2;
        double left = Math.max(l1, l2);
        double r1 = x1 + w1 / 2;
        double r2 = x2 + w2 / 2;
        double right = Math.min(r1, r2);
        return right - left;
    }
}