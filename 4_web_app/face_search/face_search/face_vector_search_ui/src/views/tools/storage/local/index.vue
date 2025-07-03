<template>
  <div class="app-container" style="padding: 8px;">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.blurry" clearable size="small" placeholder="输入内容模糊搜索" style="width: 200px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <date-range-picker v-model="query.createTime" class="date-item" />
        <rrOperation />
      </div>
      <crudOperation :permission="permission">
        <!-- 新增 -->
        <el-button
          slot="left"
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-upload"
          @click="crud.toAdd"
        >本地文件上传
        </el-button>
        <el-button
          slot="left"
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-folder-add"
          :loading="loading"
          @click="openFolder"
        >
          服务器端上传
        </el-button>
        <!-- 右侧添加一个提取通用图片特征按钮 -->
        <el-button
          slot="right"
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon"
          :disabled="isDisabled"
          @click="extractFeatures"
        >
          提取图片特征
        </el-button>
        <el-button
          slot="right"
          class="filter-item"
          size="mini"
          type="danger"
          icon="el-icon"
          :disabled="isDisabled"
          @click="deleteFeatures"
        >
          删除图片特征
        </el-button>
      </crudOperation>
      <uploader
        ref="uploader"
        :options="options"
        :file-status-text="statusText"
        :auto-start="false"
        class="uploader-example"
        @file-added="onFileAdded"
        @file-success="onFileSuccess"
        @file-error="onFileError"
        @file-removed="fileRemoved"
        @complete="complete"
      >
        <uploader-unsupport />
        <uploader-drop>
          <uploader-btn :directory="true" style="background-color: #79BBFF"><i class="el-icon-folder-add" style="margin-right: 5px" />本地文件夹上传</uploader-btn>
        </uploader-drop>
        <uploader-list />
      </uploader>
    </div>
    <!--表单组件-->
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.add ? '文件上传' : '编辑文件'" width="500px">
      <el-form ref="form" :model="form" size="small" label-width="80px">
        <el-form-item label="文件名">
          <el-input v-model="form.name" style="width: 370px;" />
        </el-form-item>
        <!--   上传文件   -->
        <el-form-item v-if="crud.status.add" label="上传">
          <el-upload
            ref="upload"
            :limit="1"
            :before-upload="beforeUpload"
            :auto-upload="false"
            :headers="headers"
            :on-success="handleSuccess"
            :on-error="handleError"
            :action="fileUploadApi + '?name=' + form.name"
          >
            <div class="eladmin-upload"><i class="el-icon-upload" /> 添加文件</div>
            <div slot="tip" class="el-upload__tip">请上传zip格式文件，且不超过1GB</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="crud.cancelCU">取消</el-button>
        <el-button v-if="crud.status.add" :loading="loading" type="primary" @click="upload">确认</el-button>
        <el-button v-else :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
      </div>
    </el-dialog>
    <!--表格渲染-->
    <el-table ref="table" v-loading="crud.loading" :data="crud.data" style="width: 100%;" @selection-change="rowSelect">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" width="60px" label="id" />
      <el-table-column prop="path" label="文件夹路径" />
      <el-table-column prop="size" width="100px" label="图片数量" />
      <el-table-column prop="status" width="180px" label="特征数量" />
      <el-table-column prop="time" width="100px" label="用时(分钟)" />
      <el-table-column prop="createTime" width="150px" label="创建日期" />
    </el-table>
    <!--分页组件-->
    <pagination />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getToken } from '@/utils/auth'
import crudFile, { serverAdd } from '@/api/tools/localStorage'
import { extractFeatures, deleteFeatures } from '@/api/tools/localStorage'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import pagination from '@crud/Pagination'
import DateRangePicker from '@/components/DateRangePicker'

const defaultForm = { id: null, name: '' }
export default {
  components: { pagination, crudOperation, rrOperation, DateRangePicker },
  cruds() {
    return CRUD({ title: '文件', url: 'api/localStorage', crudMethod: { ...crudFile }})
  },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  data() {
    return {
      isDisabled: true,
      delAllLoading: false,
      loading: false,
      headers: { 'Authorization': getToken() },
      permission: {
        edit: ['admin', 'storage:edit'],
        del: ['admin', 'storage:del']
      },
      options: {
        target: process.env.VUE_APP_BASE_API + '/api/imageInfo/uploadDir',
        maxChunkRetries: 2,
        testChunks: false,
        fileParameterName: 'imageFile',
        chunkSize: 1024 * 1024 * 1024,
        simultaneousUploads: 1,
        query: {
          type: '',
          uuid: '',
          create_time: ''
        },
        headers: {
          'Authorization': getToken()
        }
      },
      statusText: {
        success: '上传成功',
        error: '上传失败',
        uploading: '上传中',
        paused: '暂停中',
        waiting: '等待中'
      },
      attrs: {
        accept: []
      }
    }
  },
  computed: {
    ...mapGetters([
      'baseApi',
      'fileUploadApi'
    ])
  },
  created() {
    this.crud.optShow.add = false
    this.options.query.uuid = this.generateUUID()
  },
  methods: {
    rowSelect(val) {
      this.crud.selectionChangeHandler(val)
      if (this.crud.selections.length > 1 || this.crud.selections.length === 0) {
        this.isDisabled = true
      } else {
        this.isDisabled = false
      }
    },
    extractFeatures() {
      // 重置文件夹上传的uuid
      this.options.query.uuid = this.generateUUID()
      this.isDisabled = true
      this.$message({
        type: 'success',
        message: '开始提取特征，点击右侧刷新按钮，查看特征提取进度。'
      })
      let id = null
      const value = this.crud.selections
      value.forEach(function(data, index) {
        id = data.id
      })
      extractFeatures(id, 0).then(response => {
        this.$message({
          type: 'success',
          message: '提取特征成功!'
        })
        this.crud.toQuery()
      })
    },
    deleteFeatures() {
      this.$confirm('此操作将删除已提取的图片特征信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.isDisabled = true
        let id = null
        const value = this.crud.selections
        value.forEach(function(data, index) {
          id = data.id
        })
        deleteFeatures(id).then(response => {
          this.$message({
            type: 'success',
            message: '删除特征成功!'
          })
          this.crud.toQuery()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    openFolder() {
      this.loading = true
      this.$prompt('请输入服务器文件夹路径', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        this.$message({
          type: 'info',
          message: '已提交请求'
        })
        serverAdd(value).then(response => {
          this.loading = false
          this.$message({
            type: 'success',
            message: '成功添加文件!'
          })
          this.crud.toQuery()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        })
      })
    },
    // 上传文件
    upload() {
      this.$refs.upload.submit()
    },
    beforeUpload(file) {
      let isLt2M = true
      isLt2M = file.size / 1024 / 1024 < 1000
      if (!isLt2M) {
        this.loading = false
        this.$message.error('上传文件大小不能超过 1GB!')
      }
      this.form.name = file.name
      return isLt2M
    },
    handleSuccess(response, file, fileList) {
      this.crud.notify('上传成功', CRUD.NOTIFICATION_TYPE.SUCCESS)
      this.$refs.upload.clearFiles()
      this.crud.status.add = CRUD.STATUS.NORMAL
      this.crud.resetForm()
      this.crud.toQuery()
    },
    // 监听上传失败
    handleError(e, file, fileList) {
      console.log('handleError')
    },
    // 添加文件到列表还未上传,每添加一个文件，就会调用一次
    onFileAdded(file) {
      console.log('onFileAdded')
      // file.pause()
    },

    // 每个文件传输给后端之后，返回的信息
    onFileSuccess(rootFile, file, response, chunk) {
      // this.crud.toQuery()
      console.log(rootFile.name)
      // const res = JSON.parse(response)
      // if (res.code !== 10000) {
      // }
    },
    // 上传错误触发，文件还未传输到后端
    onFileError(rootFile, file, response, chunk) {
      console.log(rootFile.name)
    },
    // 移除文件
    fileRemoved(file) {
      console.log('fileRemoved')
    },
    // 所有上传文件结束
    complete() {
      console.log('complete', arguments)
      this.crud.toQuery()
    },
    generateUUID() {
      let d = new Date().getTime()
      if (window.performance && typeof window.performance.now === 'function') {
        d += performance.now() // use high-precision timer if available
      }
      const uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = (d + Math.random() * 16) % 16 | 0
        d = Math.floor(d / 16)
        return (c === 'x' ? r : (r & 0x3) | 0x8).toString(16)
      })
      return uuid
    }
  }
}
</script>

<style scoped>
 ::v-deep .el-image__error, .el-image__placeholder{
    background: none;
  }
 ::v-deep .el-image-viewer__wrapper{
    top: 55px;
  }

  .uploader-example {
    width: 90%;
    padding: 15px;
    margin: 0 auto 0;
    font-size: 12px;
    box-shadow: 0 0 10px rgba(0, 0, 0, .4);
  }
 .uploader-example .uploader-btn {
   margin-right: 8px;
   color: #ffffff;
   border: #ffffff;
 }
 /deep/ .uploader-example .uploader-list {
   max-height: 300px;
   overflow: auto;
   overflow-x: hidden;
   overflow-y: auto;
 .uploader-file[status="uploading"]> .uploader-file-info{
 > .uploader-file-status > span> i{
   visibility:hidden;
 }
 > .uploader-file-status > span> em{
   visibility:hidden;
 }
 }
 }
</style>
