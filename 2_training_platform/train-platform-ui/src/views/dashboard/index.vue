<template>
  <div class="iframe-container">
    <iframe :src="iframeUrl" frameborder="0"></iframe>
  </div>
</template>

<script>

export default {
  name: 'DashboardAdmin',
  data() {
    return {
      // ${process.env.TRAINING_BASE_API}
      iframeUrl: `http://127.0.0.1:9000/train/overview?/setlang/zh`
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
/* 整个页面布局 */
.iframe-container {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 让整个页面充满窗口 */
}

/* iframe 样式 */
iframe {
  width: 100%;
  height: 100%;
  border: none;
}
</style>
