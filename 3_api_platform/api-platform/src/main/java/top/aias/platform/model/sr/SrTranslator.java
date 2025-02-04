package top.aias.platform.model.sr;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
/**
 * 超分辨模型前后处理
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public class SrTranslator implements Translator<Image, Image> {
    private int max_range = 255;
    private String img_mode;

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) throws Exception {
        NDManager manager = ctx.getNDManager();
        NDArray array = input.toNDArray(manager).toType(DataType.FLOAT32, false).divi(max_range);

//        Image image = ImageFactory.getInstance().fromNDArray(array);
//        org.opencv.core.Mat src = (org.opencv.core.Mat) image.getWrappedImage();

        if (array.getShape().dimension() == 2) {
            throw new Exception("16-bit image not supported");
        } else if (array.getShape().get(2) == 4) {
            throw new Exception("RGBA image not supported");
//            NDArray alpha;
//            img_mode = "RGBA";
//            alpha = array.get(new NDIndex(":, :, 3"));
//            array = array.get(new NDIndex(":, :, 0:3"));
//            image = ImageFactory.getInstance().fromNDArray(array);
//            src = (org.opencv.core.Mat) image.getWrappedImage();
//            org.opencv.core.Mat dst = OpenCVUtils.cvtColor(src, Imgproc.COLOR_BGR2RGB);
//            array = ImageFactory.getInstance().fromImage(dst).toNDArray(manager);
//
//            // alpha_upsampler == 'realesrgan'
//            image = ImageFactory.getInstance().fromNDArray(alpha);
//            src = (org.opencv.core.Mat) image.getWrappedImage();
//            dst = OpenCVUtils.cvtColor(src, Imgproc.COLOR_BGR2RGB);
//            alpha = ImageFactory.getInstance().fromImage(dst).toNDArray(manager);
        } else {
            img_mode = "RGB";
//            org.opencv.core.Mat dst = OpenCVUtils.cvtColor(src, Imgproc.COLOR_BGR2RGB);
//            array = ImageFactory.getInstance().fromImage(dst).toNDArray(manager);
        }

        array = array.transpose(2, 0, 1).toType(DataType.FLOAT32, false); // HWC -> CHW RGB

        return new NDList(array);
    }

    @Override
    public Image processOutput(TranslatorContext ctx, NDList list) {
        NDArray output_img = list.singletonOrThrow();

        output_img = output_img.clip(0, 1);

        output_img = output_img.mul(255.0).round().toType(DataType.UINT8, false);

        return ImageFactory.getInstance().fromNDArray(output_img);
    }

    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }
}
