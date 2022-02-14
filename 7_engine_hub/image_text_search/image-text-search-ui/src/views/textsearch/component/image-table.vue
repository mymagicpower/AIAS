<template>
  <div class="image-table">
    <div class="head">
      <div class="bts">
        <el-form ref="form" :model="form">
          <el-input v-model="form.text" placeholder="请输入内容" class="input-with-select">
            <el-select slot="prepend" v-model="form.topK" style="width:80px;" placeholder="请选择">
              <el-option label="Top 5" value="5" />
              <el-option label="Top 10" value="10" />
              <el-option label="Top 20" value="20" />
              <el-option label="Top 50" value="50" />
              <el-option label="Top 100" value="100" />
              <el-option label="Top 200" value="200" />
            </el-select>
            <el-button slot="append" icon="el-icon-search" element-loading-text="拼命加载中" @click="onSubmit" />
          </el-input>
        </el-form>
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
      title="请输入查询文本信息"
    />
    <empty-data v-else title="未查询到信息" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { search } from '@/api/search'
import EmptyData from '@/components/empty-data/EmptyData'
import tableMixin from '@/common/mixin/table-mixin'
import ImageCard from '@/views/textsearch/component/image-card'

export default {
  name: 'ImageTable',
  components: { ImageCard, EmptyData },
  mixins: [tableMixin],
  data() {
    return {
      query: {
      },
      fullscreenLoading: false,
      form: {
        topK: 5,
        text: '',
        result: ''
      },
      imgFile: '',
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
    }
  },
  async mounted() {
    this.setEmptyTable()
  },
  methods: {
    onSubmit() {
      this.fullscreenLoading = true
      this.page.pageNum = 1
      this.type = 1
      search(this.form).then(response => {
        this.fullscreenLoading = false
        if (response.success && response.data) {
          if (response.total) {
            const data = response.data
            data.forEach(a => {
              // a.score = (new Number(a.score) * 100).toFixed(0)
              a.score = (Number(a.score)).toFixed(2)
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
      })
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

.cards {
  @include flex-row;
  max-height: calc(100vh - 300px);
  overflow-y: auto;
  flex-wrap: wrap;
}

.image-table {
  position: relative;
  box-sizing: border-box;
  width: 800px;
  height: calc(100vh - 150px);

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

.head {
  margin: 10px 0;
  @include flex-row-between-center;

  .bts {
    @include flex-row-all-center;
  }
}

</style>
