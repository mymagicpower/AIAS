from sentence_transformers import SentenceTransformer, util
model = SentenceTransformer('paraphrase-xlm-r-multilingual-v1')

#Our sentences we like to encode
sentences = ['This model generates embeddings for input sentence']

#Sentences are encoded by calling model.encode()
sentence_embeddings = model.encode(sentences)

#Print the embeddings
for sentence, embedding in zip(sentences, sentence_embeddings):
    print("Sentence:", sentence)
    print("Embedding:", embedding)
    print("")