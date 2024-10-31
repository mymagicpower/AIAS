package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FaceMaskDetect {

  private static final Logger logger = LoggerFactory.getLogger(FaceMaskDetect.class);

  public FaceMaskDetect() {}

  public DetectedObjects predict(
      Predictor<Image, DetectedObjects> faceDetector,
      Predictor<Image, Classifications> classifier,
      Image image)
      throws TranslateException {

    DetectedObjects detections = faceDetector.predict(image);
    List<DetectedObjects.DetectedObject> faces = detections.items();

    List<String> names = new ArrayList<>();
    List<Double> prob = new ArrayList<>();
    List<BoundingBox> rect = new ArrayList<>();
    for (DetectedObjects.DetectedObject face : faces) {
      Image subImg = getSubImage(image, face.getBoundingBox());
      Classifications classifications = classifier.predict(subImg);
      names.add(classifications.best().getClassName());
      prob.add(face.getProbability());
      rect.add(face.getBoundingBox());
    }

    return new DetectedObjects(names, prob, rect);
  }

  public Criteria<Image, Classifications> criteria() {
    Criteria<Image, Classifications> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, Classifications.class)
            .optTranslator(
                ImageClassificationTranslator.builder()
                    .addTransform(new Resize(128, 128))
                    .addTransform(new ToTensor()) // HWC -> CHW div(255)
                    .addTransform(
                        new Normalize(
                            new float[] {0.5f, 0.5f, 0.5f}, new float[] {1.0f, 1.0f, 1.0f}))
                    .addTransform(nd -> nd.flip(0)) // RGB -> GBR
                    .build())
            .optModelPath(Paths.get("models/face_mask.zip"))
            .optProgress(new ProgressBar())
            
            .build();

    return criteria;
  }

  private int[] extendSquare(
      double xmin, double ymin, double width, double height, double percentage) {
    double centerx = xmin + width / 2;
    double centery = ymin + height / 2;
    double maxDist = Math.max(width / 2, height / 2) * (1 + percentage);
    return new int[] {(int) (centerx - maxDist), (int) (centery - maxDist), (int) (2 * maxDist)};
    //    return new int[] {(int) xmin, (int) ymin, (int) width, (int) height};
  }

  private Image getSubImage(Image img, BoundingBox box) {
    Rectangle rect = box.getBounds();
    int width = img.getWidth();
    int height = img.getHeight();
    int[] squareBox =
        extendSquare(
            rect.getX() * width,
            rect.getY() * height,
            rect.getWidth() * width,
            rect.getHeight() * height,
            0); // 0.18
    return img.getSubImage(squareBox[0], squareBox[1], squareBox[2], squareBox[2]);
    //    return img.getSubimage(squareBox[0], squareBox[1], squareBox[2], squareBox[3]);
  }
}
