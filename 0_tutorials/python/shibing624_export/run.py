
from transformers import AutoTokenizer, AutoModel
import torch

# Mean Pooling - Take attention mask into account for correct averaging
def mean_pooling(model_output, attention_mask):
    token_embeddings = model_output[0]  # First element of model_output contains all token embeddings
    input_mask_expanded = attention_mask.unsqueeze(-1).expand(token_embeddings.size()).float()
    return torch.sum(token_embeddings * input_mask_expanded, 1) / torch.clamp(input_mask_expanded.sum(1), min=1e-9)

# Load model from HuggingFace Hub
# https://huggingface.co/shibing624/text2vec-base-chinese
# shibing624/text2vec-base-chinese
# https://huggingface.co/shibing624/text2vec-base-chinese-sentence
# shibing624/text2vec-base-chinese-sentence
# https://huggingface.co/shibing624/text2vec-base-chinese-paraphrase
# shibing624/text2vec-base-chinese-paraphrase
# https://huggingface.co/shibing624/text2vec-base-multilingual
# shibing624/text2vec-base-multilingual

modelName = 'shibing624/text2vec-base-chinese'
tokenizer = AutoTokenizer.from_pretrained(modelName)
model = AutoModel.from_pretrained(modelName, return_dict=False, torchscript=True)

sentences = ['如何更换花呗绑定银行卡']
# Tokenize sentences
encoded_input = tokenizer(sentences, padding=True, truncation=True, return_tensors='pt')
tokens = tokenizer.tokenize('如何更换花呗绑定银行卡')

# Compute token embeddings
with torch.no_grad():
    model_output = model(**encoded_input)
# Perform pooling. In this case, mean pooling.
sentence_embeddings = mean_pooling(model_output, encoded_input['attention_mask'])
print("Sentence embeddings:")
print(sentence_embeddings)