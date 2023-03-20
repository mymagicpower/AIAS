
### Download the model and place it in the /models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/CLIP-ViT-B-32-IMAGE.zip

### Feature Extraction (512-dimensional) SDK
Extract 512-dimensional feature values for images, and support 1:1 feature comparison of images to provide confidence.

### SDK features:
#### 1. Feature Extraction
Use the imagenet pre-trained model resnet50 to extract 512-dimensional features from images.

- Run example - FeatureExtractionExample
  Test image
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/feature_extraction_sdk/car1.png)

- After successful execution, the command line should display the following information:
```text
...
512
[INFO ] - [..., 0.18182503, 0.13296463, 0.22447465, 0.07165501..., 0.16957843]

```

#### Image 1:1 Comparison
Calculate the similarity of images.

- Run example - FeatureComparisonExample
  Test image: Left and right feature comparison
- 
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/feature_extraction_sdk/comparision.png)

- After successful execution, the command line should display the following information:
```text
...
[INFO ] - 0.77396494

```


### Open Source Algorithm
#### 1. Open source algorithms used by SDK
- [sentence-transformers](https://github.com/UKPLab/sentence-transformers)
- [Pretrained models](https://www.sbert.net/docs/pretrained_models.html#image-text-models)
- [Installation](https://www.sbert.net/docs/installation.html)


#### 2. How to export the model?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

- Export CPU model (PyTorch model is special, CPU & GPU models are not universal. Therefore, CPU and GPU need to be exported separately)
```text
from sentence_transformers import SentenceTransformer, util
from PIL import Image
import torch

#Load CLIP model
model = SentenceTransformer('clip-ViT-B-32', device='cpu')

#Encode an image:
# img_emb = model.encode(Image.open('two_dogs_in_snow.jpg'))

#Encode text descriptions
# text_emb = model.encode(['Two dogs in the snow', 'A cat on a table', 'A picture of London at night'])
text_emb = model.encode(['Two dogs in the snow'])
sm = torch.jit.script(model)
sm.save("models/clip-ViT-B-32/clip-ViT-B-32.pt")

#Compute cosine similarities
# cos_scores = util.cos_sim(img_emb, text_emb)
# print(cos_scores)
```

- Export GPU model
```text
  from sentence_transformers import SentenceTransformer, util
  from PIL import Image
  import torch

#Load CLIP model
model = SentenceTransformer('clip-ViT-B-32', device='gpu')

#Encode an image:
# img_emb = model.encode(Image.open('two_dogs_in_snow.jpg'))

#Encode text descriptions
# text_emb = model.encode(['Two dogs in the snow', 'A cat on a table', 'A picture of London at night'])
text_emb = model.encode(['Two dogs in the snow'])
sm = torch.jit.script(model)
sm.save("models/clip-ViT-B-32/clip-ViT-B-32.pt")

#Compute cosine similarities
# cos_scores = util.cos_sim(img_emb, text_emb)
# print(cos_scores)
```

