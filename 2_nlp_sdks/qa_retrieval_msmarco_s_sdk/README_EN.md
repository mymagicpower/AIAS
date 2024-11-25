
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/msmarco-distilbert-base-v4.zip

### Semantic Search SDK [English]

Semantic search retrieves the text from the corpus that best matches the query by calculating the similarity of sentence vectors.
The model is trained on the MS MARCO dataset and can be used for semantic search, such as: keywords / search phrases / questions, the model can find passages related to the query.

MS MARCO is a set of question and answer data sets released by Microsoft. Researchers in the field of artificial intelligence can use it to build question and answer systems that are comparable to humans.
The full name of this data set is: Microsoft MAchine Reading COmprehension, which means "Microsoft machine reading comprehension".
MS MARCO is currently the most useful data set of its kind because it is based on anonymized real-world data (Bing search engine search query data).

- Semantic search
![img](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/semantic_search.jpeg)


### SDK function:

- Short text vector extraction (average 60 words, max_seq_length 128)
- Similarity calculation

### Running example-QuestionAnswerRetrievalExample

After successful operation, the command line should see the following information:
```text
...
#Test QA statement:
[INFO]-Query sentence: How big is London
[INFO]-Passage sentence: London has 9,787,426 inhabitants at the 2011 census

#Vector dimension:
[INFO]-Vector dimension: 768

#Generate vectors:
[INFO]-Query sentence embeddings: [-0.07430013, -0.5672244, ..., -0.44672608, -0.029431352]
[INFO]-Passage embeddings: [-0.097875535, -0.3074494, ..., 0.07692761, 0.17015335]

#Calculate similarity:
[INFO]-Similarity: 0.5283209

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

# model = SentenceTransformer('stsb-distilbert-base', device='cpu')
model = SentenceTransformer('msmarco-distilbert-base-v4', device='cpu')
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
traced_model.save("models/msmarco-distilbert-base-v4.pt")
```
