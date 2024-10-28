import torch
from transformers import WhisperProcessor, WhisperForConditionalGeneration
import whisper
from typing import Tuple

model_name = 'openai/whisper-base'

class Tracable(torch.nn.Module):
    def __init__(self, config: dict):
        super().__init__()
        # load model and processor
        self.model = WhisperForConditionalGeneration.from_pretrained(model_name, return_dict=False, torchscript=True)

    def forward(self, input_features, decoder_input_ids) -> Tuple:
        config = {'use_cache': True, 'output_hidden_states':  True}

        return self.model(input_features=input_features,
                          decoder_input_ids=decoder_input_ids,
                          **config)  # return_tensor = True

    def encoder(self, input_features, decoder_input_ids) -> Tuple:
        config = {'use_cache': True,'output_hidden_states':  True}

        return self.model(input_features=input_features,
                          decoder_input_ids=decoder_input_ids,
                          **config)  # return_tensor = True


    def decoder(self, decoder_input_ids, encoder_outputs, past_key_values) -> Tuple:
        config = {'use_cache': True}

        return self.model(decoder_input_ids=decoder_input_ids,
                          encoder_outputs=encoder_outputs,
                          past_key_values=past_key_values,
                          **config)  # return_tensor = True


# %% create class
config = {}
tracable = Tracable(config)

# %% input
# load audio and pad/trim it to fit 30 seconds
audio = whisper.load_audio("./tests/jfk.flac") # audio.mp3 jfk.flac test.wav

processor = WhisperProcessor.from_pretrained(model_name)  # , language = "chinese"
# processor.tokenizer.save_pretrained(model_name)

input_features = processor(audio, sampling_rate=16000, return_tensors="pt").input_features
decoder_input_ids = torch.tensor([[50258]])

output = tracable.encoder(input_features=input_features, decoder_input_ids=decoder_input_ids)

# %% trace
tracable.eval()

# past_key_values, encoder_outputs for the model tracing with it
past_key_values = output[1]
# encoder_outputs 正常应该是(outputs[3], outputs[4])
# outputs[4] 是所有的隐藏层张量，但是只需要最后一层，考虑到encoder_outputs TupleOfTuple 结构，所以用 outputs[3] 替换。
encoder_outputs = (output[3], output[3])

converted = torch.jit.trace_module(tracable, {'encoder': [input_features, decoder_input_ids],
                                              'decoder': [decoder_input_ids, encoder_outputs, past_key_values]})

torch.jit.save(converted, "./traced_whisper_base.pt")