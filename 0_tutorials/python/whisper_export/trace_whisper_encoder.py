import torch
from transformers import WhisperProcessor, WhisperForConditionalGeneration
import whisper
from typing import Tuple

model_name = 'openai/whisper-tiny'

class Tracable(torch.nn.Module):
    def __init__(self, config: dict):
        super().__init__()
        # load model and processor
        self.model = WhisperForConditionalGeneration.from_pretrained(model_name, return_dict=False, torchscript=True)
        self.config = {'use_cache': config.get('use_cache', True),
                       'output_hidden_states': config.get('output_hidden_states', True)}

    def forward(self, input_features, decoder_input_ids) -> Tuple:
        return self.model(input_features=input_features,
                          decoder_input_ids=decoder_input_ids,
                          **self.config)  # return_tensor = True

# %% create class
config = {}
tracable = Tracable(config)

# %% input
# load audio and pad/trim it to fit 30 seconds
audio = whisper.load_audio("./tests/jfk.flac") # audio.mp3 jfk.flac test.wav

processor = WhisperProcessor.from_pretrained(model_name)
processor.tokenizer.save_pretrained(model_name)
forced_decoder_ids = processor.get_decoder_prompt_ids(task="transcribe")

input_features = processor(audio, sampling_rate=16000, return_tensors="pt").input_features
decoder_input_ids = torch.tensor([[50258]])

output = tracable(input_features=input_features, decoder_input_ids=decoder_input_ids)

# %% trace
tracable.eval()

traced_model = torch.jit.trace(tracable, (input_features, decoder_input_ids))
torch.jit.save(traced_model, "./traced_whisper_encoder_en.pt")

outputs = traced_model(input_features=input_features, decoder_input_ids=decoder_input_ids)

# save the past_key_values, encoder_outputs for the model tracing with it
torch.save(outputs[1], "traced_past_key_values_en.pt")
# encoder_outputs 正常应该是(outputs[3], outputs[4])
# outputs[4] 是所有的隐藏层张量，但是只需要最后一层，考虑到encoder_outputs TupleOfTuple 结构，所以用 outputs[3] 替换。
torch.save((outputs[3], outputs[3]), "traced_encoder_outputs_en.pt")
