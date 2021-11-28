## 语音处理包Librosa的java实现
python语音处理库librosa的java实现。

#### 常用功能：
--> 加载音频文件，读取幅值(magnitude)
- librosa.loadAndRead()

--> 梅尔频率倒谱系数
- librosa.generateMFCCFeatures()

--> 从wav提取mel(MelSpectrogram)特征值
- librosa.generateMelSpectroGram()

--> 短时傅里叶变换
- librosa.generateSTFTFeatures()

--> 短时傅立叶逆变换（ISTFT）
- librosa.generateInvSTFTFeatures()


## 运行例子 - JLibrosaExample
运行成功后，命令行应该看到下面的信息:
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

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   

### jlibrosa 地址：
https://github.com/Subtitle-Synchronizer/jlibrosa
https://github.com/Subtitle-Synchronizer/jlibrosa/blob/master/binaries/jlibrosa-1.1.8-SNAPSHOT-jar-with-dependencies.jar