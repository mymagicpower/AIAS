from transformers import AutoTokenizer, AutoModel
import torch
import torch.nn.functional as F
import ssl
ssl._create_default_https_context = ssl._create_unverified_context

# https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2
#Mean Pooling - Take attention mask into account for correct averaging
def mean_pooling(model_output, attention_mask):
    token_embeddings = model_output[0] #First element of model_output contains all token embeddings
    input_mask_expanded = attention_mask.unsqueeze(-1).expand(token_embeddings.size()).float()
    return torch.sum(token_embeddings * input_mask_expanded, 1) / torch.clamp(input_mask_expanded.sum(1), min=1e-9)


# sentence-transformers/all-MiniLM-L12-v2
# sentence-transformers/all-MiniLM-L6-v2
model_name = 'sentence-transformers/all-MiniLM-L12-v2'

tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModel.from_pretrained(model_name, return_dict=False, torchscript=True)

# Sentences we want sentence embeddings for
sentences = ['This is an example sentence']

# Tokenize sentences
encoded_input = tokenizer(sentences, padding=True, truncation=True, return_tensors='pt')

# Compute token embeddings
with torch.no_grad():
    model_output = model(**encoded_input)

# Perform pooling
sentence_embeddings = mean_pooling(model_output, encoded_input['attention_mask'])

# Normalize embeddings
sentence_embeddings = F.normalize(sentence_embeddings, p=2, dim=1)

print("Sentence embeddings:")
print(sentence_embeddings)