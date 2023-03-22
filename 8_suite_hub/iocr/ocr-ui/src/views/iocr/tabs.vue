<template>
  <el-tabs type="border-card">
    <el-tab-pane label="Anchor Point">
      <el-table
        ref="anchorTable"
        :highlight-current-row="true"
        :data="anchorDataAdd"
        style="width: 100%"
        @row-click="handleChange"
      >
        <el-table-column label="Please select the anchor point field">
          <template slot-scope="scope">
            <el-popconfirm
              confirm-button-text="OK"
              cancel-button-text="Cancel"
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete this element?"
              @onConfirm="deleteAnchorElement(scope.$index,scope.row)"
            >
              <i
                slot="reference"
                class="el-icon-circle-close"
                style="margin:0 10px 0 0"
              />
            </el-popconfirm>
            Reference Field: <el-input v-model="scope.row.value" type="text" size="small" style="width:240px" />
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <el-tab-pane label="Content Recognition Area">
      <el-table
        ref="contentTable"
        :highlight-current-row="true"
        :data="contentDataAdd"
        style="width: 100%"
        @row-click="handleChange"
      >
        <el-table-column label="Please select the content recognition area">
          <template slot-scope="scope">
            <el-popconfirm
              confirm-button-text="OK"
              cancel-button-text="Cancel"
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete this element?"
              @onConfirm="deleteContentElement(scope.$index,scope.row)"
            >
              <i
                slot="reference"
                class="el-icon-circle-close"
                style="margin:0 10px 0 0"
              />
            </el-popconfirm>
            Field Name: <el-input v-model="scope.row.field" type="text" size="small" style="width:100px" @change="fieldChange" />
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
