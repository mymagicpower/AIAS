from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch
from export import cross_encoder_wrapper

model = cross_encoder_wrapper.load_model('cross-encoder/ms-marco-TinyBERT-L-2-v2')
# model = AutoModelForSequenceClassification.from_pretrained('cross-encoder/ms-marco-TinyBERT-L-2-v2')
tokenizer = AutoTokenizer.from_pretrained('cross-encoder/ms-marco-TinyBERT-L-2-v2')

txt_tok = tokenizer(['How many people live in Berlin?'], ['Berlin is well known for its museums.'],  padding=True, truncation=True, return_tensors="pt")
print(type(txt_tok))
input_ids = txt_tok['input_ids']
print(type(input_ids))
print(input_ids.shape)

token_type_ids = txt_tok['token_type_ids']
input_mask = txt_tok['attention_mask']
input_features = {'input_ids': input_ids, 'token_type_ids': token_type_ids, 'attention_mask': input_mask}

model.eval()
#.logits
scores = model(input_features)
print(scores)

# strict=False
# Use torch.jit.trace to generate a torch.jit.ScriptModule via tracing.
traced_script_module = torch.jit.trace(model, input_features)

traced_script_module.save("models/ms-marco-TinyBERT-L-2-v2/ms-marco-TinyBERT-L-2-v2.pt")

