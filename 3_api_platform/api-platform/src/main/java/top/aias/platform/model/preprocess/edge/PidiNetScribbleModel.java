package top.aias.platform.model.preprocess.edge;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import top.aias.platform.model.preprocess.ControlPool;
import top.aias.platform.utils.NDArrayUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Scribble 涂鸦 PidiNet 模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class PidiNetScribbleModel implements AutoCloseable {
    private int detect_resolution = 512;
    private int image_resolution = 512;
    private boolean safe = false;
    private ZooModel<Image, Image> model;
    private Device device;
    private ControlPool pool;
    private String modelPath;
    private int poolSize;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public PidiNetScribbleModel(int detect_resolution, int image_resolution, String modelPath, boolean safe, int poolSize, Device device) {
        this.detect_resolution = detect_resolution;
        this.image_resolution = image_resolution;
        this.modelPath = modelPath;
        this.safe = safe;
        this.poolSize = poolSize;
        this.device = device;
    }

    public synchronized void ensureInitialized() {
        if (!initialized.get()) {
            try {
                this.model = ModelZoo.loadModel(criteria());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ModelNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedModelException e) {
                e.printStackTrace();
            }
            this.pool = new ControlPool(model, poolSize);
            initialized.set(true);
        }
    }

    public Image predict(Image img) throws TranslateException, ModelException, IOException {
        ensureInitialized();
        Predictor<Image, Image> predictor = pool.getPredictor();
        Image result = predictor.predict(img);
        pool.releasePredictor(predictor);
        return result;
    }

    public void close() {
        if (initialized.get()) {
            model.close();
            pool.close();
        }
    }

    private Criteria<Image, Image> criteria() {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get(modelPath)) // pidi_cpu.pt, pidi_gpu.pt
                        .optTranslator(new FeatureTranslator())
                        .optDevice(device)
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

            array = array.toType(DataType.UINT8, false);

            int[] hw = resize64(height, width, detect_resolution);

            array = NDImageUtils.resize(array, hw[1], hw[0], Image.Interpolation.AREA);
            array = array.flip(2);

            array = array.transpose(2, 0, 1).div(255.0f); // HWC -> CHW RGB

            return new NDList(array);
        }

        @Override
        public Image processOutput(TranslatorContext ctx, NDList list) {
            NDManager manager = NDManager.newBaseManager(ctx.getNDManager().getDevice(), "PyTorch");

            NDArray edge = list.get(list.size() - 1);

            // safe_step
            if (safe) {
                edge = this.safe_step(edge, 2);
            }

            edge = edge.mul(255.0f).clip(0, 255).toType(DataType.UINT8, false);

            Image edgeImg = ImageFactory.getInstance().fromNDArray(edge);

            int[] hw = resize64(height, width, image_resolution);
            edge = NDImageUtils.resize(edgeImg.toNDArray(manager), hw[1], hw[0], Image.Interpolation.BILINEAR);

            edge = edge.get(new NDIndex(":,:,0"));
            edge = edge.expandDims(0);
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
