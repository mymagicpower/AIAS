package me.aias.sd.translator;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class TextEncoder implements NoBatchifyTranslator<String, NDList> {

    private static final int MAX_LENGTH = 77;

    HuggingFaceTokenizer tokenizer;

    String rootPath;

    public TextEncoder(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public void prepare(TranslatorContext ctx) throws IOException {
        // sentence-transformers/msmarco-distilbert-dot-v5
        // openai/clip-vit-large-patch14
        // openai/clip-vit-base-patch32
        // https://huggingface.co/sentence-transformers/msmarco-distilbert-dot-v5
        // https://huggingface.co/runwayml/stable-diffusion-v1-5/blob/main/tokenizer/tokenizer_config.json
//        tokenizer =
//                HuggingFaceTokenizer.builder()
//                        .optPadding(true)
//                        .optPadToMaxLength()
//                        .optMaxLength(MAX_LENGTH)
//                        .optTruncation(true)
//                        .optTokenizerName("openai/clip-vit-large-patch14")
//                        .build();

        String modelPath = rootPath + "clip-vit-large-patch14";
        tokenizer =
                HuggingFaceTokenizer.builder()
                        .optPadding(true)
                        .optPadToMaxLength()
                        .optMaxLength(MAX_LENGTH)
                        .optTokenizerPath(Paths.get(modelPath))
                        .optTruncation(true)
                        .build();
    }

    @Override
    public NDList processOutput(TranslatorContext ctx, NDList list) {
        list.detach();
        return list;
    }

    @Override
    public NDList processInput(TranslatorContext ctx, String input) {
        Encoding encoding = tokenizer.encode(input);
        int[][] tokenValues = new int[1][MAX_LENGTH];
        tokenValues[0] = Arrays.stream(encoding.getIds()).mapToInt(i -> (int)i).toArray();
        NDArray ndArray = ctx.getNDManager().create(tokenValues);
        return new NDList(ndArray);
    }
}