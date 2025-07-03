package me.calvin.modules.search.face;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

/**
 * 人脸特征模型前后处理
 *
 * @author Calvin
 * @email 179209347@qq.com
 * @website www.aias.top
 */
public final class FaceFeatureTranslator implements Translator<Image, float[]> {

    public FaceFeatureTranslator() {
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
        NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);

//    float percent = 128f / Math.min(input.getWidth(), input.getHeight());
//    int resizedWidth = Math.round(input.getWidth() * percent);
//    int resizedHeight = Math.round(input.getHeight() * percent);
//        img = img.resize((resizedWidth, resizedHeight), Image.LANCZOS)

//    array = NDImageUtils.resize(array,resizedWidth,resizedHeight);
//    array = NDImageUtils.centerCrop(array,112,112);
        array = NDImageUtils.resize(array, 112, 112);

        // The network by default takes float32
        if (!array.getDataType().equals(DataType.FLOAT32)) {
            array = array.toType(DataType.FLOAT32, false);
        }

        array = array.transpose(2, 0, 1).div(255f);  // HWC -> CHW RGB

        NDArray mean =
                ctx.getNDManager().create(new float[]{0.5f, 0.5f, 0.5f}, new Shape(3, 1, 1));
        NDArray std =
                ctx.getNDManager().create(new float[]{0.5f, 0.5f, 0.5f}, new Shape(3, 1, 1));

        array = array.sub(mean);
        array = array.div(std);

//    array = array.expandDims(0);

        return new NDList(array);
    }

    @Override
    public float[] processOutput(TranslatorContext ctx, NDList list) {
        NDList result = new NDList();
        long numOutputs = list.singletonOrThrow().getShape().get(0);
        for (int i = 0; i < numOutputs; i++) {
            result.add(list.singletonOrThrow().get(i));
        }
        float[][] embeddings = result.stream().map(NDArray::toFloatArray).toArray(float[][]::new);
        float[] feature = new float[embeddings.length];
        for (int i = 0; i < embeddings.length; i++) {
            feature[i] = embeddings[i][0];
        }
        return feature;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Batchifier getBatchifier() {
        return Batchifier.STACK;
    }
}
