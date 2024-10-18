# OCR 文字识别桌面app
## 介绍
使用Electron构建UI界面，ocr识别使用paddle开源ocr模型，模型推理框架使用amazon djl推理引擎。

### 功能清单
- 图片文字识别
- 支持windows, linux, mac 一键安装
<div align="center">
<img src="https://aiart.oss-cn-shanghai.aliyuncs.com/assets/ocr.jpeg"  width = "500"/>
</div> 


## 项目配置
### 1. OCR SDK
### 1.1. 更新模型路径（替换成你的本地路径，或者参考其它的模型加载方式）
```
        String detModelUri ="E:\\desktop_app\\ocr\\models\\ch_PP-OCRv3_det_infer_onnx.zip";
        String recModelUri ="E:\\desktop_app\\ocr\\models\\ch_PP-OCRv3_rec_infer_onnx.zip";
```

### 1.2. java 命令行调试
```
java -Dfile.encoding=utf-8 -jar ocr-sdk-0.22.1.jar -i "E:\\Data_OCR\\ticket_new.png" -o "E:\\Data_OCR\\"
```

### 1.3. 如何通过url在线加载模型？
```
# 文档：
http://aias.top/AIAS/guides/load_model.html

# 使用 optModelUrls 在线加载url模型
        Criteria<Image, NDList> criteria =
                Criteria.builder()
                        .optEngine("OnnxRuntime")
                        .optModelName("inference")
                        .setTypes(Image.class, NDList.class)
                        .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_PP-OCRv3_det_infer_onnx.zip")
                        .optTranslator(new OCRDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();
						
```


### 2. OCR Electron 前端App 

### 2.1. 项目初始化
```
npm install

```

### 2.2. 开发环境
```
 npm run electron:serve
```

### 2.3. 生产环境发布
```
npm run electron:build
```

### 2.4. 如何配置启动java的命令行？
```
native_functions.js:

    //1. 开发环境：配置 jar_path
    let jar_path = "D:\\easy_AI_apps\\3_ocr\\backends_ocr\\ocr_sdk\\target\\ocr-sdk-0.22.1.jar";

    if(jar_path && (fs.existsSync(jar_path))){
        proc = require('child_process').spawn('java' , ['-Dfile.encoding=utf-8', '-jar' , jar_path , '-i' , input_path , '-o' , out_path ]);
    }
    
    //2. 生产环境：配置 java_path， jar_path, 如果环境中有java可以不使用 spawn(java_path...，直接使用 spawn('java' ....
    else{
        const path = require('path');
        let java_path =  path.join(path.dirname(__dirname), 'core','jdk-11.0.16.1','bin','java.exe');
        let jar_path =  path.join(path.dirname(__dirname), 'core' , 'ocr-sdk-0.22.1.jar' );
        // let proc = require('child_process').spawn( bin_path  , ['-i' , input_path , '-o' , out_path ]);
        proc = require('child_process').spawn(java_path, ['-Dfile.encoding=utf-8', '-jar' , jar_path , '-i' , input_path , '-o' , out_path ]);
    }
```

### 2.5. jar包该放在哪里？- 更新 native_functions.js
```
native_functions.js:

    //1. 开发环境：调试的时候，直接hardcode jar的路径
    let jar_path = "F:\dev\easy_AI_apps_win\3_ocr_desktop_app\backends_ocr\\ocr_sdk\\target\\ocr-sdk-0.20.0.jar";

    if(jar_path && (fs.existsSync(jar_path))){
        proc = require('child_process').spawn('java' , ['-Dfile.encoding=utf-8', '-jar' , jar_path , '-i' , input_path , '-o' , out_path ]);
    }
    
    //2. 生产环境：自动根据 vue.config.js里面的配置复制打包
    else{
        const path = require('path');
        let java_path =  path.join(path.dirname(__dirname), 'core','jdk-11.0.16.1','bin','java.exe');
        let jar_path =  path.join(path.dirname(__dirname), 'core' , 'ocr-sdk-0.20.0.jar' );
        // let proc = require('child_process').spawn( bin_path  , ['-i' , input_path , '-o' , out_path ]);
        proc = require('child_process').spawn(java_path, ['-Dfile.encoding=utf-8', '-jar' , jar_path , '-i' , input_path , '-o' , out_path ]);
    }
```

### 5. vue.config.js 如何配置？
```
1. mac, linux 环境配置: // "from": "../backends/ocr/"
2. windows环境配置:  "from": "E:\\ocr\\"
------------------
E:\ocr 的目录
2023/01/11  19:35    <DIR>          .
2023/08/15  19:45    <DIR>          ..
2023/01/11  19:29    <DIR>          jdk-11.0.16.1
2023/01/11  19:35       977,666,281 ocr-sdk-0.20.0.jar
------------------

            builderOptions: {
                productName:"文字识别",//项目名 这也是生成的exe文件的前缀名
                appId: 'top.aias.ocr',
                copyright:"All rights reserved by aias.top",//版权 信息
                afterSign: "./afterSignHook.js",
                "extraResources": [{
                    "from": "E:\\ocr\\" ,  // 将需要打包的文件，放在from指定的目录下
                    "to": "core",  //安装时候复制文件的目标目录
                    "filter": [
                        "**/*"
                    ]
                }],
```
