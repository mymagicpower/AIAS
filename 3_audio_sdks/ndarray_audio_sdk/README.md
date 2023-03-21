## NDArray Advanced Audio Toolbox

Current features include:

- Get the float array of an audio file
- Save an audio file
- Create a silent audio segment with given duration and sample rate
- Append a silent segment to an audio sample
- Concatenate any number of speech segments together
- Calculate root mean square energy of audio in decibels
- Normalize audio to have desired effective value in decibels
- Compute linear spectrogram using fast Fourier transform
- Hanning window
- Extract mel frequency spectral features from wav

## Running Example - AudioExample
```text
...
        NDManager manager = NDManager.newBaseManager(Device.cpu());
        float[] floatArray = AudioArrayUtils.floatData("src/test/resources/test.wav");
        // Get the float array of the audio
        logger.info("Float array of audio: {}", Arrays.toString(floatArray));

        NDArray samples = manager.create(floatArray);
        float rmsDb = AudioUtils.rmsDb(samples);
        // Calculate root mean square energy of audio in decibels
        logger.info("Root mean square energy of audio: {}", rmsDb);

        // Normalize audio to -20 dB in preparation for feature extraction
        float target_dB = -20f;
        samples = AudioUtils.normalize(samples, target_dB);
        System.out.println("Normalized audio: " + samples.toDebugString(1000000000, 1000, 1000, 1000));

        // Generate frame step size in milliseconds
        float stride_ms = 10f;
        // Generate window size for generating frames in milliseconds
        float window_ms = 20f;
        samples = AudioUtils.linearSpecgram(manager, samples, stride_ms, window_ms);
        logger.info("Linear spectrogram using fast Fourier transform: {}", samples.toDebugString(1000000000, 1000, 10, 1000));...


```

Command line output:
```text
...
[INFO ] - Root mean square energy of audio: -28.989937

[INFO ] - Normalized audio: ND: (134240) cpu() float32
[ 3.09278257e-03,  3.26460390e-03,  1.71821259e-04, ... 133240 more]

[INFO ] - Linear spectrogram using fast Fourier transform: ND: (161, 838) cpu() float32
[[-15.4571, -16.4412, -16.7098, -20.372 , -23.9935, -15.8598, -17.1589, -15.5935, ..., -14.3427],
 [-15.948 , -16.8391, -16.8302, -17.8034, -19.115 , -15.8378, -19.4812, -15.7247, ..., -14.7543],
 [-20.8405, -18.5733, -19.4289, -19.1861, -19.4255, -18.1996, -18.0149, -18.977 , ..., -17.5405],
 [-19.1938, -21.0139, -21.07  , -20.2931, -20.23  , -22.3037, -20.1103, -21.3521, ..., -20.2267],
 [-19.7823, -21.2425, -21.5705, -19.9856, -21.6053, -20.9323, -22.4014, -21.5406, ..., -20.1177],
 [-20.0329, -23.9688, -20.718 , -20.9419, -23.5446, -21.1718, -22.1597, -20.9377, ..., -21.3833],
 [-19.3693, -21.0484, -21.1794, -20.765 , -20.6318, -20.5121, -21.7306, -20.6366, ..., -21.5107],
 [-18.6552, -20.0077, -20.6954, -20.5476, -19.7953, -21.1081, -22.0988, -20.7157, ..., -20.6352],
 [-18.9167, -19.3219, -20.1954, -24.7476, -20.7662, -20.3794, -24.4699, -22.1381, ..., -22.1803],
 [-19.3055, -19.4264, -20.4486, -22.8299, -21.0847, -23.5101, -20.4897, -19.7943, ..., -22.8922],
 ... 151 more]

...

```

