/* image event */
import { canvasToImage, imageToCanvas } from '../utils'
import figureFactory from './figureFactory.js'

// Convert the point coordinates from the canvas to the the image.
function formatPointsInImage(graphics, options) {
  graphics.forEach(figure => {
    for (let i = 0; i < figure.points.length; i++) {
      figure.points[i] = canvasToImage(
        figure.points[i].x,
        figure.points[i].y,
        options.imagePosX,
        options.imagePosY,
        options.viewWidth,
        options.viewHeight,
        options.imageXOffset,
        options.imageYOffset,
        options.imageScale,
        options.scale
      )
    }
  })
}

// Convert the point coordinates from the image to the the canvas.
function formatPointsInCanvas(graphics, options) {
  graphics.forEach(figure => {
    for (let i = 0; i < figure.points.length; i++) {
      figure.points[i] = imageToCanvas(
        figure.points[i].x,
        figure.points[i].y,
        options.imagePosX,
        options.imagePosY,
        options.viewWidth,
        options.viewHeight,
        options.imageXOffset,
        options.imageYOffset,
        options.imageScale,
        options.scale
      )
    }
  })
}

// Convert the point coordinates from the canvas to the the image when moving.
function formatPointsInImageWhenMove(graphics, options) {
  const tmpGraphics = []
  graphics.forEach(figure => {
    const figureCopy = figureFactory(figure.type, { x: figure.x, y: figure.y }, figure.options)
    for (let i = 0; i < figure.points.length; i++) {
      figureCopy.points[i] = canvasToImage(
        figure.points[i].x,
        figure.points[i].y,
        options.imagePosX,
        options.imagePosY,
        options.viewWidth,
        options.viewHeight,
        options.imageXOffset,
        options.imageYOffset,
        options.imageScale,
        options.scale
      )
    }
    tmpGraphics.push(figureCopy)
  })
  return tmpGraphics
}

// Convert the point coordinates from the image to the the canvas when moving.
function formatPointsInCanvasWhenMove(graphics, options) {
  graphics.forEach(figure => {
    for (let i = 0; i < figure.points.length; i++) {
      figure.points[i] = imageToCanvas(
        figure.points[i].x,
        figure.points[i].y,
        options.imagePosX,
        options.imagePosY,
        options.viewWidth,
        options.viewHeight,
        options.imageXOffset,
        options.imageYOffset,
        options.imageScale,
        options.scale
      )
    }
  })
}

const imageEvent = {}
imageEvent.zoomIn = function(graphics, convertParams) {
  formatPointsInImage(graphics, convertParams)
  const scale = convertParams.scale * 1.1
  convertParams.scale = scale
  formatPointsInCanvas(graphics, convertParams)
  return scale
}

imageEvent.zoomOut = function(graphics, convertParams) {
  formatPointsInImage(graphics, convertParams)
  const scale = convertParams.scale * 0.9
  convertParams.scale = scale
  formatPointsInCanvas(graphics, convertParams)
  return scale
}

imageEvent.formatPointsInImage = formatPointsInImage
imageEvent.formatPointsInCanvas = formatPointsInCanvas
imageEvent.formatPointsInImageWhenMove = formatPointsInImageWhenMove
imageEvent.formatPointsInCanvasWhenMove = formatPointsInCanvasWhenMove

imageEvent.drawTmpGraphics = function(graphics, ctx) {
  graphics.forEach((graphic) => {
    graphic.draw(ctx)
  })
}

export default imageEvent
