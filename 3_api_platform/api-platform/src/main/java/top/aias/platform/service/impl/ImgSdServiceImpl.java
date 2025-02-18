package top.aias.platform.service.impl;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.aias.platform.model.sd.controlnet.*;
import top.aias.platform.model.sd.pipelines.SdControlNetPipeline;
import top.aias.platform.model.sd.pipelines.SdImg2ImgPipeline;
import top.aias.platform.model.sd.pipelines.SdTxt2ImgPipeline;
import top.aias.platform.model.sd.text.TextEncoderModel;
import top.aias.platform.model.sd.unet.UNetForControlModel;
import top.aias.platform.model.sd.unet.UNetModel;
import top.aias.platform.model.sd.vae.VaeDecoderModel;
import top.aias.platform.model.sd.vae.VaeEncoderModel;

/**
 * Stable Diffusion 图像生成服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Service
public class ImgSdServiceImpl implements top.aias.platform.service.ImgSdService {
    private Logger logger = LoggerFactory.getLogger(ImgSdServiceImpl.class);

    // 设备类型 cpu gpu
    @Value("${model.device}")
    private String deviceType;

    @Value("${model.sd.autoClose}")
    private String autoClose;

    // encoder, decoder 模型
    @Autowired
    private TextEncoderModel textEncoderModel;
    @Autowired
    private VaeEncoderModel vaeEncoderModel;
    @Autowired
    private VaeDecoderModel vaeDecoderModel;

    // Unet 模型
    @Autowired
    private UNetModel uNetModel;
    @Autowired
    private UNetForControlModel uNetForControlModel;


    // ControlNet 模型名称
    @Autowired
    private SdCannyModel sdCannyModel;
    @Autowired
    private SdDepthModel sdDepthModel;
    @Autowired
    private SdLineartAnimeModel sdLineartAnimeModel;
    @Autowired
    private SdLineartModel sdLineartModel;
    @Autowired
    private SdMlsdModel sdMlsdModel;
    @Autowired
    private SdNormalBaeModel sdNormalBaeModel;
    @Autowired
    private SdOpenPoseModel sdOpenPoseModel;
    @Autowired
    private SdP2PModel sdP2PModel;
    @Autowired
    private SdScribbleModel sdScribbleModel;
    @Autowired
    private SdSegModel sdSegModel;
    @Autowired
    private SdShuffleModel sdShuffleModel;
    @Autowired
    private SdSoftEdgeModel sdSoftEdgeModel;

    /**
     * 文本生成图像
     * @param prompt
     * @param negativePrompt
     * @param steps
     * @return
     * @throws TranslateException
     */
    @Override
    public Image txt2Image(String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdTxt2ImgPipeline model = new SdTxt2ImgPipeline(device, uNetModel, vaeDecoderModel, textEncoderModel);
            Image result = model.generateImage(prompt, negativePrompt,steps);
//            ImageUtils.saveImage(result, "txt2img_pt_gpu.png", "build/output");
        return result;
    }

    /**
     *  图生图
     *
     * @param image
     * @param prompt
     * @param negativePrompt
     * @param steps
     * @return
     * @throws TranslateException
     */
    @Override
    public Image image2Image(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdImg2ImgPipeline model = new SdImg2ImgPipeline(device, vaeEncoderModel, uNetModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);
//            ImageUtils.saveImage(result, "txt2img_pt_gpu.png", "build/output");
        return result;
    }

    @Override
    public Image sdCanny(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdCannyModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

        if (autoClose.equalsIgnoreCase("true")) {
            sdCannyModel.close();
        }

        return result;
    }

    @Override
    public Image sdDepth(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdDepthModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

        if (autoClose.equalsIgnoreCase("true")) {
            sdDepthModel.close();
        }

        return result;
    }

    @Override
    public Image sdLineartAnime(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdLineartAnimeModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

        if (autoClose.equalsIgnoreCase("true")) {
            sdLineartAnimeModel.close();
        }

        return result;
    }

    @Override
    public Image sdLineart(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdLineartModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

        if (autoClose.equalsIgnoreCase("true")) {
            sdLineartModel.close();
        }

        return result;
    }

    @Override
    public Image sdMlsd(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdMlsdModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

//        if (autoClose.equalsIgnoreCase("true")) {
//            sdMlsdModel.close();
//        }

        return result;
    }

    @Override
    public Image sdNormalBae(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdNormalBaeModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

        if (autoClose.equalsIgnoreCase("true")) {
            sdNormalBaeModel.close();
        }

        return result;
    }

    @Override
    public Image sdOpenPose(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdOpenPoseModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

        if (autoClose.equalsIgnoreCase("true")) {
            sdOpenPoseModel.close();
        }

        return result;
    }

    @Override
    public Image sdP2P(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdP2PModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

        if (autoClose.equalsIgnoreCase("true")) {
            sdP2PModel.close();
        }

        return result;
    }

    @Override
    public Image sdScribble(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdScribbleModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

        if (autoClose.equalsIgnoreCase("true")) {
            sdScribbleModel.close();
        }

        return result;
    }

    @Override
    public Image sdSeg(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdSegModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

        if (autoClose.equalsIgnoreCase("true")) {
            sdSegModel.close();
        }

        return result;
    }

    @Override
    public Image sdShuffle(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdShuffleModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

        if (autoClose.equalsIgnoreCase("true")) {
            sdShuffleModel.close();
        }

        return result;
    }

    @Override
    public Image sdSoftEdge(Image image, String prompt, String negativePrompt, int steps) throws TranslateException {
        Device device;
        if (deviceType.equalsIgnoreCase("cpu")) {
            device = Device.cpu();
        } else {
            device = Device.gpu();
        }

        SdControlNetPipeline model = new SdControlNetPipeline(device, sdSoftEdgeModel, uNetForControlModel, vaeDecoderModel, textEncoderModel);
        Image result = model.generateImage(image, prompt, negativePrompt,steps);

        if (autoClose.equalsIgnoreCase("true")) {
            sdSoftEdgeModel.close();
        }

        return result;
    }
}
