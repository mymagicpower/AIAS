<template>
  <div class="person-image-table">
    <div class="head">
      <div class="bts">
        TopK：<el-input v-model="topK" type="primary" style="margin:0 5px;width: 100px;" placeholder="输入TopK" />
        <el-upload
          ref="avatarUploader"
          :on-change="handleFileChange"
          :before-upload="handleUploadBefore"
          :on-success="handleSuccess"
          :on-error="removeField"
          drag
          :auto-upload="false"
          :headers="headers"
          :action="upload()"
          class="avatar-uploader"
          :limit="0"
          list-type="picture"
          accept="image/jpeg, image/jpg, image/png"
          :show-file-list="false"
        >
          <el-input
            style="margin-right: 5px;"
            placeholder="请上传图片或拖入图片"
          >
            <i slot="prefix" class="el-icon-search el-input__icon" />
          </el-input>
        </el-upload>
        <div class="img-upload">
          <el-image
            v-if="imgUrl"
            :src="imgUrl"
            :preview-src-list="[imgUrl]"
            fit="contain"
            lazy
            class="el-avatar"
          />
          <div v-else style=" width:0px;border: none" />
        </div>
        <el-button
          ref="queryButton"
          icon="el-icon-search"
          class="filter-item"
          size="medium"
          type="primary"
          :disabled="!imgUrl"
          @click="doCommonQuery"
        >查询</el-button>
        <el-button size="medium" type="primary" @click="restQuery">重置</el-button>
      </div>
    </div>

    <template v-if="page.total !== 0 ">
      <div class="cards">
        <image-card
          v-for="(data, index) of pageData"
          :key="index"
          :data="data"
        />
      </div>
      <el-pagination
        style="text-align: center; position: absolute; bottom: 10px;width:100%"
        :current-page.sync="page.pageNum"
        :page-sizes="[20, 40, 80, 100]"
        :page-size.sync="page.pageSize"
        layout=" total, prev, pager,next, jumper, sizes "
        :total="page.total"
      />
    </template>
    <empty-data
      v-else-if="!imgFile || !imgFile.size"
      title="您还未上传图片，请拖入图片或点击上传"
    />
    <empty-data v-else title="请点击查询按钮" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getToken } from '@/utils/auth'
import EmptyData from '@/components/empty-data/EmptyData'
import ImageCard from '@/views/imagesearch/common/component/image-card'

export default {
  name: 'CommonImageTable',
  components: { ImageCard, EmptyData },
  data() {
    return {
      headers: { 'Authorization': getToken() },
      query: {
      },
      imgFile: '',
      topK: '50',
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 20,
        total: 0
      }
    }
  },
  computed: {
    ...mapGetters([
      'baseApi',
      'imageSearchApi'
    ]),
    emptyParam() {
      return !this.imgFile && (!this.topK || !this.topK.length)
    },
    pageData() {
      return this.tableData.slice(
        (this.page.pageNum - 1) * this.page.pageSize,
        this.page.pageNum * this.page.pageSize
      )
    },
    imgUrl() {
      return this.imgFile ? this.imgFile.url : ''
    }
  },
  // watch: {
  //   query: {
  //     handler(oldValue, newValue) {
  //       this.imgFile = ''
  //     },
  //     deep: true
  //   }
  // },
  async mounted() {
    this.setEmptyTable()
  },
  methods: {
    upload() {
      return this.imageSearchApi + '?topK=' + this.topK + '&type=0'
    },
    restQuery() {
      this.imgFile = {}
      this.tableData = []
    },
    doCommonQuery() {
      this.page.pageNum = 1
      this.$refs.avatarUploader.submit()
    },
    handleSuccess(response, file, fileList) {
      if (response.success && response.data) {
        if (response.total) {
          const data = response.data
          data.forEach(a => {
            a.score = ((1 / (1 + Number(a.score))) * 100).toFixed(0)
            // a.score = (Number(a.score)).toFixed(2)
          })
          this.tableData = data.sort((a, b) => {
            return b.score - a.score
          })
          this.page.total = response.total
        } else {
          this.tableData = []
          this.page.total = 0
        }
      } else {
        this.tableData = []
        this.page.total = 0
      }
      fileList[fileList.length - 1].status = 'ready'
    },
    handleFileChange(file, fileList) {
      if (fileList[0] && fileList[0].size > 5242880) {
        this.$message.warning('请上传小于5M大小的图片')
        this.$refs.avatarUploader.clearFiles()
        return false
      }
      this.imgFile = file
      fileList[fileList.length - 1].status = 'ready'
    },
    handleUploadBefore(file) {
      if (file.size > 5242880) {
        this.$message.warning('请上传小于5M大小的图片')
        return false
      }
    },
    removeField() {
      this.$refs.avatarUploader.clearFiles()
    }
  }
}
</script>

<style lang='scss' scoped>
  @import "~@/assets/styles/base";

.left {
  margin-left: 0 !important;
}

.right {
  margin-right: 0 !important;
}

.person-image-table {
  position: relative;
  box-sizing: border-box;
  height: calc(100vh - 200px);

  :global(.el-upload-dragger) {
    border: none;
    border-radius: 0;
    box-sizing: border-box;
    width: 170px;
    height: 36px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  :global(.el-radio-button__inner) {
    color: rgba(14, 37, 60, 1);
    font-weight: 400;

    .iconfont {
      color: rgba(14, 37, 60, 1);
      font-weight: 400;
    }
  }

  :global(.el-upload) {
    border: none;
    display: inherit;
  }
}

.img-upload {
  @include all-height(36px);
  @include flex-row-between-center;
  width: 36px;
  margin: 0 0 0 8px;
  img {
    width: 36px;
    height: 36px;
  }
}

.head {
  margin: 10px 0;
  @include flex-row-between-center;

  .bts {
    @include flex-row-all-center;
  }
}

.cards {
  @include flex-row;
  max-height: calc(100vh - 300px);
  overflow-y: auto;
  flex-wrap: wrap;
}
</style>
