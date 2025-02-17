package top.aias.sd.preprocess.tests;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.sd.preprocess.pose.*;
import top.aias.sd.utils.ImageUtils;
import top.aias.sd.utils.PoseUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
/**
 * OpenPose 姿态检测
 * OpenPose 姿态检测可生成图像中角色动作姿态的骨架图(含脸部特征以及手部骨架检测)，这个骨架图可用于控制生成角色的姿态动作。
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class PoseExample {

    private static final Logger logger = LoggerFactory.getLogger(PoseExample.class);

    private PoseExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/fullbody.jpg"); // pose.png beauty.jpg
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);
        String bodyModelPath = "models/body.pt";
        String handModelPath = "models/hand.pt";
        String faceModelPath = "models/face.pt";

        HandEstimation handEstimation = new HandEstimation();
        FaceEstimation faceEstimation = new FaceEstimation();

        try (PoseModel poseModel = new PoseModel(512, 512, bodyModelPath, 1, Device.cpu());
             NDManager manager = NDManager.newBaseManager(Device.cpu());
             HandModel handModel = new HandModel(handModelPath, 1, Device.cpu());
             FaceModel faceModel = new FaceModel(faceModelPath, 1, Device.cpu());) {

            NDList ndList = poseModel.predict(img);
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
            ArrayList<ArrayList<float[]>> hands = handEstimation.hands(manager, handModel, oriImg, hands_list);
            canvas = PoseUtils.draw_handpose(manager, canvas, hands);

            // face
            ArrayList<int[]> faces_list = PoseUtils.faceDetect(candidate, subset, oriImg);
            ArrayList<ArrayList<float[]>> faces = faceEstimation.faces(manager, faceModel, oriImg, faces_list);
            canvas = PoseUtils.draw_facepose(manager, canvas, faces);


            img = ImageFactory.getInstance().fromNDArray(canvas);
            int[] hw = PoseUtils.resizeImage(img.getHeight(), img.getWidth(), 512);
            NDArray ndArray = NDImageUtils.resize(img.toNDArray(manager), hw[1], hw[0], Image.Interpolation.BILINEAR);
            img = ImageFactory.getInstance().fromNDArray(ndArray);

            ImageUtils.saveImage(img, "pose.png", "build/output");
        }
    }



}
