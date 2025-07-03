<template>
  <div class="group-card">
    <div class="card-wrapper">
      <!--大图-->
      <el-image-viewer
        v-if="showViewer"
        :on-close="closeViewer"
        :url-list="srcList"
      />
      <!--缩略图-->
      <el-image
        :src="data.thumbnailUrl"
        fit="contain"
        lazy
        class="card-img"
        @click="showImage(data.imgUrl)"
      >
        <div slot="error">
          <i class="el-icon-document" />
        </div>
      </el-image>
      <div class="score">相似度 {{ data.score }} %</div>
      <div class="footer">
        <div class="title">{{ data.id }}</div>
        <div class="date-time">{{ data.createTime }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'

export default {
  name: 'ImageCard',
  components: {
    ElImageViewer
  },
  props: {
    data: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      srcList: [],
      showViewer: false // 显示查看器
    }
  },
  methods: {
    // 查看图片
    showImage(path) {
      this.srcList = [path]
      this.showViewer = true
    },
    // 关闭查看器
    closeViewer() {
      this.showViewer = false
    }
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
