#!/usr/bin/env python
#
# Copyright 2024 Amazon.com, Inc. or its affiliates. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
# except in compliance with the License. A copy of the License is located at
#
# http://aws.amazon.com/apache2.0/
#
# or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
# BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, express or implied. See the License for
# the specific language governing permissions and limitations under the License.
import os
import sys
from typing import Any

import torch
from sam2.modeling.sam2_base import SAM2Base
from sam2.sam2_image_predictor import SAM2ImagePredictor
from torch import nn

class Sam2Wrapper(nn.Module):

    def __init__(self, sam_model: SAM2Base, multimask_output: bool) -> None:
        super().__init__()
        self.model = sam_model
        self.image_encoder = sam_model.image_encoder
        self.no_mem_embed = sam_model.no_mem_embed
        self.mask_decoder = sam_model.sam_mask_decoder
        self.prompt_encoder = sam_model.sam_prompt_encoder
        self.img_size = sam_model.image_size
        self.multimask_output = multimask_output
        self.sparse_embedding = None

    @torch.no_grad()
    def forward(self, x: torch.Tensor) -> tuple[Any, Any, Any]:
        backbone_out = self.image_encoder(x)
        backbone_out["backbone_fpn"][0] = self.model.sam_mask_decoder.conv_s0(
            backbone_out["backbone_fpn"][0])
        backbone_out["backbone_fpn"][1] = self.model.sam_mask_decoder.conv_s1(
            backbone_out["backbone_fpn"][1])

        feature_maps = backbone_out["backbone_fpn"][-self.model.
                                                    num_feature_levels:]
        vision_pos_embeds = backbone_out["vision_pos_enc"][-self.model.
                                                           num_feature_levels:]

        feat_sizes = [(x.shape[-2], x.shape[-1]) for x in vision_pos_embeds]

        # flatten NxCxHxW to HWxNxC
        vision_feats = [x.flatten(2).permute(2, 0, 1) for x in feature_maps]
        vision_feats[-1] = vision_feats[-1] + self.no_mem_embed

        feats = [
            feat.permute(1, 2, 0).reshape(1, -1, *feat_size)
            for feat, feat_size in zip(vision_feats[::-1], feat_sizes[::-1])
        ][::-1]

        return feats[0], feats[1], feats[2]

def trace_model(model_id: str):
    if torch.cuda.is_available():
        device = torch.device("cuda")
    else:
        device = torch.device("cpu")

    device = torch.device("cpu")

    model_name = f"{model_id[9:]}"
    os.makedirs(model_name, exist_ok=True)

    predictor = SAM2ImagePredictor.from_pretrained(model_id, device=device)
    model = Sam2Wrapper(predictor.model, True)

    input_image = torch.ones(1, 3, 1024, 1024).to(device)
    high_res_feats_0, high_res_feats_1, image_embed = model(input_image)

    converted = torch.jit.trace(model, (input_image), strict=False)
    torch.jit.save(converted, f"{model_name}/{model_name}.pt")

    # 导出 onnx 模型

    torch.onnx.export(converted, input_image, f'traced_model_encoder.onnx',
                      export_params=True,     # 导出训练好的模型参数
                      verbose=10,             # debug message
                      training=torch.onnx.TrainingMode.EVAL,
                      input_names=['in'],  # 为静态网络图中的输入节点设置别名，在进行onnx推理时，将input_names字段与输入数据绑定
                      output_names=['out'],  # 为输出节点设置别名
                      # 如果不设置dynamic_axes，那么对于输入形状为[4, 3, 224, 224]，在以后使用onnx进行推理时也必须输入[4, 3, 224, 224]
                      # 下面设置了输入的第0维是动态的，以后推理时batch_size的大小可以是其他动态值
                      dynamic_axes={
                          # a dictionary to specify dynamic axes of input/output
                          # each key must also be provided in input_names or output_names
                          "in": [0, 1, 2, 3],
                          "out":[0,1, 2]
                      }
                    )



if __name__ == '__main__':
    hf_model_id = sys.argv[1] if len(
        sys.argv) > 1 else "facebook/sam2-hiera-large"  # sam2-hiera-tiny   sam2-hiera-large
    trace_model(hf_model_id)