<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120">
      <el-form-item label="Epoch  ">
        <el-input v-model="form.epoch" size="small" style="width:240px"/>
      </el-form-item>
      <el-form-item label="BatchSize">
        <el-input v-model="form.batchSize" size="small" style="width:240px"/>
      </el-form-item>
      <el-form-item label="GPU Num">
        <el-input v-model="form.maxGpus" size="small" style="width:240px"/> （Maximum number of GPUs used）
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
        console.log(response)
      })
    },
    onSubmit() {
      if (isEmpty(this.form.epoch)) {
        this.$message({
          message: 'Iteration cycle is empty!',
          type: 'error'
        })
        return
      }
      if (isEmpty(this.form.batchSize)) {
        this.$message({
          message: 'Batch size is empty!'',
          type: 'error'
        })
        return
      }
      if (isEmpty(this.form.maxGpus)) {
        this.$message({
          message: 'Maximum number of GPUs is empty!'',
          type: 'error'
        })
        return
      }

      update(this.form).then(response => {
        this.$message({
          type: 'success',
          message: 'Update successful!'
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
