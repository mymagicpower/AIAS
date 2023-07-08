package me.aias.example.utils;

import ai.djl.util.Utils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public final class WordEncoder {

  List<String> words = null;
  float[][] embeddings = null;

  public WordEncoder(Path vocab, Path embedding) throws IOException {
    try (InputStream is = new FileInputStream(new File(vocab.toString()))) {
      words = Utils.readLines(is, false);
    }

    File file = new File(embedding.toString());
    INDArray array = Nd4j.readNpy(file);
    embeddings = array.toFloatMatrix();
  }

  public float[] search(String word) {
    for (int i = 0; i < words.size(); i++) {
      if (words.get(i).equals(word)) {
        return embeddings[i];
      }
    }
    return null;
  }

  public float cosineSim(float[] feature1, float[] feature2) {
    float ret = 0.0f;
    float mod1 = 0.0f;
    float mod2 = 0.0f;
    int length = feature1.length;
    for (int i = 0; i < length; ++i) {
      ret += feature1[i] * feature2[i];
      mod1 += feature1[i] * feature1[i];
      mod2 += feature2[i] * feature2[i];
    }
    return (float) ((ret / Math.sqrt(mod1) / Math.sqrt(mod2) + 1) / 2.0f);
  }
}
