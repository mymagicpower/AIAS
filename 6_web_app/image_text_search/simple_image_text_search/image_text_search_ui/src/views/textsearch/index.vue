<template>
  <div class="image-main">
    <image-table />
  </div>
</template>

<script>
import ImageTable from '@/views/textsearch/component/image-table'

export default {
  name: 'Audio',
  components: { ImageTable },
  data() {
    return {

    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/assets/styles/base";

  .image-main {
    box-sizing: border-box;
    height: 100%;
    padding: 32px 56px;
    position: relative;
  }
</style>
