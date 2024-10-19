<template>
  <div class="app-container">
    <el-form ref="form" :model="form" v-loading="fullscreenLoading">
      <el-row :gutter="20">
        <el-col :span="2"><div class="grid-content">
          <div class="sub-title">源语言</div>
        </div>
        </el-col>
        <el-col :span="5"><div class="grid-content">
          <el-select v-model="form.srcLangId" placeholder="请选择源语言">
            <el-option
              v-for="item in form.options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
        </el-col>
        <el-col :span="2"><div class="grid-content">
          <div class="sub-title">目标语言</div>
        </div>
        </el-col>
        <el-col :span="5"><div class="grid-content">
          <el-select v-model="form.targetLangId" placeholder="请选择目标语言">
            <el-option
              v-for="item in form.options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="2"><div class="grid-content">
          <div class="sub-title">输入文本</div>
        </div>
        </el-col>
        <el-col :span="12"><div class="grid-content">
          <el-input
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 30}"
            placeholder="请输入需要翻译的内容"
            v-model="form.text">
          </el-input>
        </div></el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="2"><div class="grid-content"></div></el-col>
        <el-col :span="1"><div class="grid-content">
          <el-button type="primary" size="medium" element-loading-text="拼命加载中" @click="onSubmit">翻译</el-button>
        </div></el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="2"><div class="grid-content">
          <div class="sub-title">翻译结果</div>
        </div>
        </el-col>
        <el-col :span="12"><div class="grid-content bg-purple">
          <el-input
            type="textarea"
            :autosize="{ minRows: 8, maxRows: 30}"
            placeholder=""
            v-model="form.result">
          </el-input>
        </div></el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import { translate } from '@/api/translate'

export default {
  name: 'InferenceDetail',
  components: {
  },
  data() {
    return {
      fullscreenLoading: false,
      form: {
        text: '智利北部的丘基卡马塔矿是世界上最大的露天矿之一，长约4公里，宽3公里，深1公里。',
        srcLangId: '256200',
        targetLangId: '256047',
        result: '',
        options: [{
          value: '256200',
          label: 'Chinese (Simplified)'
        }, {
          value: '256201',
          label: 'Chinese (Traditional)'
        }, {
          value: '256161',
          label: 'Spanish'
        }, {
          value: '256098',
          label: 'Korean'
        }, {
          value: '256079',
          label: 'Japanese'
        }, {
          value: '256057',
          label: 'French'
        }, {
          value: '256047',
          label: 'English'
        }, {
          value: '256042',
          label: 'German'
        }]
      }
    }
  },
  methods: {
    onSubmit() {
      this.fullscreenLoading = true
      translate(this.form).then(response => {
        this.fullscreenLoading = false
        this.form.result = response.data.result
      })
    }
  }
}
</script>

<style>
  .el-select .el-input {
    width: 230px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  .el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
</style>
