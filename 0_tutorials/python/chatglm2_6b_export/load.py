
from transformers import AutoTokenizer, AutoModel
from transformers.generation.utils import (
    LogitsProcessorList,
    StoppingCriteriaList,
    GenerationConfig,
    ModelOutput,
)
from transformers.generation.logits_process import (
    LogitsProcessor,
    LogitsProcessorList,
    TemperatureLogitsWarper,
    TopKLogitsWarper,
    TopPLogitsWarper,
)
import torch
from torch import nn
import re
from typing import Optional, Tuple, Union, List, Callable, Dict, Any
class InvalidScoreLogitsProcessor(LogitsProcessor):
    def __call__(
        self, input_ids: torch.LongTensor, scores: torch.FloatTensor
    ) -> torch.FloatTensor:
        if torch.isnan(scores).any() or torch.isinf(scores).any():
            scores.zero_()
            scores[..., 5] = 5e4
        return scores

glmModel = torch.jit.load("E:\\dev\\chatglm2-6b-export\\traced_chatglm2_6b_fp32_cpu.pt")


# %% create class
config = {}
model_name = "E:\\dev\\chatglm2_6b"

# %% input
tokenizer = AutoTokenizer.from_pretrained(model_name, trust_remote_code=True, torchscript=True)
# tokenizer.save_pretrained(model_name)

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
all_input_ids = inputs.input_ids

# update input_ids
input_ids = torch.tensor([[36474]])
all_input_ids = torch.cat([all_input_ids, input_ids], dim=-1)

attention_mask = inputs.attention_mask
# update attention mask
attention_mask = torch.cat(
    [attention_mask, attention_mask.new_ones((attention_mask.shape[0], 1))], dim=-1
)

all_position_ids = inputs.position_ids
# update position_ids
new_position_id = all_position_ids[..., -1:].clone()
new_position_id += 1
all_position_ids = torch.cat([all_position_ids, new_position_id], dim=-1)
position_ids = new_position_id

eos_token_id = 2
eos_token_id = [eos_token_id]
pad_token_id = 0
filter_value = -float("Inf")
top_k = 50
temperature = 0.8
top_p = 0.8
max_length = 8192

# keep track of which sequences are already finished
unfinished_sequences = input_ids.new(input_ids.shape[0]).fill_(1)
past_key_values = torch.load("./past_key_values.pt")

# auto-regressive generation
while True:
    # input_ids, position_ids, attention_mask, past_key_values):
    outputs = glmModel(input_ids, position_ids, attention_mask, past_key_values)

    logits_processor = LogitsProcessorList()
    logits_processor.append(InvalidScoreLogitsProcessor())

    next_token_logits = outputs[0]
    next_token_logits = next_token_logits[:, -1, :]

    # pre-process distribution
    next_token_scores = logits_processor(input_ids, next_token_logits)

    # logits_warper 1. TemperatureLogitsWarper
    next_token_scores = next_token_scores / temperature

    # logits_warper 2. TopKLogitsWarper
    top_k = min(top_k, next_token_scores.size(-1))  # Safety check
    # Remove all tokens with a probability less than the last token of the top-k
    indices_to_remove = (
        next_token_scores < torch.topk(next_token_scores, top_k)[0][..., -1, None]
    )
    next_token_scores = next_token_scores.masked_fill(indices_to_remove, filter_value)

    # logits_warper 3. TopPLogitsWarper
    sorted_logits, sorted_indices = torch.sort(next_token_scores, descending=False)
    cumulative_probs = sorted_logits.softmax(dim=-1).cumsum(dim=-1)
    # Remove tokens with cumulative top_p above the threshold (token with 0 are kept)
    sorted_indices_to_remove = cumulative_probs <= (1 - top_p)
    # scatter sorted tensors to original indexing
    indices_to_remove = sorted_indices_to_remove.scatter(
        1, sorted_indices, sorted_indices_to_remove
    )
    next_token_scores = next_token_scores.masked_fill(indices_to_remove, filter_value)

    # sample
    probs = nn.functional.softmax(next_token_scores, dim=-1)
    next_tokens = torch.multinomial(probs, num_samples=1).squeeze(1)

    # finished sentences should have their next token be a padding token
    next_tokens = next_tokens * unfinished_sequences + pad_token_id * (
        1 - unfinished_sequences
    )

    print(next_tokens)

    # update generated ids, model inputs, and length for next step
    all_input_ids = torch.cat([all_input_ids, next_tokens[:, None]], dim=-1)
    input_ids = next_tokens[:, None]

    # update past_key_values
    past_key_values = outputs[1]

    # update attention mask
    attention_mask = torch.cat(
        [attention_mask, attention_mask.new_ones((attention_mask.shape[0], 1))], dim=-1
    )

    # update position_ids
    new_position_id = all_position_ids[..., -1:].clone()
    new_position_id += 1
    all_position_ids = torch.cat([all_position_ids, new_position_id], dim=-1)
    position_ids = new_position_id

    # if eos_token was found in one sentence, set sentence to finished
    eos_token_id_tensor = torch.tensor(eos_token_id).to(input_ids.device)
    unfinished_sequences = unfinished_sequences.mul(
        next_tokens.tile(eos_token_id_tensor.shape[0], 1)
        .ne(eos_token_id_tensor.unsqueeze(1))
        .prod(dim=0)
    )

    # stop when each sentence is finished, or if we exceed the maximum length
    if unfinished_sequences.max() == 0 or input_ids.shape[-1] >= max_length:
        break