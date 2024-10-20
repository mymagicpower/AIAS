package top.aias.asr.translator;

import ai.djl.audio.processor.AudioProcessor;
import ai.djl.audio.processor.LogMelSpectrogram;
import ai.djl.audio.processor.PadOrTrim;
import ai.djl.modality.nlp.DefaultVocabulary;
import ai.djl.modality.nlp.Vocabulary;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;
import ai.djl.util.JsonUtils;
import com.google.gson.reflect.TypeToken;
import top.aias.asr.bean.DecoderOutput;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DecoderTranslator implements NoBatchifyTranslator<NDList, DecoderOutput> {

    private List<AudioProcessor> processors;
    private Vocabulary vocabulary;
    Map<String, Integer> byteDecoder;
    private int kvLength;

    public DecoderTranslator(int kvLength) {
        this.kvLength = kvLength;
        processors = new ArrayList<>();
    }

    @Override
    public void prepare(TranslatorContext ctx) throws IOException {
        Path path = ctx.getModel().getModelPath();
        Path melFile = path.resolve("mel_80_filters.npz"); // mel_80_filters.npz

        processors.add(new PadOrTrim(480000));
        // Use model"s NDManager
        NDManager modelManager = ctx.getModel().getNDManager();
        processors.add(LogMelSpectrogram.newInstance(melFile, 80, modelManager));

        Map<String, Integer> vocab;
        Map<String, Integer> added;

        Type type = new TypeToken<Map<String, Integer>>() {
        }.getType();
        try (Reader reader = Files.newBufferedReader(path.resolve("vocab.json"))) {
            vocab = JsonUtils.GSON.fromJson(reader, type);
        }
        try (Reader reader = Files.newBufferedReader(path.resolve("added_tokens.json"))) {
            added = JsonUtils.GSON.fromJson(reader, type);
        }
        try (Reader reader = Files.newBufferedReader(path.resolve("byte_decoder.json"))) {
            byteDecoder = JsonUtils.GSON.fromJson(reader, type);
        }

        String[] result = new String[vocab.size() + added.size()];
        vocab.forEach((key, value) -> result[value] = key);
        added.forEach((key, value) -> result[value] = key);
        vocabulary = new DefaultVocabulary(Arrays.asList(result));
    }

    @Override
    public NDList processInput(TranslatorContext ctx, NDList input) throws Exception {
        NDList past_key_values = input.subNDList(0, kvLength);
        for (NDArray array : past_key_values) {
            array.setName("past_key_values(" + (kvLength / 4) + ",4)");
        }

        NDList encoder_outputs = input.subNDList(kvLength, kvLength + 2);
        for (NDArray array : encoder_outputs) {
            array.setName("encoder_outputs()");
        }

        NDArray decoder_input_ids = input.get(kvLength + 2);
        decoder_input_ids.setName("decoder_input_ids");

        NDList modelInput = new NDList();

        modelInput.add(decoder_input_ids);
        modelInput.addAll(encoder_outputs);
        modelInput.addAll(past_key_values);

        NDArray placeholder = ctx.getNDManager().create("");
        placeholder.setName("module_method:decoder");
        modelInput.add(placeholder);

        return modelInput;
    }

    @Override
    public DecoderOutput processOutput(TranslatorContext ctx, NDList list) throws Exception {
        NDArray logitsOutput = list.get(0);
        NDArray outputId = greedyStepGen(logitsOutput);
        NDList pastKeyValuesList = list.subNDList(1, kvLength + 1);

        long tokenId = outputId.getLong(0);
        String text = vocabulary.getToken(tokenId);

        DecoderOutput decoderOutput = new DecoderOutput(text, outputId, vocabulary, byteDecoder, pastKeyValuesList);

        return decoderOutput;
    }

    private NDArray greedyStepGen(NDArray logits) {
        // logits:  [batch, seq, probDim]
        assert logits.getShape().getShape().length == 3 : "unexpected input";
        logits = logits.get(":, -1, :");
        return logits.argMax(-1).expandDims(1); // [batch, vacDim]
    }
}