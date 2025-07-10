<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120">
      <el-row>
        <el-col :span="9">
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
              :show-file-list="false"
              :auto-upload="false"
            >
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <el-button
                v-loading.fullscreen.lock="fullscreenLoading"
                style="margin-left: 10px;"
                type="success"
                size="small"
                element-loading-text="拼命加载中"
                @click="submitUpload"
              >上传抠图</el-button>
              <div slot="tip" class="el-upload__tip">图片格式: JPG(JPEG), PNG</div>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="">
            <div><img :src="form.base64Img2" width="500px" class="avatar"></div>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-divider />
      </el-form-item>
      <el-form-item label="图片链接">
        <el-input v-model="form.url" />
      </el-form-item>
      <el-row>
        <el-col :span="9">
          <el-form-item>
            <img :src="form.url" width="400px">
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item>
            <img :src="form.base64Img1" width="400px" class="avatar">
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-button
          v-loading.fullscreen.lock="fullscreenLoading"
          type="primary"
          size="small"
          element-loading-text="拼命加载中"
          @click="onSubmit"
        >在线抠图</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { clothSegForImageUrl } from '@/api/inference'

export default {
  name: 'InferenceDetail',
  components: {
  },
  data() {
    return {
      fullscreenLoading: false,
      form: {
        url: 'https://aias-home.oss-cn-beijing.aliyuncs.com/assets/pose.png',
        result1: '',
        result2: '',
        base64Img: ''
      }
    }
  },
  methods: {
    upload() {
      // return window.g.Base_URL + '/inference/clothSegForImageFile'
      return `${process.env.VUE_APP_BASE_API}/seg/clothSegForImageFile`
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
      this.form.base64Img2 = file.data.base64Img
      this.fullscreenLoading = false
    },
    beforeUpload(file) {
      const pass = file.type === 'image/jpg' || 'image/jpeg' || 'image/png'
      if (!pass) {
        this.$message.error('Image format should be JPG(JPEG) or PNG!')
      }
      return pass
    },
    onSubmit() {
      this.fullscreenLoading = true
      clothSegForImageUrl(this.form).then(response => {
        this.fullscreenLoading = false
        this.form.base64Img1 = response.data.base64Img
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
