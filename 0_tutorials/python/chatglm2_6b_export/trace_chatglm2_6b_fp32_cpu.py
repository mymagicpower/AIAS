from transformers import AutoTokenizer, AutoModel
import torch
from typing import Tuple

model_name = 'E:\\dev\\chatglm2_6b'

class Wrapper(torch.nn.Module):
    def __init__(self, config: dict):
        super().__init__()
        # load model
        self.model = AutoModel.from_pretrained(model_name, trust_remote_code=True).float().cpu()

    def encoder(self, input_ids, position_ids, attention_mask) -> Tuple:
        config = {'return_dict': False,
                  'output_attentions': False,
                  'output_hidden_states': False}

        model_inputs = {'input_ids': input_ids, 'attention_mask': attention_mask, 'position_ids': position_ids,
                        'past_key_values': None, 'return_last_logit': True}
        outputs = self.model(**model_inputs, **config)
        return outputs

    def decoder(self, input_ids, position_ids, attention_mask, past_key_values):
        config = {
            "return_dict": False,
            "output_attentions": False,
            "output_hidden_states": False,
            "use_cache": True,
            "return_last_logit": True}

        return self.model(
            input_ids=input_ids,
            position_ids=position_ids,
            attention_mask=attention_mask,
            past_key_values=past_key_values,
            **config
        )

# %% create class
config = {}
tracable = Wrapper(config)

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

outputs = tracable.encoder(input_ids=input_ids, position_ids=position_ids, attention_mask=attention_mask)

# %% trace
tracable.eval()

past_key_values = outputs[1]

input_ids = torch.tensor([[36474]])
new_position_id = position_ids[..., -1:].clone()
new_position_id += 1
position_ids = new_position_id
# update attention mask
attention_mask = torch.cat(
    [attention_mask, attention_mask.new_ones((attention_mask.shape[0], 1))], dim=-1
)
outputs = tracable.decoder(input_ids=input_ids, position_ids=position_ids, attention_mask=attention_mask, past_key_values=past_key_values)

traced_model = torch.jit.trace_module(tracable, {'encoder': [input_ids, position_ids, attention_mask],
                                              'decoder': [input_ids, position_ids, attention_mask, past_key_values]})


torch.jit.save(traced_model, "./traced_chatglm2_6b_fp32_cpu.pt")
