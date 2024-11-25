package top.aias.chatglm;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.ndarray.NDArray;
import ai.djl.translate.TranslateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.chatglm.model.ChatGLM2Model;
import top.aias.chatglm.tokenizer.SpProcessor;
import top.aias.chatglm.tokenizer.SpTokenizer;
import top.aias.chatglm.tokenizer.TokenUtils;
import top.aias.chatglm.utils.SearchConfig;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public final class TextGeneration {

    private static final Logger logger = LoggerFactory.getLogger(TextGeneration.class);

    private TextGeneration() {
    }

    public static void main(String[] args) throws ModelException, IOException,
            TranslateException {

        SearchConfig config = new SearchConfig();
        config.setMaxSeqLength(30);
        String input = "[Round 1]\n\n问：你好\n\n答：";

        Path tokenizerPath = Paths.get("models/tokenizer.model");
        String modelPath = "D:\\ai_projects\\products\\python\\chatglm2_6b_export\\traced_chatglm2_6b_fp32_cpu.pt";

        try (ChatGLM2Model chatGLM2Model = new ChatGLM2Model(config, modelPath, Device.cpu());
             SpTokenizer tokenizer = new SpTokenizer(tokenizerPath)) {

            System.setProperty("ai.djl.pytorch.graph_optimizer", "false");

            List<String> tokens = tokenizer.tokenize(input);
            String[] strs = tokens.toArray(new String[]{});
            // [▁[, R, ound, ▁, 1, ], <0x0A>, <0x0A>, 问, ：, 你好, <0x0A>, <0x0A>, 答, ：]
            logger.info("Tokens: " + Arrays.toString(strs));
            SpProcessor processor = tokenizer.getProcessor();
            int[] ids = processor.encode(input);
            // 790, 30951, 517, 30910, 30939, 30996, 13, 13, 54761, 31211, 39701, 13, 13, 55437, 3121
            logger.info("Tokens ID: " + Arrays.toString(ids));

            NDArray output = chatGLM2Model.sample(processor, ids);
            String result = TokenUtils.decode(processor, output);

            logger.info("{}", result);
        } finally {
            System.clearProperty("ai.djl.pytorch.graph_optimizer");
        }
    }
}