package top.aias.chatglm.utils;

public class SearchConfig {

    private float temperature;
    private float topP;
    private int topK;
    private int maxSeqLength;
    private long padTokenId;
    private long eosTokenId;

    public SearchConfig() {
        this.temperature = 0.8f;
        this.topP = 0.8f;
        this.topK = 50;
        this.maxSeqLength = 8192;
        this.eosTokenId = 2;
        this.padTokenId = 0;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getTopP() {
        return topP;
    }

    public void setTopP(float topP) {
        this.topP = topP;
    }

    public int getTopK() {
        return topK;
    }

    public void setTopK(int topK) {
        this.topK = topK;
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
}