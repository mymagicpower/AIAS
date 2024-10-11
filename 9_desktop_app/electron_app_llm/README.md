
### 大模型桌面离线版App
#### 主要特性
- 支持中/英文
- 模型支持chatglm2，baichuan2，llama2，alpaca2等
- 支持4位，8位量化，16位半精度模型。
- 支持windows及mac系统
- 支持CPU，GPU


#### 功能介绍
- 选择模型，并运行
<div align="center"> 
<img width="794" alt="image" src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/apps/desktop_llm2.png">
</div>

- 创建新的对话，与模型聊天
<div align="center"> 
<img width="794" alt="image" src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/apps/desktop_llm1.png">
</div>

#### 项目构建
```text
# 环境搭建
npm install
# 开发环境运行
npm run dev
# mac生产环境发布
npm run build:mac
# windows生产环境发布
npm run build:win
# linux生产环境发布
npm run build:linux
```

#### 调用llama的关键代码（根据环境修改命令）
```text
    let bin_path = process.env.PY_SCRIPT || "../llama_bin/main"

    let proc = require('child_process').spawn(bin_path, ['-m', model_path, '-n', "256", '--repeat_penalty', "1.0", '-r', "User:", '-f', "/Users/calvin/Downloads/llm/llama_bin/chat-with-bob.txt", '-i']);
```


#### Llama.cpp项目构建
```text
下载代码：
git clone https://github.com/ggerganov/llama.cpp && cd llama.cpp


GPU Build:
复制所有文件：
From：
C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v11.7\extras\visual_studio_integration\MSBuildExtensions
To:
C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\MSBuild\Microsoft\VC\v160\BuildCustomizations

cd D:\githubs\llama.cpp\
mkdir build
cd build
cmake -DLLAMA_CUBLAS=ON -DBUILD_SHARED_LIBS=ON ..
cmake --build . --config Release

CPU Build:
cd D:\githubs\llama.cpp\
mkdir build2
cd build2
cmake -DLLAMA_CUBLAS=OFF -DBUILD_SHARED_LIBS=ON ..
cmake --build . --config Release

```

#### Llama.cpp模型转换参考例子
```text
# install Python dependencies
python3 -m pip install -r requirements.txt

模型量化：
python3 convert.py /root/autodl-tmp/Chinese-LLaMA-2-7B/  --outfile models/llama2_7b_f16.gguf  --outtype f16

 ./quantize ./models/llama2_7b_f16.gguf ./models/llama2_7b_q4_0.bin q4_0

Linux 运行：
cd /root/autodl-tmp/llama.cpp
./main -m ./models/llama2_7b_q4_0.bin -p "古希腊历史简介"
 
Mac 运行：
cd /Users/calvin/aias_projects/llama.cpp/
./build/bin/main -m /Users/calvin/Downloads/llama2_7b_q4_0.bin -p "古希腊历史简介"
 
交互模式运行：（-i flag）
./build/bin/main -m /Users/calvin/Downloads/llama2_7b_q4_0.bin -i

```