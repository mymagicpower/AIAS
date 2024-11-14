package me.aias.sd.pipelines;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
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

public class StableDiffusionImg2ImgPipeline implements AutoCloseable {

    private static final int HEIGHT = 512;
    private static final int WIDTH = 512;
    private static final int OFFSET = 1;
    private static final float GUIDANCE_SCALE = 7.5f;
    private static final float STRENGTH = 0.75f;
    ZooModel vaeEncoderModel;
    private Predictor<Image, NDArray> vaeEncoderPredictor;
    ZooModel unetModel;
    private Predictor<NDList, NDList> unetPredictor;
    ZooModel vaeDecoderModel;
    private Predictor<NDArray, Image> vaeDecoderPredictor;
    ZooModel textEncoderModel;
    private Predictor<String, NDList> textEncoderPredictor;

    private Device device;

    public StableDiffusionImg2ImgPipeline(String root, Device device) throws ModelException, IOException {
        this.device = device;

        Criteria<Image, NDArray> vaeEncoderCriteria =
                Criteria.builder()
                        .setTypes(Image.class, NDArray.class)
                        .optModelPath(Paths.get(root + "vae_encoder.pt"))
                        .optEngine("PyTorch")
                        .optDevice(device)
                        .optProgress(new ProgressBar())
                        .optTranslator(new ImageEncoder(HEIGHT,WIDTH))
                        .build();
        this.vaeEncoderModel = vaeEncoderCriteria.loadModel();
        this.vaeEncoderPredictor = this.vaeEncoderModel.newPredictor();

        Criteria<NDList, NDList> unetCriteria =
                Criteria.builder()
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get(root + "unet.pt"))
                        .optEngine("PyTorch")
                        .optDevice(device)
                        .optProgress(new ProgressBar())
                        .optTranslator(new NoopTranslator())
                        .build();
        this.unetModel = unetCriteria.loadModel();
        this.unetPredictor = this.unetModel.newPredictor();

        Criteria<NDArray, Image> vaeDecoderCriteria =
                Criteria.builder()
                        .setTypes(NDArray.class, Image.class)
                        .optModelPath(Paths.get(root + "vae_decoder.pt"))
                        .optEngine("PyTorch")
                        .optDevice(device)
                        .optTranslator(new ImageDecoder(HEIGHT, WIDTH))
                        .optProgress(new ProgressBar())
                        .build();
        this.vaeDecoderModel = vaeDecoderCriteria.loadModel();
        this.vaeDecoderPredictor = this.vaeDecoderModel.newPredictor();

        Criteria<String, NDList> textEncoderCriteria =
                Criteria.builder()
                        .setTypes(String.class, NDList.class)
                        .optModelPath(Paths.get(root + "text_encoder.pt"))
                        .optEngine("PyTorch")
                        .optDevice(device)
                        .optProgress(new ProgressBar())
                        .optTranslator(new TextEncoder(root))
                        .build();
        this.textEncoderModel = textEncoderCriteria.loadModel();
        this.textEncoderPredictor = this.textEncoderModel.newPredictor();
    }

    public void close(){
        this.vaeEncoderModel.close();
        this.vaeEncoderPredictor.close();
        this.unetModel.close();
        this.unetPredictor.close();
        this.vaeDecoderModel.close();
        this.vaeDecoderPredictor.close();
        this.textEncoderModel.close();
        this.textEncoderPredictor.close();
    }

    public Image generateImage(Image image, String prompt, String negative_prompt, int steps)
            throws ModelException, IOException, TranslateException {
        try (NDManager manager = NDManager.newBaseManager(device, "PyTorch")) {
            // 1. Encode input prompt
            NDList textEncoding = textEncoderPredictor.predict(prompt);
            NDList uncondEncoding = textEncoderPredictor.predict(negative_prompt);
            textEncoding.attach(manager);
            uncondEncoding.attach(manager);
            NDArray textEncodingArray = textEncoding.get(1);
            NDArray uncondEncodingArray = uncondEncoding.get(1);
            NDArray embeddings = uncondEncodingArray.concat(textEncodingArray);

            // 3. Prepare timesteps
            PNDMScheduler scheduler = new PNDMScheduler(manager);
            scheduler.setTimesteps(steps, OFFSET);
            int initTimestep = (int) (steps * STRENGTH) + OFFSET;
            initTimestep = Math.min(initTimestep, steps);
            int timesteps = scheduler.timesteps.get(new NDIndex("-" + initTimestep)).toIntArray()[0];

            // 4. Prepare latent variables
            NDArray latent = vaeEncoderPredictor.predict(image);
            NDArray noise = manager.randomNormal(latent.getShape());
            latent = scheduler.addNoise(latent, noise, timesteps);

            int tStart = Math.max(steps - initTimestep + OFFSET, 0);
            int[] timestepArr = scheduler.timesteps.get(new NDIndex(tStart + ":")).toIntArray();

            // 5. Denoising loop
            ProgressBar pb = new ProgressBar("Generating", timestepArr.length);
            pb.start(0);
            for (int i = 0; i < timestepArr.length; i++) {
                NDArray t = manager.create(timestepArr[i]).toType(DataType.INT64, true);

                // expand the latents if we are doing classifier free guidance
                NDArray latentModelInput = latent.concat(latent);

                NDArray noisePred = unetPredictor.predict(buildUnetInput(embeddings, t, latentModelInput)).get(0);

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

    private static NDList buildUnetInput(NDArray input, NDArray timestep, NDArray latents) {
        input.setName("encoder_hidden_states");
        NDList list = new NDList();
        list.add(latents);
        list.add(timestep);
        list.add(input);
        return list;
    }

}