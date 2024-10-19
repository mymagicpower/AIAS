package top.aias.trans;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.trans.generate.SearchConfig;
import top.aias.trans.model.TranslationModel;

import java.io.IOException;
/**
 * 英译中
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class TextTranslation_en_zh {

    private static final Logger logger = LoggerFactory.getLogger(TextTranslation_en_zh.class);

    private TextTranslation_en_zh() {
    }

    public static void main(String[] args) throws ModelException, IOException,
            TranslateException {

        SearchConfig config = new SearchConfig();

        String modelPath = "models/opus-mt-en-zh/";
        String input = "My name is Wolfgang and I live in Berlin.";
        String input2 = "Recent research indicates that brittle fracture and step-path failure are important considerations in both natural high-mountain and engineered rock slopes. Newly developed techniques for field survey and numerical modeling of brittle fracture and step-path failure are presented in this research in an attempt to overcome many of the limitations of traditional approaches.";

        try (TranslationModel translationModel = new TranslationModel(config, modelPath, 4, Device.cpu());
        ) {
            System.setProperty("ai.djl.pytorch.graph_optimizer", "false");

            String result = translationModel.translate(input2);

            logger.info("{}", result);
        } finally {
            System.clearProperty("ai.djl.pytorch.graph_optimizer");
        }
    }
}