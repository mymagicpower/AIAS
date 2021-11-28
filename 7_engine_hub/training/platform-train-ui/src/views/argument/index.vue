<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120">
      <el-form-item label="迭代周期  ">
        <el-input v-model="form.epoch" size="small" style="width:240px"/>
      </el-form-item>
      <el-form-item label="批次大小">
        <el-input v-model="form.batchSize" size="small" style="width:240px"/>
      </el-form-item>
      <el-form-item label="GPU数量">
        <el-input v-model="form.maxGpus" size="small" style="width:240px"/> （使用GPU的数量上限）
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
        maxGpus: ''
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
        this.form.maxGpus = response.data.result.maxGpus
      }).catch(function(response) {
        console.log(response)// 发生错误时执行的代码
      })
    },
    onSubmit() {
      if (isEmpty(this.form.epoch)) {
        this.$message({
          message: '迭代周期为空!',
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
      if (isEmpty(this.form.maxGpus)) {
        this.$message({
          message: '最大GPU数量为空!',
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
