package top.aias.asr.model;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.audio.Audio;
import ai.djl.modality.audio.AudioFactory;
import ai.djl.modality.nlp.Vocabulary;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;
import org.bytedeco.ffmpeg.global.avutil;
import top.aias.asr.bean.DecoderOutput;
import top.aias.asr.model.pool.DecoderPool;
import top.aias.asr.model.pool.EncoderPool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 语音识别模型
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class WhisperModel implements AutoCloseable {

    private ZooModel<NDList, NDList> model;
    private EncoderPool encoderPool;
    private DecoderPool decoderPool;
    private int kvLength = 0;
    private int encoderIndex = 0;

    // 强制中文
    // forced_decoder_ids = processor.get_decoder_prompt_ids(language="chinese", task="transcribe")
    //force_token_map {1: 50260, 2: 50359, 3: 50363}
    private long[] forced_decoder_ids = new long[]{50260, 50359, 50363};

    public void init(String modelUri, int poolSize, int kvLength, int encoderIndex) throws MalformedModelException, ModelNotFoundException, IOException {
        this.kvLength = kvLength;
        this.encoderIndex = encoderIndex;
        this.model = ModelZoo.loadModel(criteria(modelUri));
        this.encoderPool = new EncoderPool(poolSize, model, kvLength, encoderIndex);
        this.decoderPool = new DecoderPool(poolSize, model, kvLength);
        // 此类模型图优化占时过长，关闭
        System.setProperty("ai.djl.pytorch.graph_optimizer", "false");
    }

    private Criteria<NDList, NDList> criteria(String model) {
        Criteria<NDList, NDList> criteria =
                Criteria.builder()
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get(model))
                        .optEngine("PyTorch")
//                        .optDevice(Device.cpu())
                        .optTranslator(new NoopTranslator())
                        .build();

        return criteria;
    }

    public void close() {
        this.model.close();
        this.encoderPool.close();
        this.decoderPool.close();
        System.clearProperty("ai.djl.pytorch.graph_optimizer");
    }

    public String asr(Path file, boolean isChinese) throws IOException, TranslateException {
        Audio audio =
                AudioFactory.newInstance()
                        .setChannels(1)
                        .setSampleRate(16000)
                        .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                        .fromFile(file);

        return asr(audio, isChinese);
    }

    public String asr(Audio audio, boolean isChinese) throws TranslateException {
        NDList inputs = encoder(audio);
        NDList encoder_outputs = inputs.subNDList(kvLength, kvLength + 2);
        NDArray pastOutputIds = inputs.get(kvLength + 2);
        if (isChinese) {
            pastOutputIds.set(new NDIndex(0, 0), forced_decoder_ids[0]);
        }
        List<String> sentence = new ArrayList<>();
        Map<String, Integer> byteDecoder;

        while (true) {
            DecoderOutput decoderOutput = decoder(inputs);

            // 强制中文
            // forced_decoder_ids = processor.get_decoder_prompt_ids(language="chinese", task="transcribe")
            if (isChinese) {
                forceTokensProcessor(decoderOutput, pastOutputIds);
            }

            inputs = new NDList();
            // past_key_values
            inputs.addAll(decoderOutput.getPastKeyValuesList());
            // encoder_outputs
            inputs.addAll(encoder_outputs);
            // decoder_input_ids
            inputs.add(decoderOutput.getOutputId());

            pastOutputIds = pastOutputIds.concat(decoderOutput.getOutputId(), 1);

            sentence.add(decoderOutput.getText());

            if ("<|endoftext|>".equals(decoderOutput.getText())) {
                byteDecoder = decoderOutput.getByteDecoder();
                break;
            }
        }

        String result = "";

        if (isChinese) {
            String output = String.join("", sentence);
            byte[] bytes = new byte[output.length()];

            for (int i = 0; i < output.length(); i++) {
                String c = "" + output.charAt(i);
                bytes[i] = (byte) byteDecoder.get(c).intValue();
            }

            result = new String(bytes, StandardCharsets.UTF_8);
        } else {
            result = String.join(" ", sentence);
            result = result.replaceAll("[^a-zA-Z0-9<|> ,.!]", "");
        }

        result = result.replaceAll("<\\|transcribe\\|>", "");
        result = result.replaceAll("<\\|notimestamps\\|>", "");
        result = result.replaceAll("<\\|endoftext\\|>", "");

        System.out.println(Arrays.toString(pastOutputIds.toLongArray()));

        return result;
    }

    private DecoderOutput decoder(NDList input) throws TranslateException {
        Predictor<NDList, DecoderOutput> predictor = decoderPool.getPredictor();
        DecoderOutput result = predictor.predict(input);
        decoderPool.releasePredictor(predictor);
        return result;
    }

    private NDList encoder(Audio speech) throws TranslateException {
        Predictor<Audio, NDList> predictor = encoderPool.getPredictor();
        NDList result = predictor.predict(speech);
        encoderPool.releasePredictor(predictor);
        return result;
    }

    private NDList encoder(Path file) throws IOException, TranslateException {
        Audio audio =
                AudioFactory.newInstance()
                        .setChannels(1)
                        .setSampleRate(16000)
                        .setSampleFormat(avutil.AV_SAMPLE_FMT_S16)
                        .fromFile(file);
        return encoder(audio);
    }

    private void forceTokensProcessor(DecoderOutput decoderOutput, NDArray pastOutputIds) {
        long generation_idx = pastOutputIds.getShape().getLastDimension();
        if (generation_idx <= 2) {
            long current_token = forced_decoder_ids[(int) generation_idx];
            decoderOutput.getOutputId().set(new NDIndex(0, 0), current_token);
            Vocabulary vocabulary = decoderOutput.getVocabulary();
            decoderOutput.setText(vocabulary.getToken(current_token));
        }
    }
}
