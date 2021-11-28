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
import me.aias.example.utils.AudioUtils;
import me.aias.example.utils.FfmpegUtils;
import me.aias.example.utils.SpeakerEncoder;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * SpeakerEncoder 提取音频特征
 *
 * https://github.com/babysor/MockingBird/blob/main/README-CN.md
 * https://arxiv.org/pdf/1806.04558.pdf
 * 
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class SpeakerEncoderExample {
  private static int partials_n_frames = 160;
  private static final Logger logger = LoggerFactory.getLogger(SpeakerEncoderExample.class);

  private SpeakerEncoderExample() {}

  public static void main(String[] args) throws Exception {
    Path audioFile = Paths.get("src/test/resources/biaobei-009502.mp3");
    NDManager manager = NDManager.newBaseManager(Device.cpu());

    // 使用ffmpeg 将mp3文件转为wav格式
    NDArray audioArray = FfmpegUtils.load_wav_to_torch(audioFile.toString(), 22050);
    SpeakerEncoder speakerEncoder = new SpeakerEncoder();

    try (ZooModel<NDArray, NDArray> model = ModelZoo.loadModel(speakerEncoder.criteria());
        Predictor<NDArray, NDArray> predictor = model.newPredictor()) {

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
        NDArray temp = predictor.predict(frames.get(s.getFirst() + ":" + s.getLast()));
        frameslist.add(temp);
      }
      NDArray partial_embeds = NDArrays.stack(frameslist);
      NDArray raw_embed = partial_embeds.mean(new int[] {0});
      NDArray embed = raw_embed.div(((raw_embed.pow(2)).sum()).sqrt());

      Shape shape = embed.getShape();
      logger.info("embeddings shape: {}", Arrays.toString(shape.getShape()));
      logger.info("embeddings: {}",  Arrays.toString(embed.toFloatArray()));
    }
  }
}
