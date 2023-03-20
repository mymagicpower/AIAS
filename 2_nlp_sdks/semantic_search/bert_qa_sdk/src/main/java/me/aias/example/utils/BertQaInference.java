package me.aias.example.utils;

import ai.djl.modality.nlp.qa.QAInput;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class BertQaInference {

  public BertQaInference() {}

  public Criteria<QAInput, String> criteria() {
    MxBertQATranslator translator = MxBertQATranslator.builder().setSeqLength(384).build();

    Criteria<QAInput, String> criteria =
        Criteria.builder()
            // .optApplication(Application.NLP.QUESTION_ANSWER)
            .setTypes(QAInput.class, String.class)
            .optModelPath(Paths.get("models/static_bert_qa.zip"))
            .optTranslator(translator)
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}
