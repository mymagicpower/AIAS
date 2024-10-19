import request from '@/utils/request'

export function search(data) {
  return request({
    url: 'api/search/text',
    method: 'get',
    params: {
      topK: data.topK,
      text: data.text
    }
  })
}
