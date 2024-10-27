
from transformers import AutoModelForSeq2SeqLM, AutoTokenizer

tokenizer = AutoTokenizer.from_pretrained("facebook/nllb-200-distilled-600M",  src_lang="zho_Hans")
model = AutoModelForSeq2SeqLM.from_pretrained("facebook/nllb-200-distilled-600M", )

text = "智利北部的丘基卡马塔矿是世界上最大的露天矿之一，长约4公里，宽3公里，深1公里。"
inputs = tokenizer(text, return_tensors="pt")

generated_ids = model.generate(**inputs, forced_bos_token_id=tokenizer.lang_code_to_id["eng_Latn"], max_length=30)

translation = tokenizer.batch_decode(generated_ids, skip_special_tokens=True)[0]
print("translation: " + translation)
