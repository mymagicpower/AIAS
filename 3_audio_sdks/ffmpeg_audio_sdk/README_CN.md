## ffmpeg 音频工具箱

当前功能包括：
- 音频参数转换（包含采样率、编码，位数，通道数）
- 音频格式转换 (wav转mp3编码,mp3转wav编码等)
- 获取音频数组 (float音频数组)

#### 音频数据处理基础概念
Java默认采用大端序存储方式，而实际编码的音频数据是小端序，如果处理单8bit的音频不需要做转换，
但是如果是16bit或者以上的就需要处理成小端序字节顺序。

小端序：数据的高位字节存放在地址的低端 低位字节存放在地址高端
大端序：数据的高位字节存放在地址的高端 低位字节存放在地址低端
大端序是按照数字的书写顺序进行存储的，而小端序则是反顺序进行存储的。
```text
ByteBuffer byteBuffer=ByteBuffer.wrap(data, 0,dataLength);
//将byteBuffer转成小端序并获取shortBuffer
ShortBuffer shortBuffer=byteBuffer.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
//取出shortBuffer中的short数组
short[] shortSamples=new short[dataLength/2];
shortBuffer.get(shortSamples,0,shortLength);
```

#### ffmpeg 操作音频格式转换常用命令
程序中给出了java的实现方式。
```text
1.转MP3为wav

ffmpeg -i input.mp3 -acodec pcm_s16le -ac 2 -ar 44100 output.wav

2.转m4a为wav

ffmpeg -i input.m4a -acodec pcm_s16le -ac 2 -ar 44100 output.wav

3.wav转PCM

ffmpeg -i input.wav -f s16le -ar 44100 -acodec pcm_s16le output.raw

4.PCM转wav

ffmpeg -i input.raw -f s16le -ar 44100 -ac 2 -acodec pcm_s16le output.wav

```
#### 参数解释：
```text
# ffmpeg -i INPUT -ac CHANNELS -ar FREQUENCY -acodec PCMFORMAT OUTPUT
-i 设定输入流
-f 设定输出格式 （s16le为16位，f32le为32位）
-ar 设定采样率
-ac 设定声音的Channel数 （1表示单声道）
-acodec 设定声音编解码器，未设定时则使用与输入流相同的编解码器
```

## 运行例子 - AudioExample
```text
...
    //wav sample rate 参数转换
    AudioConversionUtils.convert("src/test/resources/test.wav", "build/output/test_.wav", avcodec.AV_CODEC_ID_PCM_S16LE, 8000, 1);
    //wav转mp3编码示例
    AudioConversionUtils.convert("src/test/resources/test.wav", "build/output/test.mp3", avcodec.AV_CODEC_ID_MP3, 8000, 1);
    //mp3转wav编码示例
    AudioConversionUtils.convert("src/test/resources/test.mp3", "build/output/test.wav", avcodec.AV_CODEC_ID_PCM_S16LE, 16000, 1);
    //音频的float数组
    logger.info("audio float array: {}", Arrays.toString(AudioArrayUtils.frameData("src/test/resources/test.wav")));
...

```

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   
