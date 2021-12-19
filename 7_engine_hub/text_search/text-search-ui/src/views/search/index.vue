<template>
  <div class="app-container">
    <el-form ref="form" :model="form">
      <el-input v-model="form.text" placeholder="请输入内容" class="input-with-select">
        <el-select slot="prepend" v-model="form.topK" placeholder="请选择">
          <el-option label="Top 5" value="5" />
          <el-option label="Top 10" value="10" />
          <el-option label="Top 20" value="20" />
          <el-option label="Top 50" value="50" />
          <el-option label="Top 100" value="100" />
          <el-option label="Top 200" value="200" />
        </el-select>
        <el-button slot="append" icon="el-icon-search" element-loading-text="拼命加载中" @click="onSubmit" />

      </el-input>
      <el-row>
        <el-col :span="12">
          <el-form-item label="">
            <json-viewer
              :value="form.result"
              :expand-depth="3"
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
import { search } from '@/api/search'
import JsonViewer from 'vue-json-viewer'

export default {
  name: 'InferenceDetail',
  components: {
    JsonViewer
  },
  data() {
    return {
      fullscreenLoading: false,
      form: {
        topK: 5,
        text: '',
        result: ''
      }
    }
  },
  methods: {
    onSubmit() {
      this.fullscreenLoading = true
      search(this.form).then(response => {
        this.fullscreenLoading = false
        this.form.result = response.data.result
      })
    }
  }
}
</script>

<style>
  .el-select .el-input {
    width: 130px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
</style>
