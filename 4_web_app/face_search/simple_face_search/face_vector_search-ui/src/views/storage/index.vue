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
        <el-button slot="trigger" size="small" type="primary">Select</el-button>
        <el-button
          v-loading.fullscreen.lock="fullscreenLoading"
          style="margin-left: 10px;"
          type="success"
          size="small"
          element-loading-text="loading"
          @click="submitUpload"
        >Upload</el-button>
        <div slot="tip" class="el-upload__tip">File Type: zip</div>
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
      <el-table-column label="Name" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="Type" align="center">
        <template slot-scope="scope">
          {{ scope.row.suffix }}
        </template>
      </el-table-column>
      <el-table-column label="Size" align="center">
        <template slot-scope="scope">
          {{ scope.row.size }}
        </template>
      </el-table-column>
      <el-table-column label="Action" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="extract(scope.row)">Feature</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">Delete</el-button>
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
      this.$confirm('This operation will delete the setting, continue?', 'Deletion Confirmation', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        const id = row.id
        del(id).then(response => {
          this.$message({
            type: 'success',
            message: 'Deleted successfully!'
          })
          this.fetchData()
        })
      }).catch(() => {
        console.log('Canceled successfully!')
      })
    },
    extract(row) {
      const id = row.id
      this.$message({
        type: 'success',
        message: 'In process,please waiting...'
      })
      extract(id).then(response => {
        this.$message({
          type: 'success',
          message: 'successfully!'
        })
      })
    }
  }
}
</script>
