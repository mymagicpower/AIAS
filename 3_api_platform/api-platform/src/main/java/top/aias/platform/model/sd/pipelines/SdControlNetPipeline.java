package top.aias.platform.model.sd.pipelines;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.util.NDImageUtils;
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
import top.aias.platform.model.sd.vae.VaeDecoderModel;
import top.aias.platform.utils.NDArrayUtils;

import java.io.IOException;

public class SdControlNetPipeline implements AutoCloseable {
    private static final int HEIGHT = 512;
    private static final int WIDTH = 512;
    private static final int OFFSET = 1;
    private static final float GUIDANCE_SCALE = 7.5f;
    private static final float STRENGTH = 0.75f;
    BaseModel controlNetModel;
    UNetForControlModel unetModel;
    VaeDecoderModel vaeDecoderModel;
    TextEncoderModel textEncoderModel;
    private Device device;
    public SdControlNetPipeline(Device device, BaseModel controlNetModel, UNetForControlModel unetModel, VaeDecoderModel vaeDecoderModel, TextEncoderModel textEncoderModel) {
        this.device = device;
        this.controlNetModel = controlNetModel;
        this.unetModel = unetModel;
        this.vaeDecoderModel = vaeDecoderModel;
        this.textEncoderModel = textEncoderModel;
    }

    public void close(){
        this.controlNetModel.close();
        this.unetModel.close();
        this.vaeDecoderModel.close();
        this.textEncoderModel.close();
    }

    public Image generateImage(Image image, String prompt, String negative_prompt, int steps) throws TranslateException {
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

            // 2. Prepare image
            NDArray imageArray = prepareImage(manager, image);

            // 3. Prepare timesteps
            PNDMScheduler scheduler = new PNDMScheduler(manager);
            scheduler.setTimesteps(steps, OFFSET);

            // 4. Prepare latent variables
            Shape latentInitShape = new Shape(1, 4, HEIGHT / 8, WIDTH / 8);
            NDArray latent = manager.randomNormal(latentInitShape);

            // 5. Denoising loop
            ProgressBar pb = new ProgressBar("Generating", steps);
            pb.start(0);
            for (int i = 0; i < steps; i++) {
                NDArray t = manager.create(scheduler.timesteps.toArray()[i]).toType(DataType.INT64, true);
                // expand the latents if we are doing classifier free guidance
                NDArray latentModelInput = latent.concat(latent);

                NDList ndList = controlNetModel.predict(buildControlNetInput(embeddings, t, latentModelInput, imageArray));
                NDArray mid_block_res_sample = ndList.get(12);
                ndList.remove(12);
                NDList down_block_res_samples = ndList;

                NDArray noisePred = unetModel.predict(buildUnetInput(embeddings, t, latentModelInput, down_block_res_samples, mid_block_res_sample)).get(0);

                NDList splitNoisePred = noisePred.split(2);
                NDArray noisePredUncond = splitNoisePred.get(0);
                NDArray noisePredText = splitNoisePred.get(1);
                NDArray scaledNoisePredUncond = noisePredText.add(noisePredUncond.neg());
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

    public Image generateImage(Image original_image, Image mask_image, String prompt, String negative_prompt, int steps)
            throws ModelException, IOException, TranslateException {
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

            // 2. Prepare image
            NDArray control_image = make_inpaint_condition(manager, original_image, mask_image);

            // 3. Prepare timesteps
            PNDMScheduler scheduler = new PNDMScheduler(manager);
            scheduler.setTimesteps(steps, OFFSET);

            // 4. Prepare latent variables
            Shape latentInitShape = new Shape(1, 4, HEIGHT / 8, WIDTH / 8);
            NDArray latent = manager.randomNormal(latentInitShape);

            // 5. Denoising loop
            ProgressBar pb = new ProgressBar("Generating", steps);
            pb.start(0);
            for (int i = 0; i < steps; i++) {
                NDArray t = manager.create(scheduler.timesteps.toArray()[i]).toType(DataType.INT64, true);
                // expand the latents if we are doing classifier free guidance
                NDArray latentModelInput = latent.concat(latent);

                NDList ndList = controlNetModel.predict(buildControlNetInput(embeddings, t, latentModelInput, control_image));
                NDArray mid_block_res_sample = ndList.get(12);
                ndList.remove(12);
                NDList down_block_res_samples = ndList;

                NDArray noisePred = unetModel.predict(buildUnetInput(embeddings, t, latentModelInput, down_block_res_samples, mid_block_res_sample)).get(0);

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

    private static NDList buildControlNetInput(NDArray input, NDArray timestep, NDArray latents, NDArray image) {
        input.setName("encoder_hidden_states");
        NDList list = new NDList();
        list.add(latents);
        list.add(timestep);
        list.add(input);
        list.add(image);
        return list;
    }

    private static NDList buildUnetInput(NDArray input, NDArray timestep, NDArray latents, NDList down_block_res_samples, NDArray mid_block_res_sample) {
        input.setName("encoder_hidden_states");
        NDList list = new NDList();
        list.add(latents);
        list.add(timestep);
        list.add(input);
        for (NDArray arr : down_block_res_samples) {
            list.add(arr);
        }
        list.add(mid_block_res_sample);
        return list;
    }

    private NDArray prepareImage(NDManager manager, Image input) {
        NDArray array = input.toNDArray(manager, Image.Flag.COLOR);
        // model take 32-based size
        int h = 512;
        int w = 512;
        int[] size = resize32(h, w);
        array = NDImageUtils.resize(array, size[1], size[0]);
        array = array.transpose(2, 0, 1).div(255f);  // HWC -> CHW RGB
        array = array.expandDims(0);

        array = array.concat(array);

        return array;
    }

    private NDArray make_inpaint_condition(NDManager manager, Image image, Image mask) {
        NDArray imageArray = image.toNDArray(manager, Image.Flag.COLOR).toType(DataType.FLOAT32, false);
        NDArray maskArray = mask.toNDArray(manager, Image.Flag.COLOR).toType(DataType.FLOAT32, false);

        // preprocess image
        imageArray = imageArray.transpose(2, 0, 1);  // HWC -> CHW RGB
        imageArray = imageArray.toType(DataType.FLOAT32,true).div(255.0f);

        // preprocess mask
        maskArray = maskArray.transpose(2, 0, 1); // HWC -> CHW RGB

        NDArray R = maskArray.get("0,:,:");
        NDArray G = maskArray.get("1,:,:");
        NDArray B = maskArray.get("2,:,:");
        // 为灰度图像，每个像素用8个bit表示，0表示黑，255表示白，其他数字表示不同的灰度。
        // 灰度图转换公式：L = R * 299/1000 + G * 587/1000+ B * 114/1000
        maskArray = R.mul(299f / 1000f).add(G.mul(587f / 1000f)).add(B.mul(114f / 1000f));
        float[][] fff = NDArrayUtils.floatNDArrayToArray(maskArray);

        maskArray = maskArray.expandDims(0);
        maskArray = maskArray.concat(maskArray,0).concat(maskArray,0);

        // set as masked pixel
        // 黑色部分为 0，小于128，白色 255
        imageArray.set(maskArray.gt(128), -1.0f);
        // 蒙版部分为白色，像素值为 1， 将原图剩余部分抹除
        imageArray = imageArray.expandDims(0);
        imageArray = imageArray.concat(imageArray,0);

        return imageArray;
    }

    private int[] resize32(double h, double w) {
        double min = Math.min(h, w);
        if (min < 32) { // TODO 32 --> 8
            h = 32.0 / min * h;
            w = 32.0 / min * w;
        }
        int h32 = (int) h / 32;
        int w32 = (int) w / 32;
        return new int[]{h32 * 32, w32 * 32};
    }

}