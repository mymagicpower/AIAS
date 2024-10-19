import request from '@/utils/request'

export function imageSrForImageUrl(data) {
  return request({
    url: '/img/imageSrForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function faceResForImageUrl(data) {
  return request({
    url: '/img/faceResForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function faceGanForImageUrl(data) {
  return request({
    url: '/img/faceGanForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export default {
  imageSrForImageUrl,
  faceResForImageUrl,
  faceGanForImageUrl
}

