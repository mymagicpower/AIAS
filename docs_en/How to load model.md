
#### Common model loading methods


1. How to load a model online through URL?
```text
# Use optModelUrls to load the URL model online

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
# Use optModelPath to load the ZIP compressed package model
    Path modelPath = Paths.get("src/test/resources/ch_ppocr_mobile_v2.0_det_infer.zip");
    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelPath(modelPath)
            .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
            .optProgress(new ProgressBar())
            .build();

# Use optModelPath to load the model from the local folder
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

3. How to load a model packaged into a jar?
```text
# Use optModelUrls to load the model
# Assuming that the packaged jar file has the model at: BOOT-INF/classes/ch_ppocr_mobile_v2.0_det_infer.zip

    Criteria<Image, DetectedObjects> criteria =
        Criteria.builder()
            .optEngine("PaddlePaddle")
            .setTypes(Image.class, DetectedObjects.class)
            .optModelUrls("jar:///ch_ppocr_mobile_v2.0_det_infer.zip")
            .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
            .optProgress(new ProgressBar())
            .build();
```

