
### Download the model and place it in the /models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/crowdnet.zip

### Crowd Density Detection SDK
The CrowdNet model is a crowd density estimation model proposed in 2016. The paper is "CrowdNet: A Deep Convolutional Network for DenseCrowd Counting". The CrowdNet model is mainly composed of deep convolutional neural networks and shallow convolutional neural networks. It is trained by inputting the original image and the density map obtained by the Gaussian filter. Finally, the model estimates the number of people in the image. Of course, this can not only be used for crowd density estimation, theoretically, density estimation of other animals, etc. should also be possible.

The following is the structural diagram of the CrowdNet model. From the structural diagram, it can be seen that the CrowdNet model is composed of a deep convolutional network (Deep Network) and a shallow convolutional network (Shallow Network). The two groups of networks are spliced into one network and then input into a convolutional layer with a convolutional kernel size of 1. Finally, a density map data is obtained through interpolation, and the estimated number of people can be obtained by counting this density.

![model](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/network.png)

### SDK functions:
- Calculate the number of people
- Calculate the density map

### Running Example- CrowdDetectExample
- Test picture
![crowd](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/crowd1.jpg)

- Example code:
```text
    Path imageFile = Paths.get("src/test/resources/crowd1.jpg");
    Image image = ImageFactory.getInstance().fromFile(imageFile);

    Criteria<Image, NDList> criteria = new CrowdDetect().criteria();

    try (ZooModel model = ModelZoo.loadModel(criteria);
        Predictor<Image, NDList> predictor = model.newPredictor()) {
      NDList list = predictor.predict(image);

      //person quantity
      float q = list.get(1).toFloatArray()[0];
      int quantity = (int)(Math.abs(q) + 0.5);
      logger.info("人数 quantity: {}", quantity);
      
      NDArray densityArray = list.get(0);
      logger.info("density: {}", densityArray.toDebugString(1000000000, 1000, 1000, 1000));
```


- After the operation is successful, the command line should see the following information:
```text
[INFO ] - Person quantity: 11

[INFO ] - Density: ND: (1, 1, 80, 60) cpu() float32
[  
   [ 4.56512964e-04,  2.19504116e-04,  3.44428350e-04,  ..., -1.44560239e-04,  1.58709008e-04],
   [ 9.59073077e-05,  2.53924576e-04,  2.51444580e-04,  ..., -1.64886122e-04,  1.14555296e-04],
   [ 6.42040512e-04,  5.44962648e-04,  4.95903892e-04,  ..., -1.15299714e-04,  3.01052118e-04],
   [ 1.58930803e-03,  1.43694575e-03,  7.95312808e-04,  ...,  1.44582940e-04,  4.20258410e-04],
    ....
   [ 2.21548311e-04,  2.92199198e-04,  3.05847381e-04,  ...,  6.77200791e-04,  2.88001203e-04],
   [ 5.04880096e-04,  2.36357562e-04,  1.90203893e-04,  ...,  8.42695648e-04,  2.92608514e-04],
   [ 1.45231024e-04,  1.56763941e-04,  2.12623156e-04,  ...,  4.69507067e-04,  1.36347953e-04],
   [ 5.02332812e-04,  2.98928004e-04,  3.34762561e-04,  ...,  4.80025599e-04,  2.72601028e-04],
]

```
#### Density map
![density](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/density.png)


### Open source algorithm
#### 1. Open source algorithm used by SDK
- [PaddlePaddle-CrowdNet](https://github.com/yeyupiaoling/PaddlePaddle-CrowdNet)
#### 2. How to export the model?
- [how_to_create_paddlepaddle_model](http://docs.djl.ai/docs/paddlepaddle/how_to_create_paddlepaddle_model_zh.html)
- export_model.py
```text
import paddle
import paddle.fluid as fluid

INFER_MODEL = 'infer_model/'

def save_pretrained(dirname='infer/', model_filename=None, params_filename=None, combined=True):
if combined:
model_filename = "__model__" if not model_filename else model_filename
params_filename = "__params__" if not params_filename else params_filename
place = fluid.CPUPlace()
exe = fluid.Executor(place)

    program, feeded_var_names, target_vars = fluid.io.load_inference_model(INFER_MODEL, executor=exe)

    fluid.io.save_inference_model(
        dirname=dirname,
        main_program=program,
        executor=exe,
        feeded_var_names=feeded_var_names,
        target_vars=target_vars,
        model_filename=model_filename,
        params_filename=params_filename)


if __name__ == '__main__':
paddle.enable_static()
save_pretrained()
```