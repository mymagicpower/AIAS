import request from '@/utils/request'

export function faceDetectionForImageUrl(data) {
  return request({
    url: '/face/faceDetectionForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function featureForImageUrl(data) {
  return request({
    url: '/face/featureForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function compareForImageUrls(data) {
  return request({
    url: '/face/compareForImageUrls',
    method: 'get',
    params: {
      url1: data.url1,
      url2: data.url2
    }
  })
}

export function compareForImageFiles(data) {
  return request({
    url: '/face/compareForImageFiles',
    method: 'post',// PUT
    data
  })
}
