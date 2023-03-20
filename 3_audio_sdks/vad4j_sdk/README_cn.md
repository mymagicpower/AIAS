## 语音活动检测(Voice Activity Detection,VAD)
语音活动检测(Voice Activity Detection,VAD)又称语音端点检测,语音边界检测。目的是从声音信号流里识别和消除长时间的静音期，
静音抑制可以节省宝贵的带宽资源，可以有利于减少用户感觉到的端到端的时延。

### Mac & Linux环境 
#### 运行例子 - MacAndLinuxVadExample
运行成功后，命令行应该看到下面的信息:
```text
Mac OS X
true
[INFO ] - closing VAD
true
[INFO ] - closing VAD
```
#### 帮助
- 程序非线程安全。多实例运行需考虑并发问题。
- 不要忘记close()问题，使用try-with-resources语句处理。


### Winndows 环境 
#### 运行例子 - WindowsExample
运行成功后，命令行应该看到下面的信息:
```text
...
Mac OS X
Error loading native library: java.lang.Exception: Unsupported OS: Mac OS X
```
#### 帮助
##### 共享库文件: 
- linux: vad4j_sdk/lib/linux
- -libfvad.so
- -libwebrtcvadwrapper.so
- windows: vad4j_sdk/lib/windows 
- -libfvad.dll
- -libwebrtcvadwrapper.dll
 
##### 设置环境变量
- 共享库文件需添加到 java.library.path 
- 设置环境变量： LD_LIBRARY_PATH to /path/to/shared/libraries:$LD_LIBRARY_PATH.

##### 音频数据
输入的数据需是16-bit PCM audio数据，详细信息请参考下面的链接：
https://github.com/jitsi/jitsi-webrtc-vad-wrapper/blob/master/readme.md

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   