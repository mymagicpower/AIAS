import request from '@/utils/request'

export function infoForImageUrl(uid, data) {
  return request({
    url: '/template/infoForImageUrl',
    method: 'get',
    params: {
      uid: uid,
      url: data.url
    }
  })
}

export function getTemplate(uid) {
  return request({
    url: '/template/getTemplate',
    method: 'get',
    params: {
      uid: uid
    }
  })
}

export function getTemplates() {
  return request({
    url: '/template/getTemplates',
    method: 'get'
  })
}

export function updateTemplate(data) {
  return request({
    url: '/template/updateTemplate',
    method: 'post',
    data
  })
}

export function getLabelData(data) {
  return request({
    url: '/template/getLabelData',
    method: 'post',
    data
  })
}

export function addTemplate(name, imageFile) {
  return request({
    url: '/template/updateTemplate',
    method: 'post',
    params: {
      name: name,
      imageFile: imageFile
    }
  })
}

export function removeTemplate(uid) {
  return request({
    url: '/template/removeTemplate',
    method: 'post',
    params: {
      uid: uid
    }
  })
}

export default { getTemplate, getTemplates, updateTemplate, getLabelData, addTemplate, removeTemplate }
