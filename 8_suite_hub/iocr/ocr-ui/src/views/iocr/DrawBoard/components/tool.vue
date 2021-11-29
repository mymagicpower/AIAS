<template>
  <div class="tool">
    <el-tooltip effect="dark" content="锚点框" placement="right">
      <i class="el-icon-menu" @click="changeTool('anchor')" />
    </el-tooltip>
    <el-tooltip effect="dark" content="内容框" placement="right">
      <i class="el-icon-s-grid" @click="changeTool('rectangle')" />
    </el-tooltip>
  </div>
</template>

<script>
export default {
  name: 'Tool',
  data() {
    return {
    }
  },
  methods: {
    changeTool(eventName) {
      this.$emit('toolSelected', eventName)
    }
  }
}
</script>

<style lang="scss" scoped>
.tool {
  width: 30px;
  margin: 0px 5px;
}
[class*=" el-icon-"],
[class^="el-icon-"] {
  box-sizing: border-box;
  height: 30px;
  width: 30px;
  line-height: 30px;
  font-size: 20px;
  padding: 0 5px;
  border-bottom: 1px dashed rgba(0, 0, 0, 0.4);
  cursor: pointer;
  &:nth-child(1) {
    margin-top: 5px;
  }
  &:hover {
    border-radius: 3px;
    background-color: rgba(0, 0, 0, 0.4);
  }
}
</style>
