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
        <div slot="tip" class="el-upload__tip">File type: zip</div>
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
      this.$confirm('This action will delete the setting, do you want to continue?', 'Confirm', {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'warning'
      }).then(() => {
        const id = row.id
        del(id).then(response => {
          this.$message({
            type: 'success',
            message: 'Scuccess!'
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
          message: 'Started, refresh the page to check the progress!'
        })
      })
    }
  }
}
</script>
