
### Download the model and put it in the models directory
- 链接: https://github.com/mymagicpower/AIAS/releases/download/apps/paraphrase-xlm-r-multilingual-v1.zip

### Sentence Vector SDK [Supports 100 languages]

Sentence vector refers to mapping sentences to fixed-dimensional real vectors.
Representing variable-length sentences as fixed-length vectors serves downstream NLP tasks.

- Supports the following 100 languages:  
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/languages_100.jpeg)
 
- Sentence Vector  
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


Sentence Vector Applications:

- Semantic search: Retrieve text from a corpus that best matches the query by sentence vector similarity.
- Text clustering: Convert text to fixed-length vectors and use clustering models to cluster similar texts without supervision.
- Text classification: Represent text as sentence vectors and train text classifiers directly with a simple classifier.

### SDK Functionality:

- Sentence vector extraction
- Similarity (cosine) calculation
- max_seq_length: 128 (subword segmentation, up to an average of about 60 words for English sentences)

### Running example - SentenceEncoderExample

After running successfully, you should see the following information on the command line:

```text
...
#Test sentences:
# A set of English
[INFO ] - input Sentence1: This model generates embeddings for input sentence
[INFO ] - input Sentence2: This model generates embeddings

# A set of Chinese
[INFO ] - input Sentence3: 今天天气不错
[INFO ] - input Sentence4: 今天风和日丽

# Vector dimensions:
[INFO ] - Vector dimensions: 768

# English - Generate vectors:
[INFO ] - Sentence1 embeddings: [0.10717804, 0.0023716218, ..., -0.087652676, 0.5144994]
[INFO ] - Sentence2 embeddings: [0.06960095, 0.09246655, ..., -0.06324193, 0.2669841]

# Calculate English similarity:
[INFO ] - Similarity: 0.84808713

# Chinese - Generate vectors:
[INFO ] - Sentence1 embeddings: [0.19896796, 0.46568888,..., 0.09489663, 0.19511698]
[INFO ] - Sentence2 embeddings: [0.1639189, 0.43350196, ..., -0.025053274, -0.121924624]

# Calculate Chinese Similarity:
# Due to the use of the sentencepiece tokenizer, Chinese word segmentation is more accurate and has better precision than the 15-language model (which only segments into characters without considering words).
[INFO ] - Similarity: 0.67201

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
- export_model_100.py
```text
from sentence_transformers import SentenceTransformer
import torch

# model = SentenceTransformer('stsb-distilbert-base', device='cpu')
model = SentenceTransformer('paraphrase-xlm-r-multilingual-v1', device='cpu')
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
traced_model.save("models/paraphrase-xlm-r-multilingual-v1/paraphrase-xlm-r-multilingual-v1.pt")
```
