package me.aias.util;

import ai.djl.Device;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://gitee.com/mymagicpower/PaddlePaddle-CrowdNet

public final class CrowdDetect {

  private static final Logger logger = LoggerFactory.getLogger(CrowdDetect.class);

  public CrowdDetect() {}

  public Criteria<Image, NDList> criteria() {

    Criteria<Image, NDList> criteria =
        Criteria.builder()
            .setTypes(Image.class, NDList.class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/sec_models/crowdnet.zip")
            //            .optModelUrls("/Users/calvin/aias_projects/PaddlePaddle-CrowdNet/infer/")
            .optEngine("PaddlePaddle")
            //            .optModelName("inference")
            .optTranslator(new CrowdTranslator())
            .optProgress(new ProgressBar())
            // .optDevice(Device.cpu())
            .build();

    return criteria;
  }

  private final class CrowdTranslator implements Translator<Image, NDList> {

    CrowdTranslator() {}

    @Override
    public NDList processOutput(TranslatorContext ctx, NDList list) {
      list.detach();
      return list;
    }

    @Override
    public NDList processInput(TranslatorContext ctx, Image input) {
      NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
      array = NDImageUtils.resize(array, 640, 480);

      array = array.div(255f);

      array = array.transpose().reshape(1, 3, 640, 480);

      if (!array.getDataType().equals(DataType.FLOAT32)) {
        array = array.toType(DataType.FLOAT32, false);
      }

      return new NDList(array);
    }

    @Override
    public Batchifier getBatchifier() {
      return null;
    }
  }
}
