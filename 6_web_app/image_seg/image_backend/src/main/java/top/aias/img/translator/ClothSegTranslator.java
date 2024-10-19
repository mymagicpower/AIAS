package top.aias.img.translator;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import top.aias.img.utils.NDArrayUtils;

public final class ClothSegTranslator implements Translator<Image, Image> {
    protected Batchifier batchifier = Batchifier.STACK;

    private int width;
    private int height;
    private NDArray oriImgArray;
    private int clothCategory;
    private boolean isPostProcess = true;
    public ClothSegTranslator(int clothCategory) {
        this.clothCategory = clothCategory;
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
        width = input.getWidth();
        height = input.getHeight();

        NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
        oriImgArray = ctx.getNDManager().create(array.getShape(), array.getDataType());
        array.copyTo(oriImgArray);
        oriImgArray = oriImgArray.transpose(2, 0, 1); // HWC -> CHW RGB

        // do_resize
        array = NDImageUtils.resize(array, 768, 768, Image.Interpolation.AREA);

        // do_rescale rescale_factor = 1/255
        array = array.div(array.max()); // HWC3 255.0f

        // to_channel_dimension_format
        array = array.transpose(2, 0, 1); // HWC -> CHW RGB

        // do_normalize
        NDArray mean = ctx.getNDManager().create(new float[]{0.485f, 0.456f, 0.406f}, new Shape(3, 1, 1));
        NDArray std = ctx.getNDManager().create(new float[]{0.229f, 0.224f, 0.225f}, new Shape(3, 1, 1));
        array = array.sub(mean);
        array = array.div(std);

        return new NDList(array);
    }

    @Override
    public Image processOutput(TranslatorContext ctx, NDList list) {
        try (NDManager manager = NDManager.newBaseManager(ctx.getNDManager().getDevice(), "PyTorch")) {  //PyTorch
            NDArray pred = list.get(0);

            pred = pred.logSoftmax(0);
            pred = pred.argMax(0);
            // 只有 0,1,2,3 4个值 (0 背景， 1上半身， 2 下半身, 3 连体衣）
            if (clothCategory == 1) {
                pred.set(pred.eq(1), 1);
                pred.set(pred.eq(2), 0);
                pred.set(pred.eq(3), 0);
            } else if (clothCategory == 2) {
                pred.set(pred.eq(1), 0);
                pred.set(pred.eq(2), 1);
                pred.set(pred.eq(3), 0);
            } else if (clothCategory == 3) {
                pred.set(pred.eq(1), 0);
                pred.set(pred.eq(2), 0);
                pred.set(pred.eq(3), 1);
            } else {
                pred.set(pred.eq(1), 1);
                pred.set(pred.eq(2), 1);
                pred.set(pred.eq(3), 1);
            }


            pred = pred.expandDims(0);
            pred = pred.concat(pred, 0).concat(pred, 0);

            pred = pred.transpose(1, 2, 0);
            pred = NDImageUtils.resize(pred, width, height, Image.Interpolation.BILINEAR);
            pred = pred.transpose(2, 0, 1);

            if (isPostProcess) {
                pred = pred.get(0);
                pred = this.postProcess(manager, pred);
                pred = pred.expandDims(0);
                pred = pred.concat(pred, 0).concat(pred, 0);
            }

            // 黑色部分为 0， 白色 255
            oriImgArray = oriImgArray.mul(pred);
            Image img = OpenCVImageFactory.getInstance().fromNDArray(oriImgArray);
            return img;
        }

        // set as masked pixel
        // 黑色部分为 0，小于128，白色 255
        // alpha_matting_foreground_threshold: int = 240
        // alpha_matting_background_threshold: int = 10
        // alpha_matting_erode_size: int = 10
//            color_seg.set(pred.gt(240), 1);
//            color_seg.set(pred.lt(10), 0);
    }

    @Override
    public Batchifier getBatchifier() {
        return batchifier;
    }

    public NDArray alphaMattingCutout(NDManager manager, NDArray mask) {
        return mask;
    }


    public NDArray postProcess(NDManager manager, NDArray ndArray) {
        org.opencv.core.Mat src = NDArrayUtils.floatNDArrayToMat(ndArray);
        org.opencv.core.Mat morphMat = src.clone();
        org.opencv.core.Mat gaussMat = src.clone();
        org.opencv.core.Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3));

        // 形态学操作函数: 它可以对图像进行膨胀、腐蚀、开运算、闭运算等操作,从而得到更好的效果。
        Imgproc.morphologyEx(src, morphMat, Imgproc.MORPH_OPEN, kernel);
        // 高斯滤波器(GaussianFilter)对图像进行平滑处理
        Imgproc.GaussianBlur(morphMat, gaussMat, new Size(5, 5), 2.0f, 2.0f);
        float[][] gaussArr = NDArrayUtils.matToFloatArray(gaussMat);
        NDArray gaussNDArray = manager.create(gaussArr);
//            gaussNDArray.set(gaussNDArray.lt(127), 0);
//            gaussNDArray.set(gaussNDArray.gte(127), 1);

        gaussNDArray = gaussNDArray.toType(DataType.UINT8, false);

        // release mat
        src.release();
        morphMat.release();
        gaussMat.release();
        kernel.release();

        return gaussNDArray;
    }
}