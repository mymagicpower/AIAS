package me.aias.example.utils.detection;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class PpWordDetectionTranslator implements Translator<Image, DetectedObjects> {

    private final int max_side_len;

    public PpWordDetectionTranslator(Map<String, ?> arguments) {
        max_side_len =
                arguments.containsKey("maxLength")
                        ? Integer.parseInt(arguments.get("maxLength").toString())
                        : 960;
    }

    @Override
    public DetectedObjects processOutput(TranslatorContext ctx, NDList list) {
        NDArray result = list.singletonOrThrow();
        result = result.squeeze().mul(255f).toType(DataType.UINT8, true).gt(0.3);   // thresh=0.3
        boolean[] flattened = result.toBooleanArray();
        Shape shape = result.getShape();
        int w = (int) shape.get(0);
        int h = (int) shape.get(1);
        boolean[][] grid = new boolean[w][h];
        IntStream.range(0, flattened.length)
                .parallel()
                .forEach(i -> grid[i / h][i % h] = flattened[i]);
        List<BoundingBox> boxes = new BoundFinder(grid).getBoxes();
        List<String> names = new ArrayList<>();
        List<Double> probs = new ArrayList<>();
        int boxSize = boxes.size();
        for (int i = 0; i < boxSize; i++) {
            names.add("word");
            probs.add(1.0);
        }
        return new DetectedObjects(names, probs, boxes);
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
        NDArray img = input.toNDArray(ctx.getNDManager());
        int h = input.getHeight();
        int w = input.getWidth();
        int resize_w = w;
        int resize_h = h;

        // limit the max side
        float ratio = 1.0f;
        if (Math.max(resize_h, resize_w) > max_side_len) {
            if (resize_h > resize_w) {
                ratio = (float) max_side_len / (float) resize_h;
            } else {
                ratio = (float) max_side_len / (float) resize_w;
            }
        }

        resize_h = (int) (resize_h * ratio);
        resize_w = (int) (resize_w * ratio);

        if (resize_h % 32 == 0) {
            resize_h = resize_h;
        } else if (Math.floor((float) resize_h / 32f) <= 1) {
            resize_h = 32;
        } else {
            resize_h = (int) Math.floor((float) resize_h / 32f) * 32;
        }

        if (resize_w % 32 == 0) {
            resize_w = resize_w;
        } else if (Math.floor((float) resize_w / 32f) <= 1) {
            resize_w = 32;
        } else {
            resize_w = (int) Math.floor((float) resize_w / 32f) * 32;
        }

        img = NDImageUtils.resize(img, resize_w, resize_h);
        img = NDImageUtils.toTensor(img);
        img =
                NDImageUtils.normalize(
                        img,
                        new float[]{0.485f, 0.456f, 0.406f},
                        new float[]{0.229f, 0.224f, 0.225f});
        img = img.expandDims(0);
        return new NDList(img);
    }

    @Override
    public Batchifier getBatchifier() {
        return null;
    }

}
