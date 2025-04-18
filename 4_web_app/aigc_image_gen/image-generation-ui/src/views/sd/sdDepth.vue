<template>
  <div class="app-container">
    <el-form label-width="120">
      <el-form-item label="正向提示词">
        <el-input v-model="prompt" size="small" style="width:400px"/>
      </el-form-item>
      <el-form-item label="反向向提示词">
        <el-input v-model="negativePrompt" size="small" style="width:400px"/>
      </el-form-item>
      <el-form-item label="迭代次数">
        <el-input v-model="steps" size="small" style="width:80px"/>
      </el-form-item>
      <el-form-item label="本地图片">
        <el-upload
          ref="upload"
          name="imageFile"
          class="upload"
          :action="upload()"
          :on-preview="handlePreview"
          :on-change="handleChange"
          :on-remove="handleRemove"
          :on-success="handleSuccess"
          :before-upload="beforeUpload"
          ::limit="1"
          :show-file-list="true"
          :auto-upload="false"
          :data="uploadData"
        >
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <el-button
            v-loading.fullscreen.lock="fullscreenLoading"
            style="margin-left: 10px;"
            type="success"
            size="small"
            element-loading-text="拼命加载中"
            @click="submitUpload"
          >上传</el-button>
          <div slot="tip" class="el-upload__tip">图片格式: JPG(JPEG), PNG</div>
        </el-upload>
      </el-form-item>
      <el-row>
        <el-col :span="9">
          <div><img :src="orgBase64Img" width="400px" class="avatar"></div>
        </el-col>
        <el-col :span="12">
          <el-form-item>
            <img :src="base64Img2" width="400px" class="avatar">
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>
  </div>
</template>

<script>
export default {
  name: 'InferenceDetail',
  components: {
  },
  data() {
    return {
      fullscreenLoading: false,
      prompt: 'Stormtrooper\'s lecture in beautiful lecture hall',
      negativePrompt: 'defommed,distorted',
      steps: '25',
      base64Img: '',
      base64Img2: '',
      orgBase64Img: ''
    }
  },
  computed: {
    // 确保每次上传时动态返回正确的参数
    uploadData() {
      return {
        prompt: this.prompt,
        negativePrompt: this.negativePrompt,
        steps: this.steps
      }
    }
  },
  methods: {
    upload() {
      return `${process.env.VUE_APP_BASE_API}/api/sd/sdDepth`
    },
    submitUpload() {
      this.fullscreenLoading = true
      this.$refs.upload.submit()
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handleChange(file) {
      console.log(file)
    },
    handlePreview(file) {
      console.log(file)
    },
    handleSuccess(file) {
      this.orgBase64Img = file.data.orgBase64Img
      this.base64Img2 = file.data.base64Img
      this.fullscreenLoading = false
    },
    beforeUpload(file) {
      const pass = file.type === 'image/jpg' || 'image/jpeg' || 'image/png'
      if (!pass) {
        this.$message.error('Image format should be JPG(JPEG) or PNG!')
      }
      return pass
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
