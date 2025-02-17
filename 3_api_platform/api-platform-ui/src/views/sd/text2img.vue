<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120">
      <el-form-item label="正向提示词">
        <el-input v-model="form.prompt" size="small" style="width:400px"/>
      </el-form-item>
      <el-form-item label="反向向提示词">
        <el-input v-model="form.negativePrompt" size="small" style="width:400px"/>
      </el-form-item>
      <el-form-item label="迭代步数">
        <el-input v-model="form.steps" size="small" style="width:80px"/>
      </el-form-item>
      <el-form-item>
        <el-button
          v-loading.fullscreen.lock="fullscreenLoading"
          style="margin-left: 10px;"
          type="success"
          size="small"
          element-loading-text="拼命加载中"
          @click="onSubmit"
        >点击生成</el-button>
      </el-form-item>
      <el-form-item>
        <img :src="form.base64Img" width="400px" class="avatar">
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import { txt2Image } from '@/api/sd'

export default {
  name: 'InferenceDetail',
  components: {
  },
  data() {
    return {
      fullscreenLoading: false,
      form: {
        prompt: 'Photograph of an astronaut riding a horse in desert',
        negativePrompt: 'defommed,distorted',
        steps: '25',
        base64Img: ''
      }
    }
  },
  methods: {
    onSubmit() {
      this.fullscreenLoading = true
      txt2Image(this.form).then(response => {
        this.fullscreenLoading = false
        this.form.base64Img = response.data.base64Img
      })
    }
  }
}
</script>

<style scoped>
  .el-input {
    width: 600px;
  }

  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }

  .line {
    text-align: center;
  }
</style>
