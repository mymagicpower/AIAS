
### Download the model and put it in the models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/CLIP-ViT-B-32-IMAGE.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/CLIP-ViT-B-32-TEXT.zip

## Image & Text Cross-modal Similarity Retrieval SDK
The idea behind this SDK is to map images and texts into the same vector space. This way, cross-modal similarity retrieval between images and texts can be achieved.

- Feature vector space (composed of images & texts)

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/clip_Imagesearch.png)

### SDK features:

- Image & Text feature vector extraction
- Similarity calculation
- Softmax Confidence Calculation

## Run Example - ImageTextSearchExample

After running successfully, the command line should show the following information:
```text
...
#Test text & image:
[INFO ] - texts: [a diagram, a dog, a cat]
[INFO ] - image: src/test/resources/CLIP.png

#Vector dimension:
[INFO ] - Vector dimension: 512

#Generate image vector:
[INFO ] - image embeddings: [0.44528162, -0.24093077, ..., -0.14698291, 0.097667605]

#Generate text vector & calculate similarity:
[INFO ] - text [a diagram] embeddings: [0.05469787, -0.006111175, ..., -0.12809977, -0.49502343]
[INFO ] - Similarity: 26.80176%
[INFO ] - text [a dog] embeddings: [0.14469987, 0.022480376, ..., -0.34201097, 0.17975798]
[INFO ] - Similarity: 20.550625%
[INFO ] - text [a cat] embeddings: [0.19806248, -0.20402007, ..., -0.56643164, 0.05962909]
[INFO ] - Similarity: 20.786627%

#Softmax Confidence Calculation:
[INFO ] - Label probs: [0.9956493, 0.0019198752, 0.0024309014]

```