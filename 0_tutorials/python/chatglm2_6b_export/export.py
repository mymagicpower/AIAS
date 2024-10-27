import torch
from transformers import AutoTokenizer, AutoModel
device = 'cuda' if torch.cuda.is_available() else 'cpu'

class Wrapper(torch.nn.Module):
    """
    Wrapper for the model to be traced
    """
    def __init__(self):
        super().__init__()
        self.model = AutoModel.from_pretrained("THUDM/chatglm2-6b", trust_remote_code=True, torchscript=True).half().to(device)
        self.tokenizer = AutoTokenizer.from_pretrained("THUDM/chatglm2-6b", trust_remote_code=True, torchscript=True)

    def forward(self, input_ids):
        self.model.eval()
        input_ids = input_ids.to(device)
        print('input_ids type:', input_ids.dtype)
        outputs = self.model.generate(input_ids=input_ids, max_length=2048, num_beams=1, do_sample=True, top_p=0.8,temperature=0.8)
        return outputs[0, len(input_ids[0]) - 2:]

tokenizer = AutoTokenizer.from_pretrained("THUDM/chatglm2-6b", trust_remote_code=True)
query = "hello"
input = tokenizer([query], return_tensors="pt", padding=True)
model = Wrapper()
traced_model = torch.jit.trace(model, (input.input_ids,))
#traced_model = torch.jit.script(model) # 使用torch.jit.script()函数代替torch.jit.trace()函数
traced_model.save("chatglm2-6b.pt")