package me.aias.ocr.inference;

import ai.djl.Model;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
public class PpWordRecognitionTranslator implements Translator<Image, String> {

    private List<String> table;

    @Override
    public void prepare(TranslatorContext ctx) throws IOException {
        Model model = ctx.getModel();
        //    ppocr_keys_v1.txt
        try (InputStream is = model.getArtifact("ppocr_keys_v1.txt").openStream()) {
            table = Utils.readLines(is, true);
            table.add(0, "blank");
            table.add("");
        }
    }

    @Override
    public String processOutput(TranslatorContext ctx, NDList list) throws IOException {
        StringBuilder sb = new StringBuilder();
        NDArray tokens = list.singletonOrThrow();
        long[] indices = tokens.get(0).argMax(1).toLongArray();
        int lastIdx = 0;
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] > 0 && !(i > 0 && indices[i] == lastIdx)) {
                sb.append(table.get((int) indices[i]));
            }
        }
        return sb.toString();
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
        NDArray img = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
        //    int[] hw = resize32(input.getHeight(), input.getWidth());

        int h = input.getHeight();
        int w = input.getWidth();
        float ratio = (float) w / (float) h;
        int resized_w = (int) (Math.ceil(32 * ratio));

        //    img = NDImageUtils.resize(img, hw[1], hw[0]);
        img = NDImageUtils.resize(img, resized_w, 32);

        img = NDImageUtils.toTensor(img).sub(0.5f).div(0.5f);

        img = img.expandDims(0);
        return new NDList(img);
    }

    @Override
    public Batchifier getBatchifier() {
        return null;
    }

    private int[] resize32(double h, double w) {
        double h32Ratio = h / 32d;
        double w32 = w / h32Ratio;
        int w32Ratio = (int) Math.round(w32 / 32d);
        return new int[]{32, w32Ratio * 32}; // height: 32 (fixed), width: 32 * N
    }
}
