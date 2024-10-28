import torch
import torch.nn as nn
from sentence_transformers import SentenceTransformer
from torch import Tensor

device = "cuda" if torch.cuda.is_available() else "cpu"
model = SentenceTransformer('moka-ai/m3e-base')
sentences = ['今天天气不错']
tokenized = model.tokenize(sentences)
input_ids = tokenized['input_ids']
token_type_ids = tokenized['token_type_ids']
attention_mask = tokenized['attention_mask']

for key in tokenized:
    if isinstance(tokenized[key], Tensor):
        tokenized[key] = tokenized[key].to(device)

class Wrapper(nn.Module):
    def __init__(self):
        super(Wrapper, self).__init__()
        self.model = SentenceTransformer('moka-ai/m3e-base')
        self.model.eval()

    def forward(self, input_ids, token_type_ids, attention_mask):
        input_features = {'input_ids': input_ids, 'token_type_ids': token_type_ids, 'attention_mask': attention_mask}
        out_features = self.model.forward(input_features)
        embeddings = out_features['sentence_embedding']
        return embeddings

# %% trace
traceModel = Wrapper()
traceModel.eval()
text_features = traceModel(input_ids, token_type_ids, attention_mask)
print(text_features)

converted = torch.jit.trace(traceModel, (input_ids, token_type_ids, attention_mask))
torch.jit.save(converted, "./traced_m3e_base_model.pt")

