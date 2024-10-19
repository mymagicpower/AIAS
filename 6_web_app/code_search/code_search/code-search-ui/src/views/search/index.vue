<template>
  <div class="app-container">
    <el-form ref="form" :model="form">
      <el-input v-model="form.text" placeholder="请输入内容" class="input-with-select">
        <el-select slot="prepend" v-model="form.topK" placeholder="请选择">
          <el-option label="Top 5" value="5" />
          <el-option label="Top 10" value="10" />
          <el-option label="Top 20" value="20" />
          <el-option label="Top 50" value="50" />
        </el-select>
        <el-button slot="append" icon="el-icon-search" element-loading-text="拼命加载中" @click="onSubmit" @keydown.enter="handleEnter" />

      </el-input>
      <el-table
        :data="form.result"
        stripe
        style="width: 100%"
      >
        <el-table-column
          prop="score"
          label="相似度"
          width="100"
        >
          <template slot-scope="scope">
            {{ toFixed(scope.row.score) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="text"
          label="源码"
          width="700"
        >
          <template slot-scope="scope">
            <pre v-highlightjs>
              <code class="java">{{scope.row.text}}</code>
            </pre>
          </template>
        </el-table-column>
        <el-table-column
          prop="title"
          label="源码文件"
          width="200"
        >
          <template slot-scope="scope">
            <el-link :href="scope.row.title" target="_blank" type="primary">点击查看源码</el-link>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
  </div>
</template>

<script>
import { search } from '@/api/search'
import '@/assets/styles/prism.css'

export default {
  name: 'InferenceDetail',
  components: {
  },
  data() {
    return {
      fullscreenLoading: false,
      form: {
        topK: 20,
        text: '',
        result: []
      }
    }
  },
  mounted() {
    window.addEventListener('keydown', this.handleKeydown)
  },
  beforeDestroy() {
    window.removeEventListener('keydown', this.handleKeydown)
  },
  methods: {
    toFixed(val) {
      return Number(val).toFixed(2)
    },
    handleKeydown(event) {
      if (event.keyCode === 13) {
        console.log('Enter key was pressed')
        this.onSubmit()
      }
    },
    onSubmit() {
      this.form.result = []
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
