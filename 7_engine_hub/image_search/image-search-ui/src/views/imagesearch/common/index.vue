<template>
  <div class="person-image-main">
    <common-image-table />
  </div>
</template>

<script>
import CommonImageTable from '@/views/imagesearch/common/component/common-image-table'

export default {
  name: 'CommonImage',
  components: { CommonImageTable },
  data() {
    return {

    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/assets/styles/base";

  .person-image-main {
    box-sizing: border-box;
    height: 100%;
    padding: 32px 56px;
    position: relative;
  }
</style>
