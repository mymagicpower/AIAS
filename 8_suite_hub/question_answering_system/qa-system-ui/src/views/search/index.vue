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
        </el-select>
        <el-button slot="append" icon="el-icon-search" element-loading-text="拼命加载中" @click="onSubmit" />
      </el-input>

      <el-table
              :data="form.result"
              stripe
              style="width: 100%">
        <el-table-column
                prop="score"
                label="Score"
                width="100">
        </el-table-column>
        <el-table-column
                prop="question"
                label="Question"
                width="300">
        </el-table-column>
        <el-table-column
                prop="answer"
                label="Answer"
                width="600">
        </el-table-column>
      </el-table>
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
