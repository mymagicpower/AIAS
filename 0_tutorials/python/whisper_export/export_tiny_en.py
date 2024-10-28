from transformers import WhisperProcessor, WhisperForConditionalGeneration
import torch
import whisper

# load model and processor
processor = WhisperProcessor.from_pretrained("openai/whisper-tiny") # , language = "chinese"
processor.tokenizer.save_pretrained("openai/whisper-tiny")
model = WhisperForConditionalGeneration.from_pretrained("openai/whisper-tiny", return_dict=False, torchscript=True)
forced_decoder_ids = processor.get_decoder_prompt_ids(task="transcribe")

# load audio and pad/trim it to fit 30 seconds
audio = whisper.load_audio("./tests/jfk.flac") # audio.mp3 jfk.flac test.wav

input_features = processor(audio, sampling_rate=16000, return_tensors="pt").input_features
generated_ids = model.generate(inputs=input_features) # , forced_decoder_ids=forced_decoder_ids
transcription = processor.batch_decode(generated_ids, skip_special_tokens=True)[0]
print("Original: " + transcription)

model.eval()

# Start tracing
traced_model = torch.jit.trace_module(model, {"generate": [input_features]})
generated_ids = traced_model.generate(input_features) #  , forced_decoder_ids=forced_decoder_ids
transcription = processor.batch_decode(generated_ids, skip_special_tokens=True)[0]
print("Traced: " + transcription)

torch.jit.save(traced_model, "whisper_tiny.pt")

# is_multilingual = False
# language = 'chinese'
# task = 'transcribe'
# kwargs = {'forced_decoder_ids': [(1, 50260), (2, 50359), (3, 50363)]}
