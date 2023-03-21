<template>
  <div class="group-card">
    <div class="card-wrapper">
      <el-image
        :src="data.url | img"
        :preview-src-list="[data.url]"
        fit="contain"
        lazy
        class="card-img"
      >
        <div slot="error">
          <i class="el-icon-document" />
        </div>
      </el-image>

      <div class="score">Dis {{ data.score }}</div>
      <div class="footer">
        <div class="title">{{ data.id }}</div>
        <!--        <div class="date-time">{{ data.createTime }}</div>-->
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ImageCard',
  props: {
    data: {
      type: Object,
      required: true
    }
  },
  data() {
    return {}
  }
}
</script>

<style lang='scss' scoped>
@import "~@/assets/styles/base";

.card-wrapper {
  box-sizing: border-box;
  width: 120px;
  height: 180px;
  position: relative;

  .card-img {
    width: 120px;
    height: 120px;
  }

  .score {
    top: 120px;
    width: 120px;
    @include all-height(24px);
    position: absolute;
    background: #42b983;
    color: white;
    text-align: center;
    font-size: 14px;
    font-weight: 500;

  }

  .footer {
    margin-top: 24px;
    @include flex-column;
    font-size: 12px;
    .title {
      @include ellipsis(120px);
      @include all-height(18px);
    }

    .date-time {
      @include all-height(18px);
      @include ellipsis(120px);
    }
  }

}

.group-card {
  box-sizing: border-box;
  margin: 16px 12px;
}

</style>
