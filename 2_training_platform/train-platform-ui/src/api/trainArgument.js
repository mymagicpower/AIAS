import request from '@/utils/request'

export function getTrainArgument() {
  return request({
    url: 'api/trainArgument/info',
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'api/trainArgument/update',
    method: 'post',
    data
  })
}

export default { getTrainArgument, update }
