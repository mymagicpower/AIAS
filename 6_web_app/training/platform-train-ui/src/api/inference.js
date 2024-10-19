import request from '@/utils/request'

export function classInfoForUrl(data) {
  return request({
    url: '/api/inference/classInfoForUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function featureForImageUrl(data) {
  return request({
    url: '/api/inference/featureForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function compareForImageUrls(data) {
  return request({
    url: '/api/inference/compareForImageUrls',
    method: 'get',
    params: {
      url1: data.url1,
      url2: data.url2
    }
  })
}

export function compareForImageFiles(data) {
  return request({
    url: '/api/inference/compareForImageFiles',
    method: 'post', // PUT
    data
  })
}
