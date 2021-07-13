# 特征提取(512维)SDK
提取图片512维特征值，并支持图片1:1特征比对，给出置信度。

## SDK功能：
### 1. 特征提取
使用imagenet预训练模型resnet50，提取图片512维特征。

- 运行例子 - FeatureExtractionExample
测试图片
![img](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/feature_extraction_sdk/car1.png)

- 运行成功后，命令行应该看到下面的信息:
```text
...
512
[INFO ] - [..., 0.18182503, 0.13296463, 0.22447465, 0.07165501..., 0.16957843]

```

### 2. 图片1:1比对
计算图片相似度。

- 运行例子 - FeatureComparisonExample
测试图片: 左右特征对比
![img](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/feature_extraction_sdk/comparision.png)

- 运行成功后，命令行应该看到下面的信息:
```text
...
[INFO ] - 0.77396494

```


### 帮助 
添加依赖库：lib/aias-feature-extraction-lib-0.1.0.jar

## QQ群：
![Screenshot](https://djl-model.oss-cn-hongkong.aliyuncs.com/AIAS/OCR/OCR_QQ.png)

