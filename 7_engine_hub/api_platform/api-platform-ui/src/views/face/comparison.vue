<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120">
      <el-form-item label="图片1">
        <el-input v-model="form.url1" />
      </el-form-item>
      <el-form-item label="图片2">
        <el-input v-model="form.url2" />
      </el-form-item>
      <el-row>
        <el-col :span="9">
          <el-form-item>
            <img :src="form.url1" width="200px">
          </el-form-item>
          <el-form-item>
            <img :src="form.url2" width="200px">
          </el-form-item>
        </el-col>
        <el-col :span="9">
          <el-form-item label="">
            <json-viewer
              :value="form.result1"
              :expand-depth="4"
              copyable
              width="400px"
            />
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
        >人脸比对</el-button>
      </el-form-item>
      <el-form-item>
        <el-divider />
      </el-form-item>
      <el-row>
        <el-col :span="8">
          <div><img :src="form.base64Img1" width="200px" class="avatar"></div>
          <div><img :src="form.base64Img2" width="200px" class="avatar"></div>
          <el-form-item label="Local Image">
            <el-upload
              ref="upload"
              multiple
              name="imageFiles"
              class="upload"
              :on-preview="handlePreview"
              :on-change="handleChange"
              :on-remove="handleRemove"
              :on-exceed="handleExceed"
              :before-upload="beforeUpload"
              :file-list="fileList"
              :http-request="uploadFile"
              ::limit="2"
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
              >上传</el-button>
              <div slot="tip" class="el-upload__tip">Image format: JPG(JPEG), PNG</div>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="9">
          <el-form-item label="">
            <json-viewer
              :value="form.result2"
              :expand-depth="4"
              copyable
              width="400px"
            />
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>
  </div>
</template>

<script>
import { compareForImageUrls, compareForImageFiles } from '@/api/face'
import JsonViewer from 'vue-json-viewer'

export default {
  name: 'Feature',
  components: {
    JsonViewer
  },
  data() {
    return {
      fullscreenLoading: false,
      file: [],
      fileList: [], // upload多文件数组
      form: {
        url1: 'https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/kana1.jpg',
        url2: 'https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/face_sdk/images/kana2.jpg',
        result1: '',
        result2: '',
        base64Img: ''
      }
    }
  },
  methods: {
    upload() {
      return window.g.Base_URL + '/face/compareForImageFiles'
      // return `${process.env.VUE_APP_BASE_API}/inference/infoForImageFile`
    },
    // 上传文件
    uploadFile(param) {
      this.file.push(param.file)
    },
    submitUpload() {
      // this.$refs.upload.submit()
      if (this.fileList.length !== 2) {
        this.$message({
          message: '请选择且仅选择 2 个图像文件',
          type: 'warning'
        })
      } else {
        this.fullscreenLoading = true
        const formData = new FormData() // new formData对象
        this.$refs.upload.submit() // 提交调用uploadFile函数
        this.file.forEach(function(file) { // 遍历上传多个文件
          formData.append('imageFiles', file, file.name)
          // upData.append('file', this.file); //不要直接使用文件数组进行上传，传给后台的是两个Object
        })
        compareForImageFiles(formData).then(response => {
          this.form.base64Img1 = response.data.base64Img1
          this.form.base64Img2 = response.data.base64Img2
          this.form.result2 = response.data.result
          this.fullscreenLoading = false
          this.fileList = []
        })
      }
    },
    // 移除
    handleRemove(file, fileList) {
      this.fileList = fileList
      // return this.$confirm(`确定移除 ${ file.name }？`);
    },

    // 选取文件超过数量提示
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 2 个文件`)
    },
    // 监控上传文件列表
    handleChange(file, fileList) {
      const existFile = fileList.slice(0, fileList.length - 1).find(f => f.name === file.name)
      if (existFile) {
        this.$message.error('当前文件已经存在!')
        fileList.pop()
      }
      this.fileList = fileList
    },
    handlePreview(file) {
      console.log(file)
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
      compareForImageUrls(this.form).then(response => {
        this.fullscreenLoading = false
        this.form.result1 = response.data.result
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
