import request from '@/utils/request'

export function getLabelData(data) {
  return request({
    url: '/infer/getLabelData',
    method: 'post',
    data
  })
}

export function uploadImage(name, imageFile) {
  return request({
    url: '/infer/uploadImage',
    method: 'post',
    params: {
      name: name,
      imageFile: imageFile
    }
  })
}

export default { uploadImage, getLabelData }
