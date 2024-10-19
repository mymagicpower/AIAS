package me.aias.example.utils;

import ai.djl.Device;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * https://www.paddlepaddle.org.cn/hubdetail?name=transformer_en-de&en_category=MachineTranslation
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public final class Translation {

  private static final Logger logger = LoggerFactory.getLogger(Translation.class);

  public Translation() {}

  public Criteria<String, String[]> criteria() throws MalformedURLException {

    Criteria<String, String[]> criteria =
        Criteria.builder()
            .setTypes(String.class, String[].class)
            .optModelPath(Paths.get("models/translation_en_de.zip"))
            .optTranslator(new TranslationTranslator())
            .optEngine("PaddlePaddle") // Use PyTorch engine
            .optModelName("inference")
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
