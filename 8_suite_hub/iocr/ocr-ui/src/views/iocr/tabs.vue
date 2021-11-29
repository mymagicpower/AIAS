<template>
  <el-tabs type="border-card">
    <el-tab-pane label="参照锚点">
      <el-table
        ref="anchorTable"
        :highlight-current-row="true"
        :data="anchorDataAdd"
        style="width: 100%"
        @row-click="handleChange"
      >
        <el-table-column label="请框选参照锚点字段">
          <template slot-scope="scope">
            <el-popconfirm
              confirm-button-text="确定"
              cancel-button-text="取消"
              icon="el-icon-info"
              icon-color="red"
              title="确定删除元素？"
              @onConfirm="deleteAnchorElement(scope.$index,scope.row)"
            >
              <i
                slot="reference"
                class="el-icon-circle-close"
                style="margin:0 10px 0 0"
              />
            </el-popconfirm>
            参照字段: <el-input v-model="scope.row.value" type="text" size="small" style="width:240px" />
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="内容识别区">
      <el-table
        ref="contentTable"
        :highlight-current-row="true"
        :data="contentDataAdd"
        style="width: 100%"
        @row-click="handleChange"
      >
        <el-table-column label="请框选内容识别区">
          <template slot-scope="scope">
            <el-popconfirm
              confirm-button-text="确定"
              cancel-button-text="取消"
              icon="el-icon-info"
              icon-color="red"
              title="确定删除元素？"
              @onConfirm="deleteContentElement(scope.$index,scope.row)"
            >
              <i
                slot="reference"
                class="el-icon-circle-close"
                style="margin:0 10px 0 0"
              />
            </el-popconfirm>
            字段名称: <el-input v-model="scope.row.field" type="text" size="small" style="width:100px" @change="fieldChange" />
            <div>
              {{ scope.row.value }}
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
    anchorDataAdd() {
      const newArray = []
      this.labelData.forEach((element, i) => {
        const newElement = JSON.parse(JSON.stringify(element))
        if (element.type === 'anchor') {
          newArray.push(newElement)
        }
      })
      return newArray
    },
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
    deleteAnchorElement(index, row) {
      // alert(JSON.stringify(row))
      this.anchorDataAdd.splice(index, 1)
      this.$emit('deleteFigure', row.index)
    },
    deleteContentElement(index, row) {
      // alert(JSON.stringify(row))
      this.contentDataAdd.splice(index, 1)
      this.$emit('deleteFigure', row.index)
    },
    fieldChange(value) {
      // console.log(value)
      this.$emit('fieldChange', this.activeIndexInList, value)
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
