### 官网：
[官网链接](http://www.aias.top/)

### 下载模型
- 查看最新下载链接请查看 1_sdks/README.md

### 模型使用方法：
- 1. 用模型的名字搜索代码，找到模型的加载位置
- 2. 然后更新模型路径（代码里默认加载路径是：项目/models 文件夹）
- 3. 具体模型加载方法
- http://aias.top/AIAS/guides/load_model.html


### 自然语言与代码特征提取 SDK
自然语言与代码特征提取工具箱提供3个SDK，以满足不同精度与速度的需要。特征向量提取应用场景有：
代码推荐：基于代码特征提取算法，可以分析代码库中的代码片段，并为开发人员提供代码补全、代码片段推荐等功能，提高开发效率。
代码克隆检测：通过比较代码的特征表示，可以检测出相似的代码片段或代码文件，帮助开发人员避免代码重复和维护困难。
漏洞检测：利用代码特征提取算法，可以分析代码中潜在的漏洞模式或异常结构，帮助自动化漏洞检测和修复。
代码质量分析：通过代码特征提取，可以评估代码的复杂性、重复性、规范性等指标，帮助开发团队改进代码质量和可维护性。
自然语言处理与代码混合领域：在自然语言处理和代码之间建立桥梁，例如将自然语言描述转换为代码或代码注释生成等任务。
代码特征提取算法在软件工程领域有着广泛的应用，可以帮助开发人员更好地理解、分析和利用代码，提高软件开发的效率和质量。


- 句向量    
  ![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


### SDK功能：
- 向量提取
- 相似度（余弦）计算

### 应用场景：
- 语义搜索，通过句向量相似性，检索语料库中与query最匹配的文本
- 聚类，代码文本转为定长向量，通过聚类模型可无监督聚集相似文本
- 分类，表示成句向量，直接用简单分类器即训练文本分类器
  


#### 运行例子 - Text2VecExample
运行成功后，命令行应该看到下面的信息:
```text
...
# 测试代码：
        String input1 = "calculate cosine similarity between two vectors";
        String input2 = "  public static float dot(float[] feature1, float[] feature2) {\n" +
                "    float ret = 0.0f;\n" +
                "    int length = feature1.length;\n" +
                "    // dot(x, y)\n" +
                "    for (int i = 0; i < length; ++i) {\n" +
                "      ret += feature1[i] * feature2[i];\n" +
                "    }\n" +
                "\n" +
                "    return ret;\n" +
                "  }";
        String input3 = "  public static float cosineSim(float[] feature1, float[] feature2) {\n" +
                "    float ret = 0.0f;\n" +
                "    float mod1 = 0.0f;\n" +
                "    float mod2 = 0.0f;\n" +
                "    int length = feature1.length;\n" +
                "    for (int i = 0; i < length; ++i) {\n" +
                "      ret += feature1[i] * feature2[i];\n" +
                "      mod1 += feature1[i] * feature1[i];\n" +
                "      mod2 += feature2[i] * feature2[i];\n" +
                "    }\n" +
                "    //    dot(x, y) / (np.sqrt(dot(x, x)) * np.sqrt(dot(y, y))))\n" +
                "    return (float) (ret / Math.sqrt(mod1) / Math.sqrt(mod2));\n" +
                "  }";

# 向量维度：
[INFO ] - Vector dimensions: 768

#计算相似度：
[INFO ] - Code Similarity: 0.40372553
[INFO ] - Code Similarity: 0.57055503

```

### 开源算法
#### 1. sdk使用的开源算法
- sentence-transformers/all-mpnet-base-v2



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

