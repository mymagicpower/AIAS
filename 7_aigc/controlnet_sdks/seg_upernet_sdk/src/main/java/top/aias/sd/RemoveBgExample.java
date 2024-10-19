package top.aias.sd;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.sd.controlnet.SegUperNetDetector;
import top.aias.sd.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class RemoveBgExample {

    private static final Logger logger = LoggerFactory.getLogger(RemoveBgExample.class);

    private RemoveBgExample() {
    }

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Path imageFile = Paths.get("src/test/resources/aaa.jpg");
        Image img = OpenCVImageFactory.getInstance().fromFile(imageFile);

        try (SegUperNetDetector detector = new SegUperNetDetector(512, 512, true, Device.cpu());
             NDManager manager = NDManager.newBaseManager()) {
            Image segImg = detector.predict(img);

            NDArray maskArray = segImg.toNDArray(manager).toType(DataType.FLOAT32, false);;

            // 黑色部分为 0，小于128，白色 255
            NDArray oriImgArray = img.toNDArray(manager).toType(DataType.FLOAT32, false);;
            oriImgArray.muli(maskArray);
//            oriImgArray.set(maskArray.gte(255f), 0f);

            Image newImg = OpenCVImageFactory.getInstance().fromNDArray(oriImgArray);

            ImageUtils.saveImage(newImg, "newImg.png", "build/output");
        }
    }

    public static void TransparentBackground () {
        try {
            // 读取图片
            BufferedImage image = ImageIO.read(new File("build/output/newImg.png"));

            // 获取图片的宽度和高度
            int width = image.getWidth();
            int height = image.getHeight();

            // 遍历每个像素
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // 获取当前像素的RGB值
                    int rgba = image.getRGB(x, y);
                    // 将RGB值转换为颜色对象
                    Color color = new Color(rgba, true);
                    // 如果当前像素是黑色，则将Alpha通道值设置为0
                    if (color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == 0) {
                        color = new Color(0, 0, 0, 0);
                    }
                    // 将修改后的颜色设置回图片
                    image.setRGB(x, y, color.getRGB());
                }
            }

            // 保存修改后的图片
            ImageIO.write(image, "PNG", new File("build/output/output.png"));

            System.out.println("转换完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
