## Java implementation of Librosa voice processing package

Java implementation of the Python voice processing library Librosa.

### Common functions:

- -> Load audio files and read amplitude (magnitude)
  -librosa.loadAndRead()
- -> Mel Frequency Cepstral Coefficients
  -librosa.generateMFCCFeatures()
- -> Extract mel (MelSpectrogram) feature values from wav
  -librosa.generateMelSpectroGram()
- -> Short-time Fourier transform
  -librosa.generateSTFTFeatures()
- -> Inverse Short-time Fourier Transform (ISTFT)
  -librosa.generateInvSTFTFeatures()

## Running example - JLibrosaExample

After successful execution, the command line should display the following information:
```text
...
Audio Feature Values：
0.000040
0.000040
0.000000
0.000000
0.000100
0.000070
0.000070
0.000000
0.000000
0.000000

Sample Rate: 16000
.......

Size of MFCC Feature Values: (40 , 97 )
-643.894348
-644.722900
-645.119995
-629.035706
-542.736816
-473.609894
-457.379211
-463.240082
-407.562378
-255.510406
.......

Size of STFT Feature Values: (1025 , 97 )
Real and Imag values of STFT are 0.031431286060502925,0.0
Real and Imag values of STFT are 0.02153403593129419,0.0
Real and Imag values of STFT are 0.00658287100191068,0.0
Real and Imag values of STFT are 0.02978720482718357,0.0
Real and Imag values of STFT are 0.15048249086374446,0.0
Real and Imag values of STFT are 0.2464715605426407,0.0
Real and Imag values of STFT are 0.2570755996529303,0.0
Real and Imag values of STFT are 0.15607790080422798,0.0
Real and Imag values of STFT are -0.047981990330747716,0.0
Real and Imag values of STFT are -0.27254267235230273,0.0

```
