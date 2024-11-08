<template>
  <el-tabs type="border-card">
    <el-tab-pane label="抠图">
      <el-table
        ref="contentTable"
        :highlight-current-row="true"
        :data="contentDataAdd"
        style="width: 100%"
        @row-click="handleChange"
      >
        <el-table-column label="请选择目标区域">
          <template slot-scope="scope">
            <el-popconfirm
              confirm-button-text="确认"
              cancel-button-text="取消"
              icon="el-icon-info"
              icon-color="red"
              title="确定要删除吗?"
              @onConfirm="deleteContentElement(scope.$index,scope.row)"
            >
              <i
                slot="reference"
                class="el-icon-circle-close"
                style="margin:0 10px 0 0"
              />
            </el-popconfirm>
            <div>
              <img :src="scope.row.value" width="200px" class="avatar">
              <el-button type="success" plain @click="download(scope.row.value)">下载</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
  </el-tabs>
</template>

<script>

export default {
  name: 'Tabs',
  props: {
    labelData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      activeIndexInList: -1
    }
  },
  computed: {
    contentDataAdd() {
      const newArray = []
      this.labelData.forEach((element, i) => {
        const newElement = JSON.parse(JSON.stringify(element))
        if (element.type === 'rectangle') {
          newArray.push(newElement)
        }
      })
      return newArray
    }
  },
  methods: {
    handleChange(row) {
      // alert(JSON.stringify(row))
      if (row.index === '') {
        this.activeIndexInList = -1
      } else {
        this.activeIndexInList = row.index
        this.$emit('selectedFigure', this.activeIndexInList)
      }
    },
    deleteContentElement(index, row) {
      // alert(JSON.stringify(row))
      this.contentDataAdd.splice(index, 1)
      this.$emit('deleteFigure', row.index)
    },
    download(img) {
      var image = new Image()
      image.setAttribute('crossOrigin', 'anonymous')
      var _this = this
      image.onload = function() {
        var canvas = document.createElement('canvas')
        canvas.width = image.width
        canvas.height = image.height
        var context = canvas.getContext('2d')
        context.drawImage(image, 0, 0, image.width, image.height)
        var url = canvas.toDataURL('image/png') // 得到图片的base64编码数据
        var a = document.createElement('a') // 生成一个a元素
        var event = new MouseEvent('click') // 创建一个单击事件
        a.download = _this.projectName || 'photo' // 设置图片名称
        a.href = url // 将生成的URL设置为a.href属性
        a.dispatchEvent(event) // 触发a的单击事件
      }
      image.src = img
    }
  }
}
</script>

<style scoped>
  .text{
    height: 41px;
    text-align: center;
    line-height: 41px;
  }
  .labelData{
    color: white;
  }
</style>
