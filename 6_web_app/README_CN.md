


#### 项目清单:
- 6_web_app - [Web应用，前端VUE，后端Springboot]
```text
  1). 训练引擎
      ...
```

<div align="center">
  <table>
    <tr>
      <td>
        <div align="left">
          <p>AI 训练平台 - training</p>   
          AI训练平台提供分类模型训练能力。<br>
          并以REST API形式为上层应用提供接口。<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/images/training.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>代码语义搜索 - code_search</p>  
            用于软件开发过程中的，代码搜代码，语义搜代码。<br>s 
            1. 代码语义搜索【无向量引擎版】 <br>
            - simple_code_search<br>
            主要特性<br>
            - 支持100万以内的数据量<br>
            - 随时对数据进行插入、删除、搜索、更新等操作<br>
            2. 代码语义搜索【向量引擎版】 - code_search<br>
            主要特性<br>
            - 底层使用特征向量相似度搜索<br>
            - 单台服务器十亿级数据的毫秒级搜索<br>
            - 近实时搜索，支持分布式部署<br>
            - 随时对数据进行插入、删除、搜索、更新等操作
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/data/images/code_search_arc.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>202种语言互相翻译 Web 应用 - text_translation</p>   
          - 支持202种语言互相翻译。<br>
          - 支持 CPU / GPU<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/assets/nllb.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>一键抠图 Web 应用 - image_seg</p>   
          当前版本包含了下面功能：<br>
          - 1. 通用一键抠图<br>
          - 2. 人体一键抠图<br>
          - 3. 动漫一键抠图
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/seg_all.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>图片一键高清- image_gan</p>   
          当前版本包含了下面功能：<br>
          - 图片一键高清: 提升图片4倍分辨率。<br>
          - 头像一键高清<br>
          - 人脸一键修复<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/assets/imageSr.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>图像&文本的跨模态相似性比对检索【支持40种语言】<br>
          - image_text_search</p>   
          - 包含两个项目，满足不同场景的需要<br>
          - 1. 图像&文本的跨模态相似性比对检索<br>
          【无向量引擎版】 <br>
          - simple_image_text_search<br>
          - 支持100万以内的数据量<br>
          - 随时对数据进行插入、删除、搜索、更新等操作<br>
          - 2. 图像&文本的跨模态相似性比对检索<br>
          【向量引擎版】 <br>
          - image_text_search<br>
          - 以图搜图：上传图片搜索<br>
          - 以文搜图：输入文本搜索<br>
          - 数据管理：提供图像压缩包(zip格式)上传<br>
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/7_engine_hub/image_text_search/search3.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>文本向量搜索 - text_search</p>   
          - 包含两个项目，满足不同场景的需要<br>
          - 1. 文本向量搜索【无向量引擎版】 <br>
          - simple_text_search <br>
          - 2. 文本向量搜索【向量引擎版】 <br>
          - text_search<br>
          - 语义搜索，通过句向量相似性，<br>检索语料库中与query最匹配的文本 <br>
          - 文本聚类，文本转为定长向量，<br>通过聚类模型可无监督聚集相似文本 <br>
          - 文本分类，表示成句向量，<br>直接用简单分类器即训练文本分类器 <br>
          - RAG 用于大模型搜索增强生成
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/text_search/search.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>人像搜索 - face_search</p>   
          - 包含两个项目，满足不同场景的需要<br>
          - 1. 人像搜索【精简版】 <br>
          - simple_face_search <br>
          - 2. 人像搜索【完整版】<br>
          - face_search<br>
          - 搜索管理：提供通用图像搜索，<br>人像搜索，图像信息查看<br>
          - 存储管理：提供图像压缩包(zip格式)上传，<br>人像特征提取，通用特征提取<br>
          - 用户管理：提供用户的相关配置，<br>新增用户后，默认密码为123456<br>
          - 角色管理：对权限与菜单进行分配，<br>可根据部门设置角色的数据权限<br>
          - 菜单管理：已实现菜单动态路由，<br>后端可配置化，支持多级菜单<br>
          - 部门管理：可配置系统组织架构，<br>树形表格展示<br>
          - 岗位管理：配置各个部门的职位<br>
          - 字典管理：可维护常用一些固定的数据，<br>如：状态，性别等<br>
          - 系统日志：记录用户操作日志与异常日志，<br>方便开发人员定位排错<br>
          - SQL监控：采用druid 监控数据库访问性能，<br>默认用户名admin，密码123456<br>
          - 定时任务：整合Quartz做定时任务，<br>加入任务日志，任务运行情况一目了然<br>
          - 服务监控：监控服务器的负载情况
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/face_search/images/search.png" width = "400px"/>
        </div>
      </td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <p>语音识别Web 应用 - asr</p>   
          - 本例子提供了英文语音识别，<br>中文语音识别。
        </div>
      </td>     
      <td>
        <div align="center">
        <img src="https://aias-home.oss-cn-beijing.aliyuncs.com/products/audio/images/asr_zh.png" width = "400px"/>
        </div>
      </td>
    </tr>                                                    
  </table>
</div>



