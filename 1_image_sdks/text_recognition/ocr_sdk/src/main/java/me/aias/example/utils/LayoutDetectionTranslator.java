package me.aias.example.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.paddlepaddle.zoo.cv.objectdetection.BoundFinder;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class LayoutDetectionTranslator implements Translator<Image, DetectedObjects> {

  private int width;
  private int height;

  public LayoutDetectionTranslator() {}

  @Override
  public DetectedObjects processOutput(TranslatorContext ctx, NDList list) {
    NDArray result = list.get(0); // np_boxes
    long rows = result.size(0);

    List<BoundingBox> boxes = new ArrayList<>();
    List<String> names = new ArrayList<>();
    List<Double> probs = new ArrayList<>();

    for (long i = 0; i < rows; i++) {
      NDArray row = result.get(i);
      float[] array = row.toFloatArray();
      if (array[1] <= 0.5 || array[0] <= -1) continue;
      int clsid = (int) array[0];
      double score = array[1];
      String name = "";
      switch (clsid) {
        case 0:
          name = "Text";
          break;
        case 1:
          name = "Title";
          break;
        case 2:
          name = "List";
          break;
        case 3:
          name = "Table";
          break;
        case 4:
          name = "Figure";
          break;
        default:
          name = "Unknown";
      }

      float x = array[2] / width;
      float y = array[3] / height;
      float w = (array[4] - array[2]) / width;
      float h = (array[5] - array[3]) / height;

      Rectangle rect = new Rectangle(x, y, w, h);
      boxes.add(rect);
      names.add(name);
      probs.add(score);
    }

    return new DetectedObjects(names, probs, boxes);
  }

  @Override
  public NDList processInput(TranslatorContext ctx, Image input) {
    NDArray img = input.toNDArray(ctx.getNDManager());
    width = input.getWidth();
    height = input.getHeight();

    img = NDImageUtils.resize(img, 640, 640);
    img = img.transpose(2, 0, 1).div(255);
    img =
        NDImageUtils.normalize(
            img, new float[] {0.485f, 0.456f, 0.406f}, new float[] {0.229f, 0.224f, 0.225f});
    img = img.expandDims(0);

    NDArray scale_factor = ctx.getNDManager().create(new float[] {640f / height, 640f / width});
    scale_factor = scale_factor.toType(DataType.FLOAT32, false);
    scale_factor = scale_factor.expandDims(0);

    NDArray im_shape = ctx.getNDManager().create(new float[] {640f, 640f});
    im_shape = im_shape.toType(DataType.FLOAT32, false);
    im_shape = im_shape.expandDims(0);

    // im_shape, image, scale_factor
    return new NDList(im_shape, img, scale_factor);
  }

  @Override
  public Batchifier getBatchifier() {
    return null;
  }
}
