import torch
from typing import Tuple
# Load model directly
from transformers import AutoTokenizer, AutoModel
model_name = 'shibing624/text2vec-base-multilingual'

class Tracable(torch.nn.Module):
    def __init__(self, config: dict):
        super().__init__()
        # load model and processor
        self.model = AutoModel.from_pretrained(model_name, return_dict=False, torchscript=True)

    def encoder(self, input_ids, token_type_ids, attention_mask) -> Tuple:
        config = {'output_attentions': False}

        model_output = self.model(input_ids=input_ids,
                         token_type_ids=token_type_ids,
                         attention_mask=attention_mask,
                         **config)

        sentence_embeddings = self.mean_pooling(model_output, attention_mask)
        return  sentence_embeddings

    def mean_pooling(self, model_output, attention_mask):
        token_embeddings = model_output[0]  # First element of model_output contains all token embeddings
        input_mask_expanded = attention_mask.unsqueeze(-1).expand(token_embeddings.size()).float()
        return torch.sum(token_embeddings * input_mask_expanded, 1) / torch.clamp(input_mask_expanded.sum(1), min=1e-9)

# %% create class
config = {}
tracable = Tracable(config)

# %% input
tokenizer = AutoTokenizer.from_pretrained(model_name)
sample_text = "如何更换花呗绑定银行卡"

text_inputs = tokenizer([sample_text], return_tensors="pt")
input_ids = text_inputs.input_ids
token_type_ids = text_inputs.token_type_ids
attention_mask = text_inputs.attention_mask


sentence_embeddings = tracable.encoder(input_ids=input_ids, token_type_ids=token_type_ids, attention_mask=attention_mask)
print("Sentence embeddings:")
print(sentence_embeddings)

# %% trace
tracable.eval()




converted = torch.jit.trace_module(tracable, {'encoder': [input_ids, token_type_ids, attention_mask]})

torch.jit.save(converted, "./traced_embedding.pt")
