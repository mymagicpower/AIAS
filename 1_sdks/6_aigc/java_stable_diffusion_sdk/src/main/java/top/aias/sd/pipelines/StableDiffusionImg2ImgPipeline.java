package top.aias.sd.pipelines;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import top.aias.sd.scheduler.PNDMScheduler;

import java.io.IOException;

public class StableDiffusionImg2ImgPipeline implements AutoCloseable {
    private static final int OFFSET = 1;
    private static final float GUIDANCE_SCALE = 7.5f;
    private static final float STRENGTH = 0.75f;
    VaeEncoderModel vaeEncoderModel;
    UNetModel unetModel;
    VaeDecoderModel vaeDecoderModel;
    TextEncoderModel textEncoderModel;

    private Device device;

    public StableDiffusionImg2ImgPipeline(String root, Device device) {
        this.device = device;
        this.vaeEncoderModel = new VaeEncoderModel(root, 1, device);
        this.unetModel = new UNetModel(root, 1, device);
        this.vaeDecoderModel = new VaeDecoderModel(root, 1, device);
        this.textEncoderModel = new TextEncoderModel(root, 1, device);
    }

    public void close(){
        this.vaeEncoderModel.close();
        this.unetModel.close();
        this.vaeDecoderModel.close();
        this.textEncoderModel.close();
    }

    public Image generateImage(Image image, String prompt, String negative_prompt, int steps)
            throws ModelException, IOException, TranslateException {
        try (NDManager manager = NDManager.newBaseManager(device, "PyTorch")) {
            // 1. Encode input prompt
            NDList textEncoding = textEncoderModel.predict(prompt);
            NDList uncondEncoding = textEncoderModel.predict(negative_prompt);
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
            NDArray latent = vaeEncoderModel.predict(image);
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

                NDArray noisePred = unetModel.predict(buildUnetInput(embeddings, t, latentModelInput)).get(0);

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
            return vaeDecoderModel.predict(latent);
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