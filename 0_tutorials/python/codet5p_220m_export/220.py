from transformers import AutoModel, AutoTokenizer
import torch

checkpoint = "Salesforce/codet5p-220m-bimodal"
device = "cpu"  # for GPU usage or "cpu" for CPU usage

tokenizer = AutoTokenizer.from_pretrained(checkpoint, trust_remote_code=True)
model = AutoModel.from_pretrained(checkpoint, trust_remote_code=True).to(device)

inputs = tokenizer.encode("def print_hello_world():\tprint('Hello World!')", return_tensors="pt").to(device)
embedding = model(inputs)[0]
print(f'Dimension of the embedding: {embedding.size()[0]}, with norm={embedding.norm().item()}')
# Dimension of the embedding: 256, with norm=1.0
print(embedding)

model.eval()

traced_model = torch.jit.trace(model, (inputs), strict=False)
# converted = torch.jit.trace_module(model, {'encoder': [input_ids, token_type_ids, attention_mask]})

torch.jit.save(traced_model, "./codet5p-220m.pt")
