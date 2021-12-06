package me.calvin.modules.search.service;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.util.List;

public interface FeatureService {
  List<Float> feature(String newModelPath, Image img) throws ModelException, TranslateException, IOException;
  List<Float> commonFeature(Image img) throws IOException, ModelException, TranslateException;
  List<Float> faceFeature(Image img) throws IOException, ModelException, TranslateException;
}
