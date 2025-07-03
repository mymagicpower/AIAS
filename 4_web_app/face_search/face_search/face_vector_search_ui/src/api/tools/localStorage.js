import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/imageInfo/localAdd',
    method: 'post',
    data
  })
}

export function serverAdd(path) {
  return request({
    url: 'api/imageInfo/serverAdd',
    method: 'post',
    data: {
      fullPath: path
    }
  })
}

export function extractFeatures(id, type) {
  return request({
    url: 'api/imageInfo/extractFeatures',
    method: 'post',
    data: {
      id: id,
      type: type
    }
  })
}

export function deleteFeatures(id) {
  return request({
    url: 'api/imageInfo/deleteFeatures',
    method: 'post',
    data: id
  })
}

export function del(ids) {
  return request({
    url: 'api/imageInfo/deleteImages',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/localStorage',
    method: 'put',
    data
  })
}

export default { add, edit, del, extractFeatures, deleteFeatures }
