# 口罩检测
口罩检测助力抗击肺炎，人工智能技术正被应用到疫情防控中来。 抗疫切断传播途径中，
佩戴口罩已经几乎成为了最重要的举措之一。但是在实际场景中，仍然有不重视、不注意、
侥幸心理的人员不戴口罩，尤其在公众场合，给个人和公众造成极大的风险隐患。 
而基于人工智能的口罩检测功能可以基于摄像头视频流进行实时检测。

## SDK功能
通过rtsp取流，实时检测口罩。
- 海康/大华等摄像机的rtsp地址：rtsp://user:password@192.168.16.100:554/Streaing/Channels/1
- 海康/大华等视频平台的rtsp地址：rtsp://192.168.16.88:554/openUrl/6rcShva
- 自己的rtsp地址

## 运行人脸检测的例子
1. 首先下载例子代码
```bash
git clone https://github.com/mymagicpower/AIAS.git
```

2. 导入examples项目到IDE中：
```
cd rtsp_facemask_sdk
```

3. 运行例子代码：RtspFaceMaskDetectionExample

## 效果如下：
![result](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/mask_sdk/face-masks.png)