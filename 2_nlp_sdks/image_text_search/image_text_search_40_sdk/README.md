
### Download the model, place it in the models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/CLIP-ViT-B-32-IMAGE.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/M-BERT-Base-ViT-B.zip

### Cross-modal similarity comparison and retrieval SDK for image and text [Supports 40 languages]

### Background

OpenAI has released two new neural networks: CLIP and DALL·E. They combine natural language recognition (NLP) with image recognition, and have a better understanding of images and language in daily life. Previously, text was searched for text, and images were searched for images. Now, with the CLIP model, text can be used to search for images, and images can be used to search for text. The idea behind this is to map images and text to the same vector space. This allows for cross-modal similarity comparison and retrieval of images and text.

- Feature vector space (composed of images & text)
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip_Imagesearch.png)

### CLIP: "Unconventional" image recognition

Currently, most models learn to recognize images from labeled examples in annotated datasets, while CLIP learns to recognize images and their descriptions obtained from the Internet, i.e. it recognizes images through a description rather than single word labels like "cat" and "dog". To achieve this, CLIP learns to associate many objects with their names and descriptions, and can thus recognize objects outside the training set.

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip.png)
As shown in the figure above, the workflow of the CLIP network is to pre-train the image and text encoders to predict which images in the dataset match which text. Then, CLIP is converted into a zero-shot classifier. In addition, all classifications of the dataset are converted into labels like "photo of a dog" and the best matching image is predicted.

CLIP model address:
https://github.com/openai/CLIP/blob/main/README.md

### SDK functions:

- Image & text feature vector extraction
- Similarity calculation
- Softmax calculation of confidence

### List of supported languages:
* Albanian
* Amharic
* Arabic
* Azerbaijani
* Bengali
* Bulgarian
* Catalan
* Chinese (Simplified)
* Chinese (Traditional)
* Dutch
* English
* Estonian
* Farsi
* French
* Georgian
* German
* Greek
* Hindi
* Hungarian
* Icelandic
* Indonesian
* Italian
* Japanese
* Kazakh
* Korean
* Latvian
* Macedonian
* Malay
* Pashto
* Polish
* Romanian
* Russian
* Slovenian
* Spanish
* Swedish
* Tagalog
* Thai
* Turkish
* Urdu
* Vietnamese

### Running example - ImageTextSearchExample

After a successful run, the command line should display the following information:
```text
...
...
# Test text:
[INFO ] - texts: [Two dogs in the snow, A cat on a table, London at night]

# Test image:
[INFO ] - image: src/test/resources/two_dogs_in_snow.jpg
```
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/two_dogs_in_snow.jpeg)

```text
# Vector dimension:
[INFO ] - Vector dimension: 512

# Generate image vector:
[INFO ] - image embeddings: [0.22221693, 0.16178696, ..., -0.06122274, 0.13340257]

# Generate text vector & calculate similarity:
[INFO ] - text [Two dogs in the snow] embeddings: [0.07365318, -0.011488605, ..., -0.10090914, -0.5918399]
[INFO ] - Similarity: 30.857948%

[INFO ] - text [A cat on a table] embeddings: [0.01640176, 0.02016575, ..., -0.22862512, -0.091851026]
[INFO ] - Similarity: 10.379046%

[INFO ] - text [London at night] embeddings: [-0.19309878, -0.008406041, ..., -0.1816148, 0.12109539]
[INFO ] - Similarity: 14.382527%

# Softmax confidence calculation:
[INFO ] - texts: [Two dogs in the snow, A cat on a table, London at night]
[INFO ] - Label probs: [0.9999999, 1.2768101E-9, 6.995442E-8]

# Confidence of "Two dogs in the snow" being similar to the image: 0.9999999
```
### Open source algorithm
#### 1. Open source algorithms used by the SDK
- [sentence-transformers](https://github.com/UKPLab/sentence-transformers)
- [Pre-trained models](https://www.sbert.net/docs/pretrained_models.html#image-text-models)
- [Installation](https://www.sbert.net/docs/installation.html)
- [README](https://www.sbert.net/examples/applications/image-search/README.html)

#### 2. How to export the model?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

- Exporting CPU models (PyTorch models are special, and CPU and GPU models are not interchangeable. Therefore, CPU and GPU models need to be exported separately)
- device = torch.device("cpu")
- device = torch.device("gpu")
- export_image_search.py
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
