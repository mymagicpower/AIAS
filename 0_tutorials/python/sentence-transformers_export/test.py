from sentence_transformers import SentenceTransformer, util
model = SentenceTransformer('allenai-specter')

title = 'BERT'
abstract = 'We introduce a new language representation model called BERT'
# text = title + '[SEP]' + abstract

query_embedding = model.encode(title + '[SEP]' + abstract, convert_to_tensor=True)

print(query_embedding)
# print("Similarity:", util.pytorch_cos_sim(query_embedding, passage_embedding))
