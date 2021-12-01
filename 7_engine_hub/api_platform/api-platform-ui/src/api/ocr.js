import request from '@/utils/request'

export function infoForImageUrl(data) {
  return request({
    url: '/ocr/infoForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export function generalInfoForImageUrl(data) {
  return request({
    url: '/ocr/generalInfoForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}
