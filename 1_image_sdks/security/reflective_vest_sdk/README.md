# 反光衣检测SDK
实现施工区域或者危险区域人员穿戴检测.


### SDK功能
- 反光衣检测，给出检测框和置信度

## 运行例子 - ReflectiveVestDetectExample
- 测试图片
![pedestrian](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/sec_sdks/images/reflective_detect_result.png)

运行成功后，命令行应该看到下面的信息:
```text
[INFO ] - [
	class: "safe 0.936024010181427", probability: 0.93602, bounds: [x=0.316, y=0.628, width=0.259, height=0.370]
	class: "safe 0.9202641248703003", probability: 0.92026, bounds: [x=0.000, y=0.106, width=0.176, height=0.341]
	class: "safe 0.9085375070571899", probability: 0.90853, bounds: [x=0.578, y=0.501, width=0.221, height=0.485]
	class: "safe 0.8891122937202454", probability: 0.88911, bounds: [x=0.802, y=0.465, width=0.197, height=0.532]
	class: "unsafe 0.781899094581604", probability: 0.78189, bounds: [x=0.177, y=0.432, width=0.190, height=0.416]
]
```
