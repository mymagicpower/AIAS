package me.calvin.example.utils;

import ai.djl.modality.nlp.qa.QAInput;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BertQaInference {

  private static final Logger logger = LoggerFactory.getLogger(BertQaInference.class);

  public BertQaInference() {}

  public Criteria<QAInput, String> criteria() {
    MxBertQATranslator translator = MxBertQATranslator.builder().setSeqLength(384).build();

    Criteria<QAInput, String> criteria =
        Criteria.builder()
            .setTypes(QAInput.class, String.class)
            .optModelUrls(
                "https://djl-model.oss-cn-hongkong.aliyuncs.com/models/nlp_models/static_bert_qa.zip")
            .optTranslator(translator)
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
