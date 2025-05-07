package top.aias.face.seg;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
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
/**
 * 人脸分割模型前后处理
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 **/
public class FaceSegTranslator implements Translator<Image, Image> {
    private int[] min_max = new int[]{-1, 1};

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) throws Exception {
        NDManager manager = ctx.getNDManager();

        NDArray array = input.toNDArray(manager).toType(DataType.FLOAT32, false);

        array = array.transpose(2, 0, 1).div(255f); // HWC -> CHW RGB

        NDArray mean =
                ctx.getNDManager()
                        .create(
                                new float[]{0.5f, 0.5f, 0.5f}, new Shape(3, 1, 1));
        NDArray std =
                ctx.getNDManager().create(new float[]{0.5f, 0.5f, 0.5f}, new Shape(3, 1, 1));

        array.subi(mean).divi(std);

        return new NDList(array);
    }

    @Override
    public Image processOutput(TranslatorContext ctx, NDList list) {
        NDManager manager = ctx.getNDManager();

        NDArray out = list.get(0);
        out = out.argMax(0);

        NDArray mask = manager.zeros(out.getShape());
        int[] MASK_COLORMAP = new int[]{0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 0, 255, 0, 0, 0};

        for (int i = 0; i < MASK_COLORMAP.length; i++) {
            NDArray cutOff = out.eq(i);
            mask.set(cutOff, (float)MASK_COLORMAP[i]);
        }

        //  blur the mask
        mask = mask.expandDims(0);
        Image image = ImageFactory.getInstance().fromNDArray(mask);
        org.opencv.core.Mat src = (org.opencv.core.Mat) image.getWrappedImage();
        org.opencv.core.Mat mat = src.clone();
        Imgproc.GaussianBlur(src, mat, new Size(101, 101), 11);
        org.opencv.core.Mat dst = src.clone();
        Imgproc.GaussianBlur(mat, dst, new Size(101, 101), 11);

        mask = OpenCVImageFactory.getInstance().fromImage(dst).toNDArray(manager);

        // remove the black borders
        int thres = 10;
        mask.set(new NDIndex(":" + thres + ", :"), 0);
        mask.set(new NDIndex((-thres) + ":, :"), 0);
        mask.set(new NDIndex(":, :" + thres), 0);
        mask.set(new NDIndex(":," + (-thres) + ":"), 0);
        mask = mask.toType(DataType.FLOAT32, false).div(255);

        return ImageFactory.getInstance().fromNDArray(mask);
    }

    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }
}
