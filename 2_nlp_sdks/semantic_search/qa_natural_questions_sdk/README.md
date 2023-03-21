
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/nq-distilbert-base-v1.zip

### Natural Question Answering SDK [English]

The model is trained on Google's Natural Questions dataset (100k Google search query data and relevant passages sourced from Wikipedia). Google's Natural Questions (NQ) is a large-scale corpus for training and evaluating open-domain question answering systems. However, until recently, there were no large-scale publicly available datasets of naturally occurring questions (the kinds of questions people ask to seek information) and answers for training and evaluating question answering models. NQ is a large-scale corpus for training and evaluating open-domain question answering systems, which replicates the end-to-end process of how people find answers to questions.

![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/qa_natural_questions.jpeg)


### SDK Features:

- Query / passage [title, text] vector extraction
- Similarity calculation

### Running Example - QANaturalQuestionsExample

After running successfully, you should see the following information on the command line:
```text
...
# Test sentence:
[INFO] - query: How many people live in London?
# Passage is a pair of data composed of <title, text>.
[INFO] - passage [title, text]: [London, London has 9,787,426 inhabitants at the 2011 census.]

# Vector dimension:
[INFO] - Vector dimension: 768

# Generating vectors:
[INFO] - query embeddings: [0.04629234, -0.33281654, ..., -0.22015738, -0.06693681]
[INFO] - passage[title, text] embeddings: [-0.015913313, -0.10886402, ..., 0.48449898, -0.32266212]

# Calculating similarity:
[INFO] - Similarity: 0.650292

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
- export_model_natural_questions.py
```text
from sentence_transformers import SentenceTransformer
import torch

# model = SentenceTransformer('stsb-distilbert-base', device='cpu')
model = SentenceTransformer('nq-distilbert-base-v1', device='cpu')
model.eval()
batch_size=1
max_seq_length=128
device = torch.device("cpu")
model.to(device)
input_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_type_ids = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
input_mask = torch.zeros(batch_size, max_seq_length, dtype=torch.long).to(device)
# input_features = (input_ids, input_type_ids, input_mask)
input_features = {'input_ids': input_ids,'attention_mask': input_mask}

# traced_model = torch.jit.trace(model, example_inputs=input_features)
traced_model = torch.jit.trace(model, example_inputs=input_features,strict=False)
traced_model.save("models/nq-distilbert-base-v1/nq-distilbert-base-v1.pt")
```
