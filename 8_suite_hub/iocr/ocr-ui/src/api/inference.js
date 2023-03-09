import request from '@/utils/request'

export function generalInfoForImageUrl(data) {
  return request({
    url: '/inference/generalInfoForImageUrl',
    method: 'get',
    params: {
      url: data.url
    }
  })
}

export default { generalInfoForImageUrl }

