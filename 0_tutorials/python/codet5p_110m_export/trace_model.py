import torch
from typing import Tuple
# Load model directly
from transformers import AutoTokenizer, AutoModel
import torch.nn.functional as F


model_name = 'D:\\ai_projects\\products\\python\\codet5p_110m_export\\model'
device = "cpu"  # for GPU usage or "cpu" for CPU usage
class Tracable(torch.nn.Module):
    def __init__(self, config: dict):
        super().__init__()
        # load model and processor
        self.model = AutoModel.from_pretrained(model_name, trust_remote_code=True).to(device)

    def encoder(self, input_ids) -> Tuple:
        embedding = self.model(input_ids=input_ids)

        return embedding


# %% create class
config = {}
tracable = Tracable(config)

# %% input
tokenizer = AutoTokenizer.from_pretrained(model_name, trust_remote_code=True)
sample_text = "def print_hello_world():\tprint('Hello World!')"
input_ids = tokenizer.encode(sample_text, return_tensors="pt").to(device)


embeddings = tracable.encoder(input_ids=input_ids)
print("embeddings:")
print(embeddings)

# %% trace
tracable.eval()


converted = torch.jit.trace_module(tracable, {'encoder': [input_ids]})

torch.jit.save(converted, "./codet5p-110m.pt")
