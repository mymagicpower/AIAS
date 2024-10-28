import torch
from transformers import WhisperProcessor, WhisperForConditionalGeneration
import whisper
from typing import Tuple

model_name = 'openai/whisper-tiny'

class Tracable(torch.nn.Module):
    def __init__(self, config: dict):
        super().__init__()
        # load model
        self.model = WhisperForConditionalGeneration.from_pretrained(model_name, return_dict=False, torchscript=True)
        self.config = {'use_cache': config.get('use_cache', True)}

    def forward(self, decoder_input_ids, encoder_outputs, past_key_values) -> Tuple:
        return self.model(decoder_input_ids=decoder_input_ids,
                          encoder_outputs=encoder_outputs,
                          past_key_values=past_key_values,
                          **self.config)

# %% create class
config = {}
tracable = Tracable(config)

# %% input
processor = WhisperProcessor.from_pretrained(model_name)
processor.tokenizer.save_pretrained(model_name)

past_key_values = torch.load("traced_past_key_values_en.pt")
encoder_outputs = torch.load("traced_encoder_outputs_en.pt")

decoder_input_ids = torch.tensor([[50259]])

output = tracable(decoder_input_ids=decoder_input_ids, encoder_outputs=encoder_outputs, past_key_values=past_key_values)

# %% trace
tracable.eval()

traced_model = torch.jit.trace(tracable, (decoder_input_ids, encoder_outputs, past_key_values))
torch.jit.save(traced_model, "./traced_whisper_decoder_en.pt")

outputs = traced_model(decoder_input_ids=decoder_input_ids, encoder_outputs=encoder_outputs, past_key_values=past_key_values)
logits = outputs[0]
predicted_index = torch.argmax(logits[0, -1, :]).item()
predicted_text = processor.tokenizer.decode([predicted_index])
# results
print('predicted text:', predicted_text)