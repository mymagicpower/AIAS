import request from '@/utils/request'

export function search(data) {
  return request({
    url: 'api/search/sequence',
    method: 'get',
    params: {
      topK: data.topK,
      sequence: data.sequence
    }
  })
}
