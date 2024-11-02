## 目录：
http://aias.top/


### 以图搜图
- 包含两个项目，满足不同场景的需要

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/arc.png)

### 1. 以图搜图【无向量引擎版】 - simple_image_search
#### 1.1 主要特性
- 支持100万以内的数据量
- 随时对数据进行插入、删除、搜索、更新等操作

#### 1.2 功能介绍
- 以图搜图：上传图片搜索
- 数据管理：提供图像压缩包(zip格式)上传，图片特征提取


### 2. 以图搜图【向量引擎版】 - image_search
#### 2.1 主要特性
- 底层使用特征向量相似度搜索
- 单台服务器十亿级数据的毫秒级搜索
- 近实时搜索，支持分布式部署
- 随时对数据进行插入、删除、搜索、更新等操作
- 支持在线用户管理与服务器性能监控，支持限制单用户登录

#### 2.2 系统功能
- 搜索管理：提供通用图像搜索，人像搜索，图像信息查看
- 存储管理：提供图像压缩包(zip格式)上传，人像特征提取，通用特征提取
- 用户管理：提供用户的相关配置，新增用户后，默认密码为123456
- 角色管理：对权限与菜单进行分配，可根据部门设置角色的数据权限
- 菜单管理：已实现菜单动态路由，后端可配置化，支持多级菜单
- 部门管理：可配置系统组织架构，树形表格展示
- 岗位管理：配置各个部门的职位
- 字典管理：可维护常用一些固定的数据，如：状态，性别等
- 系统日志：记录用户操作日志与异常日志，方便开发人员定位排错
- SQL监控：采用druid 监控数据库访问性能，默认用户名admin，密码123456
- 定时任务：整合Quartz做定时任务，加入任务日志，任务运行情况一目了然
- 服务监控：监控服务器的负载情况


#### 1. 前端搜索页面功能介绍
- 首页
1). 支持图片拖拽，粘贴搜索.  
2). 适配PC，android，ios浏览器.  
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/simple_1.png"  width = "600"/>
</div> 

- 图片列表页
1). 支持图片拖拽，粘贴搜索.  
2). 图片排版自适应.
3). 图片列表下拉自动加载.  
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/simple_2.png"  width = "600"/>
</div> 

#### 2. 后台管理功能介绍
- 登录
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/login.png"  width = "600"/>
</div> 

- 用户管理
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/user.png"  width = "600"/>
</div> 

- 角色管理
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/role.png"  width = "600"/>
</div> 

- 运维管理
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/ops.png"  width = "600"/>
</div> 

- 图片上传
1). 支持zip压缩包上传.  
2). 支持服务器端文件夹上传（大量图片上传使用，比如几十万张图片入库）.  
3). 支持客户端文件夹上传.  
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/storage.png"  width = "600"/>
</div> 

- 图像搜索
<div align="center">
<img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/image_search/images/search.png"  width = "600"/>
</div> 
