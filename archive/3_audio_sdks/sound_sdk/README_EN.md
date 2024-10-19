## Audio Processing Toolkit

Java audio processing tools, including:

- Voice playback: play wav, mp3, flac, ape format audio files
- mp3 to wav conversion
- Wav file format conversion
- Cut part of the wav file (start and end time can be set)
- Merge two wav files into one

## Example - AudioPlayerExample
After successful execution, the command line should display the following information:
and you should be able to hear the played sound.
```text
Audio Format: MPEG1L3
Frames per second: 44100.0
Total frames: -1
Audio duration (seconds): -2.2675737E-5
```

## Example - MP3ToWAVExample
After successful execution, the command line should display the following information:
```text
...
File Format Type: MP3
File Format String: MP3 (.mp3) file, byte length: 387701, data format: MPEG1L3 44100.0 Hz, unknown bits per sample, stereo, unknown frame size, 38.28125 frames/second, , frame length: 371
File lenght: 387701
Frame length: 371
Channels: 2
Encoding: MPEG1L3
Frame Rate: 38.28125
Frame Size: -1
Sample Rate: 44100.0
Sample size (bits): -1
Big endian: true
Audio Format String: MPEG1L3 44100.0 Hz, unknown bits per sample, stereo, unknown frame size, 38.28125 frames/second, 
Bytes Written: 854784
```
## Example - WavToWavExample
After successful execution, the command line should display the following information:
```text
File Format Type: MP3
File Format String: MP3 (.mp3) file, byte length: 385242, data format: MPEG1L3 44100.0 Hz, unknown bits per sample, stereo, unknown frame size, 38.28125 frames/second, , frame length: 369
File lenght: 385242
Frame length: 369
Channels: 2
Encoding: MPEG1L3
Frame Rate: 38.28125
Frame Size: -1
Sample Rate: 44100.0
Sample size (bits): -1
Big endian: true
Audio Format String: MPEG1L3 44100.0 Hz, unknown bits per sample, stereo, unknown frame size, 38.28125 frames/second, 
Bytes Written: 847872
```

## Example - WaveChopExample
After successful execution, the command line should display the following information:
```text
[INFO ] - Source wave file: build/output/wav_converted.wav
[INFO ] - Wave Length: 9 seconds
[INFO ] - Wave chopped: build/output/wav_chop_result.wav
```

## Example - WavToWavExample
After successful execution, the command line should display the following information:
```text
[INFO ] - wavFile1: build/output/wav_converted.wav
[INFO ] - wavFile2: build/output/wav_converted.wav
[INFO ] - wav File appended: build/output/wav_appended.wav
```
