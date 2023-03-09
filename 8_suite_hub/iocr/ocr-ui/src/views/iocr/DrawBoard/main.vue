<template>
  <div class="container">
    <div ref="drawboard" class="drawboard">
      <div class="center">
        <div class="topBar">
          <topBar
            :current-status="currentStatus"
            @topBarEvent="topBarEvent"
          />
        </div>
        <div class="wrapper">
          <div class="tools">
            <tool @toolSelected="toolSelected" />
          </div>
          <div
            ref="view"
            v-loading="loading"
            class="view"
            element-loading-text="加载中..."
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
          >
            <canvas
              id="image"
              ref="image"
              class="canvas"
            >The browser does not support canvas
            </canvas>
            <canvas
              id="canvas"
              ref="canvas"
              class="canvas"
              @mousedown="canvasMousedown"
            >The browser does not support canvas
            </canvas>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  generateImage,
  windowToCanvas,
  canvasToImage,
  imageToCanvas,
  formatPointRange,
  fullScreen,
  exitScreen
} from './utils'
import { status } from './draw'
import figureFactory from './draw/figureFactory.js'
import topBar from './components/topBar'
import tool from './components/tool'
import imageEvent from './draw/imageEvent.js'

export default {
  name: 'Drawboard',
  components: {
    topBar,
    tool
  },
  props: {
    url: {
      type: String,
      required: true
    },
    labelDataOrigin: {
      type: Array,
      default: () => []
    },
    loadingData: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      count: 0,
      imagePosX: 0,
      imagePosY: 0,
      imageXOffset: 0,
      imageYOffset: 0,
      imageWidth: 0,
      imageHeight: 0,
      imageScale: 0,
      scale: 1,
      viewHeight: 0,
      viewWidth: 0,
      canvas: null,
      image: null,
      drawboard: null,
      view: null,
      mouseStartPoint: null,
      mouseEndPoint: null,
      lastMouseEndPoint: null,
      currentPoint: null,
      currentTool: '',
      graphics: [],
      resultData: [],
      activeGraphic: null,
      activeIndex: -1,
      pointIndex: -1,
      options: {},
      currentStatus: status.DEFAULT, // DRAWING/MOVING/UPDATING
      observer: null,
      isFullScreen: false,
      loading: false,
      imagePixelData: []
    }
  },
  computed: {
    convertParams() {
      return {
        imagePosX: this.imagePosX,
        imagePosY: this.imagePosY,
        viewWidth: this.viewWidth,
        viewHeight: this.viewHeight,
        imageXOffset: this.imageXOffset,
        imageYOffset: this.imageYOffset,
        imageScale: this.imageScale,
        scale: this.scale
      }
    }
  },
  watch: {
    // graphics: {
    //   handler() {
    //     debounce(this.sendResultData, 10)()
    //   },
    //   deep: true,
    //   immediate: true
    // },
    url: {
      handler() {
        if (this.url) {
          this.loading = true
          this.loadImage(this.url)
        }
      },
      immediate: true
    },
    activeIndex: {
      handler(val) {
        // if (this.currentStatus === 'UPDATING') {
        this.$emit('activeIndexChange', val)
        // }
      },
      immediate: true
    },
    labelDataOrigin: {
      handler(newData) {
        if (newData.length > 0) {
          this.initRenderData(newData)
        }
      },
      immediate: true,
      deep: true
    },
    loadingData: {
      handler() {
        this.loading = this.loadingData
      },
      immediate: true
    }
  },
  mounted() {
    this.initSize()
    this.observerView()
    // this.canvas.addEventListener("mousemove", this.drawNavigationLineEvent, false);
    // this.listenScroll()
    this.addRightMouseEvent()
    const _this = this// 赋值vue的this
    window.onresize = () => {
      return (() => {
        _this.refresh()
      })()
    }
  },
  beforeDestroy() {
    this.canvas.removeEventListener('mousemove', this.canvasMousemove, false)
    this.canvas.removeEventListener('mouseup', this.canvasMouseup, false)
    document.removeEventListener('keydown', this.keydownEvent, false)
    this.observer.disconnect()
    // 由于window.onresize是全局事件，在其他页面改变界面时也会执行，这样可能会出现问题，需要在出这个界面时注销window.onresize事件
    window.onresize = null
  },
  methods: {
    // listenScroll() {
    //   const w = this;
    //   (document.onkeydown = function(e) {
    //     if (e.keyCode === 17) w.ctrlDown = true
    //   }),
    //   (document.onkeyup = function(e) {
    //     if (e.keyCode === 17) w.ctrlDown = false
    //   }),
    //   document.getElementsByClassName('view')[0].addEventListener('mousewheel', (e) => {
    //     e.preventDefault()
    //     if (w.ctrlDown) {
    //       if (e.wheelDeltaY > 0) { // 放大
    //         this.topBarEvent('zoomIn')
    //       } else { // 缩小
    //         this.topBarEvent('zoomOut')
    //       }
    //     }
    //   }, false)
    // },
    addRightMouseEvent() {
      const view = document.getElementsByClassName('view')[0]
      // Prohibit the right mouse button menu display
      view.oncontextmenu = function() {
        return false
      }
      view.addEventListener(
        'mousedown',
        e => {
          if (e.button === 2) {
            this.currentStatus = status.MOVING
          }
        },
        false
      )
      view.addEventListener(
        'mouseup',
        e => {
          if (e.button === 2) {
            this.currentStatus = status.DRAWING
          }
        },
        false
      )
    },
    initSize() {
      this.canvas = this.$refs.canvas
      this.image = this.$refs.image
      this.canvasCtx = this.canvas.getContext('2d')
      this.imageCtx = this.image.getContext('2d')
      this.drawboard = this.$refs.drawboard
      this.view = this.$refs.view
      this.viewHeight = this.view.offsetHeight
      this.viewWidth = this.view.offsetWidth
      this.image.setAttribute('height', this.viewHeight)
      this.image.setAttribute('width', this.viewWidth)
      this.canvas.setAttribute('height', this.viewHeight)
      this.canvas.setAttribute('width', this.viewWidth)
      if (this.url) {
        this.loadImage(this.url)
      }
      if (this.graphics.length > 0) {
        if (this.canvasCtx) {
          this.drawBG()
          this.drawGraphics()
          this.readyForNewEvent('draw')
        }
      }
    },
    observerView() {
      this.observer = new ResizeObserver(this.initSize)
      this.observer.observe(this.view)

      this.sidebarObserver = new ResizeObserver(this.refresh)
      this.sidebarObserver.observe(this.view)
    },
    sendResultData() {
      // console.log(this.activeIndex)
      this.resultData = []
      let index = 0
      this.graphics.forEach(figure => {
        const tmpFigure = {}
        tmpFigure.index = index++
        tmpFigure.type = figure.type
        tmpFigure.field = figure.field
        if (this.activeIndex === tmpFigure.index) {
          tmpFigure.active = 1
        } else {
          tmpFigure.active = 0
        }
        // tmpFigure.value = index
        tmpFigure.points = []
        for (let i = 0; i < figure.points.length; i++) {
          const tempPoint = canvasToImage(
            figure.points[i].x,
            figure.points[i].y,
            this.imagePosX,
            this.imagePosY,
            this.viewWidth,
            this.viewHeight,
            this.imageXOffset,
            this.imageYOffset,
            this.imageScale,
            this.scale
          )
          tmpFigure.points[i] = { x: Math.round(tempPoint.x), y: Math.round(tempPoint.y) }
        }
        this.resultData.push(tmpFigure)
      })
      this.$emit('updateData', this.resultData)
    },
    getImageInfo(x, y, width, height, scale) {
      this.imagePosX = Math.round(x)
      this.imagePosY = Math.round(y)
      this.imageWidth = width
      this.imageHeight = height
      this.imageScale = scale
      this.loading = false
      this.imagePixelData = this.imageCtx.getImageData(this.imagePosX, this.imagePosY, this.imageWidth * this.imageScale, this.imageHeight * this.imageScale)
      if (this.labelDataOrigin.length > 0) {
        this.initRenderData(this.labelDataOrigin)
      }
      // this.readyForNewEvent('draw')
    },
    loadImage(url) {
      if (this.image) {
        generateImage(this.image, this.getImageInfo, url)
      } else {
        this.$nextTick(() => generateImage(this.image, this.getImageInfo, url))
      }
      this.currentStatus = status.UPDATING
    },
    initRenderData(data) {
      this.graphics = []
      const initGraphics = JSON.parse(JSON.stringify(data))
      initGraphics.forEach((figure, index) => {
        const type = figure.type
        const tmpfigure = figureFactory(type, figure.points[0], figure.options || {})
        if (figure.type === 'rectangle') {
          tmpfigure.field = figure.field
        }

        tmpfigure.points = []
        figure.points.forEach((point, index) => {
          tmpfigure.points[index] = imageToCanvas(
            point.x,
            point.y,
            this.imagePosX,
            this.imagePosY,
            this.viewWidth,
            this.viewHeight,
            this.imageXOffset,
            this.imageYOffset,
            this.imageScale,
            this.scale
          )
        })
        this.graphics.push(tmpfigure)
      })
      this.drawBG()
      this.drawGraphics()
    },
    topBarEvent(eventName) {
      switch (eventName) {
        case 'zoomIn':
          this.scale = imageEvent.zoomIn(this.graphics, this.convertParams)
          this.drawBG()
          this.drawGraphics()
          this.updateImage()
          break
        case 'zoomOut':
          this.scale = imageEvent.zoomOut(this.graphics, this.convertParams)
          this.drawBG()
          this.drawGraphics()
          this.updateImage()
          break
        case 'move':
          this.readyForNewEvent('move')
          break
        case 'fullScreen':
          if (this.isFullScreen) {
            exitScreen(this.drawboard)
            this.isFullScreen = false
          } else {
            fullScreen(this.drawboard)
            this.isFullScreen = true
          }
          break
        default:
          break
      }
    },
    toolSelected(toolName) {
      this.currentTool = toolName
      this.readyForNewEvent('draw')
    },
    // Initialize the canvas
    drawBG() {
      this.canvasCtx.clearRect(0, 0, this.viewWidth, this.viewHeight)
    },
    // drawing
    drawGraphics() {
      this.graphics.forEach((graphic, index) => {
        // format point range when the point exceeds the image boundary
        graphic.points.forEach((point, index) => {
          graphic.points[index] = formatPointRange(
            point,
            this.imagePosX,
            this.imagePosY,
            this.imageWidth,
            this.imageHeight,
            this.viewWidth,
            this.viewHeight,
            this.imageXOffset,
            this.imageYOffset,
            this.imageScale,
            this.scale
          )
        })
        // computedCenter
        graphic.computedCenter()
        graphic.draw(this.canvasCtx)
        if (this.activeIndex === index && this.currentStatus === status.UPDATING) {
          graphic.drawPoints(this.canvasCtx)
        }
      })
    },
    // drawNavigationLineEvent(e){
    //   this.drawBG();
    //   this.drawGraphics();
    // },
    canvasMousedown(e) {
      if (this.currentStatus === status.DEFAULT) return
      this.mouseStartPoint = windowToCanvas(
        this.canvas,
        e.clientX,
        e.clientY
      )
      this.lastMouseEndPoint = this.mouseStartPoint
      this.canvas.addEventListener('mousemove', this.canvasMousemove, false)
      this.canvas.addEventListener('mouseup', this.canvasMouseup, false)
      document.addEventListener('keydown', this.keydownEvent, false)
      // Do not process other logic when right click
      if (e.button === 2) return
      if (this.currentStatus === status.DRAWING) {
        if (this.activeGraphic == null) {
          for (let i = 0; i < this.graphics.length; i++) {
            // updating
            if (
              this.graphics[i].isInPath(this.canvasCtx, this.mouseStartPoint) >
                -1
            ) {
              this.canvas.style.cursor = 'crosshair'
              this.activeGraphic = this.graphics[i]
              this.activeIndex = i
              this.currentStatus = status.UPDATING
              break
            }
          }
          if (this.currentStatus === status.DRAWING) {
            this.activeGraphic = figureFactory(
              this.currentTool,
              this.mouseStartPoint,
              this.options
            )
            this.activeIndex = this.graphics.length
            this.graphics.push(this.activeGraphic)
            this.canvas.style.cursor = 'crosshair'
          }
        }
      } else if (this.currentStatus === status.UPDATING) {
        let index = 0
        for (; index < this.graphics.length; index++) {
          // 选中控制点后拖拽修改图形
          if (
            this.graphics[index].isInPath(this.canvasCtx, this.mouseStartPoint) >
              -1
          ) {
            this.canvas.style.cursor = 'crosshair'
            this.activeGraphic = this.graphics[index]
            this.activeIndex = index
            this.currentStatus = status.UPDATING
            break
          }
        }
        if (index === this.graphics.length) {
          this.$message.error('请选中标注框进行编辑。如需添加标注框，请点击左侧标注按钮!')
        } else {
          this.pointIndex = this.activeGraphic.isInPath(this.canvasCtx, this.mouseStartPoint)
        }
      }
    },
    canvasMousemove(e) {
      this.mouseEndPoint = windowToCanvas(this.canvas, e.clientX, e.clientY)
      if (this.currentStatus === status.MOVING) {
        const translateX =
            this.imageXOffset + (this.mouseEndPoint.x - this.mouseStartPoint.x)
        const translateY =
            this.imageYOffset + (this.mouseEndPoint.y - this.mouseStartPoint.y)
        const tmpConvertParams = JSON.parse(JSON.stringify(this.convertParams))
        const tmpGraphics = imageEvent.formatPointsInImageWhenMove(this.graphics, tmpConvertParams)
        this.image.style.transform = `scale(${this.scale},${this.scale}) translate(${translateX}px,${translateY}px)`
        tmpConvertParams.imageXOffset = translateX
        tmpConvertParams.imageYOffset = translateY
        imageEvent.formatPointsInCanvasWhenMove(tmpGraphics, tmpConvertParams)
        this.drawBG()
        imageEvent.drawTmpGraphics(tmpGraphics, this.canvasCtx)
      } else if (this.currentStatus === status.UPDATING && this.activeGraphic) {
        this.drawBG()
        this.drawGraphics()
        if (this.pointIndex > -1) {
          if (this.pointIndex === 999) {
            this.activeGraphic.move(this.lastMouseEndPoint, this.mouseEndPoint)
            this.lastMouseEndPoint = this.mouseEndPoint
          } else {
            this.activeGraphic.update(this.pointIndex, this.mouseEndPoint)
          }
        }
      } else if (this.currentStatus === status.DRAWING && this.activeGraphic) {
        this.drawBG()
        this.drawGraphics()
        if (['anchor', 'rectangle'].includes(this.currentTool)) {
          this.activeGraphic.initFigure(this.mouseStartPoint, this.mouseEndPoint)
        }
      }
    },
    canvasMouseup(e) {
      if (this.currentStatus === status.MOVING) {
        imageEvent.formatPointsInImage(this.graphics, this.convertParams)
        this.imageXOffset += (this.mouseEndPoint.x - this.mouseStartPoint.x)
        this.imageYOffset += (this.mouseEndPoint.y - this.mouseStartPoint.y)
        imageEvent.formatPointsInCanvas(this.graphics, this.convertParams)
        this.drawBG()
        this.drawGraphics()
        this.updateImage()
        this.readyForNewEvent('move')
      } else if (this.currentStatus === status.UPDATING) {
        if (this.activeGraphic) {
          this.drawBG()
          this.drawGraphics()
          this.sendResultData()
        }
        this.readyForNewEvent('update')
      } else if (this.currentStatus === status.DRAWING) {
        if (this.activeGraphic) {
          this.drawBG()
          this.drawGraphics()
          this.sendResultData()
        }
        this.readyForNewEvent()
        this.drawEventDone()
      }
    },
    readyForNewEvent(event = 'draw') {
      this.canvas.style.cursor = 'crosshair'
      if (event === 'draw') {
        this.activeIndex = -1
        this.activeGraphic = null
        this.currentStatus = status.DRAWING
      } else if (event === 'move') {
        this.activeIndex = -1
        this.activeGraphic = null
        this.currentStatus = status.MOVING
        this.canvas.style.cursor = 'move'
      }
      this.canvas.removeEventListener('mousemove', this.canvasMousemove, false)
      this.canvas.removeEventListener('mouseup', this.canvasMouseup, false)
    },
    keydownEvent(e) {
      if (e.keyCode === 13) {
        this.readyForNewEvent('draw')
        this.drawBG()
        this.drawGraphics()
      } else if (e.keyCode === 46) {
        if (this.activeIndex > -1) {
          this.graphics.splice(this.activeIndex, 1)
          this.$emit('deleteFigureCb', this.activeIndex)
          this.readyForNewEvent('draw')
          this.drawBG()
          this.drawGraphics()
        }
      }
    },
    deleteFigure(index) {
      if (index > -1) {
        this.graphics.splice(index, 1)
        this.readyForNewEvent('draw')
        this.drawBG()
        this.drawGraphics()
      }
    },
    selectedFigure(index) {
      if (index > -1) {
        this.activeIndex = index
        this.currentStatus = status.UPDATING
        this.drawBG()
        this.drawGraphics()
      }
    },
    initGraph(data) {
      if (data.length > 0) {
        this.initRenderData(data)
        this.resultData = data
      }
    },
    updateImage() {
      this.image.style.transform = `scale(${this.scale},${this.scale}) translate(${this.imageXOffset}px,${this.imageYOffset}px)`
    },
    drawEventDone() {
      this.$emit('drawEventDone')
    },
    refresh() {
      this.initRenderData(this.resultData)
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "styles/index";
</style>
