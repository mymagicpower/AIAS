package me.aias.example;

import ai.djl.MalformedModelException;
import ai.djl.ModelException;
import ai.djl.basicdataset.nlp.CookingStackExchange;
import ai.djl.fasttext.FtModel;
import ai.djl.fasttext.FtTrainingConfig;
import ai.djl.fasttext.FtVocabulary;
import ai.djl.fasttext.FtWord2VecWordEmbedding;
import ai.djl.modality.Classifications;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.training.TrainingResult;
import me.aias.example.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FastTextExample {

    private static final Logger logger = LoggerFactory.getLogger(FastTextExample.class);

    @Test
    public void testTrainTextClassification() throws IOException, MalformedModelException {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            throw new SkipException("fastText is not supported on windows");
        }
        try (FtModel model = new FtModel("cooking")) {
            CookingStackExchange dataset = CookingStackExchange.builder().build();

            // setup training configuration
            FtTrainingConfig config =
                    FtTrainingConfig.builder()
                            .setOutputDir(Paths.get("build"))
                            .setModelName("cooking")
                            .optEpoch(5)
                            .optLoss(FtTrainingConfig.FtLoss.HS)
                            .build();

            TrainingResult result = model.fit(config, dataset);
            Assert.assertEquals(result.getEpoch(), 5);
            Assert.assertTrue(Files.exists(Paths.get("build/cooking.bin")));
        }

        testTextClassification();
        testWord2Vec();
    }

    public void testTextClassification()
            throws IOException, MalformedModelException{
        Path path = Paths.get("build/");
        Path modelFile = path.resolve("cooking.bin");

        try (FtModel model = new FtModel("cooking")) {
            model.load(modelFile);
            String text = "Which baking dish is best to bake a banana bread ?";
            Classifications result = model.classify(text, 5);

            logger.info("{}", result);
        }
    }

    public void testWord2Vec() throws IOException, MalformedModelException {
        Path path = Paths.get("build/");
        Path modelFile = path.resolve("cooking.bin");
        try (FtModel ftModel = new FtModel("cooking");
             NDManager manager = NDManager.newBaseManager()) {
            ftModel.load(modelFile);
            FtWord2VecWordEmbedding fasttextWord2VecWordEmbedding =
                    new FtWord2VecWordEmbedding(ftModel, new FtVocabulary());
            long index = fasttextWord2VecWordEmbedding.preprocessWordToEmbed("bread");
            NDArray embedding = fasttextWord2VecWordEmbedding.embedWord(manager, index);
            float[] array = embedding.toFloatArray();
            logger.info("{}", Arrays.toString(array));
        }
    }

    @Test
    public void testBlazingText() throws IOException, ModelException {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            throw new SkipException("fastText is not supported on windows");
        }
        //下载  text_classification.bin
        logger.info("下载 text_classification.bin");
        FileUtils.downloadModel();
        
        Path path = Paths.get("build/tmp/model");
        Path modelFile = path.resolve("text_classification.bin");

        try (FtModel model = new FtModel("text_classification")) {
            model.load(modelFile);
            String text =
                    "Convair was an american aircraft manufacturing company which later expanded into rockets and spacecraft .";
            Classifications result = model.classify(text, 5);

            logger.info("{}", result);
            Assert.assertEquals(result.item(0).getClassName(), "Company");
        }
    }
}