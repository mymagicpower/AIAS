package top.aias.asr.whisper;

import ai.djl.audio.processor.AudioProcessor;
import ai.djl.audio.processor.LogMelSpectrogram;
import ai.djl.audio.processor.PadOrTrim;
import ai.djl.modality.audio.Audio;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class EncoderTranslator implements NoBatchifyTranslator<Audio, NDList> {

    private List<AudioProcessor> processors;
    private int kvLength;
    private int encoderIndex;
    public EncoderTranslator(int kvLength, int encoderIndex) {
        this.kvLength = kvLength;
        this.encoderIndex = encoderIndex;
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

    }

    @Override
    public NDList processInput(TranslatorContext ctx, Audio input) throws Exception {
        NDArray input_features = ctx.getNDManager().create(input.getData());
        for (AudioProcessor processor : processors) {
            input_features = processor.extractFeatures(input_features.getManager(), input_features);
        }
        input_features = input_features.expandDims(0);
        input_features.setName("input_features");
        NDArray decoder_input_ids = ctx.getNDManager().create(new int[]{50258}, new Shape(1, 1));
        decoder_input_ids.setName("decoder_input_ids");

        NDArray placeholder = ctx.getNDManager().create("");
        placeholder.setName("module_method:encoder");

        return new NDList(input_features, decoder_input_ids, placeholder);
    }

    @Override
    public NDList processOutput(TranslatorContext ctx, NDList list){
        NDArray logitsOutput = list.get(0);
        NDArray outputId = greedyStepGen(logitsOutput);

        NDList past_key_values = list.subNDList(1, kvLength + 1);
        // For tiny model:
        // 22 为 outputs[3] ，是最后一层输出， 23~27 为 outputs[4] ， 是所有encoder的隐藏层张量
        // encoder_outputs 正常应该是(outputs[3], outputs[4])
        // outputs[4] 是所有的隐藏层张量，但是只需要最后一层，考虑到 encoder_outputs 是 TupleOfTuple 结构，所以用 outputs[3] 替换。
        NDList encoder_outputs = new NDList(list.get(encoderIndex), list.get(encoderIndex));

        NDList result = new NDList();
        result.addAll(past_key_values);
        result.addAll(encoder_outputs);
        result.add(outputId);

        result.detach();

        return result;
    }

    private NDArray greedyStepGen(NDArray logits) {
        // logits:  [batch, seq, probDim]
        assert logits.getShape().getShape().length == 3 : "unexpected input";
        logits = logits.get(":, -1, :");
        return logits.argMax(-1).expandDims(1); // [batch, vacDim]
    }
}