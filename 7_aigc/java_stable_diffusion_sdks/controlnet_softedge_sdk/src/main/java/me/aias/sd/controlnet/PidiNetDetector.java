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
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;
import java.nio.file.Paths;

public final class PidiNetDetector implements AutoCloseable {
    private int detect_resolution = 512;
    private int image_resolution = 512;
    private boolean safe = true;
    ZooModel model;
    Predictor<Image, Image> predictor;
    private Device device;
    public PidiNetDetector(int detect_resolution, int image_resolution, boolean safe, Device device) throws ModelException, IOException {
        this.device = device;
        this.detect_resolution = detect_resolution;
        this.image_resolution = image_resolution;
        this.safe = safe;
        String type = device.getDeviceType();
        if ("gpu".equals(type)) {
            this.model = ModelZoo.loadModel(ptGpuCriteria());
        }else {
            this.model = ModelZoo.loadModel(ptCpuCriteria());
        }
        this.predictor = model.newPredictor();
    }

    public Image predict(Image img) throws TranslateException {
        return predictor.predict(img);
    }

    public void close(){
        this.model.close();
        this.predictor.close();
    }

    private Criteria<Image, Image> ptCpuCriteria() {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get("models/pytorch_cpu/pidi.pt"))
                        .optTranslator(new FeatureTranslator())
                        .optDevice(device)
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }
    private Criteria<Image, Image> ptGpuCriteria() {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get("models/pytorch_gpu/pidi.pt"))
                        .optTranslator(new FeatureTranslator())
                        .optDevice(device)
//                        .optOption("mapLocation","true")
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
            NDManager manager = ctx.getNDManager();

            NDArray edge = list.get(list.size() - 1);

            // safe_step
            if (safe) {
                edge = this.safe_step(edge, 2);
            }

            edge = edge.mul(255.0f).clip(0, 255).toType(DataType.UINT8, false);

            Image img = ImageFactory.getInstance().fromNDArray(edge);

            int[] hw = resize64(height, width, image_resolution);
            edge = NDImageUtils.resize(img.toNDArray(manager), hw[1], hw[0], Image.Interpolation.BILINEAR);

            img = ImageFactory.getInstance().fromNDArray(edge);

            return img;
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
