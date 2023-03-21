
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/speakerEncoder.zip

### Extracting feature vectors using a speaker encoder

The Google team proposed a neural system for text-to-speech synthesis that can learn the speech characteristics of multiple different speakers by using a small amount of samples and combine them into synthesized speech audio. Additionally, it can synthesize speech audio of an unknown speaker who the network has not encountered during training with just a few seconds of their audio, meaning the network has zero-shot learning ability.

Traditional natural speech synthesis systems require a lot of high-quality samples during training, and usually need hundreds or thousands of minutes of training data for each speaker. This often results in models that lack universality and cannot be widely applied in complex environments with many different speakers. These networks combine the processes of speech modeling and speech synthesis into one.

The SV2TTS work first separates these two processes and uses the first speech feature encoding network (encoder) to model the speaker's speech features. Then, it uses a second high-quality TTS network to convert features into speech.

- SV2TTS Paper
[Transfer Learning from Speaker Verification to  Multispeaker Text-To-Speech Synthesis](https://arxiv.org/pdf/1806.04558.pdf)

- Network Structure
![SV2TTS](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/voice_sdks/SV2TTS.png)

It mainly consists of three parts:
-Speaker Encoder
Extracts the speaker's voice feature information. Embeds the speaker's speech into a fixed dimension vector that represents the speaker's latent voice feature.
-Sequence-to-Sequence mapping synthesis network
Based on the Tacotron 2 mapping network, it generates log mel spectrograms using text and vector information obtained from the speaker encoder. (Mel spectrograms take the logarithm of the frequency scale Hz of the spectrogram, convert it to the mel scale, and linearly relate the sensitivity of the human ear to sound with the mel scale).
-Autoregressive speech synthesis network based on WaveNet
Converts mel spectrograms (frequency domain) into time domain waveform graphs to complete speech synthesis.

It is important to note that all three networks are trained independently. The speaker encoder network mainly serves as conditional supervision for the sequence mapping network, ensuring that the generated speech has unique voice characteristics.

### Speaker Encoder

The encoder mainly embeds reference speech signals into a fixed dimension vector space and uses this as supervision to allow the mapping network to generate original speech signals (mel spectrograms) with the same characteristics. The key role of the encoder is similarity measurement. For different speeches from the same speaker, the vector distance (cosine angle) in the embedding vector space should be as small as possible, whereas for different speakers, it should be as large as possible. Additionally, the encoder should have noise resistance and robustness in order to extract the speaker's latent voice feature information without being affected by specific speech content and background noise. These requirements coincide with those of the speaker-discriminative speech recognition model, so transfer learning can be conducted.

The encoder mainly consists of three LSTM layers. The input is a logarithmic mel spectrogram with 40 channels. After processing, the output of the last cell of the last layer is the embedding vector representation of the whole sequence after L2 regularization. In actual inference, any length of input speech signal is segmented into multiple segments with an 800ms window, and each segment is output. Finally, all output is averaged and superimposed to obtain the final embedding vector. This method is very similar to the short-time Fourier transform (STFT).

The generated embedding space vector visualization is shown in the following figure:
![embedding](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/voice_sdks/embedding.jpeg)

Different speakers correspond to different clustering ranges in the embedding space and can be easily distinguished. Speakers of different genders are located on opposite sides. (However, synthesized speech and real speech are also relatively easy to distinguish, and the distance from the clustering center of synthesized speech is farther. This indicates that the realism of synthesized speech is still not enough.)

### Running Example - SpeakerEncoderExample

After running successfully, the command line should show the following information:
```text
...
# Test speech file:
# src/test/resources/biaobei-009502.mp3

# Generate feature vectors:
[INFO] - embeddings shape: [256]
[INFO] - embeddings: [0.06272025, 0.0, 0.24136968, ..., 0.035975248, 0.0, 0.106041126, 0.027405139, 0.0, 0.07339379, 0.0]

```
