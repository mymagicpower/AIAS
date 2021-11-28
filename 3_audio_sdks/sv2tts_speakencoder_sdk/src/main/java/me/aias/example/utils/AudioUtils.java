package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import com.google.common.collect.Lists;
import com.jlibrosa.audio.JLibrosa;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;

public class AudioUtils {
  static int mel_window_step = 10;

    /**
     * 数据补齐到padl长度
     * @param wav
     * @param padl
     * @param manager
     * @return
     */
    public static NDArray pad(NDArray wav, long padl, NDManager manager){
        wav = wav.concat(manager.zeros(new Shape(padl)));
        return wav;
    }
    
  /**
   * 从wav提取mel特征值
   *
   * @param wav
   * @return
   */
  public static float[][] wav_to_mel_spectrogram(NDArray wav) {
    JLibrosa librosa = new JLibrosa();
    float[][] melSpectrogram =
        librosa.generateMelSpectroGram(wav.toFloatArray(), 16000, 1024, 40, (16000 * 10 / 1000));
    return melSpectrogram;
  }

  // 对音频进行切片
  public static Pair compute_partial_slices(
      long n_samples, int partial_utterance_n_frames, float min_pad_coverage, float overlap) {
    int samples_per_frame = (int) (16000 * mel_window_step / 1000);
    int n_frames = (int) (Math.ceil((n_samples + 1) / samples_per_frame));
    int frame_step = Math.max((Math.round(partial_utterance_n_frames * (1 - overlap))), 1);
    // Compute the slices
    LinkedList<LinkedList<Integer>> wav_slices = Lists.newLinkedList();
    LinkedList<LinkedList<Integer>> mel_slices = Lists.newLinkedList();

    int steps = Math.max(1, n_frames - partial_utterance_n_frames + frame_step + 1);
    for (int i = 0; i < steps; i += frame_step) {
      LinkedList<Integer> mel_range = Lists.newLinkedList();
      mel_range.add(i);
      mel_range.add(i + partial_utterance_n_frames);
      LinkedList<Integer> wav_range = Lists.newLinkedList();
      wav_range.add(i * samples_per_frame);
      wav_range.add((i + partial_utterance_n_frames) * samples_per_frame);
      mel_slices.add(mel_range);
      wav_slices.add(wav_range);
    }
    // Evaluate whether extra padding is warranted or not
    LinkedList<Integer> last_wav_range = wav_slices.getLast();
    float coverage =
        (float) (n_samples - last_wav_range.getFirst())
            / (last_wav_range.getLast() - last_wav_range.getFirst());
    if (coverage < min_pad_coverage && mel_slices.size() > 1) {
      mel_slices.removeLast();
      wav_slices.removeLast();
    }

    return Pair.of(wav_slices, mel_slices);
  }
}
