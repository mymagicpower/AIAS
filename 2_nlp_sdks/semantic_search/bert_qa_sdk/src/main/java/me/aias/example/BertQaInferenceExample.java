package me.aias.example;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.nlp.qa.QAInput;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import me.aias.example.utils.BertQaInference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;                 

public final class BertQaInferenceExample {

  private static final Logger logger = LoggerFactory.getLogger(BertQaInferenceExample.class);

  private BertQaInferenceExample() {}

  public static void main(String[] args) throws IOException, TranslateException, ModelException {
    BertQaInference bertQa = new BertQaInference();
    try (ZooModel<QAInput, String> model = ModelZoo.loadModel(bertQa.criteria());
        Predictor<QAInput, String> predictor = model.newPredictor()) {

      String question = "When did Radio International start broadcasting?";
      String paragraph =
          "Radio International was a general entertainment Channel.\n"
              + "Which operated between December 1983 and April 2001.";

      QAInput input = new QAInput(question, paragraph);
      logger.info("Paragraph: {}", input.getParagraph());
      logger.info("Question: {}", input.getQuestion());

      String answer = predictor.predict(input);
      logger.info("Answer: {}", answer);
    }
  }
}
