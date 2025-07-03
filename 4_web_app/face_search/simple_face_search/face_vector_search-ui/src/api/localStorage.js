import request from '@/utils/request'

export function getStorageList() {
  return request({
    url: 'api/localStorage/list',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'api/localStorage',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: 'api/localStorage/',
    method: 'delete',
    data: {
      id: id
    }
  })
}

export function edit(data) {
  return request({
    url: 'api/localStorage',
    method: 'put',
    data
  })
}

export function extract(id) {
  return request({
    url: 'api/data/extractFeatures',
    method: 'get',
    params: {
      id: id
    }
  })
}

export default { getStorageList, add, edit, del, extract }
