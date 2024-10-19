package me.aias.sd.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.opencv.OpenCVImageFactory;
import org.opencv.core.Mat;

public class ShuffleUtils {

    public static Image hwcContentShuffle(NDManager manager, Image image, int detect_resolution, int image_resolution) {

        NDArray array = image.toNDArray(manager).toType(DataType.UINT8, false);
        int[] hw = resize64(image.getHeight(), image.getWidth(), detect_resolution);
        int h = hw[0];
        int w = hw[1];
        int c = (int) (array.getShape().get(2));
        array = NDImageUtils.resize(array, h, w, Image.Interpolation.AREA);
        NDArray x = make_noise_disk(manager, h, w, 1, 256).mul((float) (w - 1));
        NDArray y = make_noise_disk(manager, h, w, 1, 256).mul((float) (h - 1));
        NDArray flow = x.concat(y, 2).toType(DataType.FLOAT32, false);
        Mat map1 = NDArrayUtils.floatNDArrayToMat(flow, 2);

        Image img = ImageFactory.getInstance().fromNDArray(array);
        Mat src = (Mat) img.getWrappedImage();
        Mat map2 = new Mat();
        Mat mat = OpenCVUtils.remap(src, map1, map2);
        img = OpenCVImageFactory.getInstance().fromImage(mat);

        hw = resize64(img.getHeight(), img.getWidth(), image_resolution);
        array = NDImageUtils.resize(img.toNDArray(manager), hw[1], hw[0], Image.Interpolation.BILINEAR);
        img = ImageFactory.getInstance().fromNDArray(array);

        return img;
    }

    private static int[] resize64(double h, double w, double resolution) {
        double k = resolution / Math.min(h, w);
        h *= k;
        w *= k;

        int height = (int) (Math.round(h / 64.0)) * 64;
        int width = (int) (Math.round(w / 64.0)) * 64;

        return new int[]{height, width};
    }

    private static NDArray make_noise_disk(NDManager manager, int H, int W, int C, int F) {
        ai.djl.ndarray.types.Shape shape = new ai.djl.ndarray.types.Shape((int) Math.floor(H / F) + 2, (int) Math.floor(W / F) + 2, C);
        NDArray noise = manager.randomUniform(0, 1, shape);
        noise = NDImageUtils.resize(noise, W + 2 * F, H + 2 * F, Image.Interpolation.BICUBIC);
        noise = noise.get(new NDIndex(F + ":" + (F + H) + "," + F + ":" + (F + W)));
        noise = noise.sub(noise.min());
        noise = noise.div(noise.max());
        return noise;
    }

}
