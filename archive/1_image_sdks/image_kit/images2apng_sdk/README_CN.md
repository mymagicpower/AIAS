## 目录：
http://aias.top/

## SDK功能
批量静态图转apng动态图。

pattern_type用于选择如何解析提供的文件名称的模式类型
sequence（序列模式）可以包含以下字符串：
 "%d" 或 "%0Nd"
 这两种字符串用于指定表示模式匹配的每个文件名中代表序列号的字符的位置。
 如果使用形式"%d0Nd"，则表示每个文件名中数字的字符串为0填充，N为表示数字的0填充数字的总数。
 注：文字字符"%"可以在模式中用字符串"%%"指定。
     
图片名称模板“image-%03d.jpg”，使用这个模板名称图片文件：
image-001.jpg
image-002.jpg
image-003.jpg
image-004.jpg
...

## 运行例子
1. 首先下载例子代码
```bash
git clone https://github.com/mymagicpower/AIAS.git
```

2. 导入examples项目到IDE中：
```
cd live_stream_sdks/images2apng_sdk
```

3. 运行例子代码：Images2Apng


## 效果如下：
录制apng保存于
```
build/output/images2Apng.apng
```
静态图序列：
![images](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_sdk/images.png)

生成apng效果图：
![gif](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/video_sdk/images2Apng.apng)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   