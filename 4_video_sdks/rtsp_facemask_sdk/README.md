
### Download the model and put it in the models directory
- Link 1: https://github.com/mymagicpower/AIAS/releases/download/apps/face_detection.zip
- Link 2: https://github.com/mymagicpower/AIAS/releases/download/apps/face_mask.zip

### Mask detection

Mask detection is helping to fight against pneumonia, and artificial intelligence technology is being applied to epidemic prevention and control. In cutting off the transmission pathway of anti-epidemic diseases, wearing masks has become one of the most important measures. However, in actual scenarios, there are still people who do not take it seriously, do not pay attention, or have a lucky mentality and do not wear masks, especially in public places, which poses great risks to individuals and the public. The mask detection function based on artificial intelligence can perform real-time detection based on the camera video stream.

## SDK function

Through rtsp streaming, real-time detection of masks (requires a desktop computer with a graphics card, otherwise it will be more laggy).

- RTSP address of cameras such as Hikvision/Dahua: rtsp://user:password@192.168.16.100:554/Streaing/Channels/1
- RTSP address of video platforms such as Hikvision/Dahua: rtsp://192.168.16.88:554/openUrl/6rcShva
- Your own RTSP address

## Run the face detection example

1. First, download the sample code
```bash
git clone https://github.com/mymagicpower/AIAS.git
```

2. Import the examples project into the IDE:
```
cd rtsp_facemask_sdk
```

3. Run the example code: RtspFaceMaskDetectionExample

## The effect is as follows:
![result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/mask_sdk/face-masks.png)
