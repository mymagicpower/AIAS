package top.aias.ocr.utils;

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

import java.io.IOException;
import java.nio.file.Paths;

/**
 * 文本超分辨
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public final class TextSrPt implements AutoCloseable {

    private int detectResolution = 512;
    ZooModel model;
    Predictor<Image, Image> predictor;
    private Device device;

    public TextSrPt(int detectResolution, Device device) throws ModelException, IOException {
        this.device = device;
        this.detectResolution = detectResolution;
        this.model = ModelZoo.loadModel(ptCriteria());
        this.predictor = model.newPredictor();
    }

    public Image predict(Image img) throws TranslateException {
        return predictor.predict(img);
    }

    public void close(){
        this.model.close();
        this.predictor.close();
    }

    private Criteria<Image, Image> ptCriteria() {
        Criteria<Image, Image> criteria =
                Criteria.builder()
                        .optEngine("PyTorch")
                        .setTypes(Image.class, Image.class)
                        .optModelPath(Paths.get("models/textbsr_traced_model.pt"))
                        .optDevice(device)
                        .optTranslator(new TextSrTranslator())
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    /**
     * 模型前后处理
     *
     * @author Calvin
     * @mail 179209347@qq.com
     * @website www.aias.top
     **/
    private final class TextSrTranslator implements Translator<Image, Image> {
        private NDManager manager;

        @Override
        public void prepare(TranslatorContext ctx){
            this.manager = NDManager.newBaseManager(ctx.getNDManager().getDevice(), "PyTorch");
        }

        @Override
        public NDList processInput(TranslatorContext ctx, Image input) {
            NDArray array = input.toNDArray(this.manager).toType(DataType.FLOAT32, false);
            float up_s = (float) detectResolution / (float) input.getHeight();
            int resized_w = (int) (up_s * input.getWidth());
            array = NDImageUtils.resize(array, resized_w, detectResolution);
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
            NDArray output_img = list.singletonOrThrow();
            output_img = output_img.mul(0.5f).add(0.5f);
            output_img = output_img.clip(0, 1);
            output_img = output_img.mul(255.0f).round().toType(DataType.UINT8, false);

            Image img = ImageFactory.getInstance().fromNDArray(output_img);

            // release resource
            this.manager.close();

            return img;
        }

        @Override
        public Batchifier getBatchifier() {
            return Batchifier.STACK;
        }
    }

}
