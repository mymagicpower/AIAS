### 官网：
[官网链接](http://www.aias.top/)


#### 语音活动检测(Voice Activity Detection,VAD)
语音活动检测(Voice Activity Detection,VAD)又称语音端点检测,语音边界检测。目的是从声音信号流里识别和消除长时间的静音期， 静音抑制可以节省宝贵的带宽资源，可以有利于减少用户感觉到的端到端的时延。
VAD引擎需要8、16、32或48 KHz的采样率的单声道、16位PCM音频作为输入。输入应该是10、20或30毫秒的音频片段。当音频输入为16 KHz时，输入数组的长度应为160、320或480。
语音活动检测可以运行在4种不同的模式。模式范围从0到3。模式0非常严格，这意味着当VAD预测为语音时，音频片段是语音的概率更高。模式3非常激进，这意味着当VAD预测为语音时，音频是语音的概率较低。如预期的那样，模式1和2逐渐降低了这种概率。

#### 帮助
##### 共享库文件:
- linux:  lib/linux
- -libfvad.so
- -libwebrtcvadwrapper.so
- windows:  lib/windows
- -libfvad.dll
- -libwebrtcvadwrapper.dll

##### linux/mac 设置环境变量
- 共享库文件需添加到 java.library.path
  LD_LIBRARY_PATH to /path/to/shared/libraries:$LD_LIBRARY_PATH.

##### windows 设置环境变量
- 共享库文件需添加到 PATH



### sdk使用的开源项目（扩展了对windows的支持）
https://github.com/jitsi/jitsi-webrtc-vad-wrapper