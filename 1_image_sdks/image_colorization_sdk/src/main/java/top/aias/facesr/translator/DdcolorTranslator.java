package top.aias.facesr.translator;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import org.opencv.imgproc.Imgproc;
import top.aias.facesr.utils.OpenCVUtils;

/**
 * 照片上色模型前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class DdcolorTranslator implements Translator<Image, Image> {
    private NDArray origL;
    private int width;
    private int height;

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {

        NDManager manager = ctx.getNDManager();
        NDArray oriArray = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR).toType(DataType.FLOAT32, false);
        oriArray = oriArray.div(255.0f);
        width = input.getWidth();
        height = input.getHeight();

        oriArray = NDImageUtils.resize(oriArray, 915, 1210, Image.Interpolation.BILINEAR);
        origL = OpenCVUtils.cvtColor(oriArray, Imgproc.COLOR_RGB2Lab);
        origL = origL.get(new NDIndex(":, :, :1")).toType(DataType.FLOAT32, false);

        // do_resize
        NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR).toType(DataType.FLOAT32, false);
        array = array.div(255.0f);
        array = NDImageUtils.resize(array, 512, 512, Image.Interpolation.BILINEAR);

        NDArray imgL = OpenCVUtils.cvtColor(array, Imgproc.COLOR_RGB2Lab);
        imgL = imgL.get(new NDIndex(":, :, :1"));

        Shape shape = imgL.getShape();
        DataType dataType = imgL.getDataType();
        NDArray img = imgL.concat(manager.zeros(shape, dataType), -1).concat(manager.zeros(shape, dataType), -1);

        NDArray grayRgbImg = OpenCVUtils.cvtColor(img, Imgproc.COLOR_Lab2RGB);
        grayRgbImg = grayRgbImg.transpose(2, 0, 1);

        return new NDList(grayRgbImg, manager.create(input.getHeight()), manager.create(input.getWidth()));
    }

    @Override
    public Image processOutput(TranslatorContext ctx, NDList list) {
        NDArray pred = list.get(0);

        pred = pred.transpose(1, 2, 0).toType(DataType.FLOAT32, false);
        pred = origL.concat(pred, -1);
        pred = OpenCVUtils.cvtColor(pred, Imgproc.COLOR_Lab2RGB);
        pred = pred.mul(255.0f).round().toType(DataType.UINT8, false);

        pred = NDImageUtils.resize(pred, width, height, Image.Interpolation.BILINEAR);

        return ImageFactory.getInstance().fromNDArray(pred);
    }

    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }
}
