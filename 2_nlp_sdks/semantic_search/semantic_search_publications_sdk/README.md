
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/allenai-specter.zip

### Academic Paper Semantic Search SDK [English]

The academic paper search model provides the ability to extract features and compare similarities of academic papers. The input parameters consist of [title, abstract] of the article. The subword level segmentation is used, with a maximum length of max_sequence_length: 256 (an average of about 130 words according to the empirical upper limit).

Used model:
[https://github.com/allenai/specter/blob/master/README.md](https://github.com/allenai/specter/blob/master/README.md)
The model is a symmetric search, and the vector space consists of title & abstract.

- Feature vector extraction
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/Universal-Sentence-Encoder.png)

- Feature vector space (consisting of title & abstract)
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/semantic_search.jpeg)


### SDK Features:

- Feature vector extraction of paper [title, abstract]
- Similarity calculation

### Run Example - SemanticSearchPublicationsExample

After successful execution, the command line should display the following information:
```text
...
# Test statement:
[INFO ] - paper1 [title, abstract]: [BERT, We introduce a new language representation model called BERT]
[INFO ] - paper2 [title, abstract]: [Attention is all you need, The dominant sequence transduction models are based on complex recurrent or convolutional neural networks]

# Vector dimensions:
[INFO ] - Vector dimension: 768

# Generate vectors:
[INFO ] - paper1[title, text] embeddings: [-0.83961445, 1.1465806, ..., 0.5574437, 0.4750324]
[INFO ] - paper2[title, text] embeddings: [-0.23870255, 1.2555068, ..., 0.052179076, 0.47623542]

# Calculate similarity:
[INFO ] - Similarity: 0.82421297

```

### Open source algorithm
#### 1. Open source algorithms used by the SDK
- [sentence-transformers](https://github.com/UKPLab/sentence-transformers)
- [Pre-trained models](https://www.sbert.net/docs/pretrained_models.html)
- [Installation](https://www.sbert.net/docs/installation.html)


#### 2. How to export the model?
- [how_to_convert_your_model_to_torchscript](http://docs.djl.ai/docs/pytorch/how_to_convert_your_model_to_torchscript.html)

- Exporting CPU models (PyTorch models are special, and CPU and GPU models are not interchangeable. Therefore, CPU and GPU models need to be exported separately)
- device='cpu'
- device='gpu'
- export_model.py
```text
from sentence_transformers import SentenceTransformer
import torch

model = SentenceTransformer('allenai-specter', device='cpu')
model.eval()
batch_size=1
max_seq_length=256
device = torch.device("cpu")
model.to(device)
input_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_type_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_mask = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_features = {'input_ids': input_ids, 'token_type_ids': input_type_ids, 'attention_mask': input_mask}

traced_model = torch.jit.trace(model, example_inputs=input_features,strict=False)
traced_model.save("models/allenai-specter/allenai-specter.pt")
```