package me.aias;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import me.aias.util.FirstOrder;
import me.aias.util.VideoUtils;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class FirstOrderExample {

  private static final Logger logger = LoggerFactory.getLogger(FirstOrderExample.class);

  private FirstOrderExample() {}

  public static void main(String[] args) throws Exception {
    // 源图
    Path imageFile = Paths.get("src/test/resources/beauty.jpg");
    // 驱动视频
    Path videoPath = Paths.get("src/test/resources/driver.mp4");
    // 生成视频
    Path outPath = Paths.get("build/output/result.mp4");

    Image image = ImageFactory.getInstance().fromFile(imageFile);
    // 获取视频关键帧
    List<Image> driverFrames = VideoUtils.getKeyFrame(videoPath.toString());
    List<BufferedImage> imgList = new ArrayList();

    FirstOrder firstOrder = new FirstOrder();
    Criteria<Image, Map> detectorCriteria = firstOrder.detector();
    Criteria<List, Image> generatorCriteria = firstOrder.generator();

    try (Predictor<Image, Map> detector = ModelZoo.loadModel(detectorCriteria).newPredictor();
        Predictor<List, Image> generator = ModelZoo.loadModel(generatorCriteria).newPredictor()) {
      Map kpSource = detector.predict(image);
      Map kpDrivingInitial = detector.predict(driverFrames.get(0));

      int total = driverFrames.size();
      // 进度条打印
      try (ProgressBar bar =
          new ProgressBarBuilder()
              .setTaskName("视频合成")
              .setStyle(ProgressBarStyle.ASCII)
              .setInitialMax(total)
              .build(); ) {
        for (Image item : driverFrames) {
          bar.step();
          List<Object> g = new ArrayList<>();
          Map kpDriving = detector.predict(item);
          g.add(image);
          g.add(kpDriving);
          g.add(kpSource);
          g.add(kpDrivingInitial);
          imgList.add((BufferedImage) generator.predict(g).getWrappedImage());
        }
      }
      VideoUtils.save(videoPath.toString(), outPath.toString(), imgList, "mp4");
    }
  }
}
