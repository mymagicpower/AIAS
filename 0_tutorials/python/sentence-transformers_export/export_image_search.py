from sentence_transformers import SentenceTransformer, util
from PIL import Image
import torch

#Load CLIP model
model = SentenceTransformer('clip-ViT-B-32', device='cpu')

#Encode an image:
# img_emb = model.encode(Image.open('two_dogs_in_snow.jpg'))

#Encode text descriptions
# text_emb = model.encode(['Two dogs in the snow', 'A cat on a table', 'A picture of London at night'])
text_emb = model.encode(['Two dogs in the snow'])
sm = torch.jit.script(model)
sm.save("models/clip-ViT-B-32/clip-ViT-B-32.pt")

#Compute cosine similarities
# cos_scores = util.cos_sim(img_emb, text_emb)
# print(cos_scores)