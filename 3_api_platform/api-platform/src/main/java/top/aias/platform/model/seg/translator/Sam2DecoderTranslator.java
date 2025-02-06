package top.aias.platform.model.seg.translator;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Mask;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;
import top.aias.platform.bean.Sam2Input;

import java.util.Collections;
import java.util.List;
/**
 * sam2 解码前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class Sam2DecoderTranslator implements NoBatchifyTranslator<Sam2Input, DetectedObjects> {
    public Sam2DecoderTranslator() {
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Sam2Input input) {
        Image image = input.getImage();
        int width = image.getWidth();
        int height = image.getHeight();
        ctx.setAttachment("width", width);
        ctx.setAttachment("height", height);

        float[] buf = input.toLocationArray(width, height);

        NDManager manager = ctx.getNDManager();
        NDArray locations = manager.create(buf, new Shape(1, buf.length / 2, 2));
        NDArray labels = manager.create(input.getLabels());

        NDList embeddings = input.getEmbeddings();

        NDArray mask = manager.zeros(new Shape(1, 1, 256, 256));
        NDArray hasMask = manager.zeros(new Shape(1));
        return new NDList(
                embeddings.get(2),
                embeddings.get(0),
                embeddings.get(1),
                locations,
                labels,
                mask,
                hasMask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DetectedObjects processOutput(TranslatorContext ctx, NDList list) {
        try (NDManager manager = NDManager.newBaseManager(Device.cpu(), "PyTorch")) {
            NDArray logits = list.get(0);
            NDArray scores = list.get(1);
            logits.attach(manager);
            scores.attach(manager);
            scores = scores.squeeze(0);
            long best = scores.argMax().getLong();

            int width = (Integer) ctx.getAttachment("width");
            int height = (Integer) ctx.getAttachment("height");

            long[] size = {height, width};
            int mode = Image.Interpolation.BILINEAR.ordinal();
            logits = logits.getNDArrayInternal().interpolation(size, mode, false);
            NDArray masks = logits.gt(0f).squeeze(0);

            float[][] dist = Mask.toMask(masks.get(best).toType(DataType.FLOAT32, true));
            Mask mask = new Mask(0, 0, width, height, dist, true);
            double probability = scores.getFloat(best);

            List<String> classes = Collections.singletonList("");
            List<Double> probabilities = Collections.singletonList(probability);
            List<BoundingBox> boxes = Collections.singletonList(mask);

            return new DetectedObjects(classes, probabilities, boxes);
        }
    }
}