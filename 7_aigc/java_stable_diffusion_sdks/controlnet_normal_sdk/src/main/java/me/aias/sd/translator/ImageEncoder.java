package me.aias.sd.translator;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;

/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public class ImageEncoder implements Translator<Image, NDArray> {
    private int height;
    private int width;

    public ImageEncoder(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public void prepare(TranslatorContext ctx) throws IOException {
    }

    public NDArray processOutput(TranslatorContext ctx, NDList list) {
        NDArray result = list.singletonOrThrow();
        result = result.mul(0.18215f);
        result.detach();
        return result;
    }

    public NDList processInput(TranslatorContext ctx, Image input) throws Exception {
        NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
        // model take 32-based size
        int[] size = resize32(height, width);
        array = NDImageUtils.resize(array,size[1],size[0]);
        array = array.transpose(2, 0, 1).div(255f);  // HWC -> CHW RGB
        array = array.mul(2).sub(1);
        array = array.expandDims(0);

        return new NDList(array);
    }

    private int[] resize32(double h, double w) {
        double min = Math.min(h, w);
        if (min < 32) {
            h = 32.0 / min * h;
            w = 32.0 / min * w;
        }
        int h32 = (int) h / 32;
        int w32 = (int) w / 32;
        return new int[] {h32 * 32, w32 * 32};
    }

    public Batchifier getBatchifier() {
        return null;
    }
}
