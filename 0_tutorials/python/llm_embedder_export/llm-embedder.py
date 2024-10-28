import torch
from transformers import AutoTokenizer, AutoModel

INSTRUCTIONS = {
    "qa": {
        "query": "Represent this query for retrieving relevant documents: ",
        "key": "Represent this document for retrieval: ",
    },
    "icl": {
        "query": "Convert this example into vector to look for useful examples: ",
        "key": "Convert this example into vector for retrieval: ",
    },
    "chat": {
        "query": "Embed this dialogue to find useful historical dialogues: ",
        "key": "Embed this historical dialogue for retrieval: ",
    },
    "lrlm": {
        "query": "Embed this text chunk for finding useful historical chunks: ",
        "key": "Embed this historical text chunk for retrieval: ",
    },
    "tool": {
        "query": "Transform this user request for fetching helpful tool descriptions: ",
        "key": "Transform this tool description for retrieval: "
    },
    "convsearch": {
        "query": "Encode this query and context for searching relevant passages: ",
        "key": "Encode this passage for retrieval: ",
    },
}

# Define queries and keys
queries = ["test query 1", "test query 2"]
keys = ["test key 1", "test key 2"]

# Load model
tokenizer = AutoTokenizer.from_pretrained('BAAI/llm-embedder')
model = AutoModel.from_pretrained('BAAI/llm-embedder')

# Add instructions for specific task (qa, icl, chat, lrlm, tool, convsearch)
instruction = INSTRUCTIONS["convsearch"]
queries = [instruction["query"] + query for query in queries]
keys = [instruction["key"] + key for key in keys]

# Tokenize sentences
query_inputs = tokenizer(queries, padding=True, return_tensors='pt')
key_inputs = tokenizer(keys, padding=True, return_tensors='pt')

# Encode
with torch.no_grad():
    query_outputs = model(**query_inputs)
    key_outputs = model(**key_inputs)
    # CLS pooling
    query_embeddings = query_outputs.last_hidden_state[:, 0]
    key_embeddings = key_outputs.last_hidden_state[:, 0]
    # Normalize
    query_embeddings = torch.nn.functional.normalize(query_embeddings, p=2, dim=1)
    key_embeddings = torch.nn.functional.normalize(key_embeddings, p=2, dim=1)

similarity = query_embeddings @ key_embeddings.T
print(similarity)
# [[0.8971, 0.8534]
# [0.8462, 0.9091]]