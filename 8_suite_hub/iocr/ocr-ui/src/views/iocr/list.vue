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
      <el-table-column label="模版名称" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="模版图片" align="center">
        <template slot-scope="scope">
          <el-image
            style="width: 50px; height: 50px"
            :src="imgUrl(scope.row.imageName)"
          />
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="200"
      >
        <template slot="header">
          <router-link :to="{path: '/iocr/create/'}">
            <el-button size="mini" type="success">新增</el-button>
          </router-link>
        </template>
        <template slot-scope="scope">
          <router-link :to="{path: '/iocr/edit/', query: {atemplate: scope.row}}">
            <el-button size="mini" type="primary">编辑</el-button>
          </router-link>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.uid)">删除</el-button>
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
        console.log(response)// 发生错误时执行的代码
      })
    },
    handleDelete(row) {
      this.$confirm('此操作将删除设置项，是否继续?', '删除确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeTemplate(row).then(response => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.fetchData()
        })
      }).catch(() => {
        console.log('取消成功')
      })
    }
  }
}
</script>
