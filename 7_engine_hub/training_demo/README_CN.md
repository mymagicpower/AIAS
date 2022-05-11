# 该项目仅用于学习研究使用，项目落地建议使用Python训练框架,如：paddlepaddle, pytorch, tensorflow等等。

# 基于 AIAS & DJL的可视化训练
前端使用VUE开发，后端使用spring boot开发。

当前实现的功能：

- 训练/验证过程
- 训练指标与可视化图表
- 在线推理

## 环境准备
### 安装JDK

For ubuntu:

```bash
sudo apt-get install openjdk-11-jdk-headless
```
For centos:

```bash
sudo yum install java-11-openjdk-devel
```
For macOS:

```bash
brew tap homebrew/cask-versions
brew update
brew cask install adoptopenjdk11
```

也可以从oracle官网下载 [Oracle JDK](https://www.oracle.com/technetwork/java/javase/overview/index.html)


## 构建前端 UI
使用npm构建:

```bash
# clone the visualization-vue project
cd aias-training-ui

# install dependency
npm install

# develop
npm run dev
```

运行成功后打开： http://localhost:8090

## 构建后端项目

运行下面的命令:

```bash
# Maven build
mvn package -DskipTests -f aias-training-demo

# enter the jar directory
cd /Path/to/aias-training-demo/target/

# Run example code
java -jar aias-training-demo-0.0.1-SNAPSHOT.jar
```

## 打开浏览器

http://localhost:8090:

- Training
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/inference_demo.png)

- Inference
![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/inference_demo.png)


## 帮助
#### 1. 检查 node.js and npm 版本:
```bash
node -v
v14.16.0
npm -v
7.8.0
```
#### 2. 安装最新的 node.js & npm:
```bash
#Clean cache
sudo npm cache clean -f
#Install n
sudo npm install -g n
#Install latest node.js
sudo n stable
#Install latest npm
sudo npm install npm@latest -g
```
#### 3. Mac OSX如果碰到下面的错误:
"Package pangocairo was not found in the pkg-config search path."  
运行:
```bash
brew install pkg-config
brew intall cairo
brew install pango
```

### 官网：
[官网链接](http://www.aias.top/)

### Git地址：   
[Github链接](https://github.com/mymagicpower/AIAS)    
[Gitee链接](https://gitee.com/mymagicpower/AIAS)   