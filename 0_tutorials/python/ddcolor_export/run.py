# https://www.modelscope.cn/models/damo/cv_ddcolor_image-colorization/summary
# C:\Users\admin\.cache\modelscope\hub\damo\cv_ddcolor_image-colorization
# D:\system\Users\admin\miniconda3\envs\modelscope\Lib\site-packages\modelscope\models\cv\image_colorization\ddcolor

import cv2
from modelscope.outputs import OutputKeys
from modelscope.models.cv.image_colorization.ddcolor.ddcolor_for_image_colorization import DDColorForImageColorization
from modelscope.pipelines.cv.ddcolor_image_colorization_pipeline import DDColorImageColorizationPipeline

img_path = 'https://modelscope.oss-cn-beijing.aliyuncs.com/test/images/audrey_hepburn.jpg'
# 传入模型id或模型目录
# img_colorization = pipeline(Tasks.image_colorization, model='damo/cv_ddcolor_image-colorization')
# result = img_colorization(img_path)

model = DDColorForImageColorization('C:\\Users\\admin\\.cache\\modelscope\\hub\\damo\\cv_ddcolor_image-colorization')
pp = DDColorImageColorizationPipeline(model)
img = pp.preprocess(img_path)
result = pp.forward(img)
result = pp.postprocess(result)
cv2.imwrite('result.png', result[OutputKeys.OUTPUT_IMG])