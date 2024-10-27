import torch
from transformers import AutoTokenizer, AutoModel
device = 'cuda' if torch.cuda.is_available() else 'cpu'

tokenizer = AutoTokenizer.from_pretrained("THUDM/chatglm2-6b", trust_remote_code=True)
query = "hello"
inputs = tokenizer([query], return_tensors="pt", padding=True)
traced_model = torch.jit.load("traced_chatglm2_6b_fp16_gpu.pt")

inputs = inputs.to("cuda")
input_ids = inputs.input_ids
position_ids = inputs.position_ids
attention_mask = inputs.attention_mask

outputs = traced_model.encoder(input_ids=input_ids, position_ids=position_ids, attention_mask=attention_mask)

print(outputs)