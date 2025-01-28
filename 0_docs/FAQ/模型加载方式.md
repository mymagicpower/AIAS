<div align="center">
  <a href="http://aias.top/">点击返回网站首页</a>
</div>  


#### 常用的模型加载方式


1. 如何通过url在线加载模型？
```text
# 使用 optModelUrls 在线加载url模型

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_ppocr_mobile_v2.0_det_infer.zip")
            .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
            .optProgress(new ProgressBar())
            .build();
```

2. 如何本地加载模型？
```text
# 使用 optModelPath 加载zip压缩包模型
    Path modelPath = Paths.get("src/test/resources/ch_ppocr_mobile_v2.0_det_infer.zip");
    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelPath(modelPath)
            .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
            .optProgress(new ProgressBar())
            .build();

# 使用 optModelPath 加载本地文件夹下模型
    Path modelPath = Paths.get("src/test/resources/ch_ppocr_mobile_v2.0_det_infer/");
    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelPath(modelPath)
            .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
            .optProgress(new ProgressBar())
            .build();            
```

3. 如何加载打包到jar中模型？
```text
# 使用 optModelUrls 加载模型
# 假设：打包好的jar包，模型在jar包的： BOOT-INF/classes/ch_ppocr_mobile_v2.0_det_infer.zip

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelUrls("jar:///ch_ppocr_mobile_v2.0_det_infer.zip")
            .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
            .optProgress(new ProgressBar())
            .build();
```



<div align="center">
  <a href="http://aias.top/">点击返回网站首页</a>
</div>  

