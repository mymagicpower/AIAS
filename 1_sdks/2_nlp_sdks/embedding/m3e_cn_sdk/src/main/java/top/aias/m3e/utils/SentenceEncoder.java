package top.aias.m3e.utils;

import ai.djl.Device;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
/**
 *
 * @author Calvin
 *
 * @email 179209347@qq.com
 **/

public final class SentenceEncoder {

  private static final Logger logger = LoggerFactory.getLogger(SentenceEncoder.class);
  private final int MAX_LENGTH = 512;
  private final HuggingFaceTokenizer tokenizer;
  public SentenceEncoder() throws IOException {
    tokenizer =
            HuggingFaceTokenizer.builder()
//                    .optPadding(true)
//                    .optPadToMaxLength()
                    .optMaxLength(MAX_LENGTH)
                    .optTokenizerPath(Paths.get("models/"))
                    .optTruncation(true)
//                    .optTokenizerName("moka-ai/m3e-base")
                    .build();
  }

  public Criteria<String, float[]> criteria() throws IOException {

    Criteria<String, float[]> criteria =
        Criteria.builder()
            .setTypes(String.class, float[].class)
            .optModelPath(Paths.get("models/traced_m3e_base_model.pt"))
            .optTranslator(new SentenceTranslator(tokenizer))
            .optEngine("PyTorch") // Use PyTorch engine
            .optDevice(Device.cpu())
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
