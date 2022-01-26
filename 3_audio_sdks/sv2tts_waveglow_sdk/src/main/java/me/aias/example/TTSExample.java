package me.aias.example;

import ai.djl.Device;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import me.aias.example.ph.SequenceUtils;
import me.aias.example.speaker.SpeakerEncoder;
import me.aias.example.tacotron2.Tacotron2Encoder;
import me.aias.example.utils.*;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * TTS 基于给定音色将文本转为语音
 * 注意: 为了防止用于非法用途，代码限定音色文件只能使用biaobei-009502.mp3
 * <p>使用特定的音色，结合文字的读音合成音频，使得合成后的音频具有目标说话人的特征。
 *
 * <p>https://github.com/CorentinJ/Real-Time-Voice-Cloning
 *
 * <p>https://arxiv.org/pdf/1806.04558.pdf
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TTSExample {
    private static int partials_n_frames = 160;
    private static final Logger logger = LoggerFactory.getLogger(TTSExample.class);

    private TTSExample() {
    }

    public static void main(String[] args) throws Exception {
        // 文本
        String text = "基于给定音色将文本转为语音";
        logger.info("文本: {}", text);
        // 目标音色保存路径
        Path audioFile = Paths.get("src/test/resources/biaobei-009502.mp3");
        logger.info("给定音色: {}", "src/test/resources/biaobei-009502.mp3");
        // 语音保存路径
        File outs = new File(Paths.get("build/output/audio.wav").toString());

        NDManager manager = NDManager.newBaseManager(Device.cpu());

        SpeakerEncoder speakerEncoder = new SpeakerEncoder();
        Tacotron2Encoder tacotron2Encoder = new Tacotron2Encoder();
        WaveGlowEncoder waveGlowEncoder = new WaveGlowEncoder();
        DenoiserEncoder denoiserEncoder = new DenoiserEncoder();

        try (ZooModel<NDArray, NDArray> speakerEncoderModel =
                     ModelZoo.loadModel(speakerEncoder.criteria());
             Predictor<NDArray, NDArray> speakerEncoderPredictor = speakerEncoderModel.newPredictor();
             ZooModel<NDList, NDArray> tacotron2Model = ModelZoo.loadModel(tacotron2Encoder.criteria());
             Predictor<NDList, NDArray> tacotron2Predictor = tacotron2Model.newPredictor();
             ZooModel<NDArray, NDArray> waveGlowModel =
                     ModelZoo.loadModel(waveGlowEncoder.criteria());
             Predictor<NDArray, NDArray> waveGlowPredictor = waveGlowModel.newPredictor();
             ZooModel<NDArray, NDArray> denoiserModel =
                     ModelZoo.loadModel(denoiserEncoder.criteria());
             Predictor<NDArray, NDArray> denoiserPredictor = denoiserModel.newPredictor()) {

            // 文本转为ID列表
            List<Integer> text_data_org = SequenceUtils.text2sequence(text);
            int[] text_dataa = text_data_org.stream().mapToInt(Integer::intValue).toArray();
            NDArray text_data = manager.create(text_dataa);
            text_data.setName("text");

            // 目标音色作为Speaker Encoder的输入: 使用ffmpeg 将目标音色mp3文件转为wav格式
            NDArray audioArray = FfmpegUtils.load_wav_to_torch(audioFile.toString(), 22050);

            // 提取这段语音的说话人特征（音色）作为Speaker Embedding
            Pair<LinkedList<LinkedList<Integer>>, LinkedList<LinkedList<Integer>>> slices =
                    AudioUtils.compute_partial_slices(audioArray.size(), partials_n_frames, 0.75f, 0.5f);
            LinkedList<LinkedList<Integer>> wave_slices = slices.getLeft();
            LinkedList<LinkedList<Integer>> mel_slices = slices.getRight();
            int max_wave_length = wave_slices.getLast().getLast();
            if (max_wave_length >= audioArray.size()) {
                audioArray = AudioUtils.pad(audioArray, (max_wave_length - audioArray.size()), manager);
            }
            float[][] fframes = AudioUtils.wav_to_mel_spectrogram(audioArray);
            NDArray frames = manager.create(fframes).transpose();
            NDList frameslist = new NDList();
            for (LinkedList<Integer> s : mel_slices) {
                NDArray temp =
                        speakerEncoderPredictor.predict(frames.get(s.getFirst() + ":" + s.getLast()));
                frameslist.add(temp);
            }
            NDArray partial_embeds = NDArrays.stack(frameslist);
            NDArray raw_embed = partial_embeds.mean(new int[]{0});
            // Speaker Embedding
            NDArray speaker_data = raw_embed.div(((raw_embed.pow(2)).sum()).sqrt());

            Shape shape = speaker_data.getShape();
            logger.info("目标音色特征向量 Shape: {}", Arrays.toString(shape.getShape()));
            logger.info("目标音色特征向量: {}", Arrays.toString(speaker_data.toFloatArray()));

            // 模型数据
            NDList input = new NDList();
            input.add(text_data);
            input.add(speaker_data);

            // 生成mel频谱数据
            NDArray mels_postnet = tacotron2Predictor.predict(input);
            shape = mels_postnet.getShape();
            logger.info("梅尔频谱数据 Shape: {}", Arrays.toString(shape.getShape()));
            logger.info("梅尔频谱数据: {}", Arrays.toString(mels_postnet.toFloatArray()));

            // 生成wav数据
            NDArray wavWithNoise = waveGlowPredictor.predict(mels_postnet);
            NDArray wav = denoiserPredictor.predict(wavWithNoise);
            SoundUtils.saveWavFile(wav.get(0), 1.0f, outs);
            logger.info("生成wav音频文件: {}", outs.toString());
        }
    }
}
