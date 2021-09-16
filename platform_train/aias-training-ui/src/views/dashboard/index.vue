<template>
  <div class="dashboard-editor-container">
    <line-chart />
  </div>
</template>

<script>
import LineChart from './components/LineChart'

export default {
  name: 'DashboardAdmin',
  components: {
    LineChart
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}
</style>
