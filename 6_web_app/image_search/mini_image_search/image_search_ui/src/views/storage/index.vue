<template>
  <div class="app-container">
    <el-row>
      <el-upload
        ref="upload"
        name="file"
        class="upload"
        :action="upload()"
        :on-preview="handlePreview"
        :on-change="handleChange"
        :on-remove="handleRemove"
        :on-success="handleSuccess"
        :on-error="handleError"
        ::limit="1"
        :show-file-list="false"
        :auto-upload="false"
      >
        <el-button size="small" type="primary">选择文件</el-button>
        <el-button
          v-loading.fullscreen.lock="fullscreenLoading"
          style="margin-left: 10px;"
          type="success"
          size="small"
          element-loading-text="loading"
          @click="submitUpload"
        >上传</el-button>
        <div class="el-upload__tip">文件类型: zip</div>
      </el-upload>
    </el-row>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="ID" align="center">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="文件名称" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="文件类型" align="center">
        <template slot-scope="scope">
          {{ scope.row.suffix }}
        </template>
      </el-table-column>
      <el-table-column label="文件大小" align="center">
        <template slot-scope="scope">
          {{ scope.row.size }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="extract(scope.row)">提取特征</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getStorageList, del, extract } from '@/api/localStorage'

export default {
  data() {
    return {
      fullscreenLoading: false,
      list: null,
      listLoading: true
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    upload() {
      return `${process.env.VUE_APP_BASE_API}/api/localStorage/file`
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
    handleSuccess() {
      this.fullscreenLoading = false
      this.fetchData()
    },
    handleError(file) {
      this.fullscreenLoading = false
    },
    fetchData() {
      this.listLoading = true
      getStorageList().then(response => {
        this.list = response.data
        this.listLoading = false
      }).catch(function(response) {
        console.log(response)
      })
    },
    handleDelete(row) {
      this.$confirm('该操作会删除选中文件及从它提取的所有图片特征，需要继续吗？', '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const id = row.id
        del(id).then(response => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.fetchData()
        })
      }).catch(() => {
        console.log('Cancel')
      })
    },
    extract(row) {
      const id = row.id
      extract(id).then(response => {
        this.$message({
          type: 'success',
          message: '开始提取特征!'
        })
      })
    }
  }
}
</script>
