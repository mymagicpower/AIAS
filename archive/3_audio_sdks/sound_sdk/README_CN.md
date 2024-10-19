## 声音处理工具包

java常用声音工具，包含：
- 语音播放：播放wav,mp3,flac,ape格式音频文件
- mp3 转 wav
- wav文件格式转换
- 截取部分wav文件（可以设置起始终止时间）
- wav文件合并（两个合并成一个）

## 运行例子 - AudioPlayerExample
运行成功后，命令行应该看到下面的信息:
并且能听到播放的声音。
```text
音频格式：MPEG1L3
每秒播放帧数：44100.0
总帧数：-1
音频时长（秒）：-2.2675737E-5
```

## 运行例子 - MP3ToWAVExample
运行成功后，命令行应该看到下面的信息:
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
## 运行例子 - WavToWavExample
运行成功后，命令行应该看到下面的信息:
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

## 运行例子 - WaveChopExample
运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - Source wave file: build/output/wav_converted.wav
[INFO ] - Wave Length: 9 seconds
[INFO ] - Wave chopped: build/output/wav_chop_result.wav
```

## 运行例子 - WavToWavExample
运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - wavFile1: build/output/wav_converted.wav
[INFO ] - wavFile2: build/output/wav_converted.wav
[INFO ] - wav File appended: build/output/wav_appended.wav
```

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   