import torch
import sentencepiece
from typing import Tuple,Dict,Any
# Load model directly
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM

model_name = 'Helsinki-NLP/opus-mt-en-zh'

class Tracable(torch.nn.Module):
    def __init__(self, config: dict):
        super().__init__()
        # load model and processor
        self.model = AutoModelForSeq2SeqLM.from_pretrained(model_name, return_dict=False, torchscript=True)

    def encoder(self, input_ids, attention_mask) -> Tuple:
        config = {'output_attentions': False, 'output_hidden_states':  False}

        return self.model.base_model.encoder(input_ids=input_ids,
                          attention_mask=attention_mask,
                          **config)  # return_tensor = True


    def decoder(self, decoder_input_ids, encoder_hidden_states, attention_mask):
        config = {'use_cache': True, 'output_attentions': False, 'output_hidden_states':  False}

        encoder_outputs = (encoder_hidden_states,)
        return self.model(decoder_input_ids=decoder_input_ids,
                          encoder_outputs=encoder_outputs,
                          attention_mask=attention_mask,
                          **config)  # return_tensor = True


    def decoder2(self, decoder_input_ids, encoder_hidden_states, attention_mask, past_key_values):
        config = {'use_cache': True, 'output_attentions': False, 'output_hidden_states':  False}

        encoder_outputs = (encoder_hidden_states,)
        return self.model(decoder_input_ids=decoder_input_ids,
                          encoder_outputs=encoder_outputs,
                          attention_mask=attention_mask,
                          past_key_values=past_key_values,
                          **config)  # return_tensor = True

# %% create class
config = {}
tracable = Tracable(config)

# %% input
tokenizer = AutoTokenizer.from_pretrained(model_name)
sample_text = "My name is Wolfgang and I live in Berlin"
text_inputs = tokenizer([sample_text], return_tensors="pt")
input_ids = text_inputs.input_ids
attention_mask = text_inputs.attention_mask


encoder_hidden_states = tracable.encoder(input_ids=input_ids, attention_mask=attention_mask)

# %% trace
tracable.eval()

# decoder_input_ids = torch.tensor([[65000],
#                                     [65000],
#                                     [65000],
#                                     [65000]])

decoder_input_ids = torch.tensor([[65000]])

# interleave with `num_beams`
num_beams = 4
expanded_return_idx = (
    torch.arange(decoder_input_ids.shape[0]).view(-1, 1).repeat(1, num_beams).view(-1).to(decoder_input_ids.device)
)
decoder_input_ids = decoder_input_ids.index_select(0, expanded_return_idx)
new_attention_mask = attention_mask.index_select(0, expanded_return_idx)
encoder_hidden_states = encoder_hidden_states[0].index_select(
    0, expanded_return_idx.to(decoder_input_ids.device)
)

output = tracable.decoder(decoder_input_ids=decoder_input_ids, encoder_hidden_states=encoder_hidden_states, attention_mask=new_attention_mask)

# past_key_values, encoder_outputs for the model tracing with it
past_key_values = output[1]

output = tracable.decoder2(decoder_input_ids=decoder_input_ids, encoder_hidden_states=encoder_hidden_states, attention_mask=new_attention_mask,past_key_values=past_key_values)

print("translation: ")

converted = torch.jit.trace_module(tracable, {'encoder': [input_ids, attention_mask],
                                              'decoder': [decoder_input_ids, encoder_hidden_states, new_attention_mask],
                                              'decoder2': [decoder_input_ids, encoder_hidden_states,new_attention_mask, past_key_values]})

torch.jit.save(converted, "./traced_translation.pt")
