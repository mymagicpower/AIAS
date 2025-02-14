package top.aias.asr.whisper;

import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.audio.Audio;
import ai.djl.modality.audio.AudioFactory;
import ai.djl.modality.nlp.Vocabulary;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.NoopTranslator;
import ai.djl.translate.TranslateException;
import org.bytedeco.ffmpeg.global.avutil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WhisperModel implements AutoCloseable {
    private ZooModel<NDList, NDList> whisperModel;
    private Predictor<Audio, NDList> encoderPredictor;
    private Predictor<NDList, DecoderOutput> decoderPredictor;
    private int kvLength = 0;
    private int encoderIndex = 0;

    // 强制中文
    // forced_decoder_ids = processor.get_decoder_prompt_ids(language="chinese", task="transcribe")
    //force_token_map {1: 50260, 2: 50359, 3: 50363}
    private long[] forced_decoder_ids = new long[]{50260, 50359, 50363};

    public WhisperModel(String model) throws ModelException, IOException {
        Criteria<NDList, NDList> criteria =
                Criteria.builder()
                        .setTypes(NDList.class, NDList.class)
                        .optModelPath(Paths.get(model))
                        .optEngine("PyTorch")
//                        .optDevice(Device.cpu())
                        .optTranslator(new NoopTranslator())
                        .build();
        whisperModel = criteria.loadModel();


        if (model.indexOf("tiny") > 0) {
            kvLength = 16;
            encoderIndex = 22;
        } else if (model.indexOf("base") > 0) {
            kvLength = 24;
            encoderIndex = 32;
        } else if (model.indexOf("small") > 0) {
            kvLength = 48;
            encoderIndex = 62;
        }

        assert kvLength == 0 : "unexpected input";
        assert encoderIndex == 0 : "unexpected input";

        encoderPredictor = whisperModel.newPredictor(new EncoderTranslator(kvLength, encoderIndex));
        decoderPredictor = whisperModel.newPredictor(new DecoderTranslator(kvLength));
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

//        System.out.println(Arrays.toString(pastOutputIds.toLongArray()));

        return result;
    }

    public DecoderOutput decoder(NDList input) throws TranslateException {
        return decoderPredictor.predict(input);
    }

    public NDList encoder(Audio speech) throws TranslateException {
        return encoderPredictor.predict(speech);
    }

    public NDList encoder(Path file) throws IOException, TranslateException {
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

    @Override
    public void close() {
        encoderPredictor.close();
        decoderPredictor.close();
        whisperModel.close();
    }
}
