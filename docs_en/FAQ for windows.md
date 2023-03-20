
#### Windows Environment Issues:
##### 1. No deep learning engine found exception.
```bash
16:57:55.313 [main] ERROR ai.djl.examples.training.util.AbstractTraining - Unexpected error
ai.djl.engine.EngineException: No deep learning engine found.
    at ai.djl.engine.Engine.getInstance(Engine.java:81) ~[main/:?]
    at ai.djl.examples.training.util.Arguments.<init>(Arguments.java:42) ~[main/:?]
    at ai.djl.examples.training.util.AbstractTraining.runExample(AbstractTraining.java:67) [main/:?]
    at ai.djl.examples.training.TrainPikachu.main(TrainPikachu.java:72) [main/:?]
```

##### 2. UnsatisfiedLinkError issue

Windows 10 load failure is often due to the lack of Windows Visual C++ related expansion packages. You can follow the steps below to repair system missing dependencies.
[Visual C++ Redistributable Packages](https://support.microsoft.com/en-us/topic/the-latest-supported-visual-c-downloads-2647da03-1eea-4433-9aff-95f26a218cc0)

If Visual Studio tools CMD is installed:

```bash
dumpbin /dependents your_dll_file.dll
```

Or install  [Dependency Walker](http://www.dependencywalker.com/)
This software loads DLL to see what dependencies are missing
(or reinstall vc to solve the problem)

If you are in China, you can use the [DirectX](https://blog.csdn.net/VBcom/article/details/6962388) repair tool to install lost dependencies.

Other common issues list:
https://docs.djl.ai/docs/development/troubleshooting.html


##### 3. java.nio.file.AccessDeniedException issue
```bash
Caused by: java.lang.IllegalStateException: Failed to download XXX native library
	... 75 common frames omitted
Caused by: java.nio.file.AccessDeniedException: C:\Users\XXX\.djl.ai\mxnet\tmp8034998170920244011 -> C:\Users\XXX\.djl.ai\mxnet\1.9.0-mkl-win-x86_64
	at sun.nio.fs.WindowsException.translateToIOException(WindowsException.java:83) ~[na:1.8.0_111]
	at sun.nio.fs.WindowsException.rethrowAsIOException(WindowsException.java:97) ~[na:1.8.0_111]
	at sun.nio.fs.WindowsFileCopy.move(WindowsFileCopy.java:301) ~[na:1.8.0_111]
	at sun.nio.fs.WindowsFileSystemProvider.move(WindowsFileSystemProvider.java:287) ~[na:1.8.0_111]
	at java.nio.file.Files.move(Files.java:1395) ~[na:1.8.0_111]
	at ai.djl.util.Utils.moveQuietly(Utils.java:120) ~[api-0.17.0.jar:na]
	at ai.djl.mxnet.jna.LibUtils.downloadMxnet(LibUtils.java:285) ~[mxnet-engine-0.17.0.jar:na]
	... 79 common frames omitted
```

Windows environment file system permission issues cause library downloads to fail. You can modify the default path of the library and model, such as selecting a path with low permission requirements like the D drive.

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/images/defaultdir.jpg"  width = "600"/>
</div>   

Reference link:
https://docs.djl.ai/docs/development/cache_management.html


