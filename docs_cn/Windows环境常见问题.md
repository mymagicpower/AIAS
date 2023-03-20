<div align="center">
  <a href="http://aias.top/">点击返回网站首页</a>
</div>  


#### Windows环境问题：
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

Windows 10 加载失败常常是因为缺少 Windows Visual C++ 相关扩展包而导致的。您可以通过下面Windows的步骤来修复系统缺失依赖项。
[Visual C++ Redistributable Packages](https://support.microsoft.com/en-us/topic/the-latest-supported-visual-c-downloads-2647da03-1eea-4433-9aff-95f26a218cc0)

如果安装了Visual Studio tools CMD:

```bash
dumpbin /dependents your_dll_file.dll
```

或者安装 [Dependency Walker](http://www.dependencywalker.com/)
这个软件载入DLL看一下缺啥依赖
（或者重装了一下vc，解决问题）

如果您在中国，可以使用 [DirectX](https://blog.csdn.net/VBcom/article/details/6962388) 修复工具 来安装遗失依赖项。

其它常见问题清单：
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

windows环境文件系统的权限问题，导致下载库失败。可以修改库和模型的默认路径，比如：选择D盘之类权限要求不高的路径。

<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/guides/images/defaultdir.jpg"  width = "600"/>
</div>   

参考链接：
https://docs.djl.ai/docs/development/cache_management.html



<div align="center">
  <a href="http://aias.top/">点击返回网站首页</a>
</div>  

