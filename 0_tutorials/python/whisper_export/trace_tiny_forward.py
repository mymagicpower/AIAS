from transformers import WhisperProcessor, WhisperForConditionalGeneration
import torch
import whisper

# load model and processor
processor = WhisperProcessor.from_pretrained("openai/whisper-tiny", language = "chinese") # , language = "chinese"
processor.tokenizer.save_pretrained("openai/whisper-tiny")
model = WhisperForConditionalGeneration.from_pretrained("openai/whisper-tiny", return_dict=False, torchscript=True)
forced_decoder_ids = processor.get_decoder_prompt_ids(language="chinese", task="transcribe")

# load audio and pad/trim it to fit 30 seconds
audio = whisper.load_audio("./tests/test.wav") # audio.mp3 jfk.flac test.wav

input_features = processor(audio, sampling_rate=16000, return_tensors="pt").input_features
# decoder_input_ids = torch.tensor([[1, 1]]) * model.config.decoder_start_token_id
decoder_input_ids = torch.tensor([[model.config.decoder_start_token_id]])
outputs = model(input_features, decoder_input_ids=decoder_input_ids, output_hidden_states=True, use_cache=True) # , forced_decoder_ids=forced_decoder_ids
past_key_values = outputs[1]
encoder_outputs = (outputs[3], outputs[4])
logits = outputs[0]
predicted_index = torch.argmax(logits[0, -1, :]).item()
predicted_text = processor.tokenizer.decode([predicted_index])
# results
print('predicted text:', predicted_text)

model.eval()
traced_model = torch.jit.trace(model, {'input_features': input_features, 'decoder_input_ids': decoder_input_ids, 'output_hidden_states':True, 'use_cache': True})
torch.jit.save(traced_model, "./traced_whisper_encoder_zh.pt")



while True:
    decoder_input_ids = torch.tensor([[predicted_index]])
    outputs = model(decoder_input_ids=decoder_input_ids, encoder_outputs=encoder_outputs, past_key_values=past_key_values, use_cache=True) # output_hidden_states=True , forced_decoder_ids=forced_decoder_ids
    logits = outputs[0]
    past_key_values = outputs[1]
    predicted_index = torch.argmax(logits[0, -1, :]).item()
    predicted_text = processor.tokenizer.decode([predicted_index])
    # results
    print('predicted text:', predicted_text)



# Start tracing
traced_model = torch.jit.trace(model, input_features)
generated_ids = traced_model(input_features) #  , forced_decoder_ids=forced_decoder_ids
transcription = processor.batch_decode(generated_ids, skip_special_tokens=True)[0]
print("Traced: " + transcription)

torch.jit.save(traced_model, "whisper_tiny.pt")

# is_multilingual = False
# language = 'chinese'
# task = 'transcribe'
# kwargs = {'forced_decoder_ids': [(1, 50260), (2, 50359), (3, 50363)]}
