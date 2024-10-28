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

public final class MlsdExample {

    private static final Logger logger = LoggerFactory.getLogger(MlsdExample.class);

    private MlsdExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/warp1.png");
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (MlsdSquareModel mlsdSquareModel = new MlsdSquareModel();
             NDManager manager = NDManager.newBaseManager(Device.cpu(), "PyTorch")) {
            mlsdSquareModel.init("models/mlsd_traced_model_onnx.zip");

            Image newImg = mlsdSquareModel.predict(image);
            if(newImg != null)
                ImageUtils.saveImage(newImg, "newImg.png", "build/output");
            else
                System.out.println("failure");
        }
    }
}
