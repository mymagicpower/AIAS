package top.aias.seg.service.impl;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Mask;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aias.seg.bean.LabelBean;
import top.aias.seg.bean.Sam2Input;
import top.aias.seg.bean.ImageBean;
import top.aias.seg.configuration.FileProperties;
import top.aias.seg.model.Sam2DecoderModel;
import top.aias.seg.model.Sam2EncoderModel;
import top.aias.seg.service.ImageService;
import top.aias.seg.utils.FileUtils;
import top.aias.seg.utils.ImageUtils;
import top.aias.seg.utils.OpenCVUtils;
import top.aias.seg.utils.PointUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 识别服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Service
public class ImageServiceImpl implements ImageService {
    private static final String TEMPLATE_LIST_FILE = "templates.json";
    private Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    /**
     * file configuration
     */
    @Autowired
    private FileProperties properties;

    /**
     * sam2 encoder model
     */
    @Autowired
    private Sam2EncoderModel sam2EncoderModel;

    /**
     * sam2 decoder model
     */
    @Autowired
    private Sam2DecoderModel sam2DecoderModel;

    public List<ImageBean> getDataList() {
        List<ImageBean> templateList = null;
        FileProperties.ElPath path = properties.getPath();
        String fileRelativePath = path.getPath().replace("\\", "/");
        String json = null;
        try {
            json = FileUtils.readFile(fileRelativePath, TEMPLATE_LIST_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isBlank(json)) {
            templateList = new Gson().fromJson(json, new TypeToken<List<ImageBean>>() {
            }.getType());
        } else {
            templateList = new ArrayList<>();
        }
        return templateList;
    }

    /**
     * 获取图片信息
     * Get Image
     *
     * @param uid
     */
    public ImageBean getImageInfo(String uid) throws IOException {
        ImageBean template = null;
        FileProperties.ElPath path = properties.getPath();
        String fileRelativePath = path.getPath().replace("\\", "/") + "templates/";
        String json = FileUtils.readFile(fileRelativePath, uid + ".json");
        if (!StringUtils.isBlank(json)) {
            template = new Gson().fromJson(json, new TypeToken<ImageBean>() {
            }.getType());
        }
        return template;
    }


    /**
     * 新增图片
     * Add Image
     *
     * @param templateBean
     */
    public synchronized void addImageInfo(ImageBean templateBean) throws IOException {
        List<ImageBean> dataList = getDataList();
        templateBean.setLabelData(null);
        dataList.add(templateBean);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(dataList);
        FileProperties.ElPath path = properties.getPath();
        // 保存列表数据
        // Save list data
        String fileRelativePath = path.getPath().replace("\\", "/");
        FileUtils.saveFile(fileRelativePath, TEMPLATE_LIST_FILE, json);
        // 保存图片数据
        // Save image data
        json = gson.toJson(templateBean);
        FileUtils.saveFile(fileRelativePath + "templates/", templateBean.getUid() + ".json", json);
    }

    public String getLabelData(String uid, LabelBean labelData) throws IOException, TranslateException {
        ImageBean template = getImageInfo(uid);
        FileProperties.ElPath path = properties.getPath();
        String fileRelativePath = path.getPath().replace("\\", "/");
        Path imageFile = Paths.get(fileRelativePath + "images/" + template.getImageName());
        Image image = OpenCVImageFactory.getInstance().fromFile(imageFile);
        int[] rect = PointUtils.rectXYWH(labelData.getPoints());

        int x = rect[0];
        int y = rect[1];
        int right = x + rect[2];
        int bottom = y + rect[3];
        int centerX = (x + right) / 2;
        int centerY = (y + bottom) / 2;
        Sam2Input input = Sam2Input.builder(image).addPoint(centerX, centerY).addBox(x, y, right, bottom).build();
        try (NDManager manager = NDManager.newBaseManager(Device.cpu(), "PyTorch");) {
            NDList embeddings = sam2EncoderModel.predict(input);
            embeddings.attach(manager);
            input.setEmbeddings(embeddings);
            DetectedObjects detection = sam2DecoderModel.predict(input);
            // 抠图
            String mattingBase64Img = matting(manager, input, detection, false);

            // 显示遮罩层
            String maskBase64Img = showMask(input, detection);
            logger.info("{}", detection);

            return mattingBase64Img;
        }
    }

    private String showMask(Sam2Input input, DetectedObjects detection) throws IOException {
        Image img = input.getImage();
        img.drawBoundingBoxes(detection, 0.8f);
        img.drawMarks(input.getPoints());
        for (Rectangle rect : input.getBoxes()) {
            OpenCVUtils.drawRectangle((Mat) img.getWrappedImage(), rect, 0xff0000, 6);
        }

        BufferedImage bufferedImage = OpenCVUtils.mat2Image((Mat) img.getWrappedImage());
        bufferedImage = ImageUtils.removeBg(bufferedImage);
        String base64Img = ImageUtils.toBase64(bufferedImage, "jpg");

        Path outputDir = Paths.get("build/output");
        Files.createDirectories(outputDir);
        Path imagePath = outputDir.resolve("sam2.png");
        img.save(Files.newOutputStream(imagePath), "png");

        return base64Img;
    }

    private String matting(NDManager manager, Sam2Input input, DetectedObjects detection, boolean gaussianBlur) throws IOException {
        Image img = input.getImage();
        List<DetectedObjects.DetectedObject> list = detection.items();
        if (list.size() == 0)
            return null;

        DetectedObjects.DetectedObject result = list.get(0);
        BoundingBox box = result.getBoundingBox();
        if (box instanceof Mask) {
            Mask mask = (Mask) box;
            float[][] probDist = mask.getProbDist();
            NDArray oriImgArray = img.toNDArray(manager, Image.Flag.COLOR);
            oriImgArray = oriImgArray.transpose(2, 0, 1);
            NDArray pred = manager.create(probDist);

            if (gaussianBlur) {
                pred = OpenCVUtils.gaussianBlur(manager, pred);
            }
            pred = pred.expandDims(0);
            pred = pred.concat(pred, 0).concat(pred, 0);

            // 黑色为 0， 白色 255
            oriImgArray = oriImgArray.mul(pred);
            Image newImg = ImageFactory.getInstance().fromNDArray(oriImgArray);

            Rectangle rect = input.getBoxes().get(0);
            newImg = newImg.getSubImage((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getWidth());

            BufferedImage bufferedImage = OpenCVUtils.mat2Image((Mat) newImg.getWrappedImage());
            bufferedImage = ImageUtils.removeBg(bufferedImage);

            String base64Img = ImageUtils.toBase64(bufferedImage, "png");

            Path outputDir = Paths.get("build/output");
            Files.createDirectories(outputDir);
            ImageUtils.saveImage(bufferedImage, "img_seg.png", outputDir.toString());

            return base64Img;
        } else {
            return null;
        }
    }
}
