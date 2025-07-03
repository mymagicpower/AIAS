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
        <el-table-column prop="imageId" label="id" />
        <el-table-column prop="uuid" label="图片uuid" />
        <el-table-column prop="preName" label="原图片名" />
<!--        <el-table-column prop="groupId" label="图片分组id" />-->
<!--        <el-table-column prop="detectObjs" label="检测目标json" />-->
        <el-table-column prop="imgUrl" label="图片相对路径" />
        <el-table-column prop="fullPath" label="fullPath" />
        <el-table-column prop="type" label="1: 本地url，0: 远程图片url" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="createBy" label="创建人" />
<!--        <el-table-column prop="updateTime" label="修改时间" />-->
<!--        <el-table-column prop="updateBy" label="修改人" />-->
        <el-table-column v-if="checkPer(['admin','imageInfo:edit','imageInfo:del'])" label="操作" width="150px" align="center">
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
import crudImageInfo from '@/api/imageInfo'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { id: null, uuid: null, preName: null, groupId: null, detectObjs: null, imgUrl: null, fullPath: null, type: null, createTime: null, createBy: null, lastUpdateTime: null, lastUpdateBy: null }
export default {
  name: 'ImageInfo',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: 'ImageInfoService', url: 'api/imageInfo', idField: 'imageId', sort: 'imageId,desc', crudMethod: { ...crudImageInfo }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'imageInfo:add'],
        edit: ['admin', 'imageInfo:edit'],
        del: ['admin', 'imageInfo:del']
      },
      rules: {
        uuid: [
          { required: true, message: '图片uuid不能为空', trigger: 'blur' }
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
