## 快速傅里叶变换(FFT)的java实现
快速傅里叶变换 (fast Fourier transform), 即利用计算机计算离散傅里叶变换（DFT)的高效、快速计算方法的统称，简称FFT。
快速傅里叶变换是1965年由J.W.库利和T.W.图基提出的。采用这种算法能使计算机计算离散傅里叶变换所需要的乘法次数大为减少，
特别是被变换的抽样点数N越多，FFT算法计算量的节省就越显著。
计算量小的显著的优点，使得FFT在信号处理技术领域获得了广泛应用，结合高速硬件就能实现对信号的实时处理。
例如，对语音信号的分析和合成，对通信系统中实现全数字化的时分制与频分制(TDM/FDM)的复用转换，在频域对信号滤波以及相关分析，
通过对雷达、声纳、振动信号的频谱分析以提高对目标的搜索和跟踪的分辨率等等，都要用到FFT。


## 运行例子 - FFTExample
```text
...
	    String audioFilePath = "src/test/resources/audio.wav";
        //-1 value implies the method to use default sample rate
        int defaultSampleRate = -1;
        //-1 value implies the method to process complete audio duration
        int defaultAudioDuration = -1;

        JLibrosa jLibrosa = new JLibrosa();

        // To read the magnitude values of audio files - equivalent to librosa.load('../audio.wav', sr=None)
        float audioFeatureValues[] = jLibrosa.loadAndRead(audioFilePath, defaultSampleRate, defaultAudioDuration);

        double[] arr = IntStream.range(0, audioFeatureValues.length).mapToDouble(i -> audioFeatureValues[i]).toArray();

        double[] fft = FFT.fft(arr);
        float[][] complex = FFT.rfft(fft);

        System.out.println("Real parts: " + Arrays.toString(complex[0]));
        System.out.println("Imaginary parts: " + Arrays.toString(complex[1]));
...
````

运行成功后，命令行应该看到下面的信息:
```text
# 复数的实部
Real parts: [-1.8461201, -1.1128254, 0.58502156, 2.6774616, -1.7226994, ..., 0.15794027]
# 复数的虚部
Imaginary parts: [0.0, 1.2845019, 2.8104274, -1.3958083, 1.2868061, ..., -0.3447435, 0.0]

```

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   