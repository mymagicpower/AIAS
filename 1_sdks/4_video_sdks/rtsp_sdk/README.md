## 目录：
http://aias.top/

### 下载模型
- 查看最新下载链接请查看 1_sdks/README.md

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html


### 口罩检测
口罩检测助力抗击肺炎，人工智能技术正被应用到疫情防控中来。 抗疫切断传播途径中，
佩戴口罩已经几乎成为了最重要的举措之一。但是在实际场景中，仍然有不重视、不注意、
侥幸心理的人员不戴口罩，尤其在公众场合，给个人和公众造成极大的风险隐患。 
而基于人工智能的口罩检测功能可以基于摄像头视频流进行实时检测。

## SDK功能
通过rtsp取流，实时（需要有显卡的台式机，否则会比较卡顿）检测口罩。
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
![result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/mask_sdk/face-masks.png)

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   


#### 帮助文档：
- http://aias.top/guides.html
- 1.性能优化常见问题:
- http://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- http://aias.top/AIAS/guides/windows.html