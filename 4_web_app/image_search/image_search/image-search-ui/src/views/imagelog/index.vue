<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px" />
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="id" />
        <el-table-column prop="uuid" label="主键ID" />
        <el-table-column prop="zipName" label="图片ZIP包名称" />
<!--        <el-table-column prop="imageList" label="上传图片清单" />-->
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="createBy" label="创建人" />
        <el-table-column v-if="checkPer(['admin','imageLog:edit','imageLog:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudImageLog from '@/api/imageLog'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { id: null, uuid: null, zipName: null, imageList: null, sts: null, createTime: null, createBy: null, lastUpdateTime: null, lastUpdateBy: null }
export default {
  name: 'ImageLog',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: 'ImageLogService', url: 'api/imageLog', idField: 'logId', sort: 'logId,desc', crudMethod: { ...crudImageLog }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'imageLog:add'],
        edit: ['admin', 'imageLog:edit'],
        del: ['admin', 'imageLog:del']
      },
      rules: {
        uuid: [
          { required: true, message: '主键ID不能为空', trigger: 'blur' }
        ],
        zipName: [
          { required: true, message: '图片ZIP包名称不能为空', trigger: 'blur' }
        ],
        imageList: [
          { required: true, message: '上传图片清单不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
        ],
        createBy: [
          { required: true, message: '创建人不能为空', trigger: 'blur' }
        ]
      }}
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
