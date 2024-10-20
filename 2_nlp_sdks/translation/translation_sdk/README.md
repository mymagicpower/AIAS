<div align="center">
	<a href="http://aias.top/">点击前往网站首页</a>
</div>  

### 下载模型，放置于models目录
- 链接: https://pan.baidu.com/s/1sCH0dPjQoBchN8T2BbTRHw?pwd=388n

## 中英互相翻译 - SDK

### SDK功能：
- 可以进行英语和中文之间的翻译
- 支持 CPU / GPU


### 运行例子 - TextGeneration
```text
// 输入文字
String input = "智利北部的丘基卡马塔矿是世界上最大的露天矿之一，长约4公里，宽3公里，深1公里。";

SearchConfig config = new SearchConfig();
config.setMaxSeqLength(128);

String modelPath = "models/opus-mt-zh-en/";

try (TranslationModel translationModel = new TranslationModel(config, modelPath, 4, Device.cpu());
) {
    System.setProperty("ai.djl.pytorch.graph_optimizer", "false");

    long start = System.currentTimeMillis();
    String result = translationModel.translate(input);
    long end = System.currentTimeMillis();
    logger.info("Time: {}", (end - start));
    logger.info("{}", result);

} finally {
    System.clearProperty("ai.djl.pytorch.graph_optimizer");
}

// 输出翻译结果
logger.info("{}", result);
[INFO ] - The Chuki Kamata mine in northern Chile is one of the largest open-pit mines in the world, about 4 km long, 3 km wide and 1 km deep.

```






<div align="center">
	<a href="http://aias.top/">点击前往网站首页</a>
</div>  