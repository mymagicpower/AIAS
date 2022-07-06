import request from '@/utils/request'

export function search(data) {
  return request({
    url: 'api/search/mol',
    method: 'get',
    params: {
      topK: data.topK,
      smiles: encodeURI(data.smiles)
    }
  })
}
