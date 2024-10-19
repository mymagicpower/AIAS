package top.aias.seg.translator;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import top.aias.seg.utils.NDArrayUtils;

/**
 * unet 前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class UNetTranslator implements Translator<Image, Image> {
    protected Batchifier batchifier = Batchifier.STACK;
    private int width;
    private int height;
    private boolean isPostProcess;
    private boolean mask;
    private NDArray oriImgArray;

    public UNetTranslator(boolean mask, boolean isPostProcess) {
        this.isPostProcess = isPostProcess;
        this.mask = mask;
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
        width = input.getWidth();
        height = input.getHeight();

        oriImgArray = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
        oriImgArray = oriImgArray.transpose(2, 0, 1); // HWC BGR -> CHW BGR

        // do_resize
        NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
        array = NDImageUtils.resize(array, 320, 320, Image.Interpolation.AREA);
//        Mat resizedMat = OpenCVUtils.resize((Mat) input.getWrappedImage(), 320, 320, Imgproc.INTER_LANCZOS4);
//        Image resizedImg = OpenCVImageFactory.getInstance().fromImage(resizedMat);
//        array = resizedImg.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);

        // do_rescale
        array = array.div(array.max()); // HWC3
        // to_channel_dimension_format
        array = array.transpose(2, 0, 1);

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

            pred = pred.get(new NDIndex("0, :, :"));
            NDArray max = pred.max();
            NDArray min = pred.min();
            pred = pred.sub(min).div(max.sub(min));

            pred = pred.mul(255);
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

            pred = pred.toType(DataType.UINT8, false);
            pred.set(pred.lt(127), 0);

            if (mask) {
                pred.set(pred.gte(127), 255);
                Image img = OpenCVImageFactory.getInstance().fromNDArray(pred);
                return img;
            } else {
                pred.set(pred.gte(127), 1);
                // 黑色部分为 0， 白色 255
                oriImgArray = oriImgArray.mul(pred);
                Image img = OpenCVImageFactory.getInstance().fromNDArray(oriImgArray);
                return img;
            }
        }
    }

    @Override
    public Batchifier getBatchifier() {
        return batchifier;
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

        // release mat
        src.release();
        morphMat.release();
        gaussMat.release();
        kernel.release();

        return gaussNDArray;
    }
}