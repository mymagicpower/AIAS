package top.aias.trans.model;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.Shape;
import ai.djl.util.Pair;
/**
 * Implementing standard beam search decoding.
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class BeamSearchScorer {
    private int num_beams;
    private float length_penalty;
    private boolean do_early_stopping;
    private int num_beam_hyps_to_keep;
    private int num_beam_groups;
    private BeamHypotheses beam_hyp;
    private boolean _done;

    public BeamSearchScorer(int num_beams, float length_penalty, boolean do_early_stopping, int num_beam_hyps_to_keep, int num_beam_groups) {
        this.num_beams = num_beams;
        this.length_penalty = length_penalty;
        this.do_early_stopping = do_early_stopping;
        this.num_beam_hyps_to_keep = num_beam_hyps_to_keep;
        this.num_beam_groups = num_beam_groups;
        beam_hyp = new BeamHypotheses(length_penalty, do_early_stopping, num_beams);
        _done = false;
    }

    public boolean isDone() {
        return _done;
    }

    public NDList process(NDManager manager, NDArray input_ids, NDArray next_scores, NDArray next_tokens, NDArray next_indices, long pad_token_id, long eos_token_id) {

        float[] next_scores_arr = next_scores.toFloatArray();
        long[] next_indices_arr = next_indices.toLongArray();
        long[] next_tokens_arr = next_tokens.toLongArray();

        NDArray next_beam_scores = manager.zeros(new Shape(1, this.num_beams), next_scores.getDataType());
        NDArray next_beam_tokens = manager.zeros(new Shape(1, this.num_beams), next_tokens.getDataType());
        NDArray next_beam_indices = manager.zeros(new Shape(1, this.num_beams), next_indices.getDataType());


        // next tokens for this sentence
        int beam_idx = 0;
        float maxScore = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < next_scores_arr.length; ++i) {
            int beam_token_rank = i;
            long next_token = next_tokens_arr[i];
            float next_score = next_scores_arr[i];
            if (maxScore < next_score) {
                maxScore = next_score;
            }
            long next_index = next_indices_arr[i];

            long batch_beam_idx = next_index;

            // add to generated hypotheses if end of sentence
            if (next_token == eos_token_id) {
                // if beam_token does not belong to top num_beams tokens, it should not be added
                if (beam_token_rank >= this.num_beams)
                    continue;
                long[] arr = input_ids.get(batch_beam_idx).toLongArray();
                // Add a new hypothesis to the list.
                beam_hyp.add(next_score, arr);
            } else {
                // add next predicted token since it is not eos_token
                next_beam_scores.set(new NDIndex(0, beam_idx), next_score);
                next_beam_tokens.set(new NDIndex(0, beam_idx), next_token);
                next_beam_indices.set(new NDIndex(0, beam_idx), batch_beam_idx);
                beam_idx += 1;
            }

            // once the beam for next step is full, don't add more tokens to it.
            if (beam_idx == this.num_beams)
                break;
        }

        long cur_len = input_ids.getShape().getLastDimension();
        this._done = this._done || beam_hyp.isDone(maxScore, cur_len);

        NDList list = new NDList();
        list.add(next_beam_scores);
        list.add(next_beam_tokens);
        list.add(next_beam_indices);

        return list;
    }

    public long[] finalize(int max_length, long eos_token_id) {

        // best_hyp_tuple
        Pair<Float, long[]> pair = beam_hyp.getPair(beam_hyp.max());
        float best_score = pair.getKey();
        long[] best_hyp = pair.getValue();
        int sent_length = best_hyp.length;

        // prepare for adding eos
        int sent_max_len = Math.min(sent_length + 1, max_length);
        long[] decodedArr = new long[sent_max_len];

        for (int i = 0; i < sent_length; ++i) {
            decodedArr[i] = best_hyp[i];
        }
        if (sent_length < max_length) {
            decodedArr[sent_length] = eos_token_id;
        }

        return decodedArr;
    }
}