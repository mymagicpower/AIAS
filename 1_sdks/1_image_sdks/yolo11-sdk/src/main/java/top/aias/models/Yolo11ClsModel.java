package top.aias.models;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.util.Utils;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.opencv.core.Mat;
import top.aias.beans.IntermediateResult;
import top.aias.utils.Dl4jUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Yolo11ClsModel {
    // Define private variable OrtSession
    private final OrtSession session;
    private final List<String> classes = new ArrayList<>();

    // Constructor
    public Yolo11ClsModel(String modelPath, String classesPath) throws OrtException, IOException {
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

        List<String> texts = Utils.readLines(Paths.get(classesPath), true);

        texts.stream()
                .filter(word -> (word != null && word != ""))
                .forEach(
                        word -> {
                            String[] ws = word.split(":");
                            classes.add(ws[1].trim());
                        });

    }

    public void close() throws OrtException {
        session.close();
    }

    /**
     * Method to call the ONNX model
     */
    public String call(Image image) throws OrtException {

        OrtEnvironment env = OrtEnvironment.getEnvironment();

        OnnxTensor inputTensor = null;
        OrtSession.Result ortOutputs = null;

        try {
            image = image.resize(224, 224, false);

            Mat mat = (Mat) image.getWrappedImage();

            INDArray array = Dl4jUtils.matToRgbINDArray(mat);

            // 对图片进行归一化
            normalizeImage(array);

            INDArray expandedImage = array.reshape(1, 3, 224, 224);

            float[][][][] arrayTo4DArray = Dl4jUtils.convertINDArrayTo4DArray(expandedImage);
            // Create input tensors
            inputTensor = OnnxTensor.createTensor(env, arrayTo4DArray);

            Map<String, OnnxTensor> inputs = new HashMap<>();
            inputs.put("images", inputTensor);

            // Call the ONNX model for calculation
            ortOutputs = session.run(inputs);

            float[][] value = (float[][]) ortOutputs.get(0).getValue();
            INDArray indArray = Nd4j.create(value);
            INDArray maxIndexArr = indArray.getRow(0).argMax();
            int maxIndex = maxIndexArr.getInt();

            array.close();
            expandedImage.close();
            indArray.close();

            return classes.get(maxIndex);

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

}