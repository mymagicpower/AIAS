package me.aias.training;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.basicdataset.cv.classification.ImageFolder;
import ai.djl.basicmodelzoo.cv.classification.ResNetV1;
import ai.djl.inference.Predictor;
import ai.djl.metric.Metrics;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Block;
import ai.djl.repository.Repository;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.TrainingResult;
import ai.djl.training.dataset.Dataset;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Pipeline;
import ai.djl.translate.TranslateException;
import cn.hutool.core.io.FileUtil;
import me.aias.training.common.Arguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Calvin
 * @date 2021-06-20
 **/
public final class TrainResNet50V1 {
    private static int batchSize = 32;
    private static int imgSize = 224;
    private static int dataSize = 1;
    private static int outChannels = 10;

    private static final Logger logger = LoggerFactory.getLogger(TrainResNet50V1.class);

    private TrainResNet50V1() {
    }

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        String modelPath = "/Users/calvin/Documents/build/training/model/";
        String dataPath = "/Users/calvin/Documents/build/training/cars";
        String testPath = "/Users/calvin/Documents/build/training/car_test";
        String modelName = "resnetv1_50";
//        TrainResNet50V1.train(modelPath, dataPath, testPath, modelName);
        Path path = Paths.get(modelPath);
        TrainResNet50V1.testSaveParameters(path,modelName);
    }


    public static TrainingResult train(String modelPath, String dataPath, String testPath, String modelName)
            throws IOException, ModelException, TranslateException {

        Arguments arguments = Arguments.parseArgs(null);
        if (arguments == null) {
            return null;
        }

        try (Model model = getModel(modelName)) {
            // get training dataset
            RandomAccessDataset trainDataset = getImgDataSet(Dataset.Usage.TRAIN.name(), dataPath,modelPath);
            RandomAccessDataset validationDataset = getImgDataSet(Dataset.Usage.VALIDATION.name(), testPath,modelPath);

            // setup training configuration
            DefaultTrainingConfig config = setupTrainingConfig(arguments);

            try (Trainer trainer = model.newTrainer(config)) {
                trainer.setMetrics(new Metrics());

                /*
                 * CIFAR10 is 32x32 image and pre processed into NCHW NDArray.
                 * 1st axis is batch axis, we can use 1 for initialization.
                 */
                Shape inputShape = new Shape(1, 3, imgSize, imgSize);

                // initialize trainer with proper input shape
                trainer.initialize(inputShape);
                EasyTrain.fit(trainer, arguments.getEpoch(), trainDataset, validationDataset);

                TrainingResult result = trainer.getTrainingResult();
                model.setProperty("Epoch", String.valueOf(result.getEpoch()));
                model.setProperty(
                        "Accuracy",
                        String.format("%.5f", result.getValidateEvaluation("Accuracy")));
                model.setProperty("Loss", String.format("%.5f", result.getValidateLoss()));

                Path path = Paths.get(modelPath);

                model.save(path, modelName);

                return result;
            }
        }
    }

    private static Model getModel(String modelName) {
        // construct new ResNet50 without pre-trained weights
        Model model = Model.newInstance(modelName);
        Block resNet50 =
                ResNetV1.builder()
                        .setImageShape(new Shape(3, imgSize, imgSize))
                        .setNumLayers(50)
                        .setOutSize(10)
                        .build();
        model.setBlock(resNet50);
        return model;
    }

    private static void testSaveParameters(Path modelPath,String modelName)
            throws IOException, ModelException, TranslateException {

        Block block = getModel(modelName).getBlock();

        Pipeline pipeline = new Pipeline()
//                    .add(new CenterCrop())
                .add(new Resize(imgSize))
//                    .add(new CenterCrop(imgSize, imgSize))
                .add(new ToTensor())
                    /*.add(
                            new Normalize(
                                    new float[] {0.485f, 0.456f, 0.406f},
                                    new float[] {0.229f, 0.224f, 0.225f}))*/;

        ImageClassificationTranslator translator =
                ImageClassificationTranslator.builder()
                        .setPipeline(pipeline)
                        .optSynsetArtifactName("synset.txt")
                        .optApplySoftmax(true)
                        .build();


        String imageUrl = "https://car2.autoimg.cn/cardfs/product/g26/M0A/5D/33/1024x0_1_q95_autohomecar__ChsEe15-6YKAULPgAAYVp-IEjZA085.jpg";
        Image img = ImageFactory.getInstance().fromUrl(imageUrl);

        Criteria<Image, Classifications> criteria =
                Criteria.builder()
                        .setTypes(Image.class, Classifications.class)
                        .optModelUrls(modelPath.toUri().toString())
                        .optTranslator(translator)
                        .optBlock(block)
                        .optModelName(modelName)
                        .optEngine("MXNet") // Use MXNet engine
                        .build();

        try (ZooModel<Image, Classifications> model = ModelZoo.loadModel(criteria);
             Predictor<Image, Classifications> predictor = model.newPredictor()) {
            Classifications classifications = predictor.predict(img);
            logger.info("Predict result: {}", classifications.topK(3));
        }

    }

    private static DefaultTrainingConfig setupTrainingConfig(Arguments arguments) {
        return new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
                .addEvaluator(new Accuracy())
                .optDevices(Device.getDevices(arguments.getMaxGpus()))
                .addTrainingListeners(TrainingListener.Defaults.logging(arguments.getOutputDir()));
    }


    private static ImageFolder getImgDataSet(String type, String path, String modelPath) throws IOException, TranslateException {

        ImageFolder dataset = ImageFolder.builder()
                .setRepository(Repository.newInstance(type, path))
                .optPipeline(
                        // create preprocess pipeline you want
                        new Pipeline()
//
                                .add(new Resize(imgSize))
//                                .add(new CenterCrop(imgSize,imgSize))
                                .add(new ToTensor())
                               /* .add(new Normalize(
                                        new float[] {0.485f, 0.456f, 0.406f},
                                        new float[] {0.229f, 0.224f, 0.225f}))*/
                )

                .setSampling(batchSize, true)
                .build();
// call prepare before using
        dataset.prepare(new ProgressBar());
        System.out.println(type);
        if ("TRAIN".equals(type)) {
            List<String> cs = dataset.getSynset();
            FileUtil.writeUtf8Lines(cs, Paths.get(modelPath).resolve("synset.txt").toUri().getPath());
            dataSize = (int) dataset.size();
            outChannels = cs.size();
            System.out.println(dataSize + "::" + cs);
        }
        return dataset;
    }
}
