<template>
  <div>
    <el-row>
      <el-col :xs="12" :sm="12" :lg="6" style="padding:16px 16px 0;margin-bottom:32px;width:70%;">
        <div id="lineChart" style="background:#fff;width:100%;height:500px;" />
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" style="padding:16px 16px 0;margin-bottom:32px;width:30%;">
        <el-card class="box-card">
          <div slot="header" class="header">
            <span>Training information</span>
            <div style="float:right;margin-top:10px;">
              <el-button
                :disabled="triggerDisabled"
                type="primary"
                size="medium"
                @click="doTrain"
              >Train
              </el-button>
              <el-button
                :disabled="displayDisabled"
                type="primary"
                size="medium"
                @click="doQuery"
              >Display
              </el-button>
            </div>
          </div>
          <div class="text item">
            Training on：{{ devices }}
          </div>
          <div class="text item">
            Epoch：{{ epoch }}
          </div>
          <!--          <div class="text item">-->
          <!--            Speed：{{ speed }} images/sec-->
          <!--          </div>-->
          <div class="text item">
            <!--            https://github.com/dreambo8563/easy-circular-progress-->
            <div class="text item">
              Training progress：
            </div>
            <Progress
              :transition-duration="0"
              :radius="45"
              :stroke-width="10"
              :value="trainingProgress"
            />
          </div>
          <div class="text item">
            <div class="text item">
              Validating progress：
            </div>
            <Progress
              :transition-duration="0"
              :radius="45"
              :stroke-width="10"
              :value="validatingProgress"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>

</template>

<script>
require('echarts/theme/macarons') // echarts theme
import Echarts from 'echarts'
import Progress from 'easy-circular-progress'
import { debounce } from '@/utils'
import EventBus from 'vertx3-eventbus-client'
import { train } from '@/api/training'

export default {
  components: {
    Progress
  },
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      triggerDisabled: false,
      displayDisabled: true,
      chart: null,
      sidebarElm: null,
      eb: null,
      devices: '',
      epoch: '',
      trainingProgress: 0,
      validatingProgress: 0,
      speed: 0
    }
  },
  mounted() {
    this.chart = Echarts.init(document.getElementById('lineChart'), 'macarons')

    const options = {
      vertxbus_reconnect_attempts_max: Infinity, // Max reconnect attempts
      vertxbus_reconnect_delay_min: 1000, // Initial delay (in ms) before first reconnect attempt
      vertxbus_reconnect_delay_max: 5000, // Max delay (in ms) between reconnect attempts
      vertxbus_reconnect_exponent: 2, // Exponential backoff factor
      vertxbus_randomization_factor: 0.5 // Randomization factor between 0 and 1
    }
    this.eb = new EventBus('http://localhost:8089/api/eventbus', options)
    this.eb.onopen = function() {
      console.log('Eventbus: open.')
    }
    this.eb.onerror = function() {
      console.error('Eventbus: error ocurred.')
    }
    this.eb.onclose = function() {
      console.error('Eventbus: closed.')
    }
    this.eb.onreconnect = function() {
      console.info('Eventbus: reconnecting...')
    }
    this.eb.enableReconnect(true)

    if (this.autoResize) {
      this.resizeHandler = debounce(() => {
        if (this.chart) {
          this.chart.resize()
        }
      }, 100)
      window.addEventListener('resize', this.resizeHandler)
    }

    // listening sidebar
    this.sidebarElm = document.getElementsByClassName('sidebar-container')[0]
    this.sidebarElm && this.sidebarElm.addEventListener('transitionend', this.sidebarResizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    if (this.autoResize) {
      window.removeEventListener('resize', this.resizeHandler)
    }

    this.sidebarElm && this.sidebarElm.removeEventListener('transitionend', this.sidebarResizeHandler)

    this.chart.dispose()
    this.chart = null
  },
  methods: {
    doQuery() {
      this.eb.registerHandler('trainer', this.event_listener)
    },
    doTrain() {
      this.trigger()
      const self = this
      setTimeout(function() {
        self.displayDisabled = false
      }, 3000)
    },
    async trigger() {
      this.triggerDisabled = true
      const id = 1
      await train(id).then(response => {
        this.$message({
          type: 'success',
          message: 'Training Complete!'
        })
      })
    },
    event_listener: function(error, message) {
      if (error) {
        console.log(error.stack)
      }
      console.info('Eventbus: trainer.')
      const data = JSON.parse(message.body)
      const accuracy = data.metrics.Accuracy
      const loss = data.metrics.SoftmaxCrossEntropyLoss
      this.devices = data.devices
      this.epoch = data.epoch
      this.trainingProgress = data.trainingProgress
      this.validatingProgress = data.validatingProgress
      this.speed = data.speed
      const xAxis = []
      const accuracyAxis = []
      const lossAxis = []
      accuracy.forEach((item) => {
        accuracyAxis.push(item.y.toString())
        xAxis.push(item.x.toString())
      })
      loss.forEach((item) => {
        lossAxis.push(item.y.toString())
      })
      this.setOptions({ xAxis, accuracyAxis, lossAxis })
    },
    sidebarResizeHandler(e) {
      if (e.propertyName === 'width') {
        this.resizeHandler()
      }
    },
    setOptions({ xAxis, accuracyAxis, lossAxis } = {}) {
      this.chart.setOption({
        title: {
          text: 'Training Performance',
          textStyle: {
            fontWeight: 'normal',
            color: '#666'
          }
        },
        xAxis: {
          data: xAxis,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 20,
          right: 20,
          bottom: 30,
          top: 50,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: true
          }
        },
        legend: {
          data: ['Accuracy', 'SoftmaxCrossEntropyLoss']
        },
        series: [{
          name: 'Accuracy', itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: accuracyAxis,
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: 'SoftmaxCrossEntropyLoss', itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: lossAxis,
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        }
        ]
      })
    }
  }
}
</script>

<style>
  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .header:before,
  .header:after {
    display: table;
    content: "";
  }
  .header:after {
    clear: both
  }

  .box-card {
    width: 300px;
  }
</style>
