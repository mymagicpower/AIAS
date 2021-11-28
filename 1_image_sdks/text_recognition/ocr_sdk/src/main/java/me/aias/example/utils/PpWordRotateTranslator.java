package me.aias.example.utils;

import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.util.Arrays;
import java.util.List;

public class PpWordRotateTranslator implements Translator<Image, Classifications> {
  List<String> classes = Arrays.asList("No Rotate", "Rotate");

  public PpWordRotateTranslator() {}

  public Classifications processOutput(TranslatorContext ctx, NDList list) {
    NDArray prob = list.singletonOrThrow();
    return new Classifications(this.classes, prob);
  }

  public NDList processInput(TranslatorContext ctx, Image input) throws Exception {
    NDArray img = input.toNDArray(ctx.getNDManager());
    img = NDImageUtils.resize(img, 192, 48);
    img = NDImageUtils.toTensor(img).sub(0.5F).div(0.5F);
    img = img.expandDims(0);
    return new NDList(new NDArray[]{img});
  }
  
  public NDList processInputBak(TranslatorContext ctx, Image input) throws Exception {
    NDArray img = input.toNDArray(ctx.getNDManager());
    int imgC = 3;
    int imgH = 48;
    int imgW = 192;

    NDArray array = ctx.getNDManager().zeros(new Shape(imgC, imgH, imgW));

    int h = input.getHeight();
    int w = input.getWidth();
    int resized_w = 0;

    float ratio = (float) w / (float) h;
    if (Math.ceil(imgH * ratio) > imgW) {
      resized_w = imgW;
    } else {
      resized_w = (int) (Math.ceil(imgH * ratio));
    }

    img = NDImageUtils.resize(img, resized_w, imgH);

    img = NDImageUtils.toTensor(img).sub(0.5F).div(0.5F);
    //    img = img.transpose(2, 0, 1);

    array.set(new NDIndex(":,:,0:" + resized_w), img);

    array = array.expandDims(0);

    return new NDList(new NDArray[] {array});
  }

  public Batchifier getBatchifier() {
    return null;
  }
}
