from transformers import AutoModelForSequenceClassification
import torch


class CrossEncoderWrapper(torch.nn.Module):
    def __init__(self, model_name):
        super().__init__()
        self.model_name = model_name
        self.transformer = AutoModelForSequenceClassification.from_pretrained(model_name)

    def forward(self, txt_tok):
        with torch.no_grad():
            scores = self.transformer(**txt_tok).logits
            print(scores)
        return scores

def load_model(name):
    return CrossEncoderWrapper(name)
