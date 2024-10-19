package top.aias.model.generate;
/**
 * 配置信息
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class SearchConfig {
    private int maxSeqLength;
    private long padTokenId;
    private long eosTokenId;
    private int beam;
    private boolean suffixPadding;

    public SearchConfig() {
        this.eosTokenId = 0;
        this.padTokenId = 65000;
        this.maxSeqLength = 512;
        this.beam = 6;
    }


    public void setEosTokenId(long eosTokenId) {
        this.eosTokenId = eosTokenId;
    }

    public int getMaxSeqLength() {
        return maxSeqLength;
    }

    public void setMaxSeqLength(int maxSeqLength) {
        this.maxSeqLength = maxSeqLength;
    }

    public long getPadTokenId() {
        return padTokenId;
    }

    public void setPadTokenId(long padTokenId) {
        this.padTokenId = padTokenId;
    }

    public long getEosTokenId() {
        return eosTokenId;
    }

    public int getBeam() {
        return beam;
    }

    public void setBeam(int beam) {
        this.beam = beam;
    }

    public boolean isSuffixPadding() {
        return suffixPadding;
    }

    public void setSuffixPadding(boolean suffixPadding) {
        this.suffixPadding = suffixPadding;
    }
}