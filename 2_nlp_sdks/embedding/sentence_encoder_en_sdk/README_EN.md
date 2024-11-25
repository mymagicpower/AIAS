
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/paraphrase-MiniLM-L6-v2.zip

### Lightweight sentence vector SDK [English]

Sentence vector refers to mapping sentences to fixed-dimensional real vectors.
Representing variable-length sentences as fixed-length vectors provides services for downstream NLP tasks.

- Sentence vector
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)


Applications of sentence vectors:
-Semantic search: Retrieve the most matching text in the corpus with the query through sentence vector similarity
-Text clustering: Convert text to fixed-length vectors, and unsupervisedly cluster similar texts through clustering models
-Text classification: Represented as sentence vectors, training text classifiers directly using simple classifiers

### SDK functions:

- Sentence vector extraction
- Similarity calculation

#### Running example - SentenceEncoderExample
After running successfully, you should see the following information on the command line:
```text
...
# Test sentences:
[INFO ] - input Sentence1: This model generates embeddings for input sentence
[INFO ] - input Sentence2: This model generates embeddings

# Vector dimensions:
[INFO ] - Vector dimensions: 384

# Generate vectors:
[INFO ] - Sentence1 embeddings: [-0.14147712, -0.025930656, -0.18829542,..., -0.11860573, -0.13064586]
[INFO ] - Sentence2 embeddings: [-0.43392915, -0.23374224, -0.12924, ..., 0.0916177, 0.080070406]

# Calculate Similarity:
[INFO ] - Similarity: 0.7306041

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
- export_model.py
```text
from sentence_transformers import SentenceTransformer
import torch

# model = SentenceTransformer('stsb-distilbert-base', device='cpu')
model = SentenceTransformer('paraphrase-MiniLM-L6-v2', device='cpu')
model.eval()
batch_size=1
max_seq_length=128
device = torch.device("cpu")
model.to(device)
input_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_type_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_mask = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
# input_features = (input_ids, input_type_ids, input_mask)
input_features = {'input_ids': input_ids, 'token_type_ids': input_type_ids, 'attention_mask': input_mask}

# traced_model = torch.jit.trace(model, example_inputs=input_features)
traced_model = torch.jit.trace(model, example_inputs=input_features,strict=False)
traced_model.save("traced_st_model.pt")
```