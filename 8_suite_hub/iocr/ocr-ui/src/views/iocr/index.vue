<template>
  <div>
    <el-row type="flex" justify="end" :gutter="20">
      <el-col :span="6">
        <el-input v-model="name" placeholder="请输入模板名称">
          <template slot="prepend">模板名称</template>
        </el-input>
      </el-col>
      <el-col :span="4">
        <el-upload
          name="imageFile"
          class="upload"
          :action="upload()"
          :on-preview="handlePreview"
          :on-change="handleChange"
          :on-remove="handleRemove"
          :on-success="handleSuccess"
          :before-upload="beforeUpload"
          ::limit="1"
          :show-file-list="false"
        >
          <el-button
            slot="trigger"
            v-loading.fullscreen.lock="fullscreenLoading"
            type="primary"
            round
            element-loading-text="拼命加载中"
          >新建模版
          </el-button>
        </el-upload>
      </el-col>
      <el-col :span="10">
        <el-button
          type="primary"
          size="medium"
          :disabled="displayDisabled"
          round
          @click="onFetchAnchors"
        >载入标注
        </el-button>
        <el-button
          type="primary"
          size="medium"
          :disabled="displayDisabled"
          round
          @click="onSubmit"
        >提交标注
        </el-button>
      </el-col>
    </el-row>
    <div class="box">
      <div class="middle">
        <DrawBoard
          ref="myDrawBoard"
          :url="selectedUrl"
          style="width:100%;height:100%"
          @updateData="updateData"
        />
      </div>
      <div class="right">
        <Tabs
          ref="myFields"
          :label-data="labelData"
          @selectedFigure="selectedFigure"
          @deleteFigure="deleteFigure"
          @fieldChange="fieldChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import Tabs from './tabs'
import DrawBoard from './DrawBoard/main'
import { updateTemplate, getTemplate, getLabelData } from '@/api/template'

export default {
  name: 'App',
  components: {
    Tabs,
    DrawBoard
  },
  data() {
    return {
      baseURL: `${process.env.VUE_APP_BASE_API}`,
      fullscreenLoading: false,
      displayDisabled: true,
      selectedUrl: '',
      selectedIndex: 0,
      uid: '',
      name: '',
      imageName: '',
      labelData: [],
      imageUrl: ''
    }
  },
  methods: {
    upload() {
      // return `${process.env.VUE_APP_BASE_API}/ocr/addTemplate`
      return this.baseURL + '/template/addTemplate' + '?name=' + this.name
      // return this.imageSearchApi + '?topK=' + this.topK + '&type=1'
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
    handleSuccess(result) {
      this.uid = result.data.result.uid
      this.imageName = result.data.result.imageName
      this.selectedUrl = this.baseURL + '/file/images/' + this.imageName
      this.displayDisabled = false
    },
    beforeUpload(file) {
      const pass = file.type === 'image/jpg' || 'image/jpeg' || 'image/png'
      if (!pass) {
        this.$message.error('Image format should be JPG(JPEG) or PNG!')
        return pass
      }
      if (this.name === '') {
        this.$message.error('请输入模板名称!')
        return false
      }
      return pass
    },
    updateData(data) {
      // this.labelData = JSON.parse(JSON.stringify(data))
      // alert(JSON.stringify(data))
      // [{"index":0,"type":"anchor","value":1,"points":[{"x":223,"y":174},{"x":660,"y":174},{"x":660,"y":16},{"x":223,"y":16}]},
      // {"index":1,"type":"anchor","value":2,""points":[{"x":704,"y":240},{"x":701,"y":240},{"x":701,"y":242},{"x":704,"y":242}]}]
      data.forEach(figure => {
        if (figure.active === 1) {
          const that = this
          const singleLabel = {}
          singleLabel.uid = this.uid
          singleLabel.labelData = figure
          getLabelData(singleLabel).then(response => {
            const resultData = []
            data.forEach(element => {
              const tmpFigure = {}
              tmpFigure.index = element.index
              tmpFigure.type = element.type
              tmpFigure.active = element.active
              tmpFigure.points = element.points
              tmpFigure.field = element.field
              if (element.active === 1) {
                tmpFigure.value = response.data.result
              } else {
                that.labelData.forEach(item => {
                  if (item.index === element.index) {
                    tmpFigure.value = item.value
                    return
                  }
                })
              }
              resultData.push(tmpFigure)
            })
            this.labelData = JSON.parse(JSON.stringify(resultData))
            // console.log(JSON.stringify(resultData))
          })
        }
      })
    },
    deleteFigure(index) {
      this.$refs.myDrawBoard.deleteFigure(index)
      this.labelData.splice(index, 1)
      let newIndex = 0
      this.labelData.forEach(element => {
        element.index = newIndex++
      })
    },
    selectedFigure(index) {
      // alert(JSON.stringify(this.labelData))
      this.$refs.myDrawBoard.selectedFigure(index)
    },
    fieldChange(index, value) {
      console.log('index: ' + index)
      // const index = params[0]
      // const value = params[1]
      console.log('value: ' + value)
      const resultData = []
      this.labelData.forEach(element => {
        const tmpFigure = {}
        tmpFigure.index = element.index
        tmpFigure.type = element.type
        tmpFigure.active = element.active
        tmpFigure.points = element.points
        tmpFigure.value = element.value
        if (element.index === index) {
          tmpFigure.field = value
        } else {
          tmpFigure.field = element.field
        }
        resultData.push(tmpFigure)
      })
      this.labelData = JSON.parse(JSON.stringify(resultData))
    },
    onFetchAnchors() {
      getTemplate(this.uid).then(response => {
        this.labelData = JSON.parse(JSON.stringify(response.data.result.labelData))
        this.$refs.myDrawBoard.initGraph(this.labelData)
      })
    },
    onSubmit() {
      let pass = true
      this.labelData.forEach(element => {
        if (element.type === 'rectangle') {
          if (typeof element.field === 'undefined' || element.field === '') {
            pass = false
            this.$message.error('内容识别字段名称不能为空!')
          }
        }
      })

      if (this.labelData.length > 1 && pass) {
        for (let i = 0; i < this.labelData.length - 1; i++) {
          for (let j = i + 1; j < this.labelData.length; j++) {
            const element1 = this.labelData[i]
            const element2 = this.labelData[j]
            if (element1.type === 'rectangle' && element2.type === 'rectangle') {
              if (element1.field === element2.field) {
                pass = false
                this.$message.error('内容识别字段名称不能重名!')
              }
            }
          }
        }
      }

      if (this.name === '') {
        pass = false
        this.$message.error('请输入模板名称!')
      }

      if (pass) {
        const template = {}
        template.uid = this.uid
        template.name = encodeURI(this.name)
        template.imageName = encodeURI(this.imageName)
        template.labelData = this.labelData
        updateTemplate(template).then(response => {
          this.$notify({
            title: '成功',
            message: response.data.result,
            type: 'success'
          })
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .box {
    height: 610px;
    display: flex;
    flex-direction: row;
    width: 100%;
    color: #bfcbd9;
  }

  .middle {
    flex: 1 0 auto;
    display: flex;
    flex-direction: column;
  }

  .right {
    background-color: #F2F6FC;
    flex: 0 0 300px;
    border-left: 1px solid #DCDFE6;
    width: 300px;
    overflow: auto;
  }

  .selected {
    border: 2px solid yellow;
  }

  .el-row {
    margin-top: 5px;
    margin-bottom: 5px;

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
