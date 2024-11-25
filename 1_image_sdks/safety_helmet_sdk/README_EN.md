
### Download the model, place it in the /models directory, and unzip
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/safety_helmet_models.zip

### Safety Helmet Detection SDK
Safety helmet detection.
-Support categories:
-Safe
-Unsafe

### SDK functions
- Safety helmet detection, giving detection boxes and confidence.
  -Three models:
  -Small model (mobilenet0.25)
  -Medium model (mobilenet1.0)
  -Large model (darknet53)

### Run the small model example - SmallSafetyHelmetDetectExample

- Test picture
![small](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/safety_helmet_result_s.png)

### Run the medium model example - MediumSafetyHelmetDetectExample

- Test picture
![medium](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/safety_helmet_result_m.png)

### Run the large model example - LargeSafetyHelmetDetectExample

- Test picture
![large](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/sec_sdks/images/safety_helmet_result_l.png)


After successful operation, the command line should see the following information:
```text
[INFO ] - [
	class: "safe 0.9983590245246887", probability: 0.99835, bounds: [x=0.244, y=0.000, width=0.086, height=0.150]
	class: "unsafe 0.998088538646698", probability: 0.99808, bounds: [x=0.226, y=0.204, width=0.115, height=0.263]
	class: "safe 0.997364342212677", probability: 0.99736, bounds: [x=0.584, y=0.247, width=0.162, height=0.302]
	class: "safe 0.9963852167129517", probability: 0.99638, bounds: [x=0.319, y=0.000, width=0.076, height=0.133]
	class: "safe 0.9952006936073303", probability: 0.99520, bounds: [x=0.757, y=0.262, width=0.111, height=0.264]
]
```

### Open source algorithm
#### Open source algorithm used by the SDK
- [Safety-Helmet-Wearing-Dataset](https://github.com/njvisionpower/Safety-Helmet-Wearing-Dataset)
