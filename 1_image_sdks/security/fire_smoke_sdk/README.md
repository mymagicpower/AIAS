# 烟火检测SDK
支持烟雾-火灾2类检测.


### SDK功能
- 烟火检测，给出检测框和置信度
- 支持类别：
- fire
- smoke

## 运行例子 - FireSmokeDetectExample
- 测试图片
![fire_detect](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/sec_sdks/images/fire_detect_result.png)

运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [
	class: "fire 0.847178041934967", probability: 0.84717, bounds: [x=0.522, y=0.516, width=0.083, height=0.173]
	class: "smoke 0.4434642493724823", probability: 0.44346, bounds: [x=0.492, y=0.000, width=0.295, height=0.116]
	class: "smoke 0.36228814721107483", probability: 0.36228, bounds: [x=0.576, y=0.110, width=0.113, height=0.121]
]
```

### 帮助 
添加依赖库：lib/aias-fire-smoke-lib-0.1.0.jar
