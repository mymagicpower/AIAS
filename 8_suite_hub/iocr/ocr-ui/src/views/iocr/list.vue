<template>
  <div class="app-container">
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
          {{ scope.row.uid }}
        </template>
      </el-table-column>
      <el-table-column label="Template Name" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="Template Image" align="center">
        <template slot-scope="scope">
          <el-image
            style="width: 50px; height: 50px"
            :src="imgUrl(scope.row.imageName)"
          />
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="Operation"
        width="200"
      >
        <template slot="header">
          <router-link :to="{path: '/iocr/create/'}">
            <el-button size="mini" type="success">Add</el-button>
          </router-link>
        </template>
        <template slot-scope="scope">
          <router-link :to="{path: '/iocr/edit/', query: {atemplate: scope.row}}">
            <el-button size="mini" type="primary">Edit</el-button>
          </router-link>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.uid)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getTemplates, removeTemplate } from '@/api/template'

export default {
  data() {
    return {
      baseURL: `${process.env.VUE_APP_BASE_API}/file/images/`,
      list: null,
      listLoading: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    imgUrl(imageName) {
      return this.baseURL + imageName
    },
    fetchData() {
      this.listLoading = false
      getTemplates().then(response => {
        this.list = response.data.result
        this.listLoading = false
      }).catch(function(response) {
        console.log(response)
      })
    },
    handleDelete(row) {
      this.$confirm('This operation will delete the setting, continue?', 'Delete confirmation', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        removeTemplate(row).then(response => {
          this.$message({
            type: 'success',
            message: 'Deleted successfully!'
          })
          this.fetchData()
        })
      }).catch(() => {
        console.log('Cancelled successfully')
      })
    }
  }
}
</script>
