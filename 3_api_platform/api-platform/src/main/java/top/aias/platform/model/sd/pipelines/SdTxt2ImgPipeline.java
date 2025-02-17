package top.aias.platform.model.sd.pipelines;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import top.aias.platform.model.sd.controlnet.BaseModel;
import top.aias.platform.model.sd.scheduler.PNDMScheduler;
import top.aias.platform.model.sd.text.TextEncoderModel;
import top.aias.platform.model.sd.unet.UNetForControlModel;
import top.aias.platform.model.sd.unet.UNetModel;
import top.aias.platform.model.sd.vae.VaeDecoderModel;

import java.io.IOException;

public class SdTxt2ImgPipeline implements AutoCloseable {

    private static final int HEIGHT = 512;
    private static final int WIDTH = 512;
    private static final int OFFSET = 1;
    private static final float GUIDANCE_SCALE = 7.5f;
    UNetModel unetModel;
    VaeDecoderModel vaeDecoderModel;
    TextEncoderModel textEncoderModel;
    private Device device;

    public SdTxt2ImgPipeline(Device device, UNetModel unetModel, VaeDecoderModel vaeDecoderModel, TextEncoderModel textEncoderModel) {
        this.device = device;
        this.unetModel = unetModel;
        this.vaeDecoderModel = vaeDecoderModel;
        this.textEncoderModel = textEncoderModel;
    }

    public void close(){
        this.unetModel.close();
        this.vaeDecoderModel.close();
        this.textEncoderModel.close();
    }

    public Image generateImage(String prompt, String negative_prompt, int steps) throws TranslateException {
        // TODO: implement this part
        try (NDManager manager = NDManager.newBaseManager(device, "PyTorch")) {
            // 1. Encode input prompt
            NDList textEncoding = textEncoderModel.predict(prompt);
            NDList uncondEncoding = textEncoderModel.predict(negative_prompt);
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
                NDArray noisePred =  unetModel.predict(new NDList(latentModelInput, t, embeddings))
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
            return vaeDecoderModel.predict(latent);
        }
    }
}