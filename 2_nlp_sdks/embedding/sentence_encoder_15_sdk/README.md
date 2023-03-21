
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/distiluse-base-multilingual-cased-v1.zip

### Sentence Vector SDK [Supports 15 languages]
Sentence vector refers to mapping sentences to fixed-dimensional real vectors.
Representing variable-length sentences as fixed-length vectors serves downstream NLP tasks.
Supports 15 languages:
Arabic, Chinese, Dutch, English, French, German, Italian, Korean, Polish, Portuguese, Russian, Spanish, Turkish.

- Sentence vector   
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


Sentence vector applications:

- Semantic search retrieves text from the corpus that matches the query best based on sentence vector similarity.
- Text clustering: Text is converted to fixed-length vectors and unsupervised clustering of similar text is performed using a clustering model.
- Text classification: Representing text as sentence vectors and directly training text classifiers using simple classifiers.

### SDK functions:

- Sentence vector extraction
- Similarity (cosine) calculation

### Running example - SentenceEncoderExample

After running successfully, the command line should see the following information:
```text
...
# Test sentences:
# A set of English sentences
[INFO ] - input Sentence1: This model generates embeddings for input sentence
[INFO ] - input Sentence2: This model generates embeddings

# A set of Chinese sentences
[INFO ] - input Sentence3: 今天天气不错
[INFO ] - input Sentence4: 今天风和日丽

# Vector dimensions:
[INFO ] - Vector dimensions: 512

# English - Generated vectors:
[INFO ] - Sentence1 embeddings: [-0.07397884, 0.023079528, ..., -0.028247012, -0.08646198]
[INFO ] - Sentence2 embeddings: [-0.084004365, -0.021871908, ..., -0.039803937, -0.090846084]

# Calculating English similarity:
[INFO ] - Similarity: 0.77445346

# Chinese - Generated vectors:
[INFO ] - Sentence1 embeddings: [0.012180057, -0.035749275, ..., 0.0208446, -0.048238125]
[INFO ] - Sentence2 embeddings: [0.016560446, -0.03528302, ..., 0.023508975, -0.046362665]

# Calculating Chinese similarity:
[INFO ] - 中文 Similarity: 0.9972926

```

### Open source algorithm
#### 1. Open source algorithms used by the SDK
- [sentence-transformers](https://github.com/UKPLab/sentence-transformers)
- [Pre-trained models](https://www.sbert.net/docs/pretrained_models.html)
- [Installation](https://www.sbert.net/docs/installation.html)


#### 2. How to export the model?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

- Exporting CPU models (PyTorch models are special, and CPU and GPU models are not interchangeable. Therefore, CPU and GPU models need to be exported separately)
- device = torch.device("cpu")
- device = torch.device("gpu")
- export_model_15.py
```text
from sentence_transformers import SentenceTransformer
import torch

# model = SentenceTransformer('stsb-distilbert-base', device='cpu')
model = SentenceTransformer('distiluse-base-multilingual-cased-v1', device='cpu')
model.eval()
batch_size=1
max_seq_length=128
device = torch.device("cpu")
model.to(device)
input_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_type_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_mask = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
# input_features = (input_ids, input_type_ids, input_mask)
input_features = {'input_ids': input_ids, 'attention_mask': input_mask}

# traced_model = torch.jit.trace(model, example_inputs=input_features)
traced_model = torch.jit.trace(model, example_inputs=input_features,strict=False)
traced_model.save("models/distiluse-base-multilingual-cased-v1/distiluse-base-multilingual-cased-v1.pt")
```

