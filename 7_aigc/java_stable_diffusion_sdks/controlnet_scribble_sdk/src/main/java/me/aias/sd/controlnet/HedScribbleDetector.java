package me.aias.sd.controlnet;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import me.aias.sd.utils.NDArrayUtils;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.nio.file.Paths;

public final class HedScribbleDetector implements AutoCloseable {
    private int detect_resolution = 512;
    private int image_resolution = 512;
    private boolean safe = false;
    ZooModel model;
    Predictor<Image, Image> predictor;
    private Device device;
    public HedScribbleDetector(int detect_resolution, int image_resolution, boolean safe, Device device) throws ModelException, IOException {
        this.detect_resolution = detect_resolution;
        this.image_resolution = image_resolution;
        this.safe = safe;
        this.device = device;
        this.model = ModelZoo.loadModel(criteria());
        this.predictor = model.newPredictor();
    }

    public Image predict(Image img) throws TranslateException {
        return predictor.predict(img);
    }

    public void close(){
        this.model.close();
        this.predictor.close();
    }

    private Criteria<Image, Image> criteria() {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get("models/pytorch/hed.pt"))
                        .optDevice(device)
                        .optTranslator(new FeatureTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    private final class FeatureTranslator implements Translator<Image, Image> {
        protected Batchifier batchifier = Batchifier.STACK;

        private int width;
        private int height;

        FeatureTranslator() {
        }

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            width = input.getWidth();
            height = input.getHeight();

            NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);

            int[] hw = resize64(height, width, detect_resolution);
            array = NDImageUtils.resize(array, hw[1], hw[0], Image.Interpolation.AREA);

            array = array.toType(DataType.UINT8, false);
            array = array.transpose(2, 0, 1); // HWC -> CHW RGB
            NDArray mean =
                    ctx.getNDManager().create(new float[]{104f, 117f, 123f}, new Shape(3, 1, 1));
            array = array.sub(mean);
            return new NDList(array);
        }

        @Override
        public Image processOutput(TranslatorContext ctx, NDList list) {
            NDManager manager = ctx.getNDManager();
            NDArray edge = list.singletonOrThrow();

            // safe_step
            if (safe) {
                edge = this.safe_step(edge, 2);
            }

            edge = edge.mul(255.0).clip(0, 255).toType(DataType.UINT8, false);

            NDArray nmsResult = nms(manager, edge, 127, 3.0f);

            Image image = ImageFactory.getInstance().fromNDArray(nmsResult);
            org.opencv.core.Mat src = (org.opencv.core.Mat) image.getWrappedImage();
            org.opencv.core.Mat mat = src.clone();
            Imgproc.GaussianBlur(src, mat, new Size(0, 0), 3.0f);

//            detected_map[detected_map > 4] = 255
            float[][] floats = NDArrayUtils.matToFloatArray(mat);
            NDArray detected_map = manager.create(floats);
            NDArray cutOff = detected_map.gt(4);
            detected_map.set(cutOff, 255f);

            cutOff = detected_map.lt(255);
            detected_map.set(cutOff, 0f);

            detected_map = detected_map.expandDims(0);
            Image img = ImageFactory.getInstance().fromNDArray(detected_map);

            int[] hw = resize64(height, width, image_resolution);
            detected_map = NDImageUtils.resize(img.toNDArray(manager), hw[1], hw[0], Image.Interpolation.BILINEAR);
            img = ImageFactory.getInstance().fromNDArray(detected_map);

            return img;
        }

        private NDArray nms(NDManager manager, NDArray x, float t, float s) {
            x = x.toType(DataType.FLOAT32, false);
            Image image = ImageFactory.getInstance().fromNDArray(x);
            org.opencv.core.Mat detected_map = (org.opencv.core.Mat) image.getWrappedImage();
            org.opencv.core.Mat mat = detected_map.clone();

            Imgproc.GaussianBlur(detected_map, mat, new Size(0, 0), s);

            byte[][] f1 = new byte[][]{{0, 0, 0}, {1, 1, 1}, {0, 0, 0}};
            byte[][] f2 = new byte[][]{{0, 1, 0}, {0, 1, 0}, {0, 1, 0}};
            byte[][] f3 = new byte[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
            byte[][] f4 = new byte[][]{{0, 0, 1}, {0, 1, 0}, {1, 0, 0}};

            org.opencv.core.Mat f1_mat = NDArrayUtils.uint8ArrayToMat(f1);
            org.opencv.core.Mat f2_mat = NDArrayUtils.uint8ArrayToMat(f2);
            org.opencv.core.Mat f3_mat = NDArrayUtils.uint8ArrayToMat(f3);
            org.opencv.core.Mat f4_mat = NDArrayUtils.uint8ArrayToMat(f4);

            putmask(manager, x, detected_map, mat, f1_mat);
            putmask(manager, x, detected_map, mat, f2_mat);
            putmask(manager, x, detected_map, mat, f3_mat);
            putmask(manager, x, detected_map, mat, f4_mat);

            NDArray z = manager.zeros(x.getShape());

            // z[y > t] = 255
            NDArray cutOff = x.gt(t);

            z.set(cutOff, 255f);

            return z;
        }

        private void putmask(NDManager manager, NDArray x, org.opencv.core.Mat src, org.opencv.core.Mat mat, org.opencv.core.Mat f_mat) {
            org.opencv.core.Mat dst = src.clone();
            Imgproc.dilate(mat, dst, f_mat);

            float[][] arrs = NDArrayUtils.matToFloatArray(dst);
            NDArray ndArray = manager.create(arrs);
            NDArray flags = ndArray.sub(x).lte(1);//.eq(x) 这里不能使用 eq，因为精度导致误差为1
            flags = flags.toType(DataType.FLOAT32,false);
            x.muli(flags); // 直接对x inplace 操作，节省内存/显存
//            this.set(y, x, flags);
        }

        private int[] resize64(double h, double w, double resolution) {

            double k = resolution / Math.min(h, w);
            h *= k;
            w *= k;

            int height = (int) (Math.round(h / 64.0)) * 64;
            int width = (int) (Math.round(w / 64.0)) * 64;

            return new int[]{height, width};
        }

        private NDArray safe_step(NDArray edge, int step) {
            edge = edge.toType(DataType.FLOAT32, false);
            edge = edge.mul((float) (step + 1));
            edge = edge.toType(DataType.INT32, false);
            edge = edge.toType(DataType.FLOAT32, false);
            edge = edge.div(step);
            return edge;
        }

        @Override
        public Batchifier getBatchifier() {
            return batchifier;
        }

    }
}
