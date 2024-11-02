<template>
  <div class="content person-image-main">
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
  .content {
    max-width: 860px;
    margin: 0 auto;
    padding: 0rem 1rem;
    text-align: left;
  }

  .person-image-main {
    box-sizing: border-box;
    height: 100%;
    padding: 32px 56px;
    position: relative;
  }
</style>
