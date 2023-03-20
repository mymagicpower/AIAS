package me.aias.example.utils.recognition;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.common.RotatedBox;
import me.aias.example.utils.opencv.NDArrayUtils;
import me.aias.example.utils.opencv.OpenCVUtils;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Point2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class OcrV3Recognition {

    private static final Logger logger = LoggerFactory.getLogger(OcrV3Recognition.class);

    public OcrV3Recognition() {
    }

    public Criteria<Image, String> recognizeCriteria() {
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("PaddlePaddle")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get("models/ch_PP-OCRv3_rec_infer.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator((new ConcurrentHashMap<String, String>())))
                        .build();
        return criteria;
    }

    public List<RotatedBox> predict(
            Image image, Predictor<Image, NDList> detector, Predictor<Image, String> recognizer)
            throws TranslateException {
        NDList boxes = detector.predict(image);

        List<RotatedBox> result = new ArrayList<>();
        long timeInferStart = System.currentTimeMillis();

        OpenCVFrameConverter.ToMat cv = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
        OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

        BufferedImage bufferedImage = OpenCVUtils.matToBufferedImage((org.opencv.core.Mat) image.getWrappedImage());
        Frame buf2Frame = new Java2DFrameConverter().convert(bufferedImage);
        org.bytedeco.opencv.opencv_core.Mat mat = cv.convertToMat(buf2Frame);

        for (int i = 0; i < boxes.size(); i++) {
            NDArray box = boxes.get(i);
//            BufferedImage bufferedImage = get_rotate_crop_image(image, box);

            float[] pointsArr = box.toFloatArray();
            float[] lt = java.util.Arrays.copyOfRange(pointsArr, 0, 2);
            float[] rt = java.util.Arrays.copyOfRange(pointsArr, 2, 4);
            float[] rb = java.util.Arrays.copyOfRange(pointsArr, 4, 6);
            float[] lb = java.util.Arrays.copyOfRange(pointsArr, 6, 8);
            int img_crop_width = (int) Math.max(distance(lt, rt), distance(rb, lb));
            int img_crop_height = (int) Math.max(distance(lt, lb), distance(rt, rb));
            List<Point> srcPoints = new ArrayList<>();
            srcPoints.add(new Point(lt[0], lt[1]));
            srcPoints.add(new Point(rt[0], rt[1]));
            srcPoints.add(new Point(rb[0], rb[1]));
            srcPoints.add(new Point(lb[0], lb[1]));
            List<Point> dstPoints = new ArrayList<>();
            dstPoints.add(new Point(0, 0));
            dstPoints.add(new Point(img_crop_width, 0));
            dstPoints.add(new Point(img_crop_width, img_crop_height));
            dstPoints.add(new Point(0, img_crop_height));

            Point2f srcPoint2f = NDArrayUtils.toOpenCVPoint2f(srcPoints, 4);
            Point2f dstPoint2f = NDArrayUtils.toOpenCVPoint2f(dstPoints, 4);

            //        try {
            //            File outputfile = new File("build/output/srcImage.jpg");
            //            ImageIO.write(bufferedImage, "jpg", outputfile);
            //        } catch (IOException e) {
            //            e.printStackTrace();
            //        }

            org.bytedeco.opencv.opencv_core.Mat dstMat = OpenCVUtils.perspectiveTransform(mat, srcPoint2f, dstPoint2f);
            org.opencv.core.Mat cvMat = converter2.convert(converter1.convert(dstMat));

            Image subImg = OpenCVImageFactory.getInstance().fromImage(cvMat);
//            ImageUtils.saveImage(subImg, i + ".png", "build/output");

            subImg = subImg.getSubImage(0,0,img_crop_width,img_crop_height);
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(subImg);
            }

            String name = recognizer.predict(subImg);
            RotatedBox rotatedBox = new RotatedBox(box, name);
            result.add(rotatedBox);

            dstMat.release();
            dstMat.close();
            cvMat.release();
            srcPoint2f.releaseReference();
            dstPoint2f.releaseReference();

        }

        mat.release();
        mat.close();
        cv.close();
        converter1.close();
        converter2.close();
        buf2Frame.close();

        long timeInferEnd = System.currentTimeMillis();
        System.out.println("time: " + (timeInferEnd - timeInferStart));

        return result;
    }

    private BufferedImage get_rotate_crop_image(Image image, NDArray box) {
        return null;
    }

    private float distance(float[] point1, float[] point2) {
        float disX = point1[0] - point2[0];
        float disY = point1[1] - point2[1];
        float dis = (float) Math.sqrt(disX * disX + disY * disY);
        return dis;
    }

    private Image rotateImg(Image image) {
        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
            return ImageFactory.getInstance().fromNDArray(rotated);
        }
    }
}
