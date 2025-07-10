# 图像高清放大桌面App
## 介绍
使用Electron构建UI界面，使用realesrgan模型，模型推理框架使用vulkan。

### 功能清单
- 单张图片分辨率放大
- 批量图片分辨率放大
- 支持 windows, macos, ubuntu
<div align="center">
<img src="https://aiart.oss-cn-shanghai.aliyuncs.com/assets/upscale.png"  width = "500"/>
</div> 


## 项目配置
### 1. 模型sdk
-模型地址：
[realsr-ncnn-vulkan](https://github.com/nihui/realsr-ncnn-vulkan)

### 1.1. 下载模型项目
```
https://github.com/nihui/realsr-ncnn-vulkan/releases
        
realesrgan_ncnn_macos
realsr-ncnn-vulkan-windows
```

### 1.2. 命令
```
realsr-ncnn-vulkan.exe -i input.jpg -o output.png -s 4

-------------------------------------------------------------------
Usage: realsr-ncnn-vulkan -i infile -o outfile [options]...

  -h                   show this help
  -v                   verbose output
  -i input-path        input image path (jpg/png/webp) or directory
  -o output-path       output image path (jpg/png/webp) or directory
  -s scale             upscale ratio (4, default=4)
  -t tile-size         tile size (>=32/0=auto, default=0) can be 0,0,0 for multi-gpu
  -m model-path        realsr model path (default=models-DF2K_JPEG)
  -g gpu-id            gpu device to use (-1=cpu, default=0) can be 0,1,2 for multi-gpu
  -j load:proc:save    thread count for load/proc/save (default=1:2:2) can be 1:2,2,2:2 for multi-gpu
  -x                   enable tta mode
  -f format            output image format (jpg/png/webp, default=ext/png)
```


### 2. Electron 前端App 

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

### 2.4. 如何配置启动realesrgan命令行？
```
native_functions.js:

    // 1. 开发环境：配置 bin_path
    // - windows bin_path:realsr-ncnn-vulkan.exe
    // - linux bin_path:realsr-ncnn-vulkan
    let bin_path = "D:\\ai_projects\\AIAS\\9_desktop_app\\desktop_app_upscale\\realesrgan_ncnn\\realsr-ncnn-vulkan.exe"

    if(bin_path && (fs.existsSync(bin_path))){
        proc = require('child_process').spawn( bin_path  , ['-i' , input_path , '-o' , out_path ]);
    }
    
    // 2. 生产环境：配置 bin_path
    // - windows bin_path:realsr-ncnn-vulkan.exe
    // - linux bin_path:realsr-ncnn-vulkan 
    else{
        const path = require('path');
        bin_path = path.join(path.dirname(__dirname), 'core' , 'realsr-ncnn-vulkan.exe' );
        proc = require('child_process').spawn( bin_path  , ['-i' , input_path , '-o' , out_path ]);
    }
```

### 2.5. realesrgan_ncnn该放在哪里？- 更新 native_functions.js
```
native_functions.js:

    //1. 开发环境：调试的时候，直接hardcode jar的路径
    let bin_path = "D:\\ai_projects\\AIAS\\9_desktop_app\\desktop_app_upscale\\realesrgan_ncnn\\realsr-ncnn-vulkan.exe"

    if(bin_path && (fs.existsSync(bin_path))){
        proc = require('child_process').spawn( bin_path  , ['-i' , input_path , '-o' , out_path ]);
    }
    
    //2. 生产环境：自动根据 vue.config.js里面的配置复制打包
    else{
        const path = require('path');
        bin_path = path.join(path.dirname(__dirname), 'core' , 'realsr-ncnn-vulkan.exe' );
        proc = require('child_process').spawn( bin_path  , ['-i' , input_path , '-o' , out_path ]);
    }

```

### 2.6. vue.config.js 如何配置？
```
1. mac, linux 环境配置: // "from": "../backends/upscale/"
2. windows环境配置:  "from": "E:\\desktop_app\\upscale\\"
------------------
E:\desktop_app\upscale 的目录

2023/01/12  10:00    <DIR>          .
2023/08/15  19:45    <DIR>          ..
2023/01/12  10:00            11,985 input.jpg
2023/01/12  10:00            10,286 input2.jpg
2023/01/12  09:49    <DIR>          models-DF2K
2023/01/12  09:49    <DIR>          models-DF2K_JPEG
2022/07/28  13:59         6,104,064 realsr-ncnn-vulkan.exe
2022/06/10  19:52           181,680 vcomp140.dll
------------------

            builderOptions: {
                productName:"图像放大",//项目名 这也是生成的exe文件的前缀名
                appId: 'top.aias.ocr',
                copyright:"All rights reserved by aias.top",//版权 信息
                afterSign: "./afterSignHook.js",
                "extraResources": [{
                    "from": "E:\\desktop_app\\upscale\\" ,  // 将需要打包的文件，放在from指定的目录下
                    "to": "core",  //安装时候复制文件的目标目录
                    "filter": [
                        "**/*"
                    ]
                }],
```

### 2.7. 如何启用web开发者工具？- background.js
```
	if (process.env.WEBPACK_DEV_SERVER_URL) {
		// Load the url of the dev server if in development mode
		await win.loadURL(process.env.WEBPACK_DEV_SERVER_URL)
		
        // 打开下面这行的注释
		// if (!process.env.IS_TEST) win.webContents.openDevTools()
	} else {
		createProtocol('app')
		// Load the index.html when not in development
		win.loadURL('app://./index.html')
```