import request from '@/utils/request'

export function train(id) {
  return request({
    url: '/train/trigger',
    method: 'get',
    params: {
      id: id
    }
  })
}
