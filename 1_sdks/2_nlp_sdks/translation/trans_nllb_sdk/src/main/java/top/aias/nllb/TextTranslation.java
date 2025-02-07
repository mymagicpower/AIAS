package top.aias.nllb;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.ndarray.NDArray;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.nllb.model.NllbModel;
import top.aias.nllb.tokenizer.TokenUtils;
import top.aias.nllb.generate.SearchConfig;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 文本翻译，支持202种语言互译
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TextTranslation {

    private static final Logger logger = LoggerFactory.getLogger(TextTranslation.class);

    private TextTranslation() {
    }

    public static void main(String[] args) throws ModelException, IOException,
            TranslateException {

        SearchConfig config = new SearchConfig();
        // 设置输出文字的最大长度
        config.setMaxSeqLength(128);
        // 设置源语言：中文 "zho_Hans": 256200
        config.setSrcLangId(256200);
        // 设置目标语言：英文 "eng_Latn": 256047
        config.setForcedBosTokenId(256047);

        // 输入文字
        String input = "智利北部的丘基卡马塔矿是世界上最大的露天矿之一，长约4公里，宽3公里，深1公里。";

        String modelPath = "models/";
        String cpuModelName = "traced_translation_cpu.pt";
        String gpuModelName = "traced_translation_gpu.pt";
        try (NllbModel nllbModel = new NllbModel(config, modelPath, cpuModelName, Device.cpu())) {

            System.setProperty("ai.djl.pytorch.graph_optimizer", "false");

            // 运行模型，获取翻译结果
            String result = nllbModel.translate(input);

            logger.info("{}", result);
        } finally {
            System.clearProperty("ai.djl.pytorch.graph_optimizer");
        }
    }
}