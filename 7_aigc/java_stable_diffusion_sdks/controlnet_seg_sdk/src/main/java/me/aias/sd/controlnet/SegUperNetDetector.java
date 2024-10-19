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
import ai.djl.ndarray.index.NDIndex;
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

import java.io.IOException;
import java.nio.file.Paths;

public final class SegUperNetDetector implements AutoCloseable {
    private int detect_resolution = 512;
    private int image_resolution = 512;
    private boolean safe = false;
    ZooModel model;
    Predictor<Image, Image> predictor;
    private Device device;
    public SegUperNetDetector(int detect_resolution, int image_resolution, boolean safe, Device device) throws ModelException, IOException {
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

    // onnx 转换失败，部分算子不支持
    private Criteria<Image, Image> criteria() {

        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get("models/pytorch/upernet.pt"))
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

            // do_resize
            array = NDImageUtils.resize(array, 512, 512, Image.Interpolation.AREA);

            // do_rescale rescale_factor = 1/255
            array = array.div(255.0f); // HWC3

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
            NDManager manager = ctx.getNDManager();
            NDArray logits = list.singletonOrThrow();

            logits = logits.transpose(1, 2, 0);

            NDArray resized_logits = NDImageUtils.resize(logits, width, height, Image.Interpolation.BILINEAR);

            resized_logits = resized_logits.transpose(2, 0, 1);

            NDArray semantic_map = resized_logits.argMax(0);

            NDArray color_seg = manager.zeros(new Shape(semantic_map.getShape().get(0), semantic_map.getShape().get(1), 3), DataType.INT32);

            int[][] ada_palette = this.pallate();

            for (int label = 0; label < ada_palette.length; label++) {
                NDArray index = semantic_map.eq(label);
                int b = ada_palette[label][0];
                int g = ada_palette[label][1];
                int r = ada_palette[label][2];
                NDArray bChannel = color_seg.get(new NDIndex(":,:,0"));
                NDArray gChannel = color_seg.get(new NDIndex(":,:,1"));
                NDArray rChannel = color_seg.get(new NDIndex(":,:,2"));
                bChannel.set(index, b);
                gChannel.set(index, g);
                rChannel.set(index, r);
            }

            Image img = ImageFactory.getInstance().fromNDArray(color_seg);
            return img;
        }

        @Override
        public Batchifier getBatchifier() {
            return batchifier;
        }

        private int[][] pallate() {
            int[][] ada_palette = new int[][]{
                    {0, 0, 0},
                    {120, 120, 120},
                    {180, 120, 120},
                    {6, 230, 230},
                    {80, 50, 50},
                    {4, 200, 3},
                    {120, 120, 80},
                    {140, 140, 140},
                    {204, 5, 255},
                    {230, 230, 230},
                    {4, 250, 7},
                    {224, 5, 255},
                    {235, 255, 7},
                    {150, 5, 61},
                    {120, 120, 70},
                    {8, 255, 51},
                    {255, 6, 82},
                    {143, 255, 140},
                    {204, 255, 4},
                    {255, 51, 7},
                    {204, 70, 3},
                    {0, 102, 200},
                    {61, 230, 250},
                    {255, 6, 51},
                    {11, 102, 255},
                    {255, 7, 71},
                    {255, 9, 224},
                    {9, 7, 230},
                    {220, 220, 220},
                    {255, 9, 92},
                    {112, 9, 255},
                    {8, 255, 214},
                    {7, 255, 224},
                    {255, 184, 6},
                    {10, 255, 71},
                    {255, 41, 10},
                    {7, 255, 255},
                    {224, 255, 8},
                    {102, 8, 255},
                    {255, 61, 6},
                    {255, 194, 7},
                    {255, 122, 8},
                    {0, 255, 20},
                    {255, 8, 41},
                    {255, 5, 153},
                    {6, 51, 255},
                    {235, 12, 255},
                    {160, 150, 20},
                    {0, 163, 255},
                    {140, 140, 140},
                    {250, 10, 15},
                    {20, 255, 0},
                    {31, 255, 0},
                    {255, 31, 0},
                    {255, 224, 0},
                    {153, 255, 0},
                    {0, 0, 255},
                    {255, 71, 0},
                    {0, 235, 255},
                    {0, 173, 255},
                    {31, 0, 255},
                    {11, 200, 200},
                    {255, 82, 0},
                    {0, 255, 245},
                    {0, 61, 255},
                    {0, 255, 112},
                    {0, 255, 133},
                    {255, 0, 0},
                    {255, 163, 0},
                    {255, 102, 0},
                    {194, 255, 0},
                    {0, 143, 255},
                    {51, 255, 0},
                    {0, 82, 255},
                    {0, 255, 41},
                    {0, 255, 173},
                    {10, 0, 255},
                    {173, 255, 0},
                    {0, 255, 153},
                    {255, 92, 0},
                    {255, 0, 255},
                    {255, 0, 245},
                    {255, 0, 102},
                    {255, 173, 0},
                    {255, 0, 20},
                    {255, 184, 184},
                    {0, 31, 255},
                    {0, 255, 61},
                    {0, 71, 255},
                    {255, 0, 204},
                    {0, 255, 194},
                    {0, 255, 82},
                    {0, 10, 255},
                    {0, 112, 255},
                    {51, 0, 255},
                    {0, 194, 255},
                    {0, 122, 255},
                    {0, 255, 163},
                    {255, 153, 0},
                    {0, 255, 10},
                    {255, 112, 0},
                    {143, 255, 0},
                    {82, 0, 255},
                    {163, 255, 0},
                    {255, 235, 0},
                    {8, 184, 170},
                    {133, 0, 255},
                    {0, 255, 92},
                    {184, 0, 255},
                    {255, 0, 31},
                    {0, 184, 255},
                    {0, 214, 255},
                    {255, 0, 112},
                    {92, 255, 0},
                    {0, 224, 255},
                    {112, 224, 255},
                    {70, 184, 160},
                    {163, 0, 255},
                    {153, 0, 255},
                    {71, 255, 0},
                    {255, 0, 163},
                    {255, 204, 0},
                    {255, 0, 143},
                    {0, 255, 235},
                    {133, 255, 0},
                    {255, 0, 235},
                    {245, 0, 255},
                    {255, 0, 122},
                    {255, 245, 0},
                    {10, 190, 212},
                    {214, 255, 0},
                    {0, 204, 255},
                    {20, 0, 255},
                    {255, 255, 0},
                    {0, 153, 255},
                    {0, 41, 255},
                    {0, 255, 204},
                    {41, 0, 255},
                    {41, 255, 0},
                    {173, 0, 255},
                    {0, 245, 255},
                    {71, 0, 255},
                    {122, 0, 255},
                    {0, 255, 184},
                    {0, 92, 255},
                    {184, 255, 0},
                    {0, 133, 255},
                    {255, 214, 0},
                    {25, 194, 194},
                    {102, 255, 0},
                    {92, 0, 255},
            };

            return ada_palette;
        }

    }
}
