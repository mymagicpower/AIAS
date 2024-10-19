package me.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import me.aias.sd.controlnet.*;
import me.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import me.aias.sd.utils.ImageUtils;
import me.aias.sd.utils.PoseUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public final class ControlNetPoseCpu {

    private ControlNetPoseCpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {

        Path imageFile = Paths.get("src/test/resources/pose.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "chef in the kitchen";

        HandEstimation handEstimation = new HandEstimation();
        FaceEstimation faceEstimation = new FaceEstimation();

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("models/pytorch_cpu/", "controlnet_openpose.pt", Device.cpu());
             PoseDetector poseDetector = new PoseDetector(512, 512,Device.cpu());
             NDManager manager = NDManager.newBaseManager(Device.cpu());
             HandDetector handDetector = new HandDetector(Device.cpu());
             FaceDetector faceDetector = new FaceDetector(Device.cpu());) {
            NDList ndList = poseDetector.predict(img);
            NDArray candidate = ndList.get(0);
            candidate.attach(manager);
            NDArray subset = ndList.get(1);
            subset.attach(manager);
            NDArray oriImg = ndList.get(2);
            oriImg.attach(manager);

            // body
            NDArray canvas = manager.zeros(oriImg.getShape(), DataType.UINT8);
            canvas = PoseUtils.draw_bodypose(manager,oriImg, canvas, candidate, subset);

            // hand
            ArrayList<int[]> hands_list = PoseUtils.handDetect(candidate, subset, oriImg);
            ArrayList<ArrayList<float[]>> hands = handEstimation.hands(manager, handDetector, oriImg, hands_list);
            canvas = PoseUtils.draw_handpose(manager, canvas, hands);

            // face
            ArrayList<int[]> faces_list = PoseUtils.faceDetect(candidate, subset, oriImg);
            ArrayList<ArrayList<float[]>> faces = faceEstimation.faces(manager, faceDetector, oriImg, faces_list);
            canvas = PoseUtils.draw_facepose(manager, canvas, faces);


            img = ImageFactory.getInstance().fromNDArray(canvas);
            int[] hw = PoseUtils.resizeImage(img.getHeight(), img.getWidth(), 512);
            NDArray ndArray = NDImageUtils.resize(img.toNDArray(manager), hw[1], hw[0], Image.Interpolation.BILINEAR);
            img = ImageFactory.getInstance().fromNDArray(ndArray);

            Image result = model.generateImage(img, prompt, "", 25);
            ImageUtils.saveImage(result, "ctrlnet_pose_pt_cpu.png", "output");
        }

    }
}