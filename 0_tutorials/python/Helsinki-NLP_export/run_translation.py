import torch
import sentencepiece
# Load model directly
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM, MinLengthLogitsProcessor
import ssl
ssl._create_default_https_context = ssl._create_unverified_context

tokenizer = AutoTokenizer.from_pretrained("Helsinki-NLP/opus-mt-zh-en")
model = AutoModelForSeq2SeqLM.from_pretrained("Helsinki-NLP/opus-mt-zh-en", return_dict=False, torchscript=True)
sample_text = "智利北部的丘基卡马塔矿是世界上最大的露天矿之一，长约4公里，宽3公里，深1公里。"

text_inputs = tokenizer([sample_text], return_tensors="pt")
input_ids = text_inputs.input_ids
attention_mask = text_inputs.attention_mask

# generated_ids = model.generate(**batch)
generated_ids = model.generate(input_ids = input_ids, attention_mask = attention_mask)
translation = tokenizer.batch_decode(generated_ids, skip_special_tokens=True)[0]
print("translation: " + translation)
