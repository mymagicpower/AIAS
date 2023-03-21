package me.aias.util;

import ai.djl.Model;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Calvin
 * @email 179209347@qq.com
 */
public class AnimalTranslator implements Translator<Image, Classifications> {
    List<String> classes;
    
    public AnimalTranslator() {
    }

    @Override
    public void prepare(TranslatorContext ctx) throws IOException {
        Model model = ctx.getModel();
        try (InputStream is = model.getArtifact("label_list.txt").openStream()) {
            classes = Utils.readLines(is, true);
//            classes.add(0, "blank");
//            classes.add("");
        }
    }

    public Classifications processOutput(TranslatorContext ctx, NDList list) {
        NDArray prob = list.singletonOrThrow();
        return new Classifications(this.classes, prob);
    }

    public NDList processInput(TranslatorContext ctx, Image input) throws Exception {
        NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);

        float percent = 256f / Math.min(input.getWidth(), input.getHeight());
        int resizedWidth = Math.round(input.getWidth() * percent);
        int resizedHeight = Math.round(input.getHeight() * percent);
//        img = img.resize((resizedWidth, resizedHeight), Image.LANCZOS)
        
        array = NDImageUtils.resize(array,resizedWidth,resizedHeight);
        array = NDImageUtils.centerCrop(array,224,224);

        // The network by default takes float32
        if (!array.getDataType().equals(DataType.FLOAT32)) {
            array = array.toType(DataType.FLOAT32, false);
        }

        array = array.transpose(2, 0, 1).div(255f);  // HWC -> CHW RGB
        
        NDArray mean =
                ctx.getNDManager().create(new float[] {0.485f, 0.456f, 0.406f}, new Shape(3, 1, 1));
        NDArray std =
                ctx.getNDManager().create(new float[] {0.229f, 0.224f, 0.225f}, new Shape(3, 1, 1));

        array = array.sub(mean);
        array = array.div(std);

        array = array.expandDims(0);
        
        return new NDList(array);
    }

    public Batchifier getBatchifier() {
        return null;
    }
}
