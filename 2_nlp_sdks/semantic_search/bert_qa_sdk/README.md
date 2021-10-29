# Bert问答SDK
基于BERT QA模型，输入一个问题及包含答案的文本段落（最大长度384），
模型可以从文本段落中找到最佳的答案。

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/bertQA.png)

## 运行例子 - BertQaInferenceExample
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

### 帮助 
引擎定制化配置，可以提升首次运行的引擎下载速度，解决外网无法访问或者带宽过低的问题。         
[引擎定制化配置](http://aias.top/engine_cpu.html)

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   