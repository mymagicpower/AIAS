import torch
from typing import Tuple
# Load model directly
from transformers import AutoTokenizer, AutoModel
import torch.nn.functional as F

# sentence-transformers/all-MiniLM-L12-v2
# sentence-transformers/all-MiniLM-L6-v2
# sentence-transformers/all-mpnet-base-v2
model_name = 'sentence-transformers/all-mpnet-base-v2'

class Tracable(torch.nn.Module):
    def __init__(self, config: dict):
        super().__init__()
        # load model and processor
        self.model = AutoModel.from_pretrained(model_name, return_dict=False, torchscript=True)

    def encoder(self, input_ids, attention_mask) -> Tuple:
        config = {'output_attentions': False}

        model_output = self.model(input_ids=input_ids,
                         attention_mask=attention_mask,
                         **config)

        sentence_embeddings = self.mean_pooling(model_output, attention_mask)
        # Normalize embeddings
        sentence_embeddings = F.normalize(sentence_embeddings, p=2, dim=1)

        return  sentence_embeddings

    def mean_pooling(self, model_output, attention_mask):
        token_embeddings = model_output[0] #First element of model_output contains all token embeddings
        input_mask_expanded = attention_mask.unsqueeze(-1).expand(token_embeddings.size()).float()
        return torch.sum(token_embeddings * input_mask_expanded, 1) / torch.clamp(input_mask_expanded.sum(1), min=1e-9)

# %% create class
config = {}
tracable = Tracable(config)

# %% input
tokenizer = AutoTokenizer.from_pretrained(model_name)
sample_text = "This is an example sentence"

text_inputs = tokenizer([sample_text], return_tensors="pt")
input_ids = text_inputs.input_ids
attention_mask = text_inputs.attention_mask


sentence_embeddings = tracable.encoder(input_ids=input_ids, attention_mask=attention_mask)
print("Sentence embeddings:")
print(sentence_embeddings)

# %% trace
tracable.eval()




converted = torch.jit.trace_module(tracable, {'encoder': [input_ids, attention_mask]})

torch.jit.save(converted, "./all-mpnet-base-v2.pt")
