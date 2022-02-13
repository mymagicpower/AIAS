<template>
  <div class="wave-table">
    <div class="head">
      <div class="bts">
        TopK：<el-input v-model="topK" type="primary" style="margin:0 5px;width: 100px;" placeholder="输入TopK" />
        <el-upload
          ref="waveUploader"
          name="audio"
          :on-change="handleFileChange"
          :before-upload="handleUploadBefore"
          :on-success="handleSuccess"
          :on-error="removeField"
          drag
          :auto-upload="false"
          :action="upload()"
          :limit="0"
          :show-file-list="false"
        >
          <el-input
            style="margin-right: 15px;"
            placeholder="请上传wav格式音频文件"
          >
            <i slot="prefix" class="el-icon-search el-input__icon" />
          </el-input>
        </el-upload>
        <el-button
          ref="queryButton"
          style="margin-left: 10px;"
          icon="el-icon-search"
          class="filter-item"
          size="medium"
          type="primary"
          @click="doFaceQuery"
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
          :class="{'left': index%8 === 0, 'right': index%8 === 7}"
        />
      </div>
      <el-pagination
        style="text-align: center; position: absolute; bottom: 10px;width:100%"
        :current-page.sync="page.pageNum"
        :page-sizes="[16, 32, 64, 128]"
        :page-size.sync="page.pageSize"
        layout=" total, prev, pager,next, jumper, sizes "
        :total="page.total"
      />
    </template>
    <empty-data
      v-else-if="!imgFile || !imgFile.size"
      title="您还未上传音频文件，请拖入音频文件或点击上传"
    />
    <empty-data v-else title="未查询到信息" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import EmptyData from '@/components/empty-data/EmptyData'
import tableMixin from '@/common/mixin/table-mixin'
import ImageCard from '@/views/search/component/image-card'

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
        pageSize: 16,
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
    }
  },
  async mounted() {
    this.setEmptyTable()
  },
  methods: {
    upload() {
      return `${process.env.VUE_APP_BASE_API}/api/search/audio` + '?topK=' + this.topK
    },
    restQuery() {
      this.imgFile = {}
      this.tableData = []
    },
    doFaceQuery() {
      this.page.pageNum = 1
      this.type = 1
      this.$refs.waveUploader.submit()
    },
    handleSuccess(response, file, fileList) {
      if (response.success && response.data) {
        if (response.total) {
          const data = response.data
          data.forEach(a => {
            // a.score = (new Number(a.score) * 100).toFixed(0)
            a.score = (new Number(a.score)).toFixed(2)
          })
          this.tableData = data.sort((a, b) => {
            return a.score - b.score
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
        this.$message.warning('请上传小于2M大小的图片')
        this.$refs.waveUploader.clearFiles()
        return false
      }
      this.imgFile = file
      fileList[fileList.length - 1].status = 'ready'
    },
    handleUploadBefore(file) {
      if (file.size > 2097152) {
        this.$message.warning('请上传小于2M大小的图片')
        return false
      }
    },
    removeField() {
      this.$refs.waveUploader.clearFiles()
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
.wave-table {
  position: relative;
  top: 15px;
  box-sizing: border-box;
  height: calc(100vh - 100px);
}

.el-upload-dragger {
  border: none;
  border-radius: 0;
  box-sizing: border-box;
  width: 200px;
  height: 40px;
  left: 2px;
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
