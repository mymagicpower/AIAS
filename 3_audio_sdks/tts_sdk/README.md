
### Download the model and place it in the models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/denoiser.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/speakerEncoder.zip
- Link 3: https://github.com/mymagicpower/AIAS/releases/download/apps/tacotron2.zip
- Link 4: https://github.com/mymagicpower/AIAS/releases/download/apps/waveGlow.zip
- 
### TTS text to speech

Note: To prevent the cloning of someone else's voice for illegal purposes, the code limits the use of voice files to only those provided in the program.
Voice cloning refers to synthesizing audio that has the characteristics of the target speaker by using a specific voice, combining the pronunciation of the text with the speaker's voice.
When training a speech cloning model, the target voice is used as the input of the Speaker Encoder, and the model extracts the speaker's features (voice) as the Speaker Embedding.
Then, when training the model to resynthesize speech of this type, in addition to the input target text, the speaker's features will also be added as additional conditions to the model's training.
During prediction, a new target voice is selected as the input of the Speaker Encoder, and its speaker's features are extracted, finally realizing the input of text and target voice and generating a speech segment of the target voice speaking the text.
The Google team proposed a neural system for text-to-speech synthesis that can learn the speech features of multiple different speakers with only a small amount of samples, and synthesize their speech audio. In addition, for speakers that the network has not encountered during training, their speech can be synthesized with only a few seconds of unknown speaker audio without retraining, that is, the network has zero-shot learning ability.
Traditional natural speech synthesis systems require a large amount of high-quality samples during training, usually requiring hundreds or thousands of minutes of training data for each speaker, which makes the model generally not universal and cannot be widely used in complex environments (with many different speakers). These networks combine the processes of speech modeling and speech synthesis.
The SV2TTS work first separates these two processes and uses the first speech feature encoding network (encoder) to model the speaker's speech features, and then uses the second high-quality TTS network to convert features into speech.

- SV2TTS paper
[Transfer Learning from Speaker Verification to  Multispeaker Text-To-Speech Synthesis](https://arxiv.org/pdf/1806.04558.pdf)

- Network structure
![SV2TTS](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/voice_sdks/SV2TTS.png)

#### Mainly composed of three parts:
#### Sound feature encoder (speaker encoder)
Extract the speaker's voice feature information. Embed the speaker's voice into a fixed dimensional vector, which represents the speaker's potential voice features.
The encoder mainly embeds the reference speech signal into a vector space of fixed dimension, and uses it as supervision to enable the mapping network to generate original voice signals (Mel spectrograms) with the same features.
The key role of the encoder is similarity measurement. For different speeches of the same speaker, the vector distance (cosine angle) in the embedding vector space should be as small as possible, while for different speakers, it should be as large as possible.
In addition, the encoder should also have the ability to resist noise and robustness, and extract the potential voice feature information of the speaker's voice without being affected by the specific speech content and background noise.
These requirements are consistent with the requirements of the speech recognition model (speaker-discriminative), so transfer learning can be performed.
The encoder is mainly composed of three layers of LSTM, and the input is a 40-channel logarithmic Mel spectrogram. After the output of the last frame cell of the last layer is processed by L2 regularization, the embedding vector representation of the entire sequence is obtained.
In actual inference, any length of input speech signal will be divided into multiple segments by an 800ms window, and each segment will get an output. Finally, all outputs are averaged and superimposed to obtain the final embedding vector.
This method is very similar to the Short-Time Fourier Transform (STFT).
The generated embedding space vector is visualized as follows:
![embedding](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/voice_sdks/embedding.jpeg)

It can be seen that different speakers correspond to different clustering ranges in the embedding space, which can be easily distinguished, and speakers of different genders are located on both sides.
(However, synthesized speech and real speech are also easy to distinguish, and synthesized speech is farther away from the clustering center. This indicates that the realism of synthesized speech is not enough.)

####Sequence-to-sequence mapping synthesis network (Tacotron 2)
Based on the Tacotron 2 mapping network, the vector obtained from the text and sound feature encoder is used to generate a logarithmic Mel spectrogram.
The Mel spectrogram takes the logarithm of the spectral frequency scale Hz and converts it to the Mel scale, so that the sensitivity of the human ear to sound is linearly positively correlated with the Mel scale.
This network is trained independently of the encoder network. The audio signal and the corresponding text are used as inputs. The audio signal is first feature-extracted by a pre-trained encoder, and then used as input to the attention layer.
The network output feature consists of a sequence of length 50ms and a step size of 12.5ms. After Mel scaling filtering and logarithmic dynamic range compression, the Mel spectrogram is obtained.
In order to reduce the influence of noisy data, L1 regularization is additionally added to the loss function of this part.

The comparison of the input Mel spectrogram and the synthesized spectrogram is shown below:
![embedding](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/voice_sdks/tacotron2.jpeg)
The red line on the right graph represents the correspondence between text and spectrogram. It can be seen that the speech signal used for reference supervision does not need to be consistent with the target speech signal in the text, which is also a major feature of the SV2TTS paper.


#### Speech synthesis network (WaveGlow)
WaveGlow: A network that synthesizes high-quality speech by relying on streams from Mel spectrograms. It combines Glow and WaveNet to generate fast, good, and high-quality rhythms without requiring automatic regression.
The Mel spectrogram (frequency domain) is converted into a time series sound waveform (time domain) to complete speech synthesis.
It should be noted that these three parts of the network are trained independently, and the voice encoder network mainly plays a conditional supervision role for the sequence mapping network, ensuring that the generated speech has the unique voice features of the speaker.

## Running Example- TTSExample

After successful operation, the command line should see the following information:
```text
...
[INFO] - Text: Convert text to speech based on the given voice
[INFO] - Given voice: src/test/resources/biaobei-009502.mp3

#Generate feature vector:
[INFO] - Speaker Embedding Shape: [256]
[INFO] - Speaker Embedding: [0.06272025, 0.0, 0.24136968, ..., 0.027405139, 0.0, 0.07339379, 0.0]
[INFO] - Mel spectrogram data Shape: [80, 331]
[INFO] - Mel spectrogram data: [-6.739388, -6.266942, -5.752069, ..., -10.643405, -10.558134, -10.5380535]
[INFO] - Generate wav audio file: build/output/audio.wav
```
The speech effect generated by the text "Convert text to speech based on the given voice":
[audio.wav](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/voice_sdks/audio.wav)

