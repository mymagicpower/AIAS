package top.aias.platform.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.aias.platform.model.preprocess.depth.DptDepthModel;
import top.aias.platform.model.preprocess.depth.MidasDepthModel;
import top.aias.platform.model.preprocess.edge.HedModel;
import top.aias.platform.model.preprocess.edge.HedScribbleModel;
import top.aias.platform.model.preprocess.edge.PidiNetModel;
import top.aias.platform.model.preprocess.edge.PidiNetScribbleModel;
import top.aias.platform.model.preprocess.lineart.LineArtAnimeModel;
import top.aias.platform.model.preprocess.lineart.LineArtCoarseModel;
import top.aias.platform.model.preprocess.lineart.LineArtModel;
import top.aias.platform.model.preprocess.lineart.MlsdModel;
import top.aias.platform.model.preprocess.normal.NormalBaeModel;
import top.aias.platform.model.preprocess.pose.*;
import top.aias.platform.model.preprocess.seg.SegUperNetModel;
import top.aias.platform.service.ImgPreProcessService;
import top.aias.platform.utils.OpenCVUtils;
import top.aias.platform.utils.PoseUtils;
import top.aias.platform.utils.ShuffleUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 图像分割服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Service
public class ImgPreProcessServiceImpl implements ImgPreProcessService {
    private Logger logger = LoggerFactory.getLogger(ImgPreProcessServiceImpl.class);

    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String deviceType;

    @Autowired
    private DptDepthModel dptDepthModel;

    @Autowired
    private MidasDepthModel midasDepthModel;

    @Autowired
    private LineArtAnimeModel lineArtAnimeModel;

    @Autowired
    private LineArtCoarseModel lineArtCoarseModel;

    @Autowired
    private LineArtModel lineArtModel;

    @Autowired
    private MlsdModel mlsdModel;

    @Autowired
    private NormalBaeModel normalBaeModel;

    @Autowired
    private PoseModel poseModel;

    @Autowired
    private HandModel handModel;

    @Autowired
    private FaceModel faceModel;

    @Autowired
    private HedScribbleModel hedScribbleModel;

    @Autowired
    private PidiNetScribbleModel pidiNetScribbleModel;

    @Autowired
    private SegUperNetModel segUperNetModel;

    @Autowired
    private HedModel hedModel;

    @Autowired
    private PidiNetModel pidiNetModel;

    @Override
    public Image canny(Image image) {
        // Canny
        org.opencv.core.Mat mat = OpenCVUtils.canny((org.opencv.core.Mat) image.getWrappedImage());
        image = OpenCVImageFactory.getInstance().fromImage(mat);
        return image;
    }

    @Override
    public Image contentShuffle(Image image) {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        try (NDManager manager = NDManager.newBaseManager(device, "PyTorch")) {  //PyTorch
            Image result = ShuffleUtils.hwcContentShuffle(manager, image, 512, 512);
            return result;
        }
    }

    @Override
    public Image depthEstimationDpt(Image image) throws ModelException, TranslateException, IOException {
        Image result = dptDepthModel.predict(image);
        return result;
    }

    @Override
    public Image depthEstimationMidas(Image image) throws ModelException, TranslateException, IOException {
        Image result = midasDepthModel.predict(image);
        return result;
    }

    @Override
    public Image lineartAnime(Image image) throws ModelException, TranslateException, IOException {
        Image result = lineArtAnimeModel.predict(image);
        return result;
    }

    @Override
    public Image lineartCoarse(Image image) throws ModelException, TranslateException, IOException {
        Image result = lineArtCoarseModel.predict(image);
        return result;
    }

    @Override
    public Image lineart(Image image) throws ModelException, TranslateException, IOException {
        Image result = lineArtModel.predict(image);
        return result;
    }

    @Override
    public Image mlsd(Image image) throws ModelException, TranslateException, IOException {
        Image result = mlsdModel.predict(image);
        return result;
    }

    @Override
    public Image normalBae(Image image) throws ModelException, TranslateException, IOException {
        Image result = normalBaeModel.predict(image);
        return result;
    }

    @Override
    public Image pose(Image img) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        HandEstimation handEstimation = new HandEstimation();
        FaceEstimation faceEstimation = new FaceEstimation();

        try (NDManager manager = NDManager.newBaseManager(device, "PyTorch")) {  //PyTorch
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

            return img;
        }
    }

    @Override
    public Image scribbleHed(Image image) throws ModelException, TranslateException, IOException {
        Image result = hedScribbleModel.predict(image);
        return result;
    }

    @Override
    public Image scribblePidinet(Image image) throws ModelException, TranslateException, IOException {
        Image result = pidiNetScribbleModel.predict(image);
        return result;
    }

    @Override
    public Image segUpernet(Image image) throws TranslateException {
        Image result = segUperNetModel.predict(image);
        return result;
    }

    @Override
    public Image softedgeHed(Image image) throws ModelException, TranslateException, IOException {
        Image result = hedModel.predict(image);
        return result;
    }

    @Override
    public Image softedgePidinet(Image image) throws ModelException, TranslateException, IOException {
        Image result = pidiNetModel.predict(image);
        return result;
    }
}
