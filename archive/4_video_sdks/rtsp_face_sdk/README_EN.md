
### Download the model and place it in the models directory
- Link: https://github.com/mymagicpower/AIAS/releases/download/apps/face_detection.zip

### Face Detection

Face recognition technology has been widely used in various fields, including face access control systems, face payment, etc. With the improvement of face recognition technology, its applications are becoming more and more extensive. Currently, China's face recognition technology is leading the world. In the security industry, mainstream security manufacturers in China have also launched their own face recognition products and solutions. The security industry is one of the main application areas of face recognition technology.
Face recognition is a popular research field in computer technology. It belongs to biometric identification technology and distinguishes individuals based on their own biological characteristics (generally referring to humans). The biological characteristics studied by biometric identification technology include faces, fingerprints, palm prints, irises, retinas, sounds (voices), body shapes, personal habits (such as the strength and frequency of keystrokes, signatures), etc. Corresponding recognition technologies include face recognition, fingerprint recognition, palmprint recognition, iris recognition, retinal recognition, voice recognition (voice recognition can be used for identity recognition or speech content recognition, only the former belongs to biometric identification technology), body shape recognition, keystroke recognition, signature recognition, etc.

## SDK functions

Detect faces in real time (requires a desktop with a graphics card, otherwise it will be more laggy) by taking RTSP streams.

- RTSP address of cameras such as Hikvision/Dahua: rtsp://user:password@192.168.16.100:554/Streaing/Channels/1
- RTSP address of video platforms such as Hikvision/Dahua: rtsp://192.168.16.88:554/openUrl/6rcShva
- Your own RTSP address

## Run the face detection example

1. First, download the example code
```bash
git clone https://github.com/mymagicpower/AIAS.git
```

2. Import the examples project into the IDE:
```
cd rtsp_face_sdk
```

3. Run the example code: RtspFaceDetectionExample


## The effect is as follows:
![result](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/faces.jpg)

