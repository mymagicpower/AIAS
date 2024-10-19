package top.aias.model.trans;

import ai.djl.util.Pair;

import java.util.ArrayList;

/**
 * Beam hypothesis
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class BeamHypotheses {
    float length_penalty;
    boolean early_stopping;
    int num_beams;
    ArrayList<Pair<Float, long[]>> beams;
    float worst_score = 1e9f;

    public BeamHypotheses(float length_penalty, boolean early_stopping, int num_beams) {
        this.length_penalty = length_penalty;
        this.early_stopping = early_stopping;
        this.num_beams = num_beams;
        beams = new ArrayList<>();
    }

    /**
     * Get length
     *
     * @return
     */
    public int getLen() {
        return beams.size();
    }

    /**
     * Add a new hypothesis to the list.
     *
     * @param sum_logprobs
     * @param hyp
     */
    public void add(float sum_logprobs, long[] hyp) {
        float score = sum_logprobs / (float) (Math.pow(hyp.length, this.length_penalty));

        if (getLen() < this.num_beams || score > this.worst_score) {
            this.beams.add(new Pair<>(score, hyp));
            if (getLen() > this.num_beams) {
                int index = min();
                this.beams.remove(index);
                index = min();
                this.worst_score = this.beams.get(index).getKey();
            }else {
                this.worst_score = Math.min(score, this.worst_score);
            }
        }
    }

    /**
     * Get Pair
     * @param index
     * @return
     */
    public Pair<Float, long[]> getPair(int index) {
        return beams.get(index);
    }

    /**
     * Get index for minmum score value
     *
     * @return
     */
    public int min() {
        float min = beams.get(0).getKey();
        int index = 0;
        for (int i = 1; i < beams.size(); ++i) {
            if (beams.get(i).getKey() < min) {
                min = beams.get(i).getKey();
                index = i;
            }
        }
        return index;
    }

    /**
     * Get index for maximum score value
     * @return
     */
    public int max() {
        float max = beams.get(0).getKey();
        int index = 0;
        for (int i = 1; i < beams.size(); ++i) {
            if (beams.get(i).getKey() > max) {
                max = beams.get(i).getKey();
                index = i;
            }
        }
        return index;
    }

    /**
     * If there are enough hypotheses and that none of the hypotheses being generated can become better than the worst
     * one in the heap, then we are done with this sentence.
     *
     * @param best_sum_logprobs
     * @param cur_len
     * @return
     */
    public boolean isDone(float best_sum_logprobs, long cur_len) {
        if (getLen() < this.num_beams)
            return false;

        if (this.early_stopping)
            return true;
        else {
            float highest_attainable_score = best_sum_logprobs / (float) Math.pow(cur_len, this.length_penalty);
            boolean ret = (this.worst_score >= highest_attainable_score);
            return ret;
        }
    }
}
