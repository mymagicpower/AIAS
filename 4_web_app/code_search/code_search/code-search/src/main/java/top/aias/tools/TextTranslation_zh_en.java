package top.aias.tools;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.model.generate.SearchConfig;
import top.aias.model.trans.TranslationModel;

import java.io.IOException;

/**
 * 中译英
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TextTranslation_zh_en {

    private static final Logger logger = LoggerFactory.getLogger(TextTranslation_zh_en.class);

    private TextTranslation_zh_en() {
    }

    public static void main(String[] args) throws ModelException, IOException,
            TranslateException {

        SearchConfig config = new SearchConfig();
        config.setMaxSeqLength(128);

        String modelPath = "models/opus-mt-zh-en/";

        String input = "智利北部的丘基卡马塔矿是世界上最大的露天矿之一，长约4公里，宽3公里，深1公里。";

        try (TranslationModel translationModel = new TranslationModel(config, modelPath, 4, Device.cpu());
        ) {
            System.setProperty("ai.djl.pytorch.graph_optimizer", "false");

            long start = System.currentTimeMillis();
            String result = translationModel.translate(input);
            long end = System.currentTimeMillis();
            logger.info("Time: {}", (end - start));
            logger.info("{}", result);

        } finally {
            System.clearProperty("ai.djl.pytorch.graph_optimizer");
        }
    }
}