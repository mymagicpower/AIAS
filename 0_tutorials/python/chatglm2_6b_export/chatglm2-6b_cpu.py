
from transformers import AutoTokenizer, AutoModel

model_name = 'E:\\dev\\chatglm2_6b'

tokenizer = AutoTokenizer.from_pretrained(model_name, trust_remote_code=True)
tokenizer.save_pretrained("chatglm2_6b")
model = AutoModel.from_pretrained(model_name, trust_remote_code=True).float().cpu()
model = model.eval()

response, history = model.chat(tokenizer, "你好", history=[])

print(response)

# response, history = model.chat(tokenizer, "晚上睡不着应该怎么办", history=history)
#
# print(response)
