package top.aias.nllb.generate;
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
    private long bosTokenId;
    private long decoderStartTokenId;
    private float encoderRepetitionPenalty;
    private long forcedBosTokenId;
    private long srcLangId;
    private float lengthPenalty;
    public SearchConfig() {
        this.maxSeqLength = 512;
        this.eosTokenId = 2;
        this.bosTokenId = 0;
        this.padTokenId = 1;
        this.decoderStartTokenId = 2;
        this.encoderRepetitionPenalty = 1.0f;
        this.srcLangId = 0;
        this.forcedBosTokenId = 0;
        this.lengthPenalty = 1.0f;

    }

    public long getSrcLangId() {
        return srcLangId;
    }

    public void setSrcLangId(long srcLangId) {
        this.srcLangId = srcLangId;
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

    public long getDecoderStartTokenId() {
        return decoderStartTokenId;
    }

    public void setDecoderStartTokenId(long decoderStartTokenId) {
        this.decoderStartTokenId = decoderStartTokenId;
    }

    public float getEncoderRepetitionPenalty() {
        return encoderRepetitionPenalty;
    }

    public void setEncoderRepetitionPenalty(float encoderRepetitionPenalty) {
        this.encoderRepetitionPenalty = encoderRepetitionPenalty;
    }

    public long getForcedBosTokenId() {
        return forcedBosTokenId;
    }

    public void setForcedBosTokenId(long forcedBosTokenId) {
        this.forcedBosTokenId = forcedBosTokenId;
    }

    public float getLengthPenalty() {
        return lengthPenalty;
    }

    public void setLengthPenalty(float lengthPenalty) {
        this.lengthPenalty = lengthPenalty;
    }

    public long getBosTokenId() {
        return bosTokenId;
    }

    public void setBosTokenId(long bosTokenId) {
        this.bosTokenId = bosTokenId;
    }
}