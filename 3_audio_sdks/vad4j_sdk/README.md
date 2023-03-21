## Voice Activity Detection (VAD)
Voice Activity Detection (VAD), also known as endpoint detection or boundary detection, is used to identify and remove long periods of silence from an audio signal stream. By suppressing silence, valuable bandwidth resources can be saved and end-to-end delay experienced by the user can be reduced.

### Mac & Linux Environment

### Running Example - MacAndLinuxVadExample

After successful execution, the command line should display the following information:
```text
Mac OS X
true
[INFO ] - closing VAD
true
[INFO ] - closing VAD
```
### Help

- The program is not thread-safe. Multiple instances need to consider concurrency issues.
  -Do not forget to handle close() problems using try-with-resources statements.

### Windows Environment

### Running Example - WindowsExample

After successful execution, the command line should display the following information:
```text
...
Mac OS X
Error loading native library: java.lang.Exception: Unsupported OS: Mac OS X
```
### Help

### Shared Library Files:

- Linux: vad4j_sdk/lib/linux
  --libfvad.so
  --libwebrtcvadwrapper.so
  -Windows: vad4j_sdk/lib/windows
  --libfvad.dll
  --libwebrtcvadwrapper.dll

### Setting Environment Variables

- The shared library files need to be added to java.library.path.
  -Set the environment variable: LD_LIBRARY_PATH to /path/to/shared/libraries:$LD_LIBRARY_PATH.

### Audio Data

The input data should be 16-bit PCM audio data. For more information, please refer to the following link:
https://github.com/jitsi/jitsi-webrtc-vad-wrapper/blob/master/readme.md