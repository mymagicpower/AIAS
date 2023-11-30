package top.aias.vad.utils;

import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 语音检测模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class SileroVAD {

    private static final Logger logger = LoggerFactory.getLogger(SileroVAD.class);

    public SileroVAD() {
    }

    /**
     * 中文文本检测
     * @return
     */
    public Criteria<NDList, NDList> criteria() {
        Criteria<NDList, NDList> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get("models/silero_vad.onnx"))
                        .optTranslator(new SileroVADTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }


}
