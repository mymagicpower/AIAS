<template>
  <el-tabs v-model="activeName" style="padding-left: 8px;">
    <el-tab-pane label="本地存储" name="first">
      <Local ref="local" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import Local from './local/index'
export default {
  name: 'Storage',
  components: { Local },
  data() {
    return {
      activeName: 'first'
    }
  },
  methods: {
  }
}
</script>

<style scoped>
</style>
