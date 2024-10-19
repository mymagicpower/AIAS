package me.aias.sd.pipelines;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;
import me.aias.sd.scheduler.PNDMScheduler;
import me.aias.sd.translator.ImageDecoder;
import me.aias.sd.translator.ImageEncoder;
import me.aias.sd.translator.TextEncoder;

import java.io.IOException;
import java.nio.file.Paths;

public class StableDiffusionTxt2ImgPipeline implements AutoCloseable {

    private static final int HEIGHT = 512;
    private static final int WIDTH = 512;
    private static final int OFFSET = 1;
    private static final float GUIDANCE_SCALE = 7.5f;
    ZooModel vaeDecoderModel;
    private Predictor<NDArray, Image> vaeDecoderPredictor;
    ZooModel textEncoderModel;
    private Predictor<String, NDList> textEncoderPredictor;
    ZooModel unetModel;
    private Predictor<NDList, NDList> unetPredictor;
    private Device device;

    public StableDiffusionTxt2ImgPipeline(String root, Device device) throws ModelException, IOException {
        this.device = device;

        Criteria<NDList, NDList> unetCriteria =
                Criteria.builder()
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get(root + "unet.pt"))
                        .optEngine("PyTorch")
                        .optProgress(new ProgressBar())
                        .optTranslator(new NoopTranslator())
                        .optDevice(device)
                        .build();
        this.unetModel = unetCriteria.loadModel();
        this.unetPredictor = this.unetModel.newPredictor();

        Criteria<NDArray, Image> vaeDecoderCriteria =
                Criteria.builder()
                        .setTypes(NDArray.class, Image.class)
                        .optModelPath(Paths.get(root + "vae_decoder.pt"))
                        .optEngine("PyTorch")
                        .optTranslator(new ImageDecoder(HEIGHT,WIDTH))
                        .optProgress(new ProgressBar())
                        .optDevice(device)
                        .build();
        this.vaeDecoderModel = vaeDecoderCriteria.loadModel();
        this.vaeDecoderPredictor = this.vaeDecoderModel.newPredictor();

        Criteria<String, NDList> textEncoderCriteria =
                Criteria.builder()
                        .setTypes(String.class, NDList.class)
                        .optModelPath(Paths.get(root + "text_encoder.pt"))
                        .optEngine("PyTorch")
                        .optProgress(new ProgressBar())
                        .optTranslator(new TextEncoder())
                        .optDevice(device)
                        .build();
        this.textEncoderModel = textEncoderCriteria.loadModel();
        this.textEncoderPredictor = this.textEncoderModel.newPredictor();
    }

    public void close(){
        this.unetModel.close();
        this.unetPredictor.close();
        this.vaeDecoderModel.close();
        this.vaeDecoderPredictor.close();
        this.textEncoderModel.close();
        this.textEncoderPredictor.close();
    }

    public Image generateImage(String prompt, int steps)
            throws ModelException, IOException, TranslateException {
        // TODO: implement this part
        try (NDManager manager = NDManager.newBaseManager(device, "PyTorch")) {
            // 1. Encode input prompt
            NDList textEncoding = textEncoderPredictor.predict(prompt);
            NDList uncondEncoding = textEncoderPredictor.predict("");
            textEncoding.attach(manager);
            uncondEncoding.attach(manager);
            NDArray textEncodingArray = textEncoding.get(1);
            NDArray uncondEncodingArray = uncondEncoding.get(1);
            NDArray embeddings = uncondEncodingArray.concat(textEncodingArray);
            // 2. Prepare timesteps
            PNDMScheduler scheduler = new PNDMScheduler(manager);
            scheduler.setTimesteps(steps, OFFSET);

            // 3. Prepare latent variables
            Shape latentInitShape = new Shape(1, 4, HEIGHT / 8, WIDTH / 8);
            NDArray latent = manager.randomNormal(latentInitShape);

            // 4. Denoising loop
            ProgressBar pb = new ProgressBar("Generating", steps);
            pb.start(0);
            for (int i = 0; i < steps; i++) {
                NDArray t = manager.create(scheduler.timesteps.toArray()[i]).toType(DataType.INT64,true);
                // expand the latents if we are doing classifier free guidance
                NDArray latentModelInput = latent.concat(latent);
                NDArray noisePred =  unetPredictor.predict(new NDList(latentModelInput, t, embeddings))
                        .get(0);
                NDList splitNoisePred = noisePred.split(2);
                NDArray noisePredUncond = splitNoisePred.get(0);
                NDArray noisePredText = splitNoisePred.get(1);
                NDArray scaledNoisePredUncond = noisePredText.sub(noisePredUncond);
                scaledNoisePredUncond = scaledNoisePredUncond.mul(GUIDANCE_SCALE);
                noisePred = noisePredUncond.add(scaledNoisePredUncond);
                latent = scheduler.step(noisePred, t, latent);
                pb.increment(1);
            }
            pb.end();

            // 5. Post-processing
            return vaeDecoderPredictor.predict(latent);
        }
    }
}