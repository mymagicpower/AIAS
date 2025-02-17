package top.aias.sd;

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
import top.aias.sd.pipelines.StableDiffusionControlNetPipeline;
import top.aias.sd.preprocess.pose.*;
import top.aias.sd.utils.ImageUtils;
import top.aias.sd.utils.PoseUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public final class ControlNetPoseGpu {

    private ControlNetPoseGpu() {}

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        String bodyModelPath = "models/body.pt";
        String handModelPath = "models/hand.pt";
        String faceModelPath = "models/face.pt";

        Path imageFile = Paths.get("src/test/resources/pose.png");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String prompt = "chef in the kitchen";

        HandEstimation handEstimation = new HandEstimation();
        FaceEstimation faceEstimation = new FaceEstimation();

        try (StableDiffusionControlNetPipeline model = new StableDiffusionControlNetPipeline("H:\\models\\aigc\\sd_gpu\\", "controlnet_openpose.pt", Device.gpu());
             PoseModel poseDetector = new PoseModel(512, 512, bodyModelPath, 1, Device.gpu());
             NDManager manager = NDManager.newBaseManager(Device.gpu());
             HandModel handDetector = new HandModel(handModelPath, 1, Device.gpu());
             FaceModel faceDetector = new FaceModel(faceModelPath, 1, Device.gpu());) {
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
            ImageUtils.saveImage(result, "ctrlnet_pose_pt_gpu.png", "build/output");
        }

    }
}