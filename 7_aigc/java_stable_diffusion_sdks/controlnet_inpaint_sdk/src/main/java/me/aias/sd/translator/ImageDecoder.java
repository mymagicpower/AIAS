package me.aias.sd.translator;

import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.opencv.OpenCVImageFactory;
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

public class ImageDecoder implements Translator<NDArray, Image> {
    private int height;
    private int width;

    public ImageDecoder(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public void prepare(TranslatorContext ctx) throws IOException {
    }

    public NDList processInput(TranslatorContext ctx, NDArray  input) throws Exception {
        input = input.div(0.18215);
        return new NDList(input);
    }
    public Image processOutput(TranslatorContext ctx, NDList list) {
        NDArray scaled = list.get(0).div(2).add(0.5).clip(0, 1);
        scaled = scaled.transpose(0, 2, 3, 1);
        scaled = scaled.mul(255).round().toType(DataType.INT8, true).get(0);
        return OpenCVImageFactory.getInstance().fromNDArray(scaled);
    }

    public Batchifier getBatchifier() {
        return null;
    }
}
