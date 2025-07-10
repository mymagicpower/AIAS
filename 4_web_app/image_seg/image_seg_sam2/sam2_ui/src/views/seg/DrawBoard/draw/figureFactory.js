const config = {
  PATH_LINEWIDTH: 1,
  PATH_STROKESTYLE: '#409EFF',
  ANCHOR_LINEWIDTH: 1,
  ANCHOR_STROKESTYLE: '#f00',
  POINT_LINEWIDTH: 2,
  POINT_STROKESTYLE: '#999',
  POINT_RADIS: 5
}

class Graph {
  constructor(type, point, options = {}) {
    this.type = type
    this.x = Math.round(point.x)
    this.y = Math.round(point.y)
    this.points = []
    this.points.push(point)
    this.options = options
    this.path_lineWidth = options.path_lineWidth || config.PATH_LINEWIDTH
    this.path_strokeStyle = options.path_strokeStyle || config.PATH_STROKESTYLE
    this.anchor_lineWidth = options.anchor_lineWidth || config.ANCHOR_LINEWIDTH
    this.anchor_strokeStyle = options.anchor_strokeStyle || config.ANCHOR_STROKESTYLE
    this.point_radis = options.point_radis || config.POINT_RADIS
    this.point_lineWidth = options.point_lineWidth || config.POINT_LINEWIDTH
    this.point_strokeStyle = options.point_strokeStyle || config.POINT_STROKESTYLE
  }
  computedCenter() {
    let x_sum = 0; let y_sum = 0
    this.points.forEach(p => {
      x_sum += p.x
      y_sum += p.y
    })
    this.x = Math.round(x_sum / this.points.length)
    this.y = Math.round(y_sum / this.points.length)
  }
  move(startPoint, endPoint) {
    const x1 = endPoint.x - startPoint.x
    const y1 = endPoint.y - startPoint.y
    // console.log('x1',x1);
    this.points = this.points.map(item => {
      return {
        x: item.x + x1,
        y: item.y + y1
      }
    })
    this.computedCenter()
  }
  update(i, point) {
    this.points[i] = point
    this.computedCenter()
  }
  createPath(ctx) {
    ctx.beginPath()
    if (this.type === 'rectangle') {
      ctx.lineWidth = this.path_lineWidth
      ctx.strokeStyle = this.path_strokeStyle
      ctx.fillStyle = 'hsla(240,100%,50%,.3)'
    } else if (this.type === 'anchor') {
      ctx.lineWidth = this.anchor_lineWidth
      ctx.strokeStyle = this.anchor_strokeStyle
      ctx.fillStyle = 'hsla(0,100%,50%,.3)'
    }
    this.points.forEach((p, i) => {
      ctx[i === 0 ? 'moveTo' : 'lineTo'](p.x, p.y)
    })
    ctx.fill()
    ctx.closePath()
  }
  isInPath(ctx, point) {
    // in the point
    for (let i = 0; i < this.points.length; i++) {
      ctx.beginPath()
      ctx.arc(this.points[i].x, this.points[i].y, this.point_radis, 0, Math.PI * 2, false)
      if (ctx.isPointInPath(point.x, point.y)) {
        return i
      }
    }
    // in the figure
    this.createPath(ctx)
    if (ctx.isPointInPath(point.x, point.y)) {
      return 999
    }
    return -1
  }

  // Draw a cube for each point
  drawPoints(ctx) {
    ctx.save()
    ctx.lineWidth = this.point_lineWidth
    ctx.strokeStyle = this.point_strokeStyle
    ctx.fillStyle = this.point_strokeStyle
    this.points.forEach(p => {
      ctx.beginPath()
      ctx.moveTo(p.x - this.point_radis, p.y - this.point_radis)
      ctx.lineTo(p.x - this.point_radis, p.y + this.point_radis)
      ctx.lineTo(p.x + this.point_radis, p.y + this.point_radis)
      ctx.lineTo(p.x + this.point_radis, p.y - this.point_radis)
      ctx.closePath()
      ctx.fill()
    })
    ctx.restore()
  }
  draw(ctx) {
    ctx.save()
    this.createPath(ctx)
    ctx.stroke()
    ctx.restore()
  }
}

/**
 * Rectangle
 */
class Rectangle extends Graph {
  constructor(type, point, options) {
    super(type, point, options)
    this.points = [point, point, point, point]
    this.type = type// "rectangle"
  }
  initFigure(startPoint, endPoint) {
    const x1 = Math.round(startPoint.x)
    const y1 = Math.round(startPoint.y)
    const x2 = Math.round(endPoint.x)
    const y2 = Math.round(endPoint.y)
    this.points[0] = {
      x: x1,
      y: y1
    }
    this.points[1] = {
      x: x2,
      y: y1
    }
    this.points[2] = {
      x: x2,
      y: y2
    }
    this.points[3] = {
      x: x1,
      y: y2
    }
    this.x = Math.round((x1 + x2) / 2)
    this.y = Math.round((y1 + y2) / 2)
  }
  update(i, point) {
    this.points[i] = point
    if (i === 0) {
      this.points[1].y = point.y
      this.points[3].x = point.x
    } else if (i === 1) {
      this.points[2].x = point.x
      this.points[0].y = point.y
    } else if (i === 2) {
      this.points[3].y = point.y
      this.points[1].x = point.x
    } else {
      this.points[0].x = point.x
      this.points[2].y = point.y
    }
    this.computedCenter()
  }
}

/**
 * factory function for creating graph
 **/
export default function figureFactory(type, point, options) {
  return new Rectangle(type, point, options)
}
