import request from '@/utils/request'

export function train(id) {
  return request({
    url: 'api/train/trigger',
    method: 'post',
    data: {
      id: id
    }
  })
}
