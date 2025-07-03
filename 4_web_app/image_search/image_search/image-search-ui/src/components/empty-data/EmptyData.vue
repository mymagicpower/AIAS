<template>
  <div class="empty-data-wrapper" :style="{height: height}">
    <div class="empty-data-data">
      <div v-if="title" class="empty-data-tips">{{ title }}</div>
      <div class="empty-data-operate">
        <slot />
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'EmptyData',
  props: {
    imgUrl: { prop: String },
    title: { prop: String },
    height: { prop: String, default: 'calc( 100vh - 300px)' }
  },
  methods: {}
}
</script>

<style lang='scss' scoped>
  @import "~@/assets/styles/base";

  .empty-data-wrapper {
    @include flex-column-all-center;

    .empty-data-data {
      @include flex-column-all-center;
      width: 400px;

      .empty-data-tips {
        margin: 10px 0;
        font-size: 12px;
        font-weight: 400;
        color: rgba(131, 138, 145, 1);
      }

      .img {
        width: 114px;
        height: 95px;
      }
    }
  }
</style>
