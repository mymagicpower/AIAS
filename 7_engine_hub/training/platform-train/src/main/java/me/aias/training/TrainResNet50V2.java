package me.aias.training;

import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.basicdataset.cv.classification.ImageFolder;
import ai.djl.metric.Metrics;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.BlockList;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.SymbolBlock;
import ai.djl.nn.core.Linear;
import ai.djl.repository.Repository;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
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
import me.aias.domain.TrainArgument;
import me.aias.training.listener.CheckpointsTrainingListener;
import me.aias.training.listener.UiTrainingListener;
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
public final class TrainResNet50V2 {
    private static int imgSize = 224;
    private static int dataSize = 1;
    private static int outChannels = 10;
    private static List<String> cs = null;

    private static final Logger logger = LoggerFactory.getLogger(TrainResNet50V2.class);

    public static void main(String[] args) throws ModelException, IOException, TranslateException {
        String newModelPath = "/Users/calvin/Documents/build/training/modelv2/";
        String fileRootPath = "/Users/calvin/Documents/Data_Cars_100";
        TrainArgument trainArgument = new TrainArgument();
        trainArgument.setBatchSize(32);
        trainArgument.setEpoch(1);
        trainArgument.setMaxGpus(2);
        TrainResNet50V2.train(trainArgument, newModelPath, fileRootPath);
        Path path = Paths.get(newModelPath);
    }

    public static TrainingResult train(TrainArgument trainArgument, String newModelPath, String fileRootPath)
            throws IOException, ModelException, TranslateException {
        fileRootPath = fileRootPath.replace("\\", "/");
        String dataPath = fileRootPath + "/" + "TRAIN";
        String testPath = fileRootPath + "/" + "VALIDATION";
        String modelName = "new_resnet_50";
        
        // get training dataset
        int batchSize = trainArgument.getBatchSize();
        RandomAccessDataset trainDataset = getImgDataSet(Dataset.Usage.TRAIN.name(), dataPath, newModelPath, batchSize);
        RandomAccessDataset validationDataset = getImgDataSet(Dataset.Usage.VALIDATION.name(), testPath, newModelPath, batchSize);

        try (Model model = getModel()) {
            // setup training configuration
            DefaultTrainingConfig config = setupTrainingConfig(trainArgument, newModelPath);

            try (Trainer trainer = model.newTrainer(config)) {
                trainer.setMetrics(new Metrics());

                //1st axis is batch axis, we can use 1 for initialization.
                Shape inputShape = new Shape(1, 3, imgSize, imgSize);

                // initialize trainer with proper input shape
                trainer.initialize(inputShape);
                EasyTrain.fit(trainer, trainArgument.getEpoch(), trainDataset, validationDataset);

                TrainingResult result = trainer.getTrainingResult();
                model.setProperty("Epoch", String.valueOf(result.getEpoch()));
                model.setProperty(
                        "Accuracy",
                        String.format("%.5f", result.getValidateEvaluation("Accuracy")));
                model.setProperty("Loss", String.format("%.5f", result.getValidateLoss()));

                Path path = Paths.get(newModelPath);

                SequentialBlock block = (SequentialBlock) model.getBlock();
                BlockList bl = block.getChildren();
                model.setBlock((SymbolBlock) bl.get(0).getValue());
                model.save(path, modelName);

                return result;
            }
        }
    }

    private static Model getModel()
            throws IOException, ModelNotFoundException, MalformedModelException {
        ImageClassificationTranslator translator =
                ImageClassificationTranslator.builder()
                        .addTransform(new ToTensor())
//                        .addTransform(new Normalize(Cifar10.NORMALIZE_MEAN, Cifar10.NORMALIZE_STD))
                        .optSynset(cs)
                        .optApplySoftmax(true)
                        .build();

        Criteria.Builder<Image, Classifications> builder =
                Criteria.builder()
                        .setTypes(Image.class, Classifications.class)
                        .optTranslator(translator)
                        .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/resnet50_v2.zip")
                        .optProgress(new ProgressBar())
                        .optModelName("resnet50_v2");
        // load the model
        Model model = ModelZoo.loadModel(builder.build());

        SequentialBlock newBlock = new SequentialBlock();
        SymbolBlock block = (SymbolBlock) model.getBlock();
        block.removeLastBlock();
        newBlock.add(block);
        // the original model don't include the flatten
        // so apply the flatten here
        newBlock.add(Blocks.batchFlattenBlock());
        newBlock.add(Linear.builder().setUnits(10).build());
        model.setBlock(newBlock);
        return model;
    }

    private static DefaultTrainingConfig setupTrainingConfig(TrainArgument trainArgument, String newModelPath) {
        return new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
                .addEvaluator(new Accuracy())
                .optDevices(Device.getDevices(trainArgument.getMaxGpus()))
                .addTrainingListeners(TrainingListener.Defaults.logging(newModelPath))
                .addTrainingListeners(new UiTrainingListener())
                .addTrainingListeners(new CheckpointsTrainingListener(newModelPath));
    }

    private static ImageFolder getImgDataSet(String type, String path, String modelPath, int batchSize) throws IOException, TranslateException {

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
            cs = dataset.getSynset();
            FileUtil.writeUtf8Lines(cs, Paths.get(modelPath).resolve("synset.txt").toUri().getPath());
            dataSize = (int) dataset.size();
            outChannels = cs.size();
            System.out.println(dataSize + "::" + cs);
        }
        return dataset;
    }
}
