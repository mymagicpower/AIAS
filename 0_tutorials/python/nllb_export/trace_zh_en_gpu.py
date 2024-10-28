import torch
import sentencepiece
from typing import Tuple,Dict,Any
# Load model directly
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM

model_name = 'facebook/nllb-200-distilled-600M'
class Tracable(torch.nn.Module):
    def __init__(self, config: dict):
        super().__init__()
        # load model and processor
        self.model = AutoModelForSeq2SeqLM.from_pretrained(model_name, return_dict=False, torchscript=True).half().cuda()

    def encoder(self, input_ids, attention_mask) -> Tuple:
        config = {'return_dict': False,
                  'output_attentions': False,
                  'output_hidden_states': False}

        return self.model.base_model.encoder(input_ids=input_ids,
                          attention_mask=attention_mask,
                          **config)


    def decoder(self, decoder_input_ids, encoder_hidden_states, attention_mask):
        config = {'use_cache': True,
                  'return_dict': False,
                  'output_attentions': False,
                  'output_hidden_states': False}

        encoder_outputs = (encoder_hidden_states,)
        return self.model(decoder_input_ids=decoder_input_ids,
                          encoder_outputs=encoder_outputs,
                          attention_mask=attention_mask,
                          **config)


    def decoder2(self, decoder_input_ids, encoder_hidden_states, attention_mask, past_key_values):
        config = {'use_cache': True,
                  'return_dict': False,
                  'output_attentions': False,
                  'output_hidden_states': False}

        encoder_outputs = (encoder_hidden_states,)
        return self.model(decoder_input_ids=decoder_input_ids,
                          encoder_outputs=encoder_outputs,
                          attention_mask=attention_mask,
                          past_key_values=past_key_values,
                          **config)

# %% create class
config = {}
tracable = Tracable(config)

# %% input
tokenizer = AutoTokenizer.from_pretrained(model_name,  src_lang="zho_Hans")
sample_text = "智利北部的丘基卡马塔矿是世界上最大的露天矿之一，长约4公里，宽3公里，深1公里。"

text_inputs = tokenizer([sample_text], return_tensors="pt")
inputs = text_inputs.to("cuda")

input_ids = text_inputs.input_ids
attention_mask = text_inputs.attention_mask

encoder_hidden_states = tracable.encoder(input_ids=input_ids, attention_mask=attention_mask)

# %% trace
tracable.eval()

decoder_input_ids = torch.tensor([[2]]).to("cuda")

output = tracable.decoder(decoder_input_ids=decoder_input_ids, encoder_hidden_states=encoder_hidden_states[0], attention_mask=attention_mask)

# past_key_values, encoder_outputs for the model tracing with it
past_key_values = output[1]

output = tracable.decoder2(decoder_input_ids=decoder_input_ids, encoder_hidden_states=encoder_hidden_states[0], attention_mask=attention_mask,past_key_values=past_key_values)

print("translation: ")

converted = torch.jit.trace_module(tracable, {'encoder': [input_ids, attention_mask],
                                              'decoder': [decoder_input_ids, encoder_hidden_states[0], attention_mask],
                                              'decoder2': [decoder_input_ids, encoder_hidden_states[0], attention_mask, past_key_values]})

torch.jit.save(converted, "./traced_translation_gpu.pt")
