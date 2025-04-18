### 目录：
https://www.aias.top/



### 1. 图像预处理模型下载：
- 链接: https://pan.baidu.com/s/1Or8i1aOpsl8pZM66i2RNVQ?pwd=uh12
- 
```bash
# 假设系统为 windows , 假设下载后的路径如下：
D:\ai_projects\AIAS\3_api_platform\api-platform\models>:

2025/02/17  19:25    <DIR>          .
2025/02/18  17:18    <DIR>          ..
2025/02/17  19:25    <DIR>          controlnet
```

#### 1.1 更新配置文件 application.yml 的 active 属性，windows环境选择，win：
- 路径：api_platform\src\main\resources\application.yml
```bash
spring:
  profiles:
    # win - windows 环境
    # mac - Mac 环境
    # linux - Linux 环境
    # online - 模型在线加载
    active: win
```
#### 1.2 更新模型路径，以 windows 为例：application-win.yml
```bash
model:
  # 模型路径
  modelPath: D:\\ai_projects\\AIAS\\3_api_platform\\api-platform\\models\\
```

### 2. 图像生成类模型下载【可选】：总大小约 62G
- 模型默认是延迟加载【首次调用的时候加载模型，也就是说，不下载模型，不影响其它功能使用】
- 生成类模型很大，全部模型加载需要至少24G的显存
- 每次只使用一个图像生成功能，至少需要 4 G 显存
- 建议显存不大的情况下，一次不要点击多个图像生成的功能

- 链接: https://pan.baidu.com/s/1Agt84-DdykIO25hkWzvwRg?pwd=9g5r

```bash
# 假设系统为 windows , 假设下载后的路径如下：
H:\models\aigc>:

2023/08/30  16:36    <DIR>          .
2023/08/30  16:36    <DIR>          ..
2023/06/21  15:22    <DIR>          sd_cpu
2023/06/21  19:10    <DIR>          sd_gpu
```
#### 2.1 更新生成类模型路径，以 windows 为例：application-win.yml
```bash
model:
  sd:
    # 模型路径
    cpuModelPath: H:\\models\\aigc\\sd_cpu\\
    gpuModelPath: H:\\models\\aigc\\sd_gpu\\
```

#### 2.2 显卡配置
- 图像生成对显卡强依赖，CPU运行需要几分钟的时间才能生成一张图片
- 显卡CUDA版本：推荐 11.x 12.x 版本
- 参考测试数据：分辨率 512*512 25步 CPU(i5处理器) 5分钟。 3060显卡20秒。
- 开关参数路径：api_platform\src\main\resources\application.yml
```bash
model:
  # 设备类型 cpu gpu
  device: gpu
```



### API 能力平台
提供开箱即用的人工智能能力平台。

- Web应用，前端VUE，后端Springboot
- 可以直接部署使用，使用UI或者调用API集成到现有的系统中。

<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 1. Canny 辅助生成 <br>
          - Canny 边缘检测预处理器，<br>
          - 可很好识别出图像内各对象的边缘轮廓，<br>
          - 常用于生成线稿。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/canny_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 2. Mlsd 辅助生成 <br>
          - MLSD 线条检测用于生成房间、<br>
          - 直线条的建筑场景效果比较好。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/mlsd_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 3. Scribble 涂鸦辅助生成 <br>
          - 不用自己画，<br>
          - 图片自动生成类似涂鸦效果的草图线条。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/scribble_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 4. SoftEdge 辅助生成 <br>
          - SoftEdge 边缘检测，<br>
          - 可保留更多柔和的边缘细节，<br>
          - 类似手绘效果。 <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/softedge_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 5. OpenPose 辅助生成 <br>
          -姿态检测可生成图像中角色动作姿态的骨架图<br>
          - (含脸部特征以及手部骨架检测)，<br>
          - 这个骨架图可用于控制生成角色的姿态动作。 <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/openpose_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 6. 语义分割辅助生成 <br>
          - 语义分割可多通道应用，<br>
          - 原理是用颜色把不同类型的对象分割开，<br>
          - 让AI能正确识别对象类型和需求生成的区界。 <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/seg_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 7. 深度估计辅助生成<br>
          - 通过提取原始图片中的深度信息， <br>
          - 生成具有原图同样深度结构的图 <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/depth_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 8. 法线贴图辅助生成<br>
          - 根据图片生成法线贴图， <br>
          - 然后根据法向贴图生成新图。 <br>
          - 适合CG或游戏美术师。 <br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/normal_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 9. 线稿提取辅助生成 <br>
          - Lineart 边缘检测预处理器，<br>
          - 可很好识别出图像内各对象的边缘轮廓，<br>
          - 用于生成线稿。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart_sd_new.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 10. 卡通线稿辅助生成 <br>
          - 卡通边缘检测预处理器，<br>
          - 可很好识别出卡通图像内各对象的边缘轮廓，<br>
          - 用于生成线稿。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/lineart_anime_sd_new.png" width = "400px"/>
        </div>
      </td>
    </tr>          
    <tr>
      <td>
        <div align="left">
          <p>图像生成系列</p>    
          - 11. 内容重洗辅助生成<br>
          - 图片内容变换位置，<br>
          - 打乱次序生成新图<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/9_aigc/images/shuffle_sd.png" width = "400px"/>
        </div>
      </td>
    </tr>                                                    
  </table>
</div>

<br/>
<hr>
<br/>




### 1. 前端部署

#### 1.1 直接运行：
```bash
npm run dev
```

#### 1.2 构建dist安装包：
```bash
npm run build:prod
```

#### 1.3 nginx部署运行(mac环境部署管理前端为例)：
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# 编辑nginx.conf

    server {
        listen       8080;
        server_name  localhost;

        location / {
            root   /Users/calvin/xxxs_ui/dist/;
            index  index.html index.htm;
        }
     ......
     
# 重新加载配置：
sudo nginx -s reload 

# 部署应用后，重启：
cd /usr/local/Cellar/nginx/1.19.6/bin

# 快速停止
sudo nginx -s stop

# 启动
sudo nginx     
```

### 2. 后端部署
#### 2.1 环境要求：
- 系统JDK 1.8+，建议11

### 3. 运行程序：
运行编译后的jar：
```bash
# 运行程序  
# -Dfile.encoding=utf-8 参数可以解决操作系统默认编码导致的中文乱码问题
nohup java -Dfile.encoding=utf-8 -jar xxxxx.jar > log.txt 2>&1 &
```

### 4. 打开浏览器
- 输入地址： http://localhost:8089



#### 帮助文档：
- https://aias.top/guides.html
- 1.性能优化常见问题:
- https://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- https://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- https://aias.top/AIAS/guides/windows.html