# fastText的Java实现
fastText是一个快速文本分类算法，与基于神经网络的分类算法相比有两大优点：
- 1、fastText在保持高精度的情况下加快了训练速度和测试速度
- 2、fastText不需要预训练好的词向量，fastText会自己训练词向量
- 3、fastText两个重要的优化：Hierarchical Softmax、N-gram

- fastText背景知识介绍：    
https://blog.csdn.net/feilong_csdn/article/details/88655927

![image](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/fastText.png)

## 运行例子 - FastTextExample
-  训练的例子代码：
```text
    try (FtModel model = new FtModel("cooking")) {
        CookingStackExchange dataset = CookingStackExchange.builder().build();

        // setup training configuration
        FtTrainingConfig config =
                FtTrainingConfig.builder()
                        .setOutputDir(Paths.get("build"))
                        .setModelName("cooking")
                        .optEpoch(5)
                        .optLoss(FtTrainingConfig.FtLoss.HS)
                        .build();

        TrainingResult result = model.fit(config, dataset);
        Assert.assertEquals(result.getEpoch(), 5);
        Assert.assertTrue(Files.exists(Paths.get("build/cooking.bin")));
    }
```


-  运行成功后，命令行应该看到下面的信息:
```text

#BlazingText 测试文本："Convair was an american aircraft manufacturing company which later expanded into rockets and spacecraft."
[INFO ] - [
	class: "Company", probability: 0.99939
	class: "MeanOfTransportation", probability: 0.00032
	class: "Artist", probability: 0.00030
	class: "Building", probability: 0.00001
	class: "OfficeHolder", probability: 0.00001
]

#CookingStack 测试文本：Which baking dish is best to bake a banana bread ?
[INFO ] - [
	class: "baking", probability: 0.33718
	class: "bread", probability: 0.23076
	class: "cake", probability: 0.08515
	class: "flour", probability: 0.01667
	class: "milk", probability: 0.01364
]

#词向量测试Word2Vec，单词：bread
[INFO ] - [-0.24370453, 0.428182, ..., -0.0740295, -0.45106947]

```

### 参考
- fastText Github:   
  https://github.com/facebookresearch/fastText/

- Docs:    
  https://fasttext.cc/docs/en/support.html

- Resources:    
  https://fasttext.cc/docs/en/english-vectors.html
  
  ### 官网：
  [官网链接](http://www.aias.top/)
  
  ### Git地址：   
  [Github链接](https://github.com/mymagicpower/AIAS)    
  [Gitee链接](https://gitee.com/mymagicpower/AIAS)   