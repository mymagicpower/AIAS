import request from '@/utils/request'

export function mnistImageUrl(data) {
  return request({
    url: '/inference/mnistImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}
