package me.aias.example.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * CTC贪婪(最佳路径)解码器
 *
 * @author Calvin <179209347@qq.com>
 */
public class CTCGreedyDecoder {

  /**
   * 由最可能的令牌组成的路径将被进一步后处理到去掉连续重复和所有空白
   *
   * @param manager
   * @param probs_seq: 每一条都是2D的概率表。每个元素都是浮点数概率的列表一个字符
   * @param vocabulary: 词汇列表
   * @param blank_index: 需要移除的空白索引
   * @return 解码后得到的 score,字符串
   * @throws Exception
   */
  public static Pair greedyDecoder(
      NDManager manager, NDArray probs_seq, List<String> vocabulary, long blank_index) {
    // 获得每个时间步的最佳索引
    float[] floats = probs_seq.toFloatArray();
    int rows = (int) probs_seq.getShape().get(0);
    int cols = (int) probs_seq.getShape().get(1);

    long[] max_index_list = probs_seq.argMax(1).toLongArray();

    List<Float> max_prob_list = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      if (max_index_list[i] != blank_index) {
        max_prob_list.add(probs_seq.getFloat(i, max_index_list[i]));
      }
    }

    // 删除连续的重复"索引"
    List<Long> index_list = new ArrayList<>();
    long current = max_index_list[0];
    index_list.add(current);
    for (int i = 1; i < max_index_list.length; i++) {
      if (max_index_list[i] != current) {
        index_list.add(max_index_list[i]);
        current = max_index_list[i];
      }
    }

    // 删除空索引
    List<Long> pure_index_list = new ArrayList<>();
    for (Long value : index_list) {
      if (value != blank_index) {
        pure_index_list.add(value);
      }
    }

    // 索引列表转换为字符串
    StringBuffer sb = new StringBuffer();
    for (Long value : pure_index_list) {
      sb.append(vocabulary.get(value.intValue()));
    }

    float score = 0;
    if (max_prob_list.size() > 0) {
      float sum = 0;
      for (Float value : max_prob_list) {
        sum += value;
      }
      score = (sum / max_prob_list.size()) * 100.0f;
    }

    return Pair.of(score, sb.toString());
  }
}
