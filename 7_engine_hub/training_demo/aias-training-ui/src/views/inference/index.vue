<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120">
      <el-form-item label="Online Image">
        <el-input v-model="form.url" />
      </el-form-item>
      <el-row>
        <el-col :span="5">
          <el-form-item>
            <img :src="form.url" width="200px">
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="">
            <textarea v-model="form.result1" style="font-size:14px;border:none;" rows="12" cols="65" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-button type="primary" round @click="onSubmit">Test</el-button>
      </el-form-item>
      <el-form-item>
        <hr style="border:1 dashed #987cb9" width="100%" color="#987cb9" SIZE="4">
      </el-form-item>
      <el-row>
        <el-col :span="5">
          <div><img :src="form.base64Img" width="200px" class="avatar"></div>
          <el-form-item label="Local Image">
            <el-upload
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
            >
              <el-button slot="trigger" type="primary" round>Upload</el-button>
              <div slot="tip" class="el-upload__tip">Image format: JPG(JPEG), PNG</div>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="">
            <textarea v-model="form.result2" style="font-size:14px;border:none;" rows="12" cols="65" />
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>
  </div>
</template>

<script>
import { mnistImageUrl } from '@/api/inference'

export default {
  name: 'InferenceDetail',
  data() {
    return {
      form: {
        url: 'https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/train_platform/0.png',
        result1: '',
        result2: '',
        base64Img: ''
      }
    }
  },
  methods: {
    upload() {
      return `${process.env.VUE_APP_BASE_API}/inference/mnistImage`
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
      this.form.base64Img = file.data.base64Img
      this.form.result2 = file.data.result
    },
    beforeUpload(file) {
      const pass = file.type === 'image/jpg' || 'image/png'
      if (!pass) {
        this.$message.error('Image format should be JPG(JPEG) or PNG!')
      }
      return pass
    },
    onSubmit() {
      mnistImageUrl(this.form).then(response => {
        this.form.result1 = response.data.result
      })
    }
  }
}
</script>

<style scoped>
  .el-input{
    width: 600px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  .line{
    text-align: center;
  }
</style>
