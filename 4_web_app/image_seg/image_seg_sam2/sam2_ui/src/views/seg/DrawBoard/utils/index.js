/**
 * load image
 * @param {Element} canvas
 * @param {Function} cb
 */
export function generateImage(canvas, cb, url) {
  const ctx2 = canvas.getContext('2d')
  ctx2.clearRect(0, 0, canvas.width, canvas.height)
  // loading image
  ctx2.save()
  const img = new Image()
  img.src = url
  let imageWidth, imageHeight, scaleValue, posX, posY
  img.setAttribute('crossOrigin', 'Anonymous')
  img.onload = function() {
    imageWidth = img.width
    imageHeight = img.height
    const scaleValueW = Math.floor((canvas.width) / imageWidth * 100) / 100
    const scaleValueH = Math.floor((canvas.height) / imageHeight * 100) / 100
    scaleValue = Math.min(scaleValueW, scaleValueH)
    posX = (canvas.width - imageWidth * scaleValue) / 2
    posY = (canvas.height - imageHeight * scaleValue) / 2
    ctx2.drawImage(
      img,
      0, 0,
      imageWidth, imageHeight,
      posX, posY,
      imageWidth * scaleValue, imageHeight * scaleValue
    )
    ctx2.restore()
    cb(posX, posY, imageWidth, imageHeight, scaleValue)
  }
  img.onerror = function() {
    console.log('image load failed!')
  }
}

/**
 * Convert point coordinates on screen to coordinates on canvas
 * @param {Object} canvas
 * @param {Number} x coordinate value
 * @param {Number} y coordinate value
 *
 */
export function windowToCanvas(canvas, x, y) {
  if (!canvas) {
    console.log('canvas is not exist!')
    return {
      x: 0,
      y: 0
    }
  }
  var box = canvas.getBoundingClientRect()
  return {
    x: x - box.left,
    y: y - box.top
  }
}

/**
 * Convert  point coordinates from canvas to image.
 * @param {Number} x
 * @param {Number} y
 * @param {Number} posX
 * @param {Number} posY
 * @param {Number} canvasWidth
 * @param {Number} canvasHeight
 * @param {Number} imageXOffset
 * @param {Number} imageYOffset
 * @param {Number} imageScale
 * @param {Number} scale
 */
export function canvasToImage(x, y, posX, posY, canvasWidth, canvasHeight, imageXOffset, imageYOffset, imageScale, scale) {
  const xvar = x - imageXOffset * scale - canvasWidth * (1 - scale) / 2
  const yvar = y - imageYOffset * scale - canvasHeight * (1 - scale) / 2

  return {
    x: (xvar - posX * scale) / (imageScale * scale),
    y: (yvar - posY * scale) / (imageScale * scale)
  }
}

/**
 * Convert  point coordinates from image to canvas.
 * @param {Number} x
 * @param {Number} y
 * @param {Number} posX
 * @param {Number} posY
 * @param {Number} canvasWidth
 * @param {Number} canvasHeight
 * @param {Number} imageXOffset
 * @param {Number} imageYOffset
 * @param {Number} imageScale
 * @param {Number} scale
 */
export function imageToCanvas(x, y, posX, posY, canvasWidth, canvasHeight, imageXOffset, imageYOffset, imageScale, scale) {
  const xvar = x * (imageScale * scale) + posX * scale + canvasWidth * (1 - scale) / 2
  const yvar = y * (imageScale * scale) + posY * scale + canvasHeight * (1 - scale) / 2

  x = xvar + imageXOffset * scale
  y = yvar + imageYOffset * scale

  return {
    x,
    y
  }
}

/**
 * format point range when the point exceeds the image boundary
 * @param {*} point
 * @param {*} imagePosX
 * @param {*} imagePosY
 * @param {*} viewWidth
 * @param {*} viewHeight
 * @param {*} imageXOffset
 * @param {*} imageYOffset
 * @param {*} imageScale
 * @param {*} scale
 */
export function formatPointRange(point, imagePosX, imagePosY, imageWidth, imageHeight, viewWidth, viewHeight, imageXOffset, imageYOffset, imageScale, scale) {
  const tempPoint = canvasToImage(
    point.x,
    point.y,
    imagePosX,
    imagePosY,
    viewWidth,
    viewHeight,
    imageXOffset,
    imageYOffset,
    imageScale,
    scale
  )
  if (tempPoint.x < 0) {
    tempPoint.x = 0
  } else if (tempPoint.x > imageWidth) {
    tempPoint.x = imageWidth
  }
  if (tempPoint.y < 0) {
    tempPoint.y = 0
  } else if (tempPoint.y > imageHeight) {
    tempPoint.y = imageHeight
  }
  const newPoint = imageToCanvas(
    tempPoint.x,
    tempPoint.y,
    imagePosX,
    imagePosY,
    viewWidth,
    viewHeight,
    imageXOffset,
    imageYOffset,
    imageScale,
    scale
  )
  return newPoint
}

/**
 * Make the element full screen.
 * @param {Object} obj
 */
export function fullScreen(obj) {
  if (obj.requestFullscreen) {
    obj.requestFullscreen()
  } else if (obj.webkitRequestFullscreen) {
    obj.webkitRequestFullscreen()
  } else if (obj.mozRequestFullscreen) {
    obj.mozRequestFullscreen()
  }
}

/**
 * Make the element exit full screen.
 * @param {Object} obj
 */
export function exitScreen() {
  if (document.exitFullscreen) {
    document.exitFullscreen()
  } else if (document.webkitExitFullscreen) {
    document.webkitExitFullscreen()
  } else if (document.mozCancelFullScreen) {
    document.mozCancelFullScreen()
  }
}

/**
 * debounce
 * @param {function} fn
 * @param {number} time
 */
export function debounce(fn, time) {
  let timer = null
  return function() {
    var args = arguments; var context = this
    clearTimeout(timer)
    timer = setTimeout(function() {
      fn.call(context, ...args)
    }, time)
  }
}
