<template>
  <div>
    <el-row type="flex" justify="end" :gutter="20">
      <el-col :span="6">
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
            element-loading-text="loading"
          >上传图片
          </el-button>
        </el-upload>
      </el-col>
      <el-col :span="10">
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
        />
      </div>
    </div>
  </div>
</template>

<script>
import Tabs from './tabs'
import DrawBoard from './DrawBoard/main'
import { getLabelData } from '@/api/inference'

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
      imageName: '',
      labelData: [],
      imageUrl: ''
    }
  },
  methods: {
    upload() {
      // return `${process.env.VUE_APP_BASE_API}/infer/uploadImage`
      return this.baseURL + '/infer/uploadImage'
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
        this.$message.error('图片格式应为: jpg, jpeg, png 或者 bmp!')
        return pass
      }
      if (this.name === '') {
        this.$message.error('请输入模版名称!')
        return false
      }
      return pass
    },
    updateData(data) {
      // this.labelData = JSON.parse(JSON.stringify(data))
      // alert(JSON.stringify(data))
      // [{"index":0,"type":"anchor","value":1,"points":[{"x":223,"y":174},{"x":660,"y":174},{"x":660,"y":16},{"x":223,"y":16}]},
      // {"index":1,"type":"anchor","value":2,""points":[{"x":704,"y":240},{"x":701,"y":240},{"x":701,"y":242},{"x":704,"y":242}]}]
      this.fullscreenLoading = true
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
            this.fullscreenLoading = false
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
    flex: 0 0 450px;
    border-left: 1px solid #DCDFE6;
    width: 450px;
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
