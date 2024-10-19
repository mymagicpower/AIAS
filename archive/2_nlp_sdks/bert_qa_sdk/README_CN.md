### 官网：
[官网链接](http://www.aias.top/)

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1KoIIDISDWlTtfLeOFXnr2g?pwd=zrmq

### Bert问答SDK
基于BERT QA模型，输入一个问题及包含答案的文本段落（最大长度384），
模型可以从文本段落中找到最佳的答案。

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/bertQA.png)

### 运行例子 - BertQaInferenceExample
- 问题: 
```text
When did Radio International start broadcasting?
```

- 包含答案的文本段落（最大长度384）:
```text
Radio International was a general entertainment Channel.
Which operated between December 1983 and April 2001.
```

运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - Paragraph: Radio International was a general entertainment Channel.
          Which operated between December 1983 and April 2001.
          
[INFO ] - Question: When did Radio International start broadcasting?

[INFO ] - Answer: [december, 1983]

```

### 开源算法
#### 无

### 其它帮助信息
http://aias.top/guides.html

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