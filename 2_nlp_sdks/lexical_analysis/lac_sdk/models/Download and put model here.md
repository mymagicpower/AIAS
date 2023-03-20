
#### Common Model Loading Methods


1. How to load a model online via URL?
```text
# Use optModelUrls to load a model via URL

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelUrls("https://aias-home.oss-cn-beijing.aliyuncs.com/models/ocr_models/ch_ppocr_mobile_v2.0_det_infer.zip")
            .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
            .optProgress(new ProgressBar())
            .build();
```

2. How to load a model locally?
```text
# Use optModelPath to load a model from a zipped file
    Path modelPath = Paths.get("src/test/resources/ch_ppocr_mobile_v2.0_det_infer.zip");
    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelPath(modelPath)
            .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
            .optProgress(new ProgressBar())
            .build();

# Use optModelPath to load a model from a local directory
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

3. How to load a model packed into a JAR file?
```text
# Use optModelUrls to load a model
# Assuming the model is located in the JAR file at:
# BOOT-INF/classes/ch_ppocr_mobile_v2.0_det_infer.zip

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelUrls("jar:///ch_ppocr_mobile_v2.0_det_infer.zip")
            .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
            .optProgress(new ProgressBar())
            .build();
```


