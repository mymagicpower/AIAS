import torch
import cv2
from typing import Tuple,Dict,Any
# Load model directly
from modelscope.models.cv.image_colorization.ddcolor.ddcolor_for_image_colorization import DDColorForImageColorization
from modelscope.pipelines.cv.ddcolor_image_colorization_pipeline import DDColorImageColorizationPipeline
import torch.nn.functional as F
import numpy as np

class Tracable(torch.nn.Module):
    def __init__(self):
        super().__init__()
        # load model and pipeline
        self.model = DDColorForImageColorization('C:\\Users\\admin\\.cache\\modelscope\\hub\\damo\\cv_ddcolor_image-colorization')
    def forward(self, image, height, width) -> Tuple:
        input = {'img': image}
        output_ab = self.model.model(**input)
        output_ab_resize = F.interpolate(output_ab, size=(height.item(), width.item()))
        return output_ab_resize

# %% create class
tracable = Tracable()

# %% input
img_path = 'https://modelscope.oss-cn-beijing.aliyuncs.com/test/images/audrey_hepburn.jpg'

model = DDColorForImageColorization('C:\\Users\\admin\\.cache\\modelscope\\hub\\damo\\cv_ddcolor_image-colorization')
pipeline = DDColorImageColorizationPipeline(model)
input = pipeline.preprocess(img_path)

# %% trace
tracable.eval()

width = torch.tensor([[pipeline.width]])

height = torch.tensor([[pipeline.height]])
output_ab_resize = tracable(input['img'], height, width)
output_ab_resize = output_ab_resize[0].float().detach().numpy().transpose(1, 2, 0)
out_lab = np.concatenate((pipeline.orig_l, output_ab_resize), axis=-1)
out_bgr = cv2.cvtColor(out_lab, cv2.COLOR_LAB2BGR)
output_img = (out_bgr * 255.0).round().astype(np.uint8)

cv2.imwrite('result.png', output_img)

converted = torch.jit.trace_module(tracable, {'forward': [input['img'], height, width]})
torch.jit.save(converted, "./traced_ddcolor_cpu.pt")
