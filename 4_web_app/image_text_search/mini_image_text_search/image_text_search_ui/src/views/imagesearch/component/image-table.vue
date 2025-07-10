<template>
  <div class="person-image-list">
    <div class="head">
      <div class="bts">
        TopK：<el-input v-model="topK" type="primary" style="margin:0 5px;width: 100px;" placeholder="TopK" />
        <el-upload
          ref="imageUploader"
          name="image"
          :on-change="handleFileChange"
          :before-upload="handleUploadBefore"
          :on-success="handleSuccess"
          :on-error="removeField"
          drag
          :auto-upload="false"
          :action="upload()"
          class="avatar-uploader"
          :limit="0"
          list-type="picture"
          accept="image/jpeg, image/jpg, image/png"
          :show-file-list="false"
        >
          <el-input
            style="margin-right: 5px;"
            placeholder="Please upload an image or drag in an image."
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
          @click="doFaceQuery"
        >Query</el-button>
        <el-button size="medium" type="primary" @click="restQuery">Reset</el-button>
      </div>
    </div>

    <template v-if="page.total !== 0 ">
      <div class="cards">
        <image-card
          v-for="(data, index) of pageData"
          :key="index"
          :data="data"
          :class="{'left': index%5 === 0, 'right': index%5 === 4}"
        />
      </div>
      <el-pagination
        style="text-align: center; position: absolute; bottom: 10px;width:100%"
        :current-page.sync="page.pageNum"
        :page-sizes="[20, 40, 80, 160]"
        :page-size.sync="page.pageSize"
        layout=" total, prev, pager,next, jumper, sizes "
        :total="page.total"
      />
    </template>
    <empty-data
      v-else-if="!imgFile || !imgFile.size"
      title="You have not uploaded an image yet. Please drag an image or click to upload."
    />
    <empty-data v-else title="No information found" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import EmptyData from '@/components/empty-data/EmptyData'
import tableMixin from '@/common/mixin/table-mixin'
import ImageCard from '@/views/imagesearch/component/image-card'

export default {
  name: 'PersonImageTable',
  components: { ImageCard, EmptyData },
  mixins: [tableMixin],
  data() {
    return {
      query: {
      },
      imgFile: '',
      topK: '5',
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
  async mounted() {
    this.setEmptyTable()
  },
  methods: {
    upload() {
      // return this.imageSearchApi + '?topK=' + this.topK + '&type=1'
      return `${process.env.VUE_APP_BASE_API}/api/search/image` + '?topK=' + this.topK
    },
    restQuery() {
      this.imgFile = {}
      this.tableData = []
    },
    doFaceQuery() {
      this.page.pageNum = 1
      this.type = 1
      this.$refs.imageUploader.submit()
    },
    handleSuccess(response, file, fileList) {
      if (response.success && response.data) {
        if (response.total) {
          const data = response.data
          data.forEach(a => {
            // a.score = (new Number(a.score) * 100).toFixed(0)
            a.score = (Number(a.score)).toFixed(2)
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
      if (fileList[0] && fileList[0].size > 2097152) {
        this.$message.warning('Please upload an image smaller than 2MB in size.')
        this.$refs.imageUploader.clearFiles()
        return false
      }
      this.imgFile = file
      fileList[fileList.length - 1].status = 'ready'
    },
    handleUploadBefore(file) {
      if (file.size > 2097152) {
        this.$message.warning('Please upload an image smaller than 2MB in size.')
        return false
      }
    },
    removeField() {
      this.$refs.imageUploader.clearFiles()
    }
  }
}
</script>

<style lang='scss'>
  @import "~@/assets/styles/base";

.left {
  margin-left: 0 !important;
}

.right {
  margin-right: 0 !important;
}

.cards {
  @include flex-row;
  max-height: calc(100vh - 300px);
  overflow-y: auto;
  flex-wrap: wrap;
}

.el-image {
  display: inline-block;
  text-align: center;
  background: #ccc;
  color: #fff;
  white-space: nowrap;
  position: relative;
  overflow: hidden;
  vertical-align: middle;
  width: 32px;
  height: 32px;
  line-height: 32px;
  border-radius: 16px;
}

.person-image-list {
  position: relative;
  top: 15px;
  box-sizing: border-box;
  width: 800px;
  height: calc(100vh - 150px);
}

.el-upload-dragger {
    border: none;
    border-radius: 0;
    box-sizing: border-box;
    width: 200px;
    height: 40px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}
.el-radio-button__inner{
  color: rgba(14, 37, 60, 1);
  font-weight: 400;
  .iconfont {
    color: rgba(14, 37, 60, 1);
    font-weight: 400;
  }
}
.el-upload{
  border: none;
  display: inherit;
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

</style>
