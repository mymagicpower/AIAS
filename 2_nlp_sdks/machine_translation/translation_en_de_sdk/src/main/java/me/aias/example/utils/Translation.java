package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

// https://www.paddlepaddle.org.cn/hubdetail?name=transformer_en-de&en_category=MachineTranslation

public final class Translation {

  private static final Logger logger = LoggerFactory.getLogger(Translation.class);

  public Translation() {}

  public Criteria<String, String[]> criteria() throws MalformedURLException {

    Criteria<String, String[]> criteria =
        Criteria.builder()
            .setTypes(String.class, String[].class)
            .optModelUrls(
                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/nlp_models/translation/translation_en_de.zip")
            //            .optModelUrls("/Users/calvin/model/transformer_en-de/")
            //            .optModelPath(
            //                Paths.get("/Users/calvin/model/transformer/transformer.zip"))
            .optTranslator(new TranslationTranslator())
            .optEngine("PaddlePaddle") // Use PyTorch engine
            .optModelName("inference")
            // .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
