from transformers import AutoTokenizer, AutoModel
import torch
from typing import Tuple

model_name = 'E:\\dev\\chatglm2_6b'

class Tracable(torch.nn.Module):
    def __init__(self, config: dict):
        super().__init__()
        # load model
        self.model = AutoModel.from_pretrained(model_name, trust_remote_code=True).float().cpu()

        self.config = {'return_dict': config.get('return_dict', False),
                       'output_attentions': config.get('output_attentions', False),
                       'output_hidden_states': config.get('output_hidden_states', False)}

    def forward(self, input_ids, attention_mask, position_ids) -> Tuple:
        model_inputs = {'input_ids': input_ids, 'attention_mask': attention_mask, 'position_ids': position_ids,
                        'past_key_values': None, 'return_last_logit': True}
        outputs = self.model(**model_inputs, **self.config)
        return outputs

# %% create class
config = {}
tracable = Tracable(config)

# %% input
tokenizer = AutoTokenizer.from_pretrained(model_name, trust_remote_code=True)

history = []
query = "你好"

if not history:
    prompt = query
else:
    prompt = ""
    for i, (old_query, response) in enumerate(history):
        prompt += "[Round {}]\n问：{}\n答：{}\n".format(i, old_query, response)
    prompt += "[Round {}]\n问：{}\n答：".format(len(history), query)

prompt = tokenizer.build_prompt(query, history=history)
inputs = tokenizer([prompt], return_tensors="pt")
inputs = inputs.to("cpu")

input_ids = inputs.input_ids
attention_mask = inputs.attention_mask
position_ids = inputs.position_ids

# auto-regressive generation

outputs = tracable(input_ids=input_ids, attention_mask=attention_mask,
                   position_ids=position_ids)

# %% trace
tracable.eval()

traced_model = torch.jit.trace(tracable, (input_ids, attention_mask, position_ids), strict=False)

torch.jit.save(traced_model, "./traced_chatglm2_6b_fp32_cpu_nokv.pt")

# save the past_key_values for the model tracing with it
torch.save(outputs[1], "./past_key_values.pt")
