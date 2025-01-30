<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120">
      <el-form-item label="训练批次">
        <el-input v-model="form.epoch" size="small" style="width:380px"/>
      </el-form-item>
      <el-form-item label="批次大小">
        <el-input v-model="form.batchSize" size="small" style="width:380px"/>
      </el-form-item>
      <el-form-item label="分类数量">
        <el-input v-model="form.nClasses" size="small" style="width:380px"/>
      </el-form-item>
      <el-form-item label="分类标签">
        <el-input v-model="form.classLabels" size="small" style="width:380px" disabled/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getTrainArgument, update } from '@/api/trainArgument'
import { isEmpty } from '@/api/cryptojs'

export default {
  name: 'TrainArgument',
  data() {
    return {
      form: {
        epoch: '',
        batchSize: '',
        nClasses: ''
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      getTrainArgument().then(response => {
        this.form.epoch = response.data.result.epoch
        this.form.batchSize = response.data.result.batchSize
        this.form.nClasses = response.data.result.nClasses
        this.form.classLabels = response.data.result.classLabels
      }).catch(function(response) {
        console.log(response)
      })
    },
    onSubmit() {
      if (isEmpty(this.form.epoch)) {
        this.$message({
          message: '训练批次为空!',
          type: 'error'
        })
        return
      }
      if (isEmpty(this.form.batchSize)) {
        this.$message({
          message: '批次大小为空!',
          type: 'error'
        })
        return
      }
      if (isEmpty(this.form.nClasses)) {
        this.$message({
          message: '分类数量为空!',
          type: 'error'
        })
        return
      }

      update(this.form).then(response => {
        this.$message({
          type: 'success',
          message: '更新成功!'
        })
      })
    }
  }
}
</script>

<style scoped>
  .el-input .el-select {
    width: 120px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  .line{
    text-align: center;
  }
</style>
