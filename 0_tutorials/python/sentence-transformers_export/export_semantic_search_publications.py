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