package top.aias.face.quality;

import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

/**
 * 人脸质量前后处理
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class FaceFeatureTranslator implements Translator<Image, float[]> {

    public FaceFeatureTranslator() {
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
        NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
        array = array.transpose(2, 0, 1).flip(0);
        array = array.div(255.0f).sub(0.5f).div(0.5f).toType(DataType.FLOAT32, false);

        return new NDList(array);
    }

    @Override
    public float[] processOutput(TranslatorContext ctx, NDList list) {
        NDArray array = list.get(0);
        float[] result = array.toFloatArray();
        return result;
    }

    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }
}
